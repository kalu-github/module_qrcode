#ifndef CZXING_IMAGESCHEDULER_H
#define CZXING_IMAGESCHEDULER_H


#include <jni.h>
#include <src/MultiFormatReader.h>
#include <src/BinaryBitmap.h>
#include "Result.h"

using namespace ZXing;

struct FrameData {
    jbyte *bytes;
    int left;
    int top;
    int cropWidth;
    int cropHeight;
    int rowWidth;
    int rowHeight;
};

class ImageScheduler {
public:
    ImageScheduler(JNIEnv *env, MultiFormatReader *_reader);

    ~ImageScheduler();

    Result readBitmap(JNIEnv *env, jobject bitmap, int left, int top, int width, int height);

    Result
    readByte(JNIEnv *env, jbyte *bytes, int left, int top, int width, int height, int rowWidth,
             int rowHeight);

    MultiFormatReader *reader;

private:
    JNIEnv *env;

    double cameraLight{};
    int scanIndex;

    static void logDecode(int scanType, int treatType, int index);
};

#endif //CZXING_IMAGESCHEDULER_H
