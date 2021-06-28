/*
* Copyright 2017 Huy Cuong Nguyen
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

#include "MultiFormatWriter.h"

#include "BitMatrix.h"
#include "qrcode/QRErrorCorrectionLevel.h"
#include "qrcode/QRWriter.h"

#include <stdexcept>

namespace ZXing {

BitMatrix
MultiFormatWriter::encode(const std::wstring& contents, int width, int height) const
{
	auto exec0 = [&](auto&& writer) {
		if (_margin >=0)
			writer.setMargin(_margin);
		return writer.encode(contents, width, height);
	};

	auto QRCodeEccLevel = [&](QRCode::Writer& writer, int eccLevel) {
		writer.setErrorCorrectionLevel(static_cast<QRCode::ErrorCorrectionLevel>(--eccLevel / 2));
	};

	auto exec1 = [&](auto&& writer, auto setEccLevel) {
		if (_encoding != CharacterSet::Unknown)
			writer.setEncoding(_encoding);
		if (_eccLevel >= 0 && _eccLevel <= 8)
			setEccLevel(writer, _eccLevel);
		return exec0(std::move(writer));
	};

	switch (_format) {
	case BarcodeFormat::QRCode: return exec1(QRCode::Writer(), QRCodeEccLevel);
	default: throw std::invalid_argument(std::string("Unsupported format: ") + ToString(_format));
	}
}

} // ZXing
