package lib.kalu.czxing.util;

import android.util.Log;

import androidx.annotation.Nullable;

import lib.kalu.czxing.jni.BuildConfig;

/**
 * description:
 * created by kalu on 2021-02-26
 */
public final class LogUtil {
    private static final String TAG = "moulde_czxing_ui";

    public static final void log(String info) {

        log(info, null);
    }

    public static final void log(String info, Throwable throwable) {

        try {

            if (!lib.kalu.czxing.jni.BuildConfig.DEBUG || "release".equalsIgnoreCase(BuildConfig.BUILD_TYPE))
                return;

            if (null == info || info.length() == 0)
                return;

            Log.d(TAG, info, throwable);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage(), e);
        }
    }
}
