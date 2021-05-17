package lib.kalu.zxing.listener;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.google.zxing.Result;

/**
 * @description:
 * @date:  2021-05-17 13:53
 */
@Keep
public interface OnCameraScanChangeListener {

    boolean onResult(@NonNull Result result);
}
