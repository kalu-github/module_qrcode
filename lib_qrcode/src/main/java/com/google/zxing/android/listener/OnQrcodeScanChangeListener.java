package com.google.zxing.android.listener;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

/**
 * description: 二维码扫描解析回调
 * created by kalu on 2021-02-24
 */
@Keep
public interface OnQrcodeScanChangeListener {

    void onSucc(@NonNull String result);

    void onFail(@NonNull String massge);

    void onCancle();
}
