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

package lib.kalu.zxing.qrcode;

import com.google.zxing.qrcode.QRCodeReader;

import lib.kalu.zxing.util.LogUtil;

/**
 * @description:
 * @date:  2021-05-17 14:18
 */
public final class QrcodePlusReader extends QRCodeReader {

    private QrcodePlusReader() {
        LogUtil.log("QrcodePlusReader =>");
    }

    private static class Holder {
        private static final QrcodePlusReader readerManager = new QrcodePlusReader();
    }

    public static final QRCodeReader getQRCodeReader() {
        return Holder.readerManager;
    }
}