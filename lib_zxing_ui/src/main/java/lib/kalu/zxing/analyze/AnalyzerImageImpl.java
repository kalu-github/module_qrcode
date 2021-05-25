package lib.kalu.zxing.analyze;

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
    default Result analyzeImage(@NonNull @NotNull ImageProxy imageProxy, int orientation) {
        LogUtil.log("analyzeImage => format =  " + imageProxy.getFormat() + ", orientation = " + (orientation == Configuration.ORIENTATION_PORTRAIT ? "竖屏" : "横屏"));

        if (imageProxy.getFormat() != ImageFormat.YUV_420_888)
            return null;

        ByteBuffer buffer = imageProxy.getPlanes()[0].getBuffer();
        byte[] original = new byte[buffer.remaining()];
        buffer.get(original);
        int width = imageProxy.getWidth();
        int height = imageProxy.getHeight();

        byte[] data = orientation == Configuration.ORIENTATION_PORTRAIT ? optimize(original, width, height, orientation, true) : original;
        int dataWidth = orientation == Configuration.ORIENTATION_PORTRAIT ? height : width;
        int dataHeight = orientation == Configuration.ORIENTATION_PORTRAIT ? width : height;
        return analyzeData(data, dataWidth, dataHeight);
    }
}
