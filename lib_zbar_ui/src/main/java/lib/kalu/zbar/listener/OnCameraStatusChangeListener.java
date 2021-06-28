package lib.kalu.zbar.listener;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import net.sourceforge.zbar.Symbol;

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
    void onResult(@NonNull Symbol result);

    /**
     * 光线传感器
     *
     * @param dark
     * @param lightLux
     */
    void onSensor(boolean dark, float lightLux);
}
