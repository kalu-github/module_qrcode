package lib.kalu.czxing.util;

import android.content.Context;
import android.os.Vibrator;

import androidx.annotation.NonNull;

/**
 * @description: Vibrator
 * @date: 2021-05-27 15:59
 */
public final class VibratorUtil {

    // 默认震动
    public static boolean vibrate = true;

    public static final void vibrator(@NonNull Context context) {

        if (!vibrate)
            return;

        try {
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(20);
        } catch (Exception e) {
            LogUtil.log("vibrator => " + e.getMessage(), e);
        }
    }

}
