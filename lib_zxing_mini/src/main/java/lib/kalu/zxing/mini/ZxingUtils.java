package lib.kalu.zxing.mini;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;

import lib.kalu.zxing.mini.common.BitMatrix;
import lib.kalu.zxing.mini.qrcode.decoder.ErrorCorrectionLevel;

/**
 * description: 创建二维码
 * create by kalu on 2019/1/30 13:08
 */
@Keep
public final class ZxingUtils {

    private static final int DEFAULT_REQ_WIDTH = 480;
    private static final int DEFAULT_REQ_HEIGHT = 640;

    /****************************** createQrcodeFromBase64 *************************************/

    @Keep
    public static String create(
            @NonNull Context context,
            @NonNull String text,
            @Nullable String base64) {

        return create(context, text, 3, 0, 0, 0, 0, base64);
    }

    @Keep
    public static String create(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @Nullable String base64) {

        return create(context, text, multiple, 0, 0, 0, 0, base64);
    }

    @Keep
    public static String create(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int margin,
            @Nullable String base64) {

        return create(context, text, multiple, margin, margin, margin, margin, base64);
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
    public static String create(
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

        String qrcode = create(context, text, multiple, marginLeft, marginTop, marginRight, marginBottom, inputStream);

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
    private static String create(
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
        String qrcode = create(context, text, multiple, marginLeft, marginTop, marginRight, marginBottom, logoBitmap);

        if (null != logoBitmap) {
            logoBitmap.recycle();
            logoBitmap = null;
        }

        return qrcode;
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
    public static String create(
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

        Bitmap bitmapQrcode = createBitmap(context, text, multiple, marginLeft, marginTop, marginRight, marginBottom, ErrorCorrectionLevel.M, null, logo);

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
    private static Bitmap createBitmapLogo(@NonNull Context context, @NonNull InputStream inputStream, @IntRange(from = 10, to = 20) int boderWidth, @ColorInt int borderColor) {

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

    private static Bitmap createBitmap(
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

            BitMatrix bitMatrix = ZxingWriter.getQrcodePlusWriter().encode(text, multiple, marginLeft, marginTop, marginRight, marginBottom, level, hints);

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
                int logoOutWidth = matrixWidth / 4;
                // logo输出高度
                int logoOutHeight = matrixHeight / 4;

                // logo范围计算
                minX = Math.abs(matrixWidth - marginLeft - marginRight) / 2 - Math.abs(logoOutWidth) / 2;
                minX += marginLeft;
                maxX = minX + logoOutWidth;
                minY = Math.abs(matrixHeight - marginTop - marginBottom) / 2 - Math.abs(logoOutWidth) / 2;
                minY += marginTop;
                maxY = minY + logoOutHeight;
//                minX = Math.abs(matrixWidth - multiple * marginLeft - multiple * marginRight) / 2 - Math.abs(logoOutWidth) / 2;
//                minX += marginLeft * multiple;
//                maxX = minX + logoOutWidth;
//                minY = Math.abs(matrixHeight - multiple * marginTop - multiple * marginBottom) / 2 - Math.abs(logoOutWidth) / 2;
//                minY += marginTop * multiple;
//                maxY = minY + logoOutHeight;

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
