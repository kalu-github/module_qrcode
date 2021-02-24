package lib.kalu.qrcode.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import lib.kalu.qrcode.encode.EncodeTool;

/**
 * description: 创建二维码
 * create by kalu on 2019/1/30 13:08
 */
@Keep
public final class ZxingUtil {

    /**
     * @param context 上下文
     * @param message 二维码信息
     * @param size    二维码大小
     * @param url     logo-url网络图片地址
     * @return
     */
    @Keep
    public static String createQrcodeFromUrl(@NonNull Context context, @NonNull String message, @IntRange(from = 100, to = 4000) int size, @NonNull String url) {

        // 检测当前线程是否是分线程
        if (Looper.getMainLooper() == Looper.myLooper())
            throw new RuntimeException("createQrcodeFromUrl 当前方法必须在分线程执行");

        InputStream inputStream = null;

        // 网络图片
        try {
            inputStream = Executors.newFixedThreadPool(1).submit(new Callable<InputStream>() {
                @Override
                public InputStream call() {

                    try {
                        InputStream openStream = new URL(url).openStream();
                        return openStream;
                    } catch (Exception e) {
                        Log.e("ZxingUtil", "createQrcodeFromUrl => " + e.getMessage(), e);
                        return null;
                    }
                }
            }).get();

        } catch (Exception e) {
            Log.e("ZxingUtil", "createQrcodeFromUrl => " + e.getMessage(), e);
        }

        String qrcode = createQrcodeFromInputStream(context, message, size, inputStream);
        return qrcode;
    }

    /**
     * @param context  上下文
     * @param message  二维码信息
     * @param size     二维码大小
     * @param filePath logo-本地图片地址
     * @return
     */
    @Keep
    public static String createQrcodeFromFile(@NonNull Context context, @NonNull String message, @IntRange(from = 100, to = 4000) int size, @NonNull String filePath) {

        FileInputStream fis = null;

        if (null != filePath && filePath.length() > 0) {
            File file = new File(filePath);
            if (null != file && file.exists() && file.isFile()) {
                try {
                    fis = new FileInputStream(file);
                    byte[] bytes = new byte[1024];
                    //循环读取
                    while (fis.read(bytes) != -1) {
                    }
                } catch (Exception e) {
                    Log.e("ZxingUtil", "createQrcodeFromFile => " + e.getMessage(), e);
                }
            }
        }

        String qrcode = createQrcodeFromInputStream(context, message, size, fis);
        if (null != fis) {
            try {
                fis.close();
                fis = null;
            } catch (Exception e) {
                Log.e("ZxingUtil", "createQrcodeFromFile => " + e.getMessage(), e);
            }
        }

        return qrcode;
    }


    /**
     * @param context 上下文
     * @param message 二维码信息
     * @param size    二维码大小
     * @param base64  logo-base64字符串
     * @return
     */
    @Keep
    public static String createQrcodeFromBase64(@NonNull Context context, @NonNull String message, @IntRange(from = 100, to = 4000) int size, @NonNull String base64) {

        InputStream inputStream = null;
        if (null != base64 && base64.length() > 0) {
            inputStream = base64ToInputStream(base64);
        }

        String qrcode = createQrcodeFromInputStream(context, message, size, inputStream);

        if (null != inputStream) {
            try {
                inputStream.close();
                inputStream = null;
            } catch (Exception e) {
            }
        }

        return qrcode;
    }

    /**
     * @param context 上下文
     * @param message 二维码信息
     * @param size    二维码大小
     * @param raw     logo-raw文件夹
     * @return
     */
    @Keep
    public static String createQrcodeFromRaw(@NonNull Context context, @NonNull String message, @IntRange(from = 100, to = 4000) int size, @RawRes int raw) {

        if (null == context)
            return null;

        InputStream inputStream = context.getResources().openRawResource(raw);
        String qrcode = createQrcodeFromInputStream(context, message, size, inputStream);
        return qrcode;
    }

    /**
     * @param context 上下文
     * @param message 二维码信息
     * @param size    二维码大小
     * @param assets  logo-assets文件夹
     * @return
     */
    @Keep
    public static String createQrcodeFromAssets(@NonNull Context context, @NonNull String message, @IntRange(from = 100, to = 4000) int size, @NonNull String assets) {

        if (null == context || null == assets || assets.length() == 0)
            return null;

        InputStream inputStream = null;
        try {
            inputStream = context.getResources().getAssets().open(assets);
        } catch (Exception e) {
            Log.e("ZxingUtil", "createQrcodeFromAssets => " + e.getMessage(), e);
        }

        String qrcode = createQrcodeFromInputStream(context, message, size, inputStream);
        return qrcode;
    }

    /**
     * @param context     上下文
     * @param message     二维码信息
     * @param size        二维码大小
     * @param inputStream logo-inputStream流
     * @return
     */
    @Keep
    public static String createQrcodeFromInputStream(@NonNull Context context, @NonNull String message, @IntRange(from = 100, to = 4000) int size, @NonNull InputStream inputStream) {

        if (null == context)
            return null;

        Bitmap logoBitmap = createBitmapLogo(context, inputStream, 14, Color.WHITE);
        String qrcode = createQrcode(context, message, size, logoBitmap);

        if (null != logoBitmap) {
            logoBitmap.recycle();
            logoBitmap = null;
        }

        return qrcode;
    }

    /**
     * @param context 上下文
     * @param message 二维码信息
     * @param size    二维码大小
     * @return
     */
    @Keep
    public static String createQrcode(@NonNull Context context, @NonNull String message, @IntRange(from = 100, to = 4000) int size) {

        String qrcode = createQrcode(context, message, size, null);
        return qrcode;
    }

