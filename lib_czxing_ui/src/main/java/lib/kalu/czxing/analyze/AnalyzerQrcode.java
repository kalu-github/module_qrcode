package lib.kalu.czxing.analyze;

import android.content.Context;

import androidx.annotation.NonNull;

import lib.kalu.czxing.jni.BarcodeReader;
import lib.kalu.czxing.jni.BarcodeResult;
import lib.kalu.czxing.util.LogUtil;

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
    public BarcodeResult decodeRect(@NonNull Context context, @NonNull byte[] crop, int cropWidth, int cropHeight) {
        try {
            BarcodeResult read = createReader().read(crop, 0, 0, cropWidth, cropHeight, cropWidth, cropHeight);
            return read;
        } catch (Exception e) {
            LogUtil.log("decodeRect[exception] => " + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public BarcodeResult decodeFull(@NonNull Context context, @NonNull byte[] original, int originalWidth, int originalHeight) {
        try {
            return createReader().read(original, 0, 0, originalWidth, originalHeight, originalWidth, originalHeight);
        } catch (Exception e) {
            LogUtil.log("decodeRect[exception] => " + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public BarcodeReader createReader() {
        return BarcodeReader.getInstance();
    }

    @Override
    public float ratio() {
        return 0.6F;
//        return 1F;
    }
}
