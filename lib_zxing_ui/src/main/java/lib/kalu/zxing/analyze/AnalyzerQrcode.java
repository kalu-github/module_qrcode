package lib.kalu.zxing.analyze;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.exception.ChecksumException;
import com.google.zxing.exception.FormatException;
import com.google.zxing.exception.NotFoundException;
import com.google.zxing.exception.ReaderException;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.source.PlanarYUVLuminanceSource;

import org.jetbrains.annotations.NotNull;

import lib.kalu.zxing.qrcode.QrcodePlusReader;
import lib.kalu.zxing.util.LogUtil;

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
    public Result decodeRect(@NonNull Context context, @NonNull byte[] crop, int cropWidth, int cropHeight) throws ReaderException {
        try {
            Result result = createReader().decode(new BinaryBitmap(new HybridBinarizer(new PlanarYUVLuminanceSource(crop, cropWidth, cropHeight, 0, 0, cropWidth, cropHeight))));
            if (null != result) {
                LogUtil.log("decodeRect[succ] => text = " + result.getText());
                return result;
            } else {
                LogUtil.log("decodeRect[fail] =>");
                return null;
            }
        } catch (NotFoundException e) {
            LogUtil.log("decodeRect[exception] => " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LogUtil.log("decodeRect[exception] => " + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Result decodeFull(@NonNull Context context, @NonNull byte[] original, int originalWidth, int originalHeight) {
        try {
            Result result = createReader().decode(new BinaryBitmap(new HybridBinarizer(new PlanarYUVLuminanceSource(original, originalWidth, originalHeight, 0, 0, originalWidth, originalHeight))));
            if (null != result) {
                LogUtil.log("decodeFull[succ] => text = " + result.getText());
                return result;
            } else {
                LogUtil.log("decodeFull[fail] =>");
                return null;
            }
        } catch (Exception e) {
            LogUtil.log("decodeFull[exception] => " + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Reader createReader() {
//        return new QRCodeReader();
        return QrcodePlusReader.getQRCodeReader();
    }

    @Override
    public float ratio() {
        return 0.6F;
//        return 1F;
    }
}
