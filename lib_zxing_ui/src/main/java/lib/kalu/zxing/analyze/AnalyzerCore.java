package lib.kalu.zxing.analyze;

import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;

import lib.kalu.zxing.camerax.DecodeConfig;
import lib.kalu.zxing.util.LogUtil;

import androidx.annotation.Nullable;

import java.util.Map;

/**
 * @description:
 * @date: 2021-05-07 14:56
 */
abstract class AnalyzerCore extends AnalyzerImageProxy {

    private float mAreaRectRatio = 0.6F; // 识别区域比例，默认0.6

    public AnalyzerCore() {
    }

    public AnalyzerCore(@Nullable DecodeConfig config) {
        if (null != config) {
            this.mAreaRectRatio = config.getRectRatio();
        }
    }

    @Nullable
    @Override
    public Result analyze(byte[] data, int width, int height) {

        // 全屏扫描
        if (mAreaRectRatio >= 1F) {
            LogUtil.log("analyze[全屏扫描] => ");
            return analyze(data, width, height, 0, 0, width, height);
        }
        // 区域扫描
        else {
            float min = Math.min(height, width) * (mAreaRectRatio * 1.1F);
            float left = width * 0.5f - min * 0.5f;
            float top = height * 0.5f - min * 0.5f;
            float right = left + min;
            float bottom = top + min;
            LogUtil.log("analyze[区域扫描] => left = " + left + ", top = " + top + ", right = " + right + ", bottom = " + bottom);
            return analyze(data, width, height, (int) left, (int) top, (int) right, (int) bottom);
        }
    }
}
