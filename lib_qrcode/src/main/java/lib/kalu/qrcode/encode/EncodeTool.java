package lib.kalu.qrcode.encode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.zxing.EncodeHintType;
import com.google.zxing.barcode.QRCodeWriter;
import com.google.zxing.barcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.common.BitMatrix;

import java.util.Map;

/**
 * description: 创建二维码
 * create by kalu on 2019/1/30 13:08
 */
public final class EncodeTool {

    public static Bitmap createBitmapQrcode(
            @NonNull Context context,
            @NonNull String text,
            @Nullable Bitmap logo) {
        return createBitmapQrcode(context, text, 3, 0, 0, 0, 0, ErrorCorrectionLevel.M, null, logo);
    }

    public static Bitmap createBitmapQrcode(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @Nullable Bitmap logo) {
        return createBitmapQrcode(context, text, multiple, 0, 0, 0, 0, ErrorCorrectionLevel.M, null, logo);
    }

    public static Bitmap createBitmapQrcode(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int margin,
            @Nullable Bitmap logo) {
        return createBitmapQrcode(context, text, multiple, margin, margin, margin, margin, ErrorCorrectionLevel.M, null, logo);
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
        return createBitmapQrcode(context, text, multiple, marginLeft, marginTop, marginRight, marginBottom, ErrorCorrectionLevel.M, null, logo);
    }

    public static Bitmap createBitmapQrcode(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginLeft,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginTop,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginRight,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginBottom,
            @NonNull ErrorCorrectionLevel level,
            @Nullable Bitmap logo) {
        return createBitmapQrcode(context, text, multiple, marginLeft, marginTop, marginRight, marginBottom, level, null, logo);
    }

    public static Bitmap createBitmapQrcode(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginLeft,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginTop,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginRight,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginBottom,
            @NonNull ErrorCorrectionLevel level,
            @Nullable Map<EncodeHintType, ?> hints,
            @Nullable Bitmap logo) {

        if (null == context || null == text || text.length() == 0)
            return null;

        try {

            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(text, multiple, marginLeft, marginTop, marginRight, marginBottom, level, hints);

            int size = bitMatrix.getWidth();

            // step2
            int[] pixels = new int[size * size];
            if (null == logo) {
                for (int y = 0; y < size; y++) {
                    for (int x = 0; x < size; x++) {
                        if (bitMatrix.get(x, y)) {
                            pixels[y * size + x] = 0xff000000;
                        } else {
                            pixels[y * size + x] = 0xffffffff;
                        }
                    }
                }
            } else {

                int IMAGE_HALFWIDTH = size / 8;
                int width = bitMatrix.getWidth();//矩阵高度
                int height = bitMatrix.getHeight();//矩阵宽度
                int halfW = width / 2;
                int halfH = height / 2;
                Matrix m = new Matrix();
                float sx = 2f * IMAGE_HALFWIDTH / logo.getWidth();
                float sy = 2f * IMAGE_HALFWIDTH / logo.getHeight();
                m.setScale(sx, sy);
                //设置缩放信息
                //将logo图片按martix设置的信息缩放
                logo = Bitmap.createBitmap(logo, 0, 0, logo.getWidth(), logo.getHeight(), m, false);

                for (int y = 0; y < size; y++) {
                    for (int x = 0; x < size; x++) {
                        if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                                && y > halfH - IMAGE_HALFWIDTH
                                && y < halfH + IMAGE_HALFWIDTH) {
                            //该位置用于存放图片信息
                            //记录图片每个像素信息
                            pixels[y * width + x] = logo.getPixel(x - halfW + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
                        } else {
                            if (bitMatrix.get(x, y)) {
                                pixels[y * size + x] = 0xff000000;
                            } else {
                                pixels[y * size + x] = 0xffffffff;
                            }
                        }
                    }
                }
            }

            // step3
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);

            return bitmap;

        } catch (Exception e) {
            Log.e("EncodeTool", "createBitmapQrcode => " + e.getMessage(), e);
            return null;
        }
    }
}
