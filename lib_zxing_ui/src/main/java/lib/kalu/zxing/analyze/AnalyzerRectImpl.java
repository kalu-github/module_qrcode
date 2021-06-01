package lib.kalu.zxing.analyze;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ResultPoint;
import com.google.zxing.exception.NotFoundException;
import com.google.zxing.exception.ReaderException;
import com.google.zxing.source.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import lib.kalu.zxing.util.LogUtil;

/**
 * @description:
 * @date: 2021-05-07 14:57
 */
interface AnalyzerRectImpl extends AnalyzerDataImpl {

    @Override
    default Result analyzeRect(@NonNull Context context, @NonNull byte[] crop, int cropWidth, int cropHeight, int cropLeft, int cropTop, @NonNull byte[] original, int originalWidth, int originalHeight) {
        try {
            return decodeRect(context, crop, cropWidth, cropHeight);
        } catch (ReaderException e) {
            return decodeFull(context, original, originalWidth, originalHeight);
        } catch (Exception e) {
            return null;
        }
    }
}