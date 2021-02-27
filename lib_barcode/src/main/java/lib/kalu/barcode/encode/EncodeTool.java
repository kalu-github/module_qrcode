package lib.kalu.barcode.encode;

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

            int matrixWidth = bitMatrix.getWidth();
            int matrixHeight = bitMatrix.getHeight();
            if (matrixWidth != matrixHeight)
                return null;

            // Bitmap颜色
            int[] pixels = new int[matrixWidth * matrixHeight];

            // logo范围
            int minX = -1;
            int maxX = -1;
            int minY = -1;
            int maxY = -1;

            // 缩放logo
            Bitmap scaleBitmap = null;

            if (null != logo) {

                // logo输出宽度
                int logoOutWidth = matrixWidth / 5;
                // logo输出高度
                int logoOutHeight = matrixHeight / 5;

                // logo范围计算
                minX = Math.abs(matrixWidth - multiple * marginLeft - multiple * marginRight) / 2 - Math.abs(logoOutWidth) / 2;
                minX += marginLeft * multiple;
                maxX = minX + logoOutWidth;
                minY = Math.abs(matrixHeight - multiple * marginTop - multiple * marginBottom) / 2 - Math.abs(logoOutWidth) / 2;
                minY += marginTop * multiple;
                maxY = minY + logoOutHeight;

                // logo真实高度
                int logoRealWidth = logo.getWidth();
                int logoRealHeight = logo.getHeight();

                // Matrix缩放至指定大小
                Matrix matrix = new Matrix();
                float sx = ((float) logoOutHeight) / logoRealWidth;
                float sy = ((float) logoOutHeight) / logoRealHeight;
                matrix.setScale(sx, sy);
                scaleBitmap = Bitmap.createBitmap(logo, 0, 0, logoRealWidth, logoRealHeight, matrix, false);

                if (null != logo && !logo.isRecycled()) {
                    logo.recycle();
                    logo = null;
                }
            }

            // 二维码合成颜色
            for (int y = 0; y < matrixHeight; y++) {

                for (int x = 0; x < matrixWidth; x++) {

                    // LOGO图片
                    if (null != scaleBitmap && x > minX && x < maxX && y > minY && y < maxY) {
                        int pixel = scaleBitmap.getPixel(x - minX, y - minX);
                        pixels[y * matrixHeight + x] = pixel;
                    }
                    // 黑色
                    else if (bitMatrix.get(x, y)) {
                        pixels[y * matrixHeight + x] = 0xff000000;
                    }
                    // 白色
                    else {
                        pixels[y * matrixHeight + x] = 0xffffffff;
                    }
                }
            }

            if (null != scaleBitmap && !scaleBitmap.isRecycled()) {
                scaleBitmap.recycle();
                scaleBitmap = null;
            }

            // step3
            Bitmap createBitmap = Bitmap.createBitmap(matrixWidth, matrixHeight, Bitmap.Config.ARGB_8888);
            createBitmap.setPixels(pixels, 0, matrixWidth, 0, 0, matrixWidth, matrixHeight);

            return createBitmap;

        } catch (Exception e) {
            Log.e("EncodeTool", "createBitmapQrcode => " + e.getMessage(), e);
            return null;
        }
    }
}
