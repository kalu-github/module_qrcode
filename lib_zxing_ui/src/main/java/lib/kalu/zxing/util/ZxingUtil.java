package lib.kalu.zxing.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import lib.kalu.zxing.qrcode.DecodeTool;
import lib.kalu.zxing.qrcode.EncodeTool;

/**
 * description: 创建二维码
 * create by kalu on 2019/1/30 13:08
 */
@Keep
public final class ZxingUtil {

    /**
     * @param filePath 二维码本地文件路径
     * @return
     */
    @Keep
    public static final String decodeQrcodeFromFile(@NonNull String filePath) {

        return DecodeTool.decodeQrcodeFromFile(filePath);
    }

    /**
     * @param context 上下文
     * @param uri     二维码本地文件uri
     * @return
     */
    @Keep
    public static final String decodeQrcodeFromUrl(@NonNull Context context, @NonNull Uri uri) {
        return DecodeTool.decodeQrcodeFromUri(context, uri);
    }

    /****************************** createQrcodeFromBase64 *************************************/

    @Keep
    public static String createQrcodeFromBase64(
            @NonNull Context context,
            @NonNull String text,
            @Nullable String base64) {

        return createQrcodeFromBase64(context, text, 3, 0, 0, 0, 0, base64);
    }

    @Keep
    public static String createQrcodeFromBase64(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @Nullable String base64) {

        return createQrcodeFromBase64(context, text, multiple, 0, 0, 0, 0, base64);
    }

    @Keep
    public static String createQrcodeFromBase64(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int margin,
            @Nullable String base64) {

        return createQrcodeFromBase64(context, text, multiple, margin, margin, margin, margin, base64);
    }

    /**
     * @param context      上下文context
     * @param text         二维码信息
     * @param multiple     二维码放大倍数(from = 3, to = 100)
     * @param marginLeft   二维码白边左边距
     * @param marginTop    二维码白边上边距
     * @param marginRight  二维码白边右边距
     * @param marginBottom 二维码白边下边距
     * @param base64       二维码中间logo
     * @return
     */
    @Keep
    public static String createQrcodeFromBase64(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginLeft,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginTop,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginRight,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginBottom,
            @Nullable String base64) {

        InputStream inputStream = null;
        if (null != base64 && base64.length() > 0) {
            inputStream = base64ToInputStream(base64);
        }

        String qrcode = createQrcodeFromInputStream(context, text, multiple, marginLeft, marginTop, marginRight, marginBottom, inputStream);

        if (null != inputStream) {
            try {
                inputStream.close();
                inputStream = null;
            } catch (Exception e) {
            }
        }

        return qrcode;
    }

    /****************************** createQrcodeFromBase64 *************************************/


    /****************************** createQrcodeFromAssets *************************************/

    @Keep
    public static String createQrcodeFromAssets(
            @NonNull Context context,
            @NonNull String text,
            @Nullable String assets) {

        return createQrcodeFromAssets(context, text, 4, 0, 0, 0, 0, assets);
    }

    @Keep
    public static String createQrcodeFromAssets(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @Nullable String assets) {

        return createQrcodeFromAssets(context, text, multiple, 0, 0, 0, 0, assets);
    }

    @Keep
    public static String createQrcodeFromAssets(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int margin,
            @Nullable String assets) {

        return createQrcodeFromAssets(context, text, multiple, margin, margin, margin, margin, assets);
    }

    /**
     * @param context      上下文context
     * @param text         二维码信息
     * @param multiple     二维码放大倍数(from = 3, to = 100)
     * @param marginLeft   二维码白边左边距
     * @param marginTop    二维码白边上边距
     * @param marginRight  二维码白边右边距
     * @param marginBottom 二维码白边下边距
     * @param assets       二维码中间logo
     * @return
     */
    @Keep
    public static String createQrcodeFromAssets(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginLeft,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginTop,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginRight,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginBottom,
            @Nullable String assets) {

        if (null == context || null == assets || assets.length() == 0)
            return null;

        InputStream inputStream = null;
        try {
            inputStream = context.getResources().getAssets().open(assets);
        } catch (Exception e) {
            Log.e("ZxingUtil", "createQrcodeFromAssets => " + e.getMessage(), e);
        }

        String qrcode = createQrcodeFromInputStream(context, text, multiple, marginLeft, marginTop, marginRight, marginBottom, inputStream);
        return qrcode;
    }

    /****************************** createQrcodeFromAssets *************************************/


    /****************************** createQrcodeFromFile *************************************/

