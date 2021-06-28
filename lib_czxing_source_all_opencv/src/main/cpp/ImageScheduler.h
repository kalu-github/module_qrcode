#ifndef CZXING_IMAGESCHEDULER_H
#define CZXING_IMAGESCHEDULER_H


#include <jni.h>
#include <opencv2/core/mat.hpp>
#include <src/MultiFormatReader.h>
#include <src/BinaryBitmap.h>
#include "Result.h"
#include "QRCodeRecognizer.h"

using namespace cv;
using namespace ZXing;

class ImageScheduler {
public:
    ImageScheduler(JNIEnv *env, MultiFormatReader *_reader);

    ~ImageScheduler();

    Result readBitmap(JNIEnv *env, jobject bitmap, int left, int top, int width, int height);

    Result
    readByte(JNIEnv *env, jbyte *bytes, int left, int top, int width, int height, int rowWidth,
             int rowHeight);

    Result decodeGrayPixels(const Mat &gray);

    Result decodeThresholdPixels(const Mat &gray);

    Result decodeAdaptivePixels(const Mat &gray);

    Result decodeNegative(const Mat &gray);

    void setOpenCVDetectValue(int value);

    MultiFormatReader *reader;

private:
    JNIEnv *env;
    QRCodeRecognizer *qrCodeRecognizer;

    double cameraLight{};
    int scanIndex;
    // openCV 探测强度，[0-10]，强度越低，验证越严格，越不容易放大
    int openCVDetectValue = 10;

    Result recognizerQrCode(const Mat &mat);

    Result zxingDecode(const Mat &mat);

    static void logDecode(int scanType, int treatType, int index);

    bool analysisBrightness(const Mat &gray);
};

#endif //CZXING_IMAGESCHEDULER_H
