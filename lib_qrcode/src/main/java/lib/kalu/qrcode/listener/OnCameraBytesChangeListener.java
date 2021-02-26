package lib.kalu.qrcode.listener;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

/**
 * description: 二维码扫描解析回调
 * created by kalu on 2021-02-24
 */
@Keep
public interface OnCameraBytesChangeListener {

    void onSucc(@NonNull String result);

    void onOpen();
}