    /**
     * @param context 上下文
     * @param message 二维码信息
     * @param size    二维码大小
     * @param logo    logo-bitmap
     * @return
     */
    @Keep
    public static String createQrcode(@NonNull Context context, @NonNull String message, @IntRange(from = 100, to = 4000) int size, @Nullable Bitmap logo) {

        if (null == context || null == message || message.length() == 0)
            return null;

        Bitmap bitmapQrcode = EncodeTool.createBitmapQrcode(context, message, size, logo);
        String saveBitmapLocal = saveBitmapLocal(context, bitmapQrcode);

        if (null != logo) {
            logo.recycle();
            logo = null;
        }

        return saveBitmapLocal;
    }

    /**
     * 保存bitmap至本地
     *
     * @param context
     * @param bitmap
     * @return
     */
    @Keep
    private static String saveBitmapLocal(@NonNull Context context, @NonNull Bitmap bitmap) {

        if (null == context || null == bitmap)
            return null;

        try {

            File dir = context.getFilesDir();
            if (!dir.exists() || !dir.isDirectory()) {
                dir.mkdir();
            }

            String parent = dir.getAbsolutePath();
            String child = "temp_qrcode.png";

            File file = new File(parent, child);
            if (file.exists()) {
                file.delete();
            }

            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.flush();
            fos.close();

            if (null != bitmap) {
                bitmap.recycle();
            }

            return parent + File.separator + child;

        } catch (Exception e) {
            Log.e("ZxingUtil", "saveBitmapLocal => " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * decodeBitmapFromInputStream
     *
     * @param inputStream
     * @return
     */
    private static Bitmap createBitmapLogo(@NonNull Context context, @NonNull InputStream inputStream, @IntRange(from = 4, to = 14) int boderWidth, @ColorInt int borderColor) {

        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;//不缩放
            options.inJustDecodeBounds = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                options.outConfig = Bitmap.Config.RGB_565;
            }
            options.inSampleSize = 2;

            Bitmap logoBitmap = BitmapFactory.decodeStream(inputStream, null, options);
            Bitmap bitmapBorder = createBitmapBorder(logoBitmap, options, boderWidth, borderColor);

            if (null != logoBitmap) {
                logoBitmap.recycle();
                logoBitmap = null;
            }

            if (null != inputStream) {
                inputStream.close();
                inputStream = null;
            }

            return bitmapBorder;

        } catch (Exception e) {
            Log.e("ZxingUtil", "decodeBitmapFromInputStream => " + e.getMessage(), e);
            return null;
        }
    }

    private static Bitmap createBitmapLogo(@NonNull Context context, @NonNull String base64, @IntRange(from = 4, to = 14) int boderWidth, @ColorInt int borderColor) {

        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;//不缩放
            options.inJustDecodeBounds = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                options.outConfig = Bitmap.Config.RGB_565;
            }
            options.inSampleSize = 2;

            Bitmap logoBitmap = base64ToBitmap(base64, options);
            Bitmap bitmapBorder = createBitmapBorder(logoBitmap, options, boderWidth, borderColor);

            if (null != logoBitmap) {
                logoBitmap.recycle();
                logoBitmap = null;
            }

            return bitmapBorder;

        } catch (Exception e) {
            Log.e("ZxingUtil", "decodeBitmapFromInputStream => " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * 描白边
     *
     * @param logoBitmap
     * @param options
     * @param border
     * @return
     */
    private static @Nullable
    Bitmap createBitmapBorder(@Nullable Bitmap logoBitmap, @NonNull BitmapFactory.Options options, @IntRange(from = 0, to = 100) int border, @ColorInt int color) {

        if (null == logoBitmap)
            return null;

        // 不需要白边
        if (border == 0) {
            return logoBitmap;
        }

        // 边框容错
        int size = Math.min(options.outWidth, options.outHeight) / 10;
        if (border > size) {
            border = size;
        }

        int width = options.outWidth + 2 * border;
        int height = options.outHeight + 2 * border;
        // 创建一个空的Bitmap(内存区域),宽度等于第一张图片的宽度，高度等于两张图片高度总和
        Bitmap logo = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // 将bitmap放置到绘制区域,并将要拼接的图片绘制到指定内存区域
        Canvas canvas = new Canvas(logo);
        Rect src = new Rect(0, 0, options.outWidth, options.outHeight);
        Rect dst = new Rect(border, border, options.outWidth + border, options.outHeight + border);
        canvas.drawBitmap(logoBitmap, src, dst, null);

        Paint paint = new Paint();
        // 设置边框颜色
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        //  设置边框宽度
        paint.setStrokeWidth(border);
        canvas.drawRect(border * 0.5f, border * 0.5f, width - border * 0.5f, height - border * 0.5f, paint);

        if (null != logoBitmap) {
            logoBitmap.recycle();
        }

        return logo;
    }

    /**
     * 将Base64转换成为Bitmap
     *
     * @param base64
     * @param options
     * @return
     */
    private static Bitmap base64ToBitmap(@NonNull String base64, @NonNull BitmapFactory.Options options) {
        try {
            byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            return bitmap;
        } catch (Exception e) {
            Log.e("ZxingUtil", "base64ToBitmap => " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * description: 将Base64转换成为Bitmap
     * created by kalu on 2021-02-18
     */
    private static InputStream base64ToInputStream(@NonNull String base64) {
        //将字符串转换为byte数组
        String trim = base64.trim();
        byte[] bytes = Base64.decode(trim, Base64.DEFAULT);
        //转化为输入流
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        return inputStream;
    }
}
