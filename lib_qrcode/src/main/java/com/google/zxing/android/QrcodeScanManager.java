package com.google.zxing.android;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.Keep;

import com.google.zxing.android.listener.OnQrcodeScanChangeListener;
import com.google.zxing.android.config.QrcodeScanConfig;
import com.google.zxing.android.other.ActResultRequest;

/**
 * description: 二维码扫描
 * created by kalu on 2021-02-24
 */
@Keep
public final class QrcodeScanManager {

    //常量
    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_FAIL = 1;
    public static final int RESULT_CANCLE = 2;
    public static final String INTENT_KEY_RESULT_SUCCESS = "INTENT_KEY_RESULT_SUCCESS";
    public static final String INTENT_KEY_RESULT_ERROR = "INTENT_KEY_RESULT_ERROR";


    //跳转传入的数据
    public static final String INTENT_KEY_CONFIG_MODEL = "INTENT_KEY_CONFIG_MODEL";


    public static void start(Activity activity, OnQrcodeScanChangeListener scanCallback) {
        start(activity, null, scanCallback);
    }

    public static void start(Activity activity, QrcodeScanConfig qrcodeScanConfig, OnQrcodeScanChangeListener scanCallback) {
        if (qrcodeScanConfig == null) {
            qrcodeScanConfig = new QrcodeScanConfig.Builder().builder();
        }
        Intent intent = new Intent(activity.getApplicationContext(), CaptureActivity.class);
        //传递数据
        intent.putExtra(QrcodeScanManager.INTENT_KEY_CONFIG_MODEL, qrcodeScanConfig);
        ActResultRequest actResultRequest = new ActResultRequest(activity);
        actResultRequest.startForResult(intent, scanCallback);
        activity.overridePendingTransition(qrcodeScanConfig.getActivityOpenAnime(), android.R.anim.fade_out);
    }
}
