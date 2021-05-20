package com.google.zxing.qrcode;

import androidx.annotation.NonNull;

import com.google.zxing.DecodeHintType;

import java.util.EnumMap;
import java.util.Map;

/**
 * @description:
 * @date: 2021-05-07 14:55
 */
public final class QrcodeDecodeHintTypeManager {

    public static final Map<DecodeHintType, Object> DECODE_HINT_TYPE = createDecodeHint();

    public static Map<DecodeHintType, Object> createDecodeHint() {
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        addDecodeHintTypes(hints);
        return hints;
    }

    private static void addDecodeHintTypes(@NonNull Map<DecodeHintType, Object> hints) {
        // 优化精度
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        // 解码设置编码方式为：utf-8
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        // hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, NULL);
        // hints.put(DecodeHintType.RETURN_CODABAR_START_END, NULL);
    }
}