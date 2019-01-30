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

package com.google.zxing.core;

import com.google.zxing.core.common.BitMatrix;
import com.google.zxing.core.qrcode.decoder.ErrorCorrectionLevel;

/**
 * The base class for all objects which encode/generate a barcode image.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public interface Writer {

    BitMatrix encode(String str, int width, int height) throws WriterException;

    BitMatrix encode(String str, int width, int height, int margin) throws WriterException;

    BitMatrix encode(String str, int width, int height, ErrorCorrectionLevel level) throws WriterException;

    /**
     * @param str    二维码信息
     * @param width  宽
     * @param height 高
     * @param margin 外边距
     * @param level  编码等级
     * @return
     * @throws WriterException
     */
    BitMatrix encode(String str, int width, int height, int margin, ErrorCorrectionLevel level) throws WriterException;
}