    @Keep
    public static String createQrcodeFromFile(
            @NonNull Context context,
            @NonNull String text,
            @Nullable String filePath) {
        return createQrcodeFromFile(context, text, 3, 0, 0, 0, 0, filePath);
    }

    @Keep
    public static String createQrcodeFromFile(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @Nullable String filePath) {
        return createQrcodeFromFile(context, text, multiple, 0, 0, 0, 0, filePath);
    }

    @Keep
    public static String createQrcodeFromFile(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int margin,
            @Nullable String filePath) {
        return createQrcodeFromFile(context, text, multiple, margin, margin, margin, margin, filePath);
    }

    /**
     * @param context      上下文context
     * @param text         二维码信息
     * @param multiple     二维码放大倍数(from = 3, to = 100)
     * @param marginLeft   二维码白边左边距
     * @param marginTop    二维码白边上边距
     * @param marginRight  二维码白边右边距
     * @param marginBottom 二维码白边下边距
     * @param filePath     二维码中间logo
     * @return
     */
    @Keep
    public static String createQrcodeFromFile(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginLeft,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginTop,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginRight,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginBottom,
            @Nullable String filePath) {

        Bitmap bitmap = null;

        try {
            bitmap = createBitmapLogoFromFile(context, filePath, 14, Color.WHITE);
        } catch (Exception e) {
            Log.e("ZxingUtil", "createQrcodeFromFile => " + e.getMessage(), e);
        }

        String qrcode = createQrcode(context, text, multiple, marginLeft, marginTop, marginRight, marginBottom, bitmap);
        if (null != bitmap) {
            bitmap.recycle();
            bitmap = null;
        }

        return qrcode;
    }

    /****************************** createQrcodeFromFile *************************************/


    /****************************** createQrcodeFromUrl *************************************/

    @Keep
    public static String createQrcodeFromUrl(
            @NonNull Context context,
            @NonNull String text,
            @Nullable String url) {

        return createQrcodeFromUrl(context, text, 3, 0, 0, 0, 0, url);
    }

    @Keep
    public static String createQrcodeFromUrl(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @Nullable String url) {

        return createQrcodeFromUrl(context, text, multiple, 0, 0, 0, 0, url);
    }

    @Keep
    public static String createQrcodeFromUrl(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int margin,
            @Nullable String url) {

        return createQrcodeFromUrl(context, text, multiple, margin, margin, margin, margin, url);
    }

    /**
     * @param context      上下文context
     * @param text         二维码信息
     * @param multiple     二维码放大倍数(from = 3, to = 100)
     * @param marginLeft   二维码白边左边距
     * @param marginTop    二维码白边上边距
     * @param marginRight  二维码白边右边距
     * @param marginBottom 二维码白边下边距
     * @param url          二维码中间logo
     * @return
     */
    @Keep
    public static String createQrcodeFromUrl(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginLeft,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginTop,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginRight,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginBottom,
            @Nullable String url) {

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

        String qrcode = createQrcodeFromInputStream(context, text, multiple, marginLeft, marginTop, marginRight, marginBottom, inputStream);
        return qrcode;
    }

    /****************************** createQrcodeFromUrl *************************************/

    /****************************** createQrcodeFromRaw *************************************/

    @Keep
    public static String createQrcodeFromRaw(
            @NonNull Context context,
            @NonNull String text,
            @RawRes int raw) {

        return createQrcodeFromRaw(context, text, 3, 0, 0, 0, 0, raw);
    }

    @Keep
    public static String createQrcodeFromRaw(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @RawRes int raw) {

        return createQrcodeFromRaw(context, text, multiple, 0, 0, 0, 0, raw);
    }

    @Keep
    public static String createQrcodeFromRaw(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int margin,
            @RawRes int raw) {

        return createQrcodeFromRaw(context, text, multiple, margin, margin, margin, margin, raw);
    }

    /**
     * @param context      上下文context
     * @param text         二维码信息
     * @param multiple     二维码放大倍数(from = 3, to = 100)
     * @param marginLeft   二维码白边左边距
     * @param marginTop    二维码白边上边距
     * @param marginRight  二维码白边右边距
     * @param marginBottom 二维码白边下边距
     * @param raw          二维码中间logo
     * @return
     */
    @Keep
    public static String createQrcodeFromRaw(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginLeft,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginTop,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginRight,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginBottom,
            @RawRes int raw) {

        if (null == context)
            return null;

        InputStream inputStream = context.getResources().openRawResource(raw);
        String qrcode = createQrcodeFromInputStream(context, text, multiple, marginLeft, marginTop, marginRight, marginBottom, inputStream);
        return qrcode;
    }

