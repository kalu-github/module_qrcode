package lib.kalu.zbar.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.NonNull;

import net.sourceforge.zbar.Format;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.io.InputStream;

/**
 * @description:
 * @date: 2021-06-03 14:20
 */
public final class ZbarUtil {

    public static final String decodeFromUrl(@NonNull Context context, @NonNull Uri uri) {

        if (null == context || null == uri || null == uri.toString() || uri.toString().length() <= 0)
            return null;

        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, null);
            return decodeFromBitmap(context, bitmap);
        } catch (Exception e) {
            return null;
        }
    }

    public static final String decodeFromBitmap(@NonNull Context context, @NonNull Bitmap bitmap) {

        if (null == context || null == bitmap || bitmap.isRecycled())
            return null;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        byte[] bytes = BitmapUtil.getYUVByBitmap(bitmap);
        if (null != bitmap) {
            bitmap.recycle();
        }

        Image zbar = new Image(width, height, Format.Y800);
        zbar.setData(bytes);
        zbar.setCrop(0, 0, width, height);

        String result = null;
        SymbolSet symbolSet = null;
        ImageScanner scanner = ImageScanner.getInstance();

        int code = scanner.scanImage(zbar);
        if (code != 0) {
            symbolSet = scanner.getResults();
            for (Symbol sym : symbolSet) {
                if (sym.getType() == Symbol.QRCODE && null != sym.getData() && sym.getData().length() > 0) {
                    result = sym.getData();
                    break;
                }
            }
        }

        if (null != zbar) {
            zbar.destroy();
        }

        if (null != symbolSet) {

            for (Symbol symbol : symbolSet) {
                if (null != symbol) {
                    symbol.destroy();
                }
            }

            symbolSet.destroy();
        }

        if (null != scanner) {
            scanner.destroy();
        }

        return result;
    }
}
