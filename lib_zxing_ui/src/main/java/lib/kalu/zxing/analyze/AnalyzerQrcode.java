package lib.kalu.zxing.analyze;

import com.google.zxing.Reader;

import lib.kalu.zxing.qrcode.QrcodePlusReader;

/**
 * @description: QR-Code图像分析器
 * @date: 2021-05-20 14:03
 */
public final class AnalyzerQrcode implements AnalyzerRectImpl {

    @Override
    public Reader createReader() {
        return QrcodePlusReader.getQRCodeReader();
    }

    @Override
    public float ratio() {
        return 0.6F;
//        return 1F;
    }
}
