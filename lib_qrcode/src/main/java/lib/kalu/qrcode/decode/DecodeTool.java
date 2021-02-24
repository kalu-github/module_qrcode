package lib.kalu.qrcode.decode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.io.FileNotFoundException;
import java.nio.charset.Charset;

/**
 * description: zxingutil
 * create by kalu on 2019/1/25 17:10
 */
public class DecodeTool {

    public static String decode(String path) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int sampleSize = options.outHeight / 400;
        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return decode(bitmap);
    }

    public static String decode(final Bitmap bitmap) {

        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
            Result result = QRCodeReader.obtain().decode(new BinaryBitmap(new HybridBinarizer(source)));
            if (result != null) {

                String str = result.getText();
                boolean ISO = Charset.forName("ISO-8859-1").newEncoder().canEncode(str);
                if (ISO) {
                    return new String(str.getBytes("ISO-8859-1"), "GB2312");
                } else {
                    return str;
                }
            } else {
                return null;
            }

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param uri：图片的本地url地址
     * @return Bitmap；
     */
    public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
//            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
            //压缩图片
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
            options.inSampleSize = calculateInSampleSize(options, 720, 1280);
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            //使用需要的宽高的最大值来计算比率
            final int suitedValue = reqHeight > reqWidth ? reqHeight : reqWidth;
            final int heightRatio = Math.round((float) height / (float) suitedValue);
            final int widthRatio = Math.round((float) width / (float) suitedValue);

            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;//用最大
        }
        return inSampleSize;
    }
}
