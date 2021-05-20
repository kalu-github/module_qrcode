package lib.kalu.zxing.analyze;

import com.google.zxing.Reader;

import lib.kalu.zxing.qrcode.QrcodePlusReader;

/**
 * @description: 图像分析器
 * @date: 2021-05-07 14:57
 */
public class AnalyzerQrcode extends AnalyzerFormat {

    @Override
    public Reader createReader() {
        return QrcodePlusReader.getQRCodeReader();
    }
}
