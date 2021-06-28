package lib.kalu.zbar.analyze;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.sourceforge.zbar.Format;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import org.jetbrains.annotations.NotNull;

import lib.kalu.zbar.util.LogUtil;

/**
 * @description: QR-Code图像分析器
 * @date: 2021-05-20 14:03
 */
public final class AnalyzerQrcode implements AnalyzerRectImpl {

    private AnalyzerQrcode() {
        LogUtil.log("AnalyzerQrcode =>");
    }

    private static class Holder {
        private static final AnalyzerQrcode instance = new AnalyzerQrcode();
    }

    public static final AnalyzerQrcode getAnalyzer() {
        return Holder.instance;
    }

    @Override
    public Symbol decodeRect(@NonNull @NotNull Context context, @NonNull @NotNull byte[] crop, int cropWidth, int cropHeight) {

        Image zbar = new Image(cropWidth, cropHeight, Format.Y800);
        zbar.setData(crop);
        zbar.setCrop(0, 0, cropWidth, cropHeight);

        ImageScanner reader = createReader();
        int code = reader.scanImage(zbar);
        if (code == 0) {
            LogUtil.log("decodeRect[faul] =>");
            release(zbar, null, null);
            return null;
        }

        SymbolSet symbolSet = reader.getResults();
        for (Symbol sym : symbolSet) {
            if (sym.getType() == Symbol.QRCODE && null != sym.getData() && sym.getData().length() > 0) {
                LogUtil.log("decodeRect[succ] => " + sym.getData());
                release(zbar, symbolSet, reader);
                return sym;
            }
        }

        LogUtil.log("decodeRect[faul] =>");
        release(zbar, symbolSet, null);
        return null;
    }

    @Override
    public Symbol decodeFull(@NonNull @NotNull Context context, @NonNull @NotNull byte[] original, int originalWidth, int originalHeight) {

        Image zbar = new Image(originalWidth, originalHeight, Format.Y800);
        zbar.setData(original);
        zbar.setCrop(0, 0, originalWidth, originalHeight);

        ImageScanner reader = createReader();
        int code = reader.scanImage(zbar);
        if (code == 0) {
            LogUtil.log("decodeFull[faul] =>");
            release(zbar, null, null);
            return null;
        }

        SymbolSet symbolSet = reader.getResults();
        for (Symbol sym : symbolSet) {
            if (sym.getType() == Symbol.QRCODE && null != sym.getData() && sym.getData().length() > 0) {
                LogUtil.log("decodeFull[succ] => " + sym.getData());
                release(zbar, symbolSet, reader);
                return sym;
            }
        }

        LogUtil.log("decodeFull[fail] =>");
        release(zbar, symbolSet, null);
        return null;
    }

    private void release(@Nullable Image image, @Nullable SymbolSet symbols, @Nullable ImageScanner scanner) {

        if (null != image) {
            image.destroy();
        }

        if (null != symbols) {

            for (Symbol symbol : symbols) {
                if (null != symbol) {
                    symbol.destroy();
                }
            }

            symbols.destroy();
        }

        if (null != scanner) {
            scanner.destroy();
        }
    }

    @Nullable
    @Override
    public ImageScanner createReader() {
        return ImageScanner.getInstance();
    }

    @Override
    public float ratio() {
        return 0.6F;
//        return 1F;
    }
}
