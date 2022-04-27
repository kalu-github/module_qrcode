package com.huan.zxing.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogUtil {

    public static final void log(@NonNull String tag, @Nullable String msg) {

        if (null == tag || tag.length() == 0 || null == msg || msg.length() == 0)
            return;

        Logger.getLogger("moudle_zxing_source[" + tag + "]").log(Level.INFO, msg);
    }
}
