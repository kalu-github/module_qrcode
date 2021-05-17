package lib.kalu.zxing.analyze;

import android.content.res.Configuration;
import android.graphics.ImageFormat;

import com.google.zxing.Result;

import java.nio.ByteBuffer;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageProxy;

import lib.kalu.zxing.util.LogUtil;

/**
 * @description: 图像分析器
 * @date: 2021-05-07 14:57
 */
abstract class AnalyzerImageProxy implements AnalyzerImpl {

    @Override
    public Result analyze(@NonNull ImageProxy image, int orientation) {
        LogUtil.log("analyze => format =  " + image.getFormat() + ", orientation = " + (orientation == Configuration.ORIENTATION_PORTRAIT ? "竖屏" : "横屏"));

        if (image.getFormat() != ImageFormat.YUV_420_888)
            return null;

        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        int width = image.getWidth();
        int height = image.getHeight();

        // 竖屏
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            byte[] bytes = new byte[data.length];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    bytes[x * height + height - y - 1] = data[x + y * width];
                }
            }
            return analyze(bytes, height, width);
        }
        // 横屏
        else {
            return analyze(data, width, height);
        }
    }
}
