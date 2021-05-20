package lib.kalu.zxing.camerax;

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
