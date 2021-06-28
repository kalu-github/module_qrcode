package lib.kalu.czxing.analyze;

import android.content.Context;

import androidx.annotation.NonNull;

import lib.kalu.czxing.jni.BarcodeResult;

/**
 * @description:
 * @date: 2021-05-07 14:57
 */
interface AnalyzerRectImpl extends AnalyzerDataImpl {

    @Override
    default BarcodeResult analyzeRect(@NonNull Context context, @NonNull byte[] crop, int cropWidth, int cropHeight, int cropLeft, int cropTop, @NonNull byte[] original, int originalWidth, int originalHeight) {
        return decodeRect(context, crop, cropWidth, cropHeight);
    }
}