package com.google.zxing.android.encode;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.FileOutputStream;

/**
 * description: 创建二维码
 * create by kalu on 2019/1/30 13:08
 */
public class EncodeUtil {

    public static Bitmap encode(String str) {
        return encode(str, 500);
    }

    public static Bitmap encode(String str, int size) {
        return encode(str, size, null);
    }

    public static Bitmap encode(String str, Resources resources, int logo) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, logo, options);
        int sampleSize = options.outHeight / 400;
        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            options.outConfig = Bitmap.Config.RGB_565;
        }
        options.inJustDecodeBounds = false;
        return encode(str, 500, BitmapFactory.decodeResource(resources, logo, options));
    }

    public static Bitmap encode(String str, Bitmap logo) {
        return encode(str, 500, logo);
    }

    public static Bitmap encode(String str, Resources resources, int size, int logo) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, logo, options);
        int sampleSize = options.outHeight / 400;
        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            options.outConfig = Bitmap.Config.RGB_565;
        }
        options.inJustDecodeBounds = false;
        return encode(str, size, BitmapFactory.decodeResource(resources, logo, options));
    }

    public static @Nullable
    String encodeDrawable(@NonNull Context context, @NonNull String str, @DrawableRes int resId) {

        try {

            Resources resources = context.getResources();

            BitmapFactory.Options options = new BitmapFactory.Options();
            //  TypedValue value = new TypedValue();
            // context.getResources().openRawResource(resId, value);
            //  options.inTargetDensity = value.density;
            options.inScaled = false;//不缩放

            Bitmap logo = BitmapFactory.decodeResource(resources, resId, options);

//            Paint paint = new Paint();
//            Canvas canvas = new Canvas();
//            paint.setStyle(Paint.Style.STROKE);   //空心
//            paint.setAlpha(75);   //
//            //canvas.drawBitmap ( vBitmap , 50, 100, null );  //无透明
//            canvas.drawBitmap(logo, 50, 200, paint);  //有透明

            Bitmap bitmap = encode(str, 500, logo);

            if (null == bitmap && null != logo) {
                logo.recycle();
                Log.e("EncodeUtil", "encode => recycle1");
            }

            File dir = context.getFilesDir();
            if (!dir.exists() || !dir.isDirectory()) {
                dir.mkdir();
                Log.e("EncodeUtil", "encode => 生成文件目录");
            }

            String parent = dir.getAbsolutePath();
            String child = "qrLogo.png";

            File file = new File(parent, child);
            if (file.exists()) {
                file.delete();
                Log.e("EncodeUtil", "encode => 删除文件缓存");
            }

            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.flush();
            fos.close();
            Log.e("EncodeUtil", "encode => 关闭流");

            if (null != logo) {
                logo.recycle();
                Log.e("EncodeUtil", "encode => recycle2");
            }

            if (null != bitmap) {
                bitmap.recycle();
                Log.e("EncodeUtil", "encode => recycle3");
            }

            return parent + File.separator + child;

        } catch (Exception e) {
            Log.e("EncodeUtil", "encode => " + e.getMessage(), e);
            return null;
        }
    }

    public static @Nullable
    String encodeRaw(@NonNull Context context, @NonNull String str, @RawRes int resId) {

        try {

            Resources resources = context.getResources();

            BitmapFactory.Options options = new BitmapFactory.Options();
            TypedValue value = new TypedValue();
            context.getResources().openRawResource(resId, value);
            options.inTargetDensity = value.density;
            options.inScaled = false;//不缩放

            Bitmap logo = BitmapFactory.decodeResource(resources, resId, options);

//            Paint paint = new Paint();
//            Canvas canvas = new Canvas();
//            paint.setStyle(Paint.Style.STROKE);   //空心
//            paint.setAlpha(75);   //
//            //canvas.drawBitmap ( vBitmap , 50, 100, null );  //无透明
//            canvas.drawBitmap(logo, 50, 200, paint);  //有透明

            Bitmap bitmap = encode(str, 500, logo);

            if (null == bitmap && null != logo) {
                logo.recycle();
                Log.e("EncodeUtil", "encode => recycle1");
            }

            File dir = context.getFilesDir();
            if (!dir.exists() || !dir.isDirectory()) {
                dir.mkdir();
                Log.e("EncodeUtil", "encode => 生成文件目录");
            }

            String parent = dir.getAbsolutePath();
            String child = "qrLogo.png";

            File file = new File(parent, child);
            if (file.exists()) {
                file.delete();
                Log.e("EncodeUtil", "encode => 删除文件缓存");
            }

            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.flush();
            fos.close();
            Log.e("EncodeUtil", "encode => 关闭流");

            if (null != logo) {
                logo.recycle();
                Log.e("EncodeUtil", "encode => recycle2");
            }

            if (null != bitmap) {
                bitmap.recycle();
                Log.e("EncodeUtil", "encode => recycle3");
            }

            return parent + File.separator + child;

        } catch (Exception e) {
            Log.e("EncodeUtil", "encode => " + e.getMessage(), e);
            return null;
        }
    }

    public static @Nullable
    String encodeUrl(@NonNull Context context, @NonNull String str, @NonNull String url) {

        try {

            return null;

        } catch (Exception e) {
            Log.e("EncodeUtil", "encode => " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * 生成Bitmap
     *
     * @param str
     * @param size
     * @param logo
     * @return
     */
    private static @Nullable
    Bitmap encode(@NonNull String str, @IntRange(from = 100, to = 10000) int size, @Nullable Bitmap logo) {

        // 容错
        if (TextUtils.isEmpty(str))
            return null;

        try {

            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(str, size, size, 0, null == logo ? ErrorCorrectionLevel.L : ErrorCorrectionLevel.H);

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

                int IMAGE_HALFWIDTH = size / 10;
                int width = bitMatrix.getWidth();//矩阵高度
                int height = bitMatrix.getHeight();//矩阵宽度
                int halfW = width / 2;
                int halfH = height / 2;
                Matrix m = new Matrix();
                float sx = (float) 2 * IMAGE_HALFWIDTH / logo.getWidth();
                float sy = (float) 2 * IMAGE_HALFWIDTH / logo.getHeight();
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
            Log.e("EncodeUtil", "encode => " + e.getMessage(), e);
            return null;
        }
    }
}
