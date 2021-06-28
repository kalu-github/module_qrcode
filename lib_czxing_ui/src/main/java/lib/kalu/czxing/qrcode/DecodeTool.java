package lib.kalu.czxing.qrcode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;

import lib.kalu.czxing.jni.BarcodeReader;
import lib.kalu.czxing.jni.BarcodeResult;
import lib.kalu.czxing.util.LogUtil;

/**
 * description: zxingutil
 * create by kalu on 2019/1/25 17:10
 */
class DecodeTool {

    public static String decodeQrcodeFromFile(@NonNull String filePath) {

        if (null == filePath || filePath.length() == 0)
            return null;

        File file = new File(filePath);
        if (!file.exists() || !file.isFile())
            return null;

//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(filePath, options);
//        int sampleSize = options.outHeight / 400;
//        if (sampleSize <= 0)
//            sampleSize = 1;
//        options.inSampleSize = sampleSize;
//        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, null);
        return decodeQrcodeFromBitmap(bitmap);
    }

    /**
     * @param uri：图片的本地url地址
     * @return Bitmap；
     */
    public static String decodeQrcodeFromUri(@NonNull Context context, @NonNull Uri uri) {

        try {

//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
//            options.inSampleSize = 2;
//            options.inJustDecodeBounds = false;

            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, null);
            return decodeQrcodeFromBitmap(bitmap);
        } catch (Exception e) {
            LogUtil.log("decodeQrcodeFromUri => " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * @param bitmap
     * @return
     */
    private static String decodeQrcodeFromBitmap(@NonNull Bitmap bitmap) {

        if (null == bitmap || bitmap.isRecycled())
            return null;

        try {

            BarcodeResult result = BarcodeReader.getInstance().read(bitmap);

            if (null != bitmap || !bitmap.isRecycled()) {
                bitmap.recycle();
            }

            if (null == result)
                return null;

            String text = result.getText();
            if (null == text || text.length() == 0)
                return null;

            boolean ISO = Charset.forName("ISO-8859-1").newEncoder().canEncode(text);
            if (ISO) {
                return new String(text.getBytes("ISO-8859-1"), "GB2312");
            } else {
                return text;
            }

        } catch (Exception e) {
            LogUtil.log("decodeQrcodeFromBitmap => " + e.getMessage(), e);
            return null;
        }
    }

//    // 计算图片的缩放值
//    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//        if (height > reqHeight || width > reqWidth) {
//            //使用需要的宽高的最大值来计算比率
//            final int suitedValue = reqHeight > reqWidth ? reqHeight : reqWidth;
//            final int heightRatio = Math.round((float) height / (float) suitedValue);
//            final int widthRatio = Math.round((float) width / (float) suitedValue);
//
//            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;//用最大
//        }
//        return inSampleSize;
//    }
}
