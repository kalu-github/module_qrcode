package lib.kalu.qrcode.util;

import android.util.Log;

import androidx.annotation.Nullable;

import lib.kalu.qrcode.BuildConfig;

/**
 * description:
 * created by kalu on 2021-02-26
 */
public final class LogUtil {

    public static final String MOULDE_ZXING_TAG = "moulde_zxing";
    public static final String MOULDE_ZXING_BUILD_RELEASE = "release";

    public static final void log(@Nullable String info) {

        try {

            if (!BuildConfig.DEBUG)
                return;

            if (MOULDE_ZXING_BUILD_RELEASE.equals(BuildConfig.BUILD_TYPE))
                return;

            if (null == info || info.length() == 0)
                return;

            Log.d(MOULDE_ZXING_TAG, info);

        } catch (Exception e) {
        }
    }
}