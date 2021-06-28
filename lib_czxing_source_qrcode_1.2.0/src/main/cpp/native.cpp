#include <jni.h>
#include <string>
#include "JNIUtils.h"
#include "MultiFormatReader.h"
#include "DecodeHints.h"
#include "Result.h"
#include <vector>
#include "MultiFormatWriter.h"
#include "BitMatrix.h"
#include <sys/time.h>
#include <src/ReadBarcode.h>
#include <android/bitmap.h>

JavaVM *javaVM = nullptr;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    javaVM = vm;

    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR; // JNI version not supported.
    }

    return JNI_VERSION_1_6;
}

struct LockedPixels {
    JNIEnv *env;
    jobject bitmap;
    void *pixels = nullptr;

    LockedPixels(JNIEnv *env, jobject bitmap) : env(env), bitmap(bitmap) {
        if (AndroidBitmap_lockPixels(env, bitmap, &pixels) != ANDROID_BITMAP_RESUT_SUCCESS)
            pixels = nullptr;
    }

    operator const uint8_t *() const { return static_cast<const uint8_t *>(pixels); }

    ~LockedPixels() {
        if (pixels)
            AndroidBitmap_unlockPixels(env, bitmap);
    }
};

static std::vector<ZXing::BarcodeFormat> GetFormats(JNIEnv *env, jintArray formats) {
    std::vector<ZXing::BarcodeFormat> result;
    jsize len = env->GetArrayLength(formats);
    if (len > 0) {
        std::vector<jint> elems(len);
        env->GetIntArrayRegion(formats, 0, elems.size(), elems.data());
        result.resize(len);
        for (jsize i = 0; i < len; ++i) {
            result[i] = ZXing::BarcodeFormat(elems[i]);
        }
    }
    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_lib_kalu_czxing_jni_Native_writeBytes(JNIEnv *env, jobject instance, jstring content_,
                                      jint width, jint height, jint color,
                                      jstring format_, jobjectArray result) {
    const char *content = env->GetStringUTFChars(content_, 0);
    const char *format = env->GetStringUTFChars(format_, 0);
    try {
        std::wstring wContent;
        wContent = ANSIToUnicode(content);

        ZXing::MultiFormatWriter writer(ZXing::BarcodeFormatFromString(format));
        ZXing::BitMatrix bitMatrix = writer.encode(wContent, width, height);

        if (bitMatrix.empty()) {
            return -1;
        }

        int size = width * height;
        jintArray pixels = env->NewIntArray(size);
        int black = color;
        int white = 0xffffffff;
        int index = 0;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int pix = bitMatrix.get(j, i) ? black : white;
                env->SetIntArrayRegion(pixels, index, 1, &pix);
                index++;
            }
        }
        env->SetObjectArrayElement(result, 0, pixels);
        env->ReleaseStringUTFChars(format_, format);
        env->ReleaseStringUTFChars(content_, content);
    }
    catch (const std::exception &e) {
        ThrowJavaException(env, e.what());
    }
    catch (...) {
        ThrowJavaException(env, "Unknown exception");
    }
    return 0;
}

extern "C"
JNIEXPORT jint JNICALL
Java_lib_kalu_czxing_jni_Native_readBitmap(JNIEnv *env, jobject instance,
                                     jobject bitmap, jint left, jint top, jint width,
                                     jint height, jobjectArray result) {

    try {

        AndroidBitmapInfo bmInfo;
        AndroidBitmap_getInfo(env, bitmap, &bmInfo);

        ZXing::ImageFormat fmt = ZXing::ImageFormat::None;
        switch (bmInfo.format) {
            case ANDROID_BITMAP_FORMAT_A_8:
                fmt = ZXing::ImageFormat::Lum;
                break;
            case ANDROID_BITMAP_FORMAT_RGBA_8888:
                fmt = ZXing::ImageFormat::RGBX;
                break;
            default:
                ThrowJavaException(env, "Unsupported format");
        }

        auto pixels = LockedPixels(env, bitmap);

        if (!pixels)
            ThrowJavaException(env, "Failed to lock/read AndroidBitmap data");

        auto image = ZXing::ImageView{pixels, (int) bmInfo.width, (int) bmInfo.height, fmt, 0, 0};
//        auto image = ZXing::ImageView{pixels, (int) bmInfo.width, (int) bmInfo.height, fmt,
//                               (int) bmInfo.stride};

        auto hints = ZXing::DecodeHints()
                .setTryHarder(false)
                .setTryRotate(false);

        auto readResult = ReadBarcode(image, hints);

        if (readResult.isValid()) {
            env->SetObjectArrayElement(result, 0, ToJavaString(env, readResult.text()));
            if (!readResult.resultPoints().empty()) {
                env->SetObjectArrayElement(result, 1,
                                           ToJavaArray(env, readResult.resultPoints()));
            }
            return static_cast<int>(readResult.format());
        } else {
            return -1;
        }
    } catch (const std::exception &e) {
        ThrowJavaException(env, e.what());
    } catch (...) {
        ThrowJavaException(env, "Unknown exception");
    }
    return -1;
}

extern "C"
JNIEXPORT jint JNICALL
Java_lib_kalu_czxing_jni_Native_readBytes(JNIEnv *env, jobject instance,
                                     jbyteArray bytes_, jint left, jint top,
                                     jint cropWidth, jint cropHeight,
                                     jint rowWidth, jint rowHeight,
                                     jobjectArray result) {


    try {

        jbyte *bytes = env->GetByteArrayElements(bytes_, NULL);

        auto image = ZXing::ImageView{reinterpret_cast<const uint8_t *>(bytes), cropWidth,
                                      cropHeight,
                                      ZXing::ImageFormat::Lum, 0, 0};

        auto hints = ZXing::DecodeHints()
                .setTryHarder(false)
                .setTryRotate(false);

        auto readResult = ReadBarcode(image, hints);

        if (readResult.isValid()) {
            env->SetObjectArrayElement(result, 0, ToJavaString(env, readResult.text()));
            if (!readResult.resultPoints().empty()) {
                env->SetObjectArrayElement(result, 1, ToJavaArray(env, readResult.resultPoints()));
            }

            env->ReleaseByteArrayElements(bytes_, bytes, 0);
            return static_cast<int>(readResult.format());
        } else {

            env->ReleaseByteArrayElements(bytes_, bytes, 0);
            return -1;
        }

    } catch (const std::exception &e) {
        ThrowJavaException(env, e.what());
        return -1;
    } catch (...) {
        ThrowJavaException(env, "Unknown exception");
        return -1;
    }
}