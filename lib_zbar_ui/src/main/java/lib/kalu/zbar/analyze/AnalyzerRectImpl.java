package lib.kalu.zbar.analyze;

import android.content.Context;

import androidx.annotation.NonNull;

import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import lib.kalu.zbar.util.LogUtil;

/**
 * @description:
 * @date: 2021-05-07 14:57
 */
interface AnalyzerRectImpl extends AnalyzerDataImpl {

    @Override
    default Symbol analyzeRect(@NonNull Context context, @NonNull byte[] crop, int cropWidth, int cropHeight, int cropLeft, int cropTop, @NonNull byte[] original, int originalWidth, int originalHeight) {
        Symbol symbolR = decodeRect(context, crop, cropWidth, cropHeight);
        if (null != symbolR)
            return symbolR;

        return decodeFull(context, original, originalWidth, originalHeight);
    }
}