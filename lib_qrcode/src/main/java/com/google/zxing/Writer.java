/*
 * Copyright 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.zxing.barcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.common.BitMatrix;

import java.util.Map;

/**
 * The base class for all objects which encode/generate a barcode image.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public interface Writer {

    default BitMatrix encode(@NonNull String text) throws WriterException {
        return encode(text, 1, 0, 0, 0, 0, ErrorCorrectionLevel.M, null);
    }

    default BitMatrix encode(@NonNull String text, ErrorCorrectionLevel level) throws WriterException {
        return encode(text, 1, 0, 0, 0, 0, level, null);
    }

    default BitMatrix encode(@NonNull String text, @IntRange(from = 1, to = 100) int multiple, ErrorCorrectionLevel level) throws WriterException {
        return encode(text, multiple, 0, 0, 0, 0, level, null);
    }

    default BitMatrix encode(@NonNull String text, @IntRange(from = 1, to = 100) int multiple, @IntRange(from = 0, to = Integer.MAX_VALUE) int margin, ErrorCorrectionLevel level) throws WriterException {
        return encode(text, multiple, margin, margin, margin, margin, level, null);
    }

    default BitMatrix encode(@NonNull String text, @IntRange(from = 1, to = 100) int multiple, @IntRange(from = 0, to = Integer.MAX_VALUE) int margin, ErrorCorrectionLevel level, Map<EncodeHintType, ?> hints) throws WriterException {
        return encode(text, multiple, margin, margin, margin, margin, level, hints);
    }

    /**
     * @param text         二维码文字信息
     * @param multiple     二维码放大倍数
     * @param level        二维码容错等级
     * @param marginLeft   左边距
     * @param marginTop    上边距
     * @param marginRight  右边距
     * @param marginBottom 下边距
     * @param level
     * @param hints
     * @return
     */
    BitMatrix encode(
            @NonNull String text,
            @IntRange(from = 1, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginLeft,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginTop,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginRight,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginBottom,
            @NonNull ErrorCorrectionLevel level,
            @Nullable Map<EncodeHintType, ?> hints) throws WriterException;
}