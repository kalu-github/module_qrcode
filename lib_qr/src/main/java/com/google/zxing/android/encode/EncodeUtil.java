package com.google.zxing.android.encode;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
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
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * description: 创建二维码
 * create by kalu on 2019/1/30 13:08
 */
public class EncodeUtil {

    public static Bitmap encode(String str) {
        return encode(str, 500);
    }

    public static Bitmap encode(String str, int size) {
        return createQr(str, size, null);
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
        return createQr(str, 500, BitmapFactory.decodeResource(resources, logo, options));
    }

    public static Bitmap encode(String str, Bitmap logo) {
        return createQr(str, 500, logo);
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
        return createQr(str, size, BitmapFactory.decodeResource(resources, logo, options));
    }

    public static @Nullable
    String encodeDrawable(@NonNull Context context, @NonNull String str, @DrawableRes int resId) {

        try {

            Bitmap logo = createLogoWhiteEdgeDrawable(context, resId, 100);
            if (null == logo)
                return null;

            Bitmap bitmap = createQr(str, 500, logo);

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

            Bitmap logo = createLogoWhiteEdgeRaw(context, resId, 100);
            if (null == logo)
                return null;

            Bitmap bitmap = createQr(str, 500, logo);

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

            Bitmap logo = createLogoWhiteEdgeUrl(context, url, 100);
            if (null == logo)
                return null;

            Bitmap bitmap = createQr(str, 500, logo);

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

    /**
     * bitmap 描白边
     *
     * @param border
     * @return
     */
    private static @Nullable
    Bitmap createLogoWhiteEdgeUrl(@NonNull Context context, @NonNull String url, @IntRange(from = 0, to = 100) int border) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;//不缩放
        options.inJustDecodeBounds = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            options.outConfig = Bitmap.Config.RGB_565;
        }

        Bitmap bitmap = null;

        try {

            // 网络图片
            bitmap = Executors.newSingleThreadExecutor().submit(new Callable<Bitmap>() {
                @Override
                public Bitmap call() {
                    try {

                        InputStream is = new URL(url).openStream();
                        Bitmap logo = BitmapFactory.decodeStream(is, null, options);

                        if (null != is) {
                            is.close();
                            Log.e("EncodeUtil", "encode => recycle4");
                        }

                        return logo;

                    } catch (Exception e) {

                        Log.e("EncodeUtil", "encode => " + e.getMessage(), e);
                        return null;
                    }
                }
            }).get();

        } catch (Exception e) {

            Log.e("EncodeUtil", "encode => " + e.getMessage(), e);
        }

        Bitmap logo = createWhiteEdge(context, bitmap, options, border);
        return logo;
    }

    /**
     * bitmap 描白边
     *
     * @param resId
     * @param border
     * @return
     */
    private static @Nullable
    Bitmap createLogoWhiteEdgeDrawable(@NonNull Context context, @DrawableRes int resId, @IntRange(from = 0, to = 100) int border) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;//不缩放
        options.inJustDecodeBounds = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            options.outConfig = Bitmap.Config.RGB_565;
        }

        Resources resources = context.getResources();
        BitmapFactory.decodeResource(resources, resId, options);
        Bitmap bitmap = BitmapFactory.decodeResource(resources, resId, options);

        Bitmap logo = createWhiteEdge(context, bitmap, options, border);
        return logo;
    }

    /**
     * bitmap 描白边
     *
     * @param resId
     * @param border
     * @return
     */
    private static @Nullable
    Bitmap createLogoWhiteEdgeRaw(@NonNull Context context, @RawRes int resId, @IntRange(from = 0, to = 100) int border) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;//不缩放
        options.inJustDecodeBounds = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            options.outConfig = Bitmap.Config.RGB_565;
        }

        TypedValue value = new TypedValue();
        context.getResources().openRawResource(resId, value);
        options.inTargetDensity = value.density;
//        BitmapFactory.decodeResource(resources, logo, options);

        Resources resources = context.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(resources, resId, options);


        Bitmap logo = createWhiteEdge(context, bitmap, options, border);
        return logo;
    }


    /**
     * 描白边
     *
     * @param context
     * @param bitmap
     * @param options
     * @param border
     * @return
     */
    private static @Nullable
    Bitmap createWhiteEdge(@NonNull Context context, @Nullable Bitmap bitmap, @NonNull BitmapFactory.Options options, @IntRange(from = 0, to = 100) int border) {

        if (null == bitmap)
            return null;

        // 不需要白边
        if (border == 0) {
            return bitmap;
        }

        // 边框容错
        int size = Math.min(options.outWidth, options.outHeight) / 10;
        if (border > size) {
            border = size;
        }

        int width = options.outWidth + 2 * border;
        int height = options.outHeight + 2 * border;
        //创建一个空的Bitmap(内存区域),宽度等于第一张图片的宽度，高度等于两张图片高度总和
        Bitmap logo = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        //将bitmap放置到绘制区域,并将要拼接的图片绘制到指定内存区域
        Canvas canvas = new Canvas(logo);
        Rect src = new Rect(0, 0, options.outWidth, options.outHeight);
        Rect dst = new Rect(border, border, options.outWidth + border, options.outHeight + border);
        canvas.drawBitmap(bitmap, src, dst, null);

        Paint paint = new Paint();
        //设置边框颜色
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        //设置边框宽度
        paint.setStrokeWidth(border);
        canvas.drawRect(border * 0.5f, border * 0.5f, width - border * 0.5f, height - border * 0.5f, paint);

        if (null != bitmap) {
            bitmap.recycle();
        }

        return logo;
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
    Bitmap createQr(@NonNull String str, @IntRange(from = 100, to = 10000) int size, @Nullable Bitmap logo) {

        // 容错
        if (TextUtils.isEmpty(str))
            return null;

        try {

            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(str, size, size, null == logo ? ErrorCorrectionLevel.L : ErrorCorrectionLevel.H);

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
