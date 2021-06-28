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
    Aztec,///< Aztec (2D)
    Codabar,///< Codabar (1D)
    Code39,///< Code39 (1D)
    Code93,///< Code93 (1D)
    Code128,///< Code128 (1D)
    DataBar,///< GS1 DataBar)formerly known as RSS 14
    DataBarExpanded,///< GS1 DataBar Expanded)formerly known as RSS EXPANDED
    DataMatrix,///< DataMatrix (2D)
    EAN8,///< EAN-8 (1D)
    EAN13,///< EAN-13 (1D)
    ITF,///< ITF (Interleaved Two of Five) (1D)
    MaxiCode,///< MaxiCode (2D)
    PDF417,///< PDF417 (1D) or (2D)
    QRCode,///< QR Code (2D)
    UPCA,///< UPC-A (1D)
    UPCE;///< UPC-E (1D)

    public static BarcodeFormat valueOf(int code) {
        switch (code) {
            case 1 << 0:
                return Aztec;
            case 1 << 1:
                return Codabar;
            case 1 << 2:
                return Code39;
            case 1 << 3:
                return Code93;
            case 1 << 4:
                return Code128;
            case 1 << 5:
                return DataBar;
            case 1 << 6:
                return DataBarExpanded;
            case 1 << 7:
                return DataMatrix;
            case 1 << 8:
                return EAN8;
            case 1 << 9:
                return EAN13;
            case 1 << 10:
                return ITF;
            case 1 << 11:
                return MaxiCode;
            case 1 << 12:
                return PDF417;
            case 1 << 13:
                return QRCode;
            case 1 << 14:
                return UPCA;
            case 1 << 15:
                return UPCE;
            default:
                return None;
        }
    }
}
