package lib.kalu.zxing.analyze;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.ImageFormat;

import com.google.zxing.Result;

import java.nio.ByteBuffer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.ImageProxy;

import org.jetbrains.annotations.NotNull;

import lib.kalu.zxing.util.LogUtil;

/**
 * @description: 图像分析器
 * @date: 2021-05-07 14:57
 */
interface AnalyzerImageImpl extends AnalyzerBaseImpl {

    @Nullable
    @Override
    default Result analyzeImage(@NonNull Context context, @NonNull ImageProxy imageProxy, int orientation) {
        LogUtil.log("analyzeImage => format =  " + imageProxy.getFormat() + ", orientation = " + (orientation == Configuration.ORIENTATION_PORTRAIT ? "竖屏" : "横屏"));

        if (imageProxy.getFormat() != ImageFormat.YUV_420_888)
            return null;

        ByteBuffer buffer = imageProxy.getPlanes()[0].getBuffer();
        byte[] original = new byte[buffer.remaining()];
        buffer.get(original);
        int originalWidth = imageProxy.getWidth();
        int originalHeight = imageProxy.getHeight();

        // ratio() > 1F ? 全屏扫描 : 区域扫描
        float ratio = ratio() * 1.1F;
        int cropWidth = ratio >= 1F ? originalWidth : (int) (Math.min(originalWidth, originalHeight) * ratio);
        int cropHeight = ratio >= 1F ? originalHeight : cropWidth;
        int cropLeft = ratio >= 1F ? 0 : originalWidth / 2 - cropWidth / 2;
        int cropTop = ratio >= 1F ? 0 : originalHeight / 2 - cropHeight / 2;
        byte[] crop = crop(original, originalWidth, originalHeight, cropWidth, cropHeight, cropLeft, cropTop, orientation);
        return analyzeData(context, crop, cropWidth, cropHeight, cropLeft, cropTop, original, originalWidth, originalHeight);
    }
}
