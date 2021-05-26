package lib.kalu.zxing.analyze;

import android.content.Context;
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
    Result analyzeImage(@NonNull Context context, @NonNull ImageProxy imageProxy, int orientation);

    /**
     *
     * @param context
     * @param crop
     * @param cropWidth
     * @param cropHeight
     * @param cropLeft
     * @param cropTop
     * @param originalWidth
     * @param originalHeight
     * @return
     */
    @Nullable
    Result analyzeData(@NonNull Context context, @NonNull byte[] crop, int cropWidth, int cropHeight, int cropLeft, int cropTop, int originalWidth, int originalHeight);

    /**
     *
     * @param context
     * @param crop
     * @param cropWidth
     * @param cropHeight
     * @param cropLeft
     * @param cropTop
     * @param originalWidth
     * @param originalHeight
     * @return
     */
    Result analyzeRect(@NonNull Context context, @NonNull byte[] crop, int cropWidth, int cropHeight, int cropLeft, int cropTop, int originalWidth, int originalHeight);

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

//    /**
//     * 相机预览的每一帧数据, 进行优化
//     *
//     * @param original    相机帧数据
//     * @param dataWidth   相机帧数据, 原始宽
//     * @param dataHeight  相机帧数据, 原始高
//     * @param orientation 方向
//     * @param optimize    优化开关：1. 伽马增强， 2. 线性增强
//     * @return
//     */
//    default byte[] optimize(@NonNull byte[] original, @NonNull int dataWidth, @NonNull int dataHeight, int orientation, boolean optimize) {
//        LogUtil.log("optimize => orientation = " + (orientation == Configuration.ORIENTATION_PORTRAIT ? "竖屏" : "横屏") + ", optimize = " + optimize + ", dataWidth = " + dataWidth + ", dataHeight = " + dataHeight);
//
//        byte[] data = orientation == Configuration.ORIENTATION_PORTRAIT || optimize ? new byte[original.length] : original;
//        short random = 0;
//        if (optimize) {
//            random = (short) (Math.random() * 4 + 3);
//        }
//
//        boolean isBreak = false;
//        for (int y = 0; y < dataHeight; y++) {
//
//            if (isBreak)
//                break;
//
//            for (int x = 0; x < dataWidth; x++) {
//
//                int i = x + y * dataWidth;
//                if (optimize) {
//                    byte a = original[i];
//                    // 1. 伽马增强
//                    byte b = (byte) (255 * Math.pow((a & 0xff) / 255f, 4f));
//                    // 2. 线性增强
//                    byte c = (byte) (b * random);
//                    original[i] = c;
//                }
//
//                // 不需要优化, 并且不是竖屏, 跳出嵌套循环
//                if (!optimize && orientation != Configuration.ORIENTATION_PORTRAIT) {
//                    isBreak = true;
//                    break;
//                }
//                // 竖屏切换坐标值
//                else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
//                    int j = x * dataHeight + dataHeight - y - 1;
//                    data[j] = original[i];
//                }
//            }
//        }
//
//
////        // 优化
////        if (optimize) {
////            short random = (short) (Math.random() * 4 + 3);
////            for (int i = 0; i < dataWidth * dataHeight; i++) {
////                byte a = original[i];
////                // 1. 伽马增强
////                byte b = (byte) (255 * Math.pow((a & 0xff) / 255f, 4f));
////                // 2. 线性增强
////                byte c = (byte) (b * random);
////                original[i] = c;
////            }
////        }
////
////        byte[] data = orientation == Configuration.ORIENTATION_PORTRAIT || optimize ? new byte[original.length] : original;
////
////        // 竖屏
////        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
////            for (int y = 0; y < dataHeight; y++) {
////                for (int x = 0; x < dataWidth; x++) {
////                    data[x * dataHeight + dataHeight - y - 1] = original[x + y * dataWidth];
////                }
////            }
////        }
//
//        return data;
//    }

    /**
     * 直接对相机的原始数据裁剪, 裁剪后的byte[]扔给zxing直接处理
     * 1. 裁剪区域
     * 2. 伽马增强
     * 3. 线性增强
     * 4. 容错竖屏
     *
     * @param original    相机原始帧数据：byte[]
     * @param dataWidth   相机原始帧数据：width
     * @param dataHeight  相机原始帧数据：height
     * @param outWidth    裁剪输出帧数据：width
     * @param outHeight   裁剪输出帧数据：height
     * @param left        裁剪原始帧数据坐标：left
     * @param top         裁剪原始帧数据坐标：top
     * @param orientation 横屏竖屏
     * @return
     */
    default byte[] crop(@NonNull byte[] original, @NonNull int originalWidth, @NonNull int originalHeight, int cropWidth, int cropHeight, int cropLeft, int cropTop, int orientation) {
        LogUtil.log("crop => orientation = " + (orientation == Configuration.ORIENTATION_PORTRAIT ? "竖屏" : "横屏") + ", originalWidth = " + originalWidth + ", originalHeight = " + originalHeight);

        int yMin = cropTop;
        int yMax = yMin + cropHeight;
        int xMin = cropLeft;
        int xMax = cropLeft + cropWidth;

        byte[] crop = new byte[cropWidth * cropHeight];
        short random = (short) (Math.random() * 4 + 3);

        for (int y = yMin; y < yMax; y++) {
            for (int x = xMin; x < xMax; x++) {

                // 下标：原始帧数据
                int indexOriginal = x + y * originalWidth;
                // 下标：裁剪帧数据
                int indexData = (x - xMin) + (y - cropTop) * cropWidth;
//                int indexData;
//                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
//                    indexData = (x - xMin) * outHeight + outHeight - (y - top) - 1;
//                } else {
//                    indexData = (x - xMin) + (y - top) * outWidth;
//                }

                byte a = original[indexOriginal];
                // 1. 伽马增强
                byte b = (byte) (255 * Math.pow((a & 0xff) / 255f, 4f));
                // 2. 线性增强
                byte c = (byte) (b * random);
                // 3. 竖屏切换坐标值
                crop[indexData] = c;
            }
        }
        return crop;
    }
}
