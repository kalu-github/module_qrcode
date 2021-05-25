package lib.kalu.zxing.analyze;

import android.content.res.Configuration;

import com.google.zxing.Reader;
import com.google.zxing.Result;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.ImageProxy;

import lib.kalu.zxing.util.LogUtil;

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
     * @param tryAgain   重试
     * @return
     */
    Result analyzeRect(byte[] data, int dataWidth, int dataHeight, int left, int top, int outWidth, int outHeight);

    @Nullable
    Reader createReader();

    /**
     * 默认全屏扫描
     *
     * @return
     */
    default float ratio() {
        return 1F;
    }

    /**
     * 相机预览的每一帧数据, 进行优化
     *
     * @param original    相机帧数据
     * @param dataWidth   相机帧数据, 原始宽
     * @param dataHeight  相机帧数据, 原始高
     * @param orientation 方向
     * @param optimize    优化开关：1. 伽马增强， 2. 线性增强
     * @return
     */
    default byte[] optimize(@NonNull byte[] original, @NonNull int dataWidth, @NonNull int dataHeight, int orientation, boolean optimize) {
        LogUtil.log("optimize=> orientation = " + (orientation == Configuration.ORIENTATION_PORTRAIT ? "竖屏" : "横屏") + ", optimize = " + optimize + ", dataWidth = " + dataWidth + ", dataHeight = " + dataHeight);

        byte[] data = orientation == Configuration.ORIENTATION_PORTRAIT || optimize ? new byte[original.length] : original;
        short random = 0;
        if (optimize) {
            random = (short) (Math.random() * 4 + 3);
        }

        boolean isBreak = false;
        for (int y = 0; y < dataHeight; y++) {

            if (isBreak)
                break;

            for (int x = 0; x < dataWidth; x++) {

                int i = x + y * dataWidth;
                if (optimize) {
                    byte a = original[i];
                    // 1. 伽马增强
                    byte b = (byte) (255 * Math.pow((a & 0xff) / 255f, 4f));
                    // 2. 线性增强
                    byte c = (byte) (b * random);
                    original[i] = c;
                }

                // 不需要优化, 并且不是竖屏, 跳出嵌套循环
                if (!optimize && orientation != Configuration.ORIENTATION_PORTRAIT) {
                    isBreak = true;
                    break;
                }
                // 竖屏切换坐标值
                else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    int j = x * dataHeight + dataHeight - y - 1;
                    data[j] = original[i];
                }
            }
        }


//        // 优化
//        if (optimize) {
//            short random = (short) (Math.random() * 4 + 3);
//            for (int i = 0; i < dataWidth * dataHeight; i++) {
//                byte a = original[i];
//                // 1. 伽马增强
//                byte b = (byte) (255 * Math.pow((a & 0xff) / 255f, 4f));
//                // 2. 线性增强
//                byte c = (byte) (b * random);
//                original[i] = c;
//            }
//        }
//
//        byte[] data = orientation == Configuration.ORIENTATION_PORTRAIT || optimize ? new byte[original.length] : original;
//
//        // 竖屏
//        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
//            for (int y = 0; y < dataHeight; y++) {
//                for (int x = 0; x < dataWidth; x++) {
//                    data[x * dataHeight + dataHeight - y - 1] = original[x + y * dataWidth];
//                }
//            }
//        }

        return data;
    }
}
