#include <opencv2/opencv.hpp>
#include <opencv2/imgproc/types_c.h>
#include <src/BinaryBitmap.h>
#include "ImageScheduler.h"
#include "JNIUtils.h"
#include <unistd.h>

int DEFAULT_MIN_LIGHT = 70;

int SCAN_ZXING = 0;
int SCAN_ZBAR = 1;

int SCAN_TREAT_GRAY = 0;
int SCAN_TREAT_THRESHOLD = 1;
int SCAN_TREAT_ADAPTIVE = 2;
int SCAN_TREAT_NEGATIVE = 3;
int SCAN_TREAT_CUSTOMIZE = 4;

ImageScheduler::ImageScheduler(JNIEnv *env, MultiFormatReader *_reader) {
    this->env = env;
    this->reader = _reader;
    qrCodeRecognizer = new QRCodeRecognizer();
}

ImageScheduler::~ImageScheduler() {
    DELETE(env)
    DELETE(reader)
    DELETE(qrCodeRecognizer)
    delete &cameraLight;
    scanIndex = 0;
}

Result
ImageScheduler::readByte(JNIEnv *env, jbyte *bytes, int left, int top, int cropWidth, int cropHeight,
                         int rowWidth,
                         int rowHeight) {

    try {
        scanIndex++;
        LOGE("start preTreatMat..., scanIndex = %d", scanIndex);

        Mat src(rowHeight + rowHeight / 2, rowWidth, CV_8UC1, bytes);

        Mat gray;
        cvtColor(src, gray, COLOR_YUV2GRAY_NV21);

        if (left != 0) {
            gray = gray(Rect(left, top, cropWidth, cropHeight));
        }

        // 分析亮度，如果亮度过低，不进行处理
        analysisBrightness(gray);
        if (cameraLight < 30) {
            Result result(DecodeStatus::NotFound);
            return result;
        }

        // 正常解析策略 偶数次zxing解析，奇数次zbar解析
        return decodeGrayPixels(gray);

    } catch (const std::exception &e) {
        LOGE("preTreatMat error...");
        Result result(DecodeStatus::NotFound);
        return result;
    }
}

/**
 * 1.1
 * 直接解析gray后的mat
 * 顺时针旋转90度图片，得到正常的图片（Android的后置摄像头获取的格式是横着的，需要旋转90度）
 * @param gray
 */
Result ImageScheduler::decodeGrayPixels(const Mat &gray) {
    LOGE("start GrayPixels...");

    Mat mat;
    rotate(gray, mat, ROTATE_90_CLOCKWISE);

    Result result = zxingDecode(mat);
    if (result.isValid()) {
        logDecode(SCAN_ZXING, SCAN_TREAT_GRAY, scanIndex);
        return result;
    } else {
       return decodeThresholdPixels(gray);
    }
}

/**
 * 1.2
 * 如果gray化没有解析出来，尝试提升亮度，处理图片亮度过低时的情况
 * 并进行二值化处理，让二维码更清晰
 * 同时旋转180度
 * @param gray
 */
Result ImageScheduler::decodeThresholdPixels(const Mat &gray) {
    LOGE("start ThresholdPixels...");

    Mat mat;
    rotate(gray, mat, ROTATE_180);

    // 提升亮度
    if (cameraLight < 80) {
        mat.convertTo(mat, -1, 1.0, 30);
    }

    threshold(mat, mat, 50, 255, CV_THRESH_OTSU);

    Result result = zxingDecode(mat);
    if (result.isValid()) {
        logDecode(SCAN_ZXING, SCAN_TREAT_THRESHOLD, scanIndex);
        return result;
    } else {
        return recognizerQrCode(gray);
    }
}

/**
 * 2.2 降低图片亮度，再次识别图像，处理亮度过高时的情况
 * 逆时针旋转90度，即旋转了270度
 * @param gray
 */
Result ImageScheduler::decodeAdaptivePixels(const Mat &gray) {
    if (scanIndex % 3 != 0) {
        Result result(DecodeStatus::NotFound);
        return result;
    }
    LOGE("start AdaptivePixels...");

    Mat mat;
    rotate(gray, mat, ROTATE_90_COUNTERCLOCKWISE);

    // 降低图片亮度
    Mat lightMat;
    mat.convertTo(lightMat, -1, 1.0, -60);

    adaptiveThreshold(lightMat, lightMat, 255, ADAPTIVE_THRESH_MEAN_C,
                      THRESH_BINARY, 55, 3);

    Result result = zxingDecode(lightMat);
    if (result.isValid()) {
        logDecode(SCAN_ZBAR, SCAN_TREAT_ADAPTIVE, scanIndex);
        return result;
    } else {
        return decodeNegative(gray);
    }
}

/**
 * 2.3 处理反色图片
 * @param gray
 */
