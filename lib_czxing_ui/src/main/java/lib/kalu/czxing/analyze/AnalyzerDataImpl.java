package lib.kalu.czxing.analyze;

import android.content.Context;

import lib.kalu.czxing.jni.BarcodeResult;
import lib.kalu.czxing.util.LogUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @description:
 * @date: 2021-05-07 14:56
 */
interface AnalyzerDataImpl extends AnalyzerImageImpl {
    @Nullable
    @Override
    default BarcodeResult analyzeData(@NonNull Context context, @NonNull byte[] crop, int cropWidth, int cropHeight, int cropLeft, int cropTop, @NonNull byte[] original, int originalWidth, int originalHeight) {

        LogUtil.log("analyzeData => cropWidth = " + cropWidth + ", cropHeight = " + cropHeight + ", originalWidth = " + originalWidth + ", originalHeight = " + originalHeight);
        return analyzeRect(context, crop, cropWidth, cropHeight, cropLeft, cropTop, original, originalWidth, originalHeight);
    }

    //    @Nullable
//    @Override
//    default Result analyzeData(byte[] data, int dataWidth, int dataHeight) {
//
//        // ratio() > 1F ? 全屏扫描 : 区域扫描
//        int outWidth = ratio() >= 1F ? dataWidth : (int) (Math.min(dataWidth, dataHeight) * ratio());
//        int outHeight = ratio() >= 1F ? dataHeight : outWidth;
//        int left = ratio() >= 1F ? 0 : dataWidth / 2 - outWidth / 2;
//        int top = ratio() >= 1F ? 0 : dataHeight / 2 - outHeight / 2;
//
//        LogUtil.log("analyzeData => dataWidth = " + dataWidth + ", dataHeight = " + dataHeight + ", left = " + left + ", top = " + top + ", outWidth = " + outWidth + ", outHeight = " + outHeight);
//        return analyzeRect(data, dataWidth, dataHeight, left, top, outWidth, outHeight);
//    }
}
