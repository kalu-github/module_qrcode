package lib.kalu.zxing.camerax.analyze;

import android.content.res.Configuration;
import android.graphics.ImageFormat;

import com.google.zxing.Result;

import java.nio.ByteBuffer;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageProxy;

import lib.kalu.zxing.camerax.util.LogUtil;

/**
 * @description: 图像分析器
 * @date: 2021-05-07 14:57
 */
abstract class AnalyzerYUV420888 implements AnalyzerImpl {

    @Override
    public Result analyze(@NonNull ImageProxy image, int orientation) {
        LogUtil.w("imageFormat: " + image.getFormat());

        if (image.getFormat() != ImageFormat.YUV_420_888)
            return null;

        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        int width = image.getWidth();
        int height = image.getHeight();

        if (orientation != Configuration.ORIENTATION_PORTRAIT)
            return analyze(data, width, height);

        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rotatedData[x * height + height - y - 1] = data[x + y * width];
            }
        }
        return analyze(rotatedData, height, width);
    }
}
