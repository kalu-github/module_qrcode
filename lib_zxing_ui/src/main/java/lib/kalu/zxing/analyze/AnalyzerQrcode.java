package lib.kalu.zxing.analyze;

import com.google.zxing.DecodeHintType;
import com.google.zxing.Reader;
import com.google.zxing.qrcode.QRCodeReader;

import lib.kalu.zxing.camerax.DecodeConfig;
import lib.kalu.zxing.qrcode.QrcodePlusReader;

import java.util.Map;

import androidx.annotation.Nullable;

/**
 * @description: 图像分析器
 * @date: 2021-05-07 14:57
 */
public class AnalyzerQrcode extends AnalyzerFormat {

    public AnalyzerQrcode() {
        this(null);
    }

    public AnalyzerQrcode(@Nullable Map<DecodeHintType, Object> hints) {
        super(hints);
    }

    @Override
    public Reader createReader() {
        return QrcodePlusReader.getQRCodeReader();
    }

}
