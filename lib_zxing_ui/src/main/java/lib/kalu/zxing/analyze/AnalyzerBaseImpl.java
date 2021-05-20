package lib.kalu.zxing.analyze;

import android.content.res.Configuration;

import com.google.zxing.Reader;
import com.google.zxing.Result;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.ImageProxy;

/**
 * @description: 分析器
 * @date: 2021-05-07 14:56
 */
public interface AnalyzerBaseImpl {

    @Nullable
    Result analyzeImage(@NonNull ImageProxy imageProxy, int orientation);

    @Nullable
    Result analyzeData(byte[] data, int dataWidth, int dataHeight);

    /**
     * @param data       图片原始帧数据
     * @param dataWidth  原始宽度
     * @param dataHeight 原始高度
     * @param left       裁剪左起始位置
     * @param top        裁剪上起始位置
     * @param outWidth   裁剪宽度
     * @param outHeight  裁剪高度
     * @return
     */
    Result analyzeRect(byte[] data, int dataWidth, int dataHeight, int left, int top, int outWidth, int outHeight);

    @Nullable
    Reader createReader();

    default float ratio() {
        return 0.6F;
    }

    default byte[] optimize(int orientation, @NonNull byte[] original, @NonNull int dataWidth, @NonNull int dataHeight) {
        return optimize(orientation, false, original, dataWidth, dataHeight);
    }

    /**
     * 没有任何色彩， 灰度化后跟原图应该是差不多， zxing扫不出的原因就是理应的黑色区域灰度值过高，这里要想办法降低。这里笔者经过大量测试发现一个极其巧妙的处理方式
     *
     * @param data
     * @param dataWidth
     * @param dataHeight
     */
    default byte[] optimize(int orientation, boolean optimizeColor, @NonNull byte[] original, @NonNull int dataWidth, @NonNull int dataHeight) {

        byte[] data = orientation == Configuration.ORIENTATION_PORTRAIT || optimizeColor ? original.clone() : original;

        // 竖屏
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            for (int y = 0; y < dataHeight; y++) {
                for (int x = 0; x < dataWidth; x++) {
                    data[x * dataHeight + dataHeight - y - 1] = original[x + y * dataWidth];
                }
            }
        }

        // 颜色线性增强
        if (optimizeColor) {
//            int width = orientation == Configuration.ORIENTATION_PORTRAIT ? dataHeight : dataWidth;
//            int height = orientation == Configuration.ORIENTATION_PORTRAIT ? dataWidth : dataHeight;
//            short random = (short) (Math.random() * 4 + 3);
//            for (int i = 0; i < width * height; i++) {
//                data[i] = (byte) (original[i] * random);
//            }
        }

        return data;
    }
}
