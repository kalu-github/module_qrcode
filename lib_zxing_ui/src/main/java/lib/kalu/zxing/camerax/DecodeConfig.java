package lib.kalu.zxing.camerax;

import android.graphics.Rect;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;

import java.util.Map;

import androidx.annotation.FloatRange;

/**
 * @description:
 * @date: 2021-05-17 10:57
 */
public final class DecodeConfig {

    /**
     * 识别区域比例，默认0.6
     */
    private float rectRatio = 0.6F;

    public float getRectRatio() {
        return rectRatio;
    }

    public void setRectRatio(float rectRatio) {
        this.rectRatio = rectRatio;
    }

    public void setFull(boolean isFull) {
        rectRatio = isFull ? 1F : rectRatio;
    }
}
