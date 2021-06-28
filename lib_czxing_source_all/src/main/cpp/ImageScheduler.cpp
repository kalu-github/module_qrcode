#include <src/BinaryBitmap.h>
#include "ImageScheduler.h"
#include "JNIUtils.h"
#include <unistd.h>

int DEFAULT_MIN_LIGHT = 70;

int SCAN_ZXING = 0;

ImageScheduler::ImageScheduler(JNIEnv *env, MultiFormatReader *_reader) {
    this->env = env;
    this->reader = _reader;
}

ImageScheduler::~ImageScheduler() {
    DELETE(env)
    DELETE(reader)
    delete &cameraLight;
    scanIndex = 0;
}

Result
ImageScheduler::readByte(JNIEnv *env, jbyte *bytes, int left, int top, int cropWidth,
                         int cropHeight,
                         int rowWidth,
                         int rowHeight) {

    try {
        scanIndex++;
        LOGE("start preTreatMat..., scanIndex = %d", scanIndex);
        char *data = (char *) bytes;
        auto binImage = BinaryBitmapFromBytesC1(data, left, top, cropWidth, cropHeight);
        Result result = reader->read(*binImage);
        if (result.isValid()) {
            LOGE("zxing decode succ data = %s", result.text().c_str());
        } else {
            LOGE("zxing decode fail");
        }

        // 正常解析策略 偶数次zxing解析，奇数次zbar解析
        return result;

    } catch (const std::exception &e) {
        LOGE("preTreatMat error...");
        Result result(DecodeStatus::NotFound);
        return result;
    }
}

void ImageScheduler::logDecode(int scanType, int treatType, int index) {
//    String scanName = scanType == SCAN_ZXING ? "zxing" : "zbar";
//    LOGE("%s decode success, treat type = %d, scan index = %d",
//         scanName.c_str(), treatType, index);
}

Result
ImageScheduler::readBitmap(JNIEnv *env, jobject bitmap, int left, int top, int width, int height) {

    auto binImage = BinaryBitmapFromJavaBitmap(env, bitmap, left, top, width, height);
    if (!binImage) {
        LOGE("create binary bitmap fail");
        return Result(DecodeStatus::NotFound);
    }
    LOGE("zxing decode success");

    return reader->read(*binImage);
}