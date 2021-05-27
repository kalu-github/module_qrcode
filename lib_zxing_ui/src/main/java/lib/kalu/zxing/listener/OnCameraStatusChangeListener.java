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

    boolean onResult(@NonNull Result result);

    /**
     * 手电筒
     *
     * @param open
     */
    void onFlash(boolean dark, float lightLux);
}
