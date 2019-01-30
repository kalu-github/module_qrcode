/*
 * Copyright (C) 2008 ZXing authors
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

package com.google.zxing.android.decode;

import android.os.Handler;
import android.os.Looper;

import com.google.zxing.android.CaptureActivity;

import java.util.concurrent.CountDownLatch;

/**
 * This thread does all the heavy lifting of decoding the images.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public class DecodeThread extends Thread {

  public static final String BARCODE_BITMAP = "barcode_bitmap";
  public static final String BARCODE_SCALED_FACTOR = "barcode_scaled_factor";

  private final CaptureActivity activity;
  private Handler handler;
  private final CountDownLatch handlerInitLatch;

  public DecodeThread(CaptureActivity activity, String characterSet) {

    this.activity = activity;
    handlerInitLatch = new CountDownLatch(1);

    // The prefs can't change while the thread is running, so pick them up once here.
//    if (decodeFormats == null || decodeFormats.isEmpty()) {
//      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
//      decodeFormats = EnumSet.noneOf(BarcodeFormat.class);
//      if (prefs.getBoolean(PreferencesActivity.KEY_DECODE_1D_PRODUCT, true)) {
//        decodeFormats.addAll(DecodeFormatManager.PRODUCT_FORMATS);
//      }
//      if (prefs.getBoolean(PreferencesActivity.KEY_DECODE_1D_INDUSTRIAL, true)) {
//        decodeFormats.addAll(DecodeFormatManager.INDUSTRIAL_FORMATS);
//      }
//      if (prefs.getBoolean(PreferencesActivity.KEY_DECODE_QR, true)) {
//        decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
//      }
//      if (prefs.getBoolean(PreferencesActivity.KEY_DECODE_DATA_MATRIX, true)) {
//        decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
//      }
//      if (prefs.getBoolean(PreferencesActivity.KEY_DECODE_AZTEC, false)) {
//        decodeFormats.addAll(DecodeFormatManager.AZTEC_FORMATS);
//      }
//      if (prefs.getBoolean(PreferencesActivity.KEY_DECODE_PDF417, false)) {
//        decodeFormats.addAll(DecodeFormatManager.PDF417_FORMATS);
//      }
//    }
//    if (characterSet != null) {
//      hints.put(DecodeHintType.CHARACTER_SET, characterSet);
//    }
//    hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, resultPointCallback);
//    Log.i("DecodeThread", "Hints: " + hints);
  }

  public Handler getHandler() {
    try {
      handlerInitLatch.await();
    } catch (InterruptedException ie) {
      // continue?
    }
    return handler;
  }

  @Override
  public void run() {
    Looper.prepare();
    handler = new DecodeHandler(activity);
    handlerInitLatch.countDown();
    Looper.loop();
  }

}
