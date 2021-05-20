/*
 * Copyright 2009 ZXing authors
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

package com.google.zxing.source;

/**
 * The purpose of this class hierarchy is to abstract different bitmap implementations across
 * platforms into a standard interface for requesting greyscale luminance values. The interface
 * only provides immutable methods; therefore crop and rotation create copies. This is to ensure
 * that one Reader does not modify the original luminance source and leave it in an unknown state
 * for other Readers in the chain.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public interface LuminanceSourceImpl {

    byte[] getRow(int y, byte[] row);

    byte[] getMatrix();

    int getWidth();

    int getHeight();

    default boolean isCropSupported() {
        return false;
    }

    default boolean isRotateSupported() {
        return false;
    }

    default LuminanceSourceImpl crop(int left, int top, int width, int height) {
        throw new UnsupportedOperationException("This luminance source does not support cropping.");
    }

    default LuminanceSourceImpl invert() {
        return new LuminanceSourceInverted(this);
    }

    default LuminanceSourceImpl rotateCounterClockwise() {
        throw new UnsupportedOperationException("This luminance source does not support rotation by 90 degrees.");
    }

    default LuminanceSourceImpl rotateCounterClockwise45() {
        throw new UnsupportedOperationException("This luminance source does not support rotation by 45 degrees.");
    }

    default String logToString() {
        byte[] row = new byte[getWidth()];
        StringBuilder result = new StringBuilder(getHeight() * (getHeight() + 1));
        for (int y = 0; y < getHeight(); y++) {
            row = getRow(y, row);
            for (int x = 0; x < getWidth(); x++) {
                int luminance = row[x] & 0xFF;
                char c;
                if (luminance < 0x40) {
                    c = '#';
                } else if (luminance < 0x80) {
                    c = '+';
                } else if (luminance < 0xC0) {
                    c = '.';
                } else {
                    c = ' ';
                }
                result.append(c);
            }
            result.append('\n');
        }
        return result.toString();
    }
}
