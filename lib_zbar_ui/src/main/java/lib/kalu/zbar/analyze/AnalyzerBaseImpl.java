package lib.kalu.zbar.analyze;

import android.content.Context;
import android.content.res.Configuration;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.ImageProxy;

import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;

import lib.kalu.zbar.util.LogUtil;

/**
 * @description: 分析器
 * @date: 2021-05-07 14:56
 */
public interface AnalyzerBaseImpl {

    @Nullable
    Symbol analyzeImage(@NonNull Context context, @NonNull ImageProxy imageProxy, int orientation);

    /**
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
    Symbol analyzeData(@NonNull Context context, @NonNull byte[] crop, int cropWidth, int cropHeight, int cropLeft, int cropTop, @NonNull byte[] original, int originalWidth, int originalHeight);

    /**
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
    Symbol analyzeRect(@NonNull Context context, @NonNull byte[] crop, int cropWidth, int cropHeight, int cropLeft, int cropTop, @NonNull byte[] original, int originalWidth, int originalHeight);

    Symbol decodeRect(@NonNull Context context, @NonNull byte[] crop, int cropWidth, int cropHeight);

    Symbol decodeFull(@NonNull Context context, @NonNull byte[] original, int originalWidth, int originalHeight);

    @Nullable
    ImageScanner createReader();

    /**
     * 默认全屏扫描
     *
     * @return
     */
    float ratio();

    default byte[] crop(@NonNull byte[] original, @NonNull int originalWidth, @NonNull int originalHeight, int cropWidth, int cropHeight, int cropLeft, int cropTop, int orientation, boolean isGM, boolean isXX) {
        LogUtil.log("crop => orientation = " + (orientation == Configuration.ORIENTATION_PORTRAIT ? "竖屏" : "横屏") + ", originalWidth = " + originalWidth + ", originalHeight = " + originalHeight);

        int yMin = cropTop;
        if (yMin < 0) {
            yMin = 0;
        }
        int yMax = yMin + cropHeight;
        if (yMin > originalHeight) {
            yMin = originalHeight;
        }
        int xMin = cropLeft;
        if (xMin < 0) {
            xMin = 0;
        }
        int xMax = cropLeft + cropWidth;
        if (xMax > originalWidth) {
            xMax = originalWidth;
        }

        byte[] crop = new byte[cropWidth * cropHeight];
        short random = 0;
        if (isXX) {
            random = (short) (Math.random() * 4 + 3);
        }

        for (int y = yMin; y < yMax; y++) {
            for (int x = xMin; x < xMax; x++) {

                int indexOriginal = x + y * originalWidth;
                int indexData;
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    indexData = (x - xMin) * cropHeight + cropHeight - (y - cropTop) - 1;
                } else {
                    indexData = (x - xMin) + (y - cropTop) * cropWidth;
                }

                if (isGM && isXX) {
                    byte a = original[indexOriginal];
                    byte b = (byte) (255 * Math.pow((a & 0xff) / 255f, 4f));
                    byte c = (byte) (b * random);
                    crop[indexData] = c;
                } else if (isGM) {
                    byte a = original[indexOriginal];
                    byte b = (byte) (255 * Math.pow((a & 0xff) / 255f, 4f));
                    crop[indexData] = b;
                } else if (isXX) {
                    byte a = original[indexOriginal];
                    byte c = (byte) (a * random);
                    crop[indexData] = c;
                } else {
                    crop[indexData] = original[indexOriginal];
                }
            }
        }
        return crop;
    }
}
