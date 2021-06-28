package lib.kalu.czxing.jni;

import android.graphics.Bitmap;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

@Keep
class Native {

    static {
        System.loadLibrary("czxing");
    }

    private Native() {
    }

    private static class Holder {
        private static final Native instance = new Native();
    }

    public static Native getInstance() {
        return Holder.instance;
    }

    native int readBitmap(@NonNull Bitmap bitmap, @NonNull int left, @NonNull int top, @NonNull int width, @NonNull int height, @NonNull Object[] result);

    native int readBytes(@NonNull byte[] bytes, @NonNull int left, @NonNull int top, @NonNull int width, @NonNull int height, @NonNull int rowWidth, @NonNull int rowHeight, @NonNull Object[] result);

    native int writeBytes(@NonNull String content, @NonNull int width, @NonNull int height, @NonNull int color, @NonNull String format, @NonNull Object[] result);
}
