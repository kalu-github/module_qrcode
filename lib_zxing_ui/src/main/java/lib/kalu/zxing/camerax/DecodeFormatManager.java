package lib.kalu.zxing.camerax;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * @description:
 * @date: 2021-05-07 14:55
 */
public final class DecodeFormatManager {

    /**
     * QR_CODE (最常用的二维码)
     */
    public static final Map<DecodeHintType, Object> CODE_TYPE = createDecodeHint(BarcodeFormat.QR_CODE);

    /**
     * 默认支持的格式
     * 包括如下几种格式：
     * {@link BarcodeFormat#QR_CODE}
     *
     * @return
     */
    private static List<BarcodeFormat> getDefaultFormats() {
        List<BarcodeFormat> list = new ArrayList<>();
        list.add(BarcodeFormat.QR_CODE);
        return list;
    }

    private static <T> List<T> singletonList(T o) {
        return Collections.singletonList(o);
    }

    /**
     * 支持解码的格式
     *
     * @param barcodeFormats {@link BarcodeFormat}
     * @return
     */
    public static Map<DecodeHintType, Object> createDecodeHints(@NonNull BarcodeFormat... barcodeFormats) {
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        addDecodeHintTypes(hints, Arrays.asList(barcodeFormats));
        return hints;
    }

    /**
     * 支持解码的格式
     *
     * @param barcodeFormat {@link BarcodeFormat}
     * @return
     */
    public static Map<DecodeHintType, Object> createDecodeHint(@NonNull BarcodeFormat barcodeFormat) {
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        addDecodeHintTypes(hints, singletonList(barcodeFormat));
        return hints;
    }

    /**
     * @param hints
     * @param formats
     */
    private static void addDecodeHintTypes(Map<DecodeHintType, Object> hints, List<BarcodeFormat> formats) {
        // Image is known to be of one of a few possible formats.
        hints.put(DecodeHintType.POSSIBLE_FORMATS, formats);
        // Spend more time to try to find a barcode; optimize for accuracy, not speed.
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        // Specifies what character encoding to use when decoding, where applicable (type String)
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
    }

}
