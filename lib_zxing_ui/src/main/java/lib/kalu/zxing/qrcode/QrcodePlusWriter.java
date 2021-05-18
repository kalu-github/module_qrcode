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

package lib.kalu.zxing.qrcode;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Dimension;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;

import java.util.Map;

import lib.kalu.zxing.util.LogUtil;

/**
 * This object renders a QR Code as a BitMatrix 2D array of greyscale values.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class QrcodePlusWriter extends QRCodeWriter {

    private QrcodePlusWriter() {
        LogUtil.log("QrcodePlusWriter =>");
    }

    private static class Holder {
        private static final QrcodePlusWriter qrcodePlusWriter = new QrcodePlusWriter();
    }

    public static final QrcodePlusWriter getQrcodePlusWriter() {
        return QrcodePlusWriter.Holder.qrcodePlusWriter;
    }

    public BitMatrix encode(@NonNull String text,
                            @IntRange(from = 3, to = 100) int multiple,
                            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginLeft,
                            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginTop,
                            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginRight,
                            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginBottom,
                            ErrorCorrectionLevel level, Map<EncodeHintType, ?> hints) throws WriterException {

        if (null == text || text.length() == 0)
            throw new IllegalArgumentException("error -> text null");

        if (multiple < 3)
            throw new IllegalArgumentException("error -> multiple < 3");

        if (marginLeft < 0)
            throw new IllegalArgumentException("error -> marginLeft < 0");

        if (marginTop < 0)
            throw new IllegalArgumentException("error -> marginTop < 0");

        if (marginRight < 0)
            throw new IllegalArgumentException("error -> marginRight < 0");

        if (marginBottom < 0)
            throw new IllegalArgumentException("error -> marginBottom < 0");


        QRCode code = Encoder.encode(text, level, hints);
        return createBitMatrix(code, multiple, marginLeft, marginTop, marginRight, marginBottom);
    }

    // Note that the input matrix uses 0 == white, 1 == black, while the output matrix uses
    // 0 == black, 255 == white (i.e. an 8 bit greyscale bitmap).

    /**
     * @param code         二维码二位数据, 由二维码具体text生成
     * @param multiple     二维码放大倍数
     * @param marginLeft   左边距
     * @param marginTop    上边距
     * @param marginRight  右边距
     * @param marginBottom 下边距
     * @return
     */
    private static final BitMatrix createBitMatrix(
            @NonNull QRCode code,
            @IntRange(from = 3, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginLeft,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginTop,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginRight,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginBottom) {

        ByteMatrix input = code.getMatrix();
        if (input == null) {
            throw new IllegalStateException();
        }
        int inputWidth = input.getWidth();
        int inputHeight = input.getHeight();
//        int qrWidth = inputWidth + (quietZone * 2);
//        int qrHeight = inputHeight + (quietZone * 2);
//        int outputWidth = Math.max(width, qrWidth);
//        int outputHeight = Math.max(height, qrHeight);

        int outputWidth = inputWidth * multiple + marginLeft + marginRight;
        int outputHeight = inputHeight * multiple + marginTop + marginBottom;

        // Padding includes both the quiet zone and the extra white pixels to accommodate the requested
        // dimensions. For example, if input is 25x25 the QR will be 33x33 including the quiet zone.
        // If the requested size is 200x160, the multiple will be 4, for a QR of 132x132. These will
        // handle all the padding from 100x100 (the actual QR) up to 200x160.

//        int leftPadding = (outputWidth - (inputWidth * multiple)) / 2;
//        int topPadding = (outputHeight - (inputHeight * multiple)) / 2;
        int leftPadding = marginLeft;
        int topPadding = marginTop;

        BitMatrix output = new BitMatrix(outputWidth, outputHeight);
        for (int inputY = 0, outputY = topPadding; inputY < inputHeight; inputY++, outputY += multiple) {
            // Write the contents of this row of the barcode
            for (int inputX = 0, outputX = leftPadding; inputX < inputWidth; inputX++, outputX += multiple) {
                if (input.get(inputX, inputY) == 1) {
                    output.setRegion(outputX, outputY, multiple, multiple);
                }
            }
        }

        return output;
    }

    @Override
    public BitMatrix encode(String s, BarcodeFormat barcodeFormat, int i, int i1) throws WriterException {
        return null;
    }

    @Override
    public BitMatrix encode(String s, BarcodeFormat barcodeFormat, int i, int i1, Map<EncodeHintType, ?> map) throws WriterException {
        return null;
    }
}