Result ImageScheduler::decodeNegative(const Mat &gray) {
    Mat negativeMat;
    bitwise_not(gray, negativeMat);

    Result result = zxingDecode(negativeMat);
    if (result.isValid()) {
        logDecode(SCAN_ZBAR, SCAN_TREAT_NEGATIVE, scanIndex);
        return result;
    } else {
        return recognizerQrCode(gray);
    }
}

/**
 * 3.0
 * 不能使用内置的解析出来，使用自定的图片解析策略
 */
Result ImageScheduler::recognizerQrCode(const Mat &mat) {
    // 强度为 0，外层不需要 openCV 介入
    if (openCVDetectValue == 0) {
        Result result(DecodeStatus::NotFound);
        return result;
    }
    // 7次没有解析出来，尝试聚焦
    if (scanIndex % 7 == 0) {
//        javaCallHelper->onFocus();
    }
    // 只有3的倍数次才去使用OpenCV处理
//    if (scanIndex % 3 != 0) {
//        return;
//    }
    LOGE("start to recognizerQrCode..., scanIndex = %d ", scanIndex);

    cv::Rect rect;
    qrCodeRecognizer->processData(mat, &rect);
    // 一般认为，长度小于120的一般是误报
    if (rect.empty() || rect.height < 120) {
        Result result(DecodeStatus::NotFound);
        return result;
    }

    ResultPoint point1(rect.tl().x, rect.tl().y);
    ResultPoint point2(rect.br().x, rect.tl().y);
    ResultPoint point3(rect.tl().x, rect.br().y);

    std::vector<ResultPoint> points;
    points.push_back(point1);
    points.push_back(point2);
    points.push_back(point3);

    Result result(DecodeStatus::NotFound);
    result.setResultPoints(std::move(points));

//    javaCallHelper->onResult(result, SCAN_TREAT_CUSTOMIZE);

    LOGE("end recognizerQrCode..., scanIndex = %d height = %d width = %d", scanIndex, rect.height,
         rect.width);

    return result;
}

Result ImageScheduler::zxingDecode(const Mat &mat) {
    auto binImage = BinaryBitmapFromBytesC1(mat.data, 0, 0, mat.cols, mat.rows);
    Result result = reader->read(*binImage);
    if (result.isValid()) {
        LOGE("zxing decode success, result data = %s", result.text().c_str());
//        javaCallHelper->onResult(result, SCAN_ZXING);
    }
    return result;
}

bool ImageScheduler::analysisBrightness(const Mat &gray) {
    LOGE("start analysisBrightness...");

    // 平均亮度
    Scalar scalar = mean(gray);
    cameraLight = scalar.val[0];
    LOGE("平均亮度 %lf", cameraLight);
    // 判断在时间范围 AMBIENT_BRIGHTNESS_WAIT_SCAN_TIME * lightSize 内是不是亮度过暗
    bool isDark = cameraLight < DEFAULT_MIN_LIGHT;
//    javaCallHelper->onBrightness(isDark);

    return isDark;
}

//void saveMat(){
//        Mat resultMat(height, width, CV_8UC1, pixels);
//        imwrite("/storage/emulated/0/scan/result.jpg", mat);
//}

void ImageScheduler::logDecode(int scanType, int treatType, int index) {
    String scanName = scanType == SCAN_ZXING ? "zxing" : "zbar";
    LOGE("%s decode success, treat type = %d, scan index = %d",
         scanName.c_str(), treatType, index);
}

void ImageScheduler::setOpenCVDetectValue(int value) {
    this->openCVDetectValue = value;
}

Result
ImageScheduler::readBitmap(JNIEnv *env, jobject bitmap, int left, int top, int width, int height) {

    Mat src;
    BitmapToMat(env, bitmap, src);

    Mat gray;
    cvtColor(src, gray, COLOR_RGBA2GRAY);

    auto gWidth = static_cast<unsigned int>(gray.cols);
    auto gHeight = static_cast<unsigned int>(gray.rows);
    const void *raw = gray.data;
//    Image image(gWidth, gHeight, "Y800", raw, gWidth * gHeight);
//    LOGE("zbar Code cols = %d row = %d", gray.cols, gray.rows);
//
//    if (zbarScanner->scan(image) > 0) {
//        Image::SymbolIterator symbol = image.symbol_begin();
//        LOGE("zbar Code Data = %s", symbol->get_data().c_str());
//        if (symbol->get_type() == zbar_symbol_type_e::ZBAR_QRCODE) {
//            Result resultBar(DecodeStatus::NoError);
//            resultBar.setFormat(BarcodeFormat::QR_CODE);
//            resultBar.setText(ANSIToUnicode(symbol->get_data()));
//            image.set_data(nullptr, 0);
//            LOGE("zbar decode success");
//            return resultBar;
//        }
//    } else {
//        image.set_data(nullptr, 0);
//    }

    auto binImage = BinaryBitmapFromJavaBitmap(env, bitmap, left, top, width, height);
    if (!binImage) {
        LOGE("create binary bitmap fail");
        return Result(DecodeStatus::NotFound);
    }
    LOGE("zxing decode success");

    return reader->read(*binImage);
}