    /****************************** createQrcodeFromRaw *************************************/

    /****************************** createQrcodeFromInputStream *************************************/

    /**
     * @param context      上下文context
     * @param text         二维码信息
     * @param multiple     二维码放大倍数(from = 3, to = 100)
     * @param marginLeft   二维码白边左边距
     * @param marginTop    二维码白边上边距
     * @param marginRight  二维码白边右边距
     * @param marginBottom 二维码白边下边距
     * @param inputStream  二维码中间logo
     * @return
     */
    @Keep
    private static String createQrcodeFromInputStream(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginLeft,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginTop,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginRight,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginBottom,
            @Nullable InputStream inputStream) {

        if (null == context)
            return null;

        Bitmap logoBitmap = createBitmapLogo(context, inputStream, 14, Color.WHITE);
        String qrcode = createQrcode(context, text, multiple, marginLeft, marginTop, marginRight, marginBottom, logoBitmap);

        if (null != logoBitmap) {
            logoBitmap.recycle();
            logoBitmap = null;
        }

        return qrcode;
    }

    /****************************** createQrcodeFromInputStream *************************************/

    /****************************** createQrcode *************************************/

    public static String createQrcode(
            @NonNull Context context,
            @NonNull String text) {

        return createQrcode(context, text, 3, 0, 0, 0, 0, null);
    }

    public static String createQrcode(
            @NonNull Context context,
            @NonNull String text,
            @Nullable Bitmap logo) {

        return createQrcode(context, text, 3, 0, 0, 0, 0, logo);
    }

    public static String createQrcode(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @Nullable Bitmap logo) {

        return createQrcode(context, text, multiple, 0, 0, 0, 0, logo);
    }

    public static String createQrcode(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int margin,
            @Nullable Bitmap logo) {

        return createQrcode(context, text, multiple, margin, margin, margin, margin, logo);
    }

    /**
     * @param context      上下文context
     * @param text         二维码信息
     * @param multiple     二维码放大倍数(from = 3, to = 100)
     * @param marginLeft   二维码白边左边距
     * @param marginTop    二维码白边上边距
     * @param marginRight  二维码白边右边距
     * @param marginBottom 二维码白边下边距
     * @param logo         二维码中间logo
     * @return
     */
    @Keep
    public static String createQrcode(
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

        Bitmap bitmapQrcode = EncodeTool.createBitmapQrcode(context, text, multiple, marginLeft, marginTop, marginRight, marginBottom, logo);

        int hashcode = text.hashCode();
        String saveBitmapLocal = saveBitmapLocal(context, bitmapQrcode, hashcode);

        if (null != logo) {
            logo.recycle();
            logo = null;
        }

        return saveBitmapLocal;
    }

    /****************************** createQrcode *************************************/

    /**
     * 保存bitmap至本地
     *
     * @param context  上下文
     * @param bitmap   bitmap
     * @param hashcode 文件名hashcode
     * @return
     */
    @Keep
    private static String saveBitmapLocal(@NonNull Context context, @NonNull Bitmap bitmap, @NonNull int hashcode) {

        if (null == context || null == bitmap)
            return null;

        try {

            String root = context.getFilesDir().getAbsolutePath() + File.separatorChar + "qrcode";
            File dir = new File(root);
            if (!dir.exists()) {
                dir.mkdirs();
            } else if (dir.isFile()) {
                dir.delete();
                dir.mkdirs();
            }

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("temp_qrcode_");
            stringBuilder.append(hashcode);
            stringBuilder.append(".png");

            String child = stringBuilder.toString();

            File file = new File(dir, child);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            Log.e("ZxingUtil", "saveBitmapLocal =>  path = " + file.getAbsolutePath());

            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.flush();
            fos.close();

            if (null != bitmap) {
                bitmap.recycle();
            }

            return root + File.separator + child;

        } catch (Exception e) {
            Log.e("ZxingUtil", "saveBitmapLocal => " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * createBitmapLogoFromFile
     *
     * @param filePath
     * @return
     */
    private static Bitmap createBitmapLogoFromFile(@NonNull Context context, @NonNull String filePath, @IntRange(from = 4, to = 14) int boderWidth, @ColorInt int borderColor) {

        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;//不缩放
            options.inJustDecodeBounds = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                options.outConfig = Bitmap.Config.RGB_565;
            }
            options.inSampleSize = 2;

            Bitmap logoBitmap = BitmapFactory.decodeFile(filePath, options);
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
        int multiple = Math.min(options.outWidth, options.outHeight) / 10;
        if (border > multiple) {
            border = multiple;
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
