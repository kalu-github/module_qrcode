#pragma once
/*
* Copyright 2016 Nu-book Inc.
* Copyright 2016 ZXing authors
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

#include "Flags.h"

#include <string>
#include <vector>

namespace ZXing {

/**
* Enumerates barcode formats known to this package.
*/
enum class BarcodeFormat
{
	// The values are an implementation detail. The c++ use-case (ZXing::Flags) could have been designed such that it
	// would not have been necessary to explicitly set the values to single bit constants. This has been done to ease
	// the interoperability with C-like interfaces, the python and the Qt wrapper.
	None            = 0,         ///< Used as a return value if no valid barcode has been detected
	QRCode          = (1 << 13), ///< QR Code (2D)
};

ZX_DECLARE_FLAGS(BarcodeFormats, BarcodeFormat)

const char* ToString(BarcodeFormat format);
std::string ToString(BarcodeFormats formats);

/**
 * @brief Parse a string into a BarcodeFormat. '-' and '_' are optional.
 * @return None if str can not be parsed as a valid enum value
 */
BarcodeFormat BarcodeFormatFromString(const std::string& str);

/**
 * @brief Parse a string into a set of BarcodeFormats.
 * Separators can be (any combination of) '|', ',' or ' '.
 * Underscores are optional and input can be lower case.
 * e.g. "EAN-8 qrcode, Itf" would be parsed into [EAN8, QRCode, ITF].
 * @throws std::invalid_parameter Throws if the string can not be fully parsed.
 */
BarcodeFormats BarcodeFormatsFromString(const std::string& str);

} // ZXing
