package lib.kalu.zxing.analyze;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ResultPoint;
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
    default Result analyzeRect(@NonNull @NotNull Context context, @NonNull @NotNull byte[] crop, int cropWidth, int cropHeight, int cropLeft, int cropTop, int originalWidth, int originalHeight) {
        try {
            PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(crop, cropWidth, cropHeight, 0, 0, cropWidth, cropHeight);
            Result result = createReader().decode(new BinaryBitmap(new HybridBinarizer(source)));
            if (null != result) {
                ResultPoint[] resultPoints = result.getResultPoints();
                LogUtil.log("analyzeRect[succ] => x = " + resultPoints[0].getX() + ", y = " + resultPoints[0].getY() + ", cropLeft = " + cropLeft + ", cropTop = " + cropTop + ", originalWidth = " + originalWidth + ", originalHeight = " + originalHeight);
                return result;
            } else {
                LogUtil.log("analyzeRect[fail] => null");
                return null;
            }
        } catch (Exception e1) {
            LogUtil.log("analyzeRect[exception] => " + e1.getMessage(), e1);
            return null;
        }
    }
}