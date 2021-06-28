package lib.kalu.zbar.util;

import android.util.Log;
import androidx.annotation.Nullable;

import lib.kalu.zbar.BuildConfig;

/**
 * description:
 * created by kalu on 2021-02-26
 */
public final class LogUtil {

    public static final void log(@Nullable String info) {

        log(info, null);
    }

    public static final void log(@Nullable String info, @Nullable Throwable throwable) {

        try {

            if (!BuildConfig.DEBUG || "release".equalsIgnoreCase(BuildConfig.BUILD_TYPE) || null == info || info.length() == 0)
                return;

            Log.d("moulde_zbar", info, throwable);

        } catch (Exception e) {
            Log.d("moulde_zbar", e.getMessage(), e);
        }
    }
}
