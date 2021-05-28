package lib.kalu.zxing.listener;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.google.zxing.Result;

/**
 * @description:
 * @date: 2021-05-17 13:53
 */
@Keep
public interface OnCameraStatusChangeListener {

    /**
     * 结果
     *
     * @param result
     */
    void onResult(@NonNull Result result);

    /**
     * 光线传感器
     *
     * @param dark
     * @param lightLux
     */
    void onSensor(boolean dark, float lightLux);
}
