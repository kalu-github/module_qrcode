/*
 * Copyright 2007 ZXing authors
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

package com.google.zxing.qrcode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.exception.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.exception.FormatException;
import com.google.zxing.exception.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.qrcode.decoder.Decoder;
import com.google.zxing.qrcode.decoder.QRCodeDecoderMetaData;
import com.google.zxing.qrcode.detector.Detector;

import java.util.List;
import java.util.Map;

/**
 * This implementation can detect and decode QR Codes in an image.
 *
 * @author Sean Owen
 */
public class QRCodeReader implements Reader {

    private final Decoder decoder = new Decoder();

    protected final Decoder getDecoder() {
        return decoder;
    }

    @Override
    public Result decode(@NonNull BinaryBitmap image) throws NotFoundException, ChecksumException, FormatException {
        return decode(image, QrcodeDecodeHintTypeManager.DECODE_HINT_TYPE);
    }

    @Override
    public final Result decode(@NonNull BinaryBitmap image, @Nullable Map<DecodeHintType, ?> hints) throws NotFoundException, ChecksumException, FormatException {

        DetectorResult detectorResult = new Detector(image.getBlackMatrix()).detect(hints);
        DecoderResult decoderResult = decoder.decode(detectorResult.getBits(), hints);
        ResultPoint[] points = detectorResult.getPoints();

       // LogUtil.log("QRCodeReader", "decode => " + decoderResult.getText());

        // If the code was mirrored: swap the bottom-left and the top-right points.
        if (decoderResult.getOther() instanceof QRCodeDecoderMetaData) {
            ((QRCodeDecoderMetaData) decoderResult.getOther()).applyMirroredCorrection(points);
        }

        Result result = new Result(decoderResult.getText(), decoderResult.getRawBytes(), points);
        List<byte[]> byteSegments = decoderResult.getByteSegments();
        if (byteSegments != null) {
            result.putMetadata(ResultMetadataType.BYTE_SEGMENTS, byteSegments);
        }
        String ecLevel = decoderResult.getECLevel();
        if (ecLevel != null) {
            result.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, ecLevel);
        }
        if (decoderResult.hasStructuredAppend()) {
            result.putMetadata(ResultMetadataType.STRUCTURED_APPEND_SEQUENCE, decoderResult.getStructuredAppendSequenceNumber());
            result.putMetadata(ResultMetadataType.STRUCTURED_APPEND_PARITY, decoderResult.getStructuredAppendParity());
        }
        return result;
    }

    @Override
    public void reset() {
        // do nothing
    }
}
