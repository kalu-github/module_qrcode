package lib.kalu.barcode;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.Keep;

import lib.kalu.barcode.listener.OnQrcodeScanChangeListener;
import lib.kalu.barcode.config.QrcodeConfig;
import lib.kalu.barcode.other.ActResultRequest;

/**
 * description: 条形码扫描
 * created by kalu on 2021-02-24
 */
@Keep
public final class BarcodeManager {

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

    public static void start(Activity activity, QrcodeConfig qrcodeConfig, OnQrcodeScanChangeListener scanCallback) {
        if (qrcodeConfig == null) {
            qrcodeConfig = new QrcodeConfig.Builder().builder();
        }
        Intent intent = new Intent(activity.getApplicationContext(), BarcodeActivity.class);
        //传递数据
        intent.putExtra(BarcodeManager.INTENT_KEY_CONFIG_MODEL, qrcodeConfig);
        ActResultRequest actResultRequest = new ActResultRequest(activity);
        actResultRequest.startForResult(intent, scanCallback);
       // activity.overridePendingTransition(qrcodeConfig.getActivityOpenAnime(), android.R.anim.fade_out);
    }
}
