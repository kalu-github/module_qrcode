package lib.kalu.zxing.camerax.analyze;

import com.google.zxing.DecodeHintType;
import com.google.zxing.Reader;
import com.google.zxing.qrcode.QRCodeReader;

import lib.kalu.zxing.camerax.DecodeConfig;

import java.util.Map;

import androidx.annotation.Nullable;

/**
 * @description: 图像分析器
 * @date: 2021-05-07 14:57
 */
public class AnalyzerQrcode extends AnalyzerFormat {

    public AnalyzerQrcode() {
        this((DecodeConfig) null);
    }

    public AnalyzerQrcode(@Nullable Map<DecodeHintType, Object> hints) {
        this(new DecodeConfig().setHints(hints));
    }

    public AnalyzerQrcode(@Nullable DecodeConfig config) {
        super(config);
    }

    @Override
    public Reader createReader() {
        return new QRCodeReader();
    }

}
