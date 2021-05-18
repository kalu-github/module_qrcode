package lib.kalu.zxing.qrcode;

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

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import lib.kalu.zxing.camerax.DecodeFormatManager;
import lib.kalu.zxing.util.LogUtil;

/**
 * description: 创建二维码
 * create by kalu on 2019/1/30 13:08
 */
@Keep
public final class QrcodeTool {

    private static final int DEFAULT_REQ_WIDTH = 480;
    private static final int DEFAULT_REQ_HEIGHT = 640;

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
            Log.e("QrcodeTool", "createQrcodeFromAssets => " + e.getMessage(), e);
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
            Log.e("QrcodeTool", "createQrcodeFromFile => " + e.getMessage(), e);
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
                        Log.e("QrcodeTool", "createQrcodeFromUrl => " + e.getMessage(), e);
                        return null;
                    }
                }
            }).get();

        } catch (Exception e) {
            Log.e("QrcodeTool", "createQrcodeFromUrl => " + e.getMessage(), e);
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

        Bitmap logoBitmap = createBitmapLogo(context, inputStream, 20, Color.WHITE);
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
            Log.e("QrcodeTool", "saveBitmapLocal =>  path = " + file.getAbsolutePath());

            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.flush();
            fos.close();

            if (null != bitmap) {
                bitmap.recycle();
            }

            return root + File.separator + child;

        } catch (Exception e) {
            Log.e("QrcodeTool", "saveBitmapLocal => " + e.getMessage(), e);
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
            Log.e("QrcodeTool", "decodeBitmapFromInputStream => " + e.getMessage(), e);
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
            Log.e("QrcodeTool", "decodeBitmapFromInputStream => " + e.getMessage(), e);
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
            Log.e("QrcodeTool", "decodeBitmapFromInputStream => " + e.getMessage(), e);
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
            Log.e("QrcodeTool", "base64ToBitmap => " + e.getMessage(), e);
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

    /********************************/

    /**
     * 解析二维码图片
     *
     * @param bitmapPath 需要解析的图片路径
     * @return
     */
    public static String parseQRCode(String bitmapPath) {
        Result result = parseQRCodeResult(bitmapPath);
        if (result != null) {
            return result.getText();
        }
        return null;
    }

    /**
     * 解析二维码图片
     *
     * @param bitmapPath 需要解析的图片路径
     * @return
     */
    public static Result parseQRCodeResult(String bitmapPath) {
        return parseQRCodeResult(bitmapPath, DEFAULT_REQ_WIDTH, DEFAULT_REQ_HEIGHT);
    }

    /**
     * 解析二维码图片
     *
     * @param bitmapPath 需要解析的图片路径
     * @param reqWidth   请求目标宽度，如果实际图片宽度大于此值，会自动进行压缩处理，当 reqWidth 和 reqHeight都小于或等于0时，则不进行压缩处理
     * @param reqHeight  请求目标高度，如果实际图片高度大于此值，会自动进行压缩处理，当 reqWidth 和 reqHeight都小于或等于0时，则不进行压缩处理
     * @return
     */
    public static Result parseQRCodeResult(String bitmapPath, int reqWidth, int reqHeight) {
        return parseCodeResult(bitmapPath, reqWidth, reqHeight, null);
    }

    /**
     * 解析一维码/二维码图片
     *
     * @param bitmapPath 需要解析的图片路径
     * @param hints      解析编码类型
     * @return
     */
    public static String parseCode(String bitmapPath, Map<DecodeHintType, Object> hints) {
        Result result = parseCodeResult(bitmapPath, hints);
        if (result != null) {
            return result.getText();
        }
        return null;
    }

    /**
     * 解析一维码/二维码图片
     *
     * @param bitmapPath
     * @param hints      解析编码类型
     * @return
     */
    public static Result parseCodeResult(String bitmapPath, Map<DecodeHintType, Object> hints) {
        return parseCodeResult(bitmapPath, DEFAULT_REQ_WIDTH, DEFAULT_REQ_HEIGHT, hints);
    }

    /**
     * 解析一维码/二维码图片
     *
     * @param bitmapPath 需要解析的图片路径
     * @param reqWidth   请求目标宽度，如果实际图片宽度大于此值，会自动进行压缩处理，当 reqWidth 和 reqHeight都小于或等于0时，则不进行压缩处理
     * @param reqHeight  请求目标高度，如果实际图片高度大于此值，会自动进行压缩处理，当 reqWidth 和 reqHeight都小于或等于0时，则不进行压缩处理
     * @param hints      解析编码类型
     * @return
     */
    public static Result parseCodeResult(String bitmapPath, int reqWidth, int reqHeight, Map<DecodeHintType, Object> hints) {
        Result result = null;
        MultiFormatReader reader = new MultiFormatReader();
        try {
            reader.setHints(hints);
            RGBLuminanceSource source = getRGBLuminanceSource(compressBitmap(bitmapPath, reqWidth, reqHeight));
            if (source != null) {
                result = decodeInternal(reader, source);
                if (result == null) {
                    result = decodeInternal(reader, source.invert());
                }
                if (result == null && source.isRotateSupported()) {
                    result = decodeInternal(reader, source.rotateCounterClockwise());
                }
            }

        } catch (Exception e) {
            LogUtil.log(e.getMessage());
        } finally {
            reader.reset();
        }

        return result;
    }

    private static Result decodeInternal(@NonNull MultiFormatReader reader, @NonNull LuminanceSource source) {
        try {

            // 优先采用GlobalHistogramBinarizer解析
            Result result = reader.decode(new BinaryBitmap(new GlobalHistogramBinarizer(source)));

            if (null == result) {
                // 采用HybridBinarizer解析
                return reader.decode(new BinaryBitmap(new HybridBinarizer(source)));
            } else {
                return result;
            }

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 压缩图片
     *
     * @param path
     * @return
     */
    private static Bitmap compressBitmap(String path, int reqWidth, int reqHeight) {
        if (reqWidth > 0 && reqHeight > 0) {//都大于进行判断是否压缩

            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
            newOpts.inJustDecodeBounds = true;//获取原始图片大小
            BitmapFactory.decodeFile(path, newOpts);// 此时返回bm为空
            float width = newOpts.outWidth;
            float height = newOpts.outHeight;
            // 缩放比，由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
            int wSize = 1;// wSize=1表示不缩放
            if (width > reqWidth) {// 如果宽度大的话根据宽度固定大小缩放
                wSize = (int) (width / reqWidth);
            }
            int hSize = 1;// wSize=1表示不缩放
            if (height > reqHeight) {// 如果高度高的话根据宽度固定大小缩放
                hSize = (int) (height / reqHeight);
            }
            int size = Math.max(wSize, hSize);
            if (size <= 0)
                size = 1;
            newOpts.inSampleSize = size;// 设置缩放比例
            // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
            newOpts.inJustDecodeBounds = false;

            return BitmapFactory.decodeFile(path, newOpts);

        }

        return BitmapFactory.decodeFile(path);
    }


    /**
     * 获取RGBLuminanceSource
     *
     * @param bitmap
     * @return
     */
    private static RGBLuminanceSource getRGBLuminanceSource(@NonNull Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        return new RGBLuminanceSource(width, height, pixels);

    }
}
