package lib.kalu.zxing.analyze;

import com.google.zxing.Reader;
import com.google.zxing.qrcode.QRCodeReader;

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
    public Reader createReader() {
//        return new QRCodeReader();
        return QrcodePlusReader.getQRCodeReader();
    }

    @Override
    public long failCount() {
        return 10;
    }

    @Override
    public float ratio() {
        return 0.6F;
//        return 1F;
    }
}
