package lib.kalu.czxing.qrcode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lib.kalu.czxing.jni.BarcodeFormat;
import lib.kalu.czxing.jni.BarcodeWriter;

/**
 * description: 创建二维码
 * create by kalu on 2019/1/30 13:08
 */
class EncodeTool {

    public static Bitmap createBitmapQrcode(
            @NonNull Context context,
            @NonNull String text,
            @Nullable Bitmap logo) {
        return createBitmapQrcode(context, text, 3, 0, 0, 0, 0, logo);
    }

    public static Bitmap createBitmapQrcode(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @Nullable Bitmap logo) {
        return createBitmapQrcode(context, text, multiple, 0, 0, 0, 0, logo);
    }

    public static Bitmap createBitmapQrcode(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int margin,
            @Nullable Bitmap logo) {
        return createBitmapQrcode(context, text, multiple, margin, margin, margin, margin, logo);
    }

    public static Bitmap createBitmapQrcode(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginLeft,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginTop,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginRight,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginBottom,
            @Nullable Bitmap logo) {

        if (null == context || null == text || text.length() == 0)
            return null;

        try {

            Bitmap bitmap = BarcodeWriter.getInstance().write(text, 600, 600, Color.BLACK, BarcodeFormat.QRCode, logo);
            return bitmap;
        } catch (Exception e) {
            Log.e("EncodeTool", "createBitmapQrcode => " + e.getMessage(), e);
            return null;
        }
    }
}
