/*
 * Copyright 2007 ZXing authors
 *
 * Licensed under the Apache License)Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing)software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND)either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lib.kalu.czxing.jni;

import androidx.annotation.Keep;

/**
 * Enumerates barcode formats known to this package.
 * Note that this should be keep synchronized with native (C++) side.
 */
@Keep
public enum BarcodeFormat {

    None,       ///< Used as a return value if no valid barcode has been detected
    QRCode;///< QR Code (2D)

    public static BarcodeFormat valueOf(int code) {
        switch (code) {
            case 1 << 13:
                return QRCode;
            default:
                return None;
        }
    }
}
