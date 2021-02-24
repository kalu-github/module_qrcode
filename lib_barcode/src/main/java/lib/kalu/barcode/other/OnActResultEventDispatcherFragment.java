package lib.kalu.barcode.other;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;

import lib.kalu.barcode.QrcodeManager;
import lib.kalu.barcode.listener.OnQrcodeScanChangeListener;

/**
 * <pre>
 *     author : maning
 *     e-mail : xxx@xx
 *     time   : 2018/06/04
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class OnActResultEventDispatcherFragment extends Fragment {
    public static final String TAG = "on_act_result_event_dispatcher";

    private SparseArray<OnQrcodeScanChangeListener> mCallbacks = new SparseArray<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void startForResult(Intent intent, OnQrcodeScanChangeListener callback) {
        mCallbacks.put(callback.hashCode(), callback);
        startActivityForResult(intent, callback.hashCode());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        OnQrcodeScanChangeListener callback = mCallbacks.get(requestCode);
        mCallbacks.remove(requestCode);

        if (null == callback)
            return;

        switch (resultCode) {
            case QrcodeManager.RESULT_SUCCESS:
                String result = data.getStringExtra(QrcodeManager.INTENT_KEY_RESULT_SUCCESS);
                callback.onSucc(result);
                break;
            case QrcodeManager.RESULT_FAIL:
                String message = data.getStringExtra(QrcodeManager.INTENT_KEY_RESULT_ERROR);
                callback.onFail(message);
                break;
            case QrcodeManager.RESULT_CANCLE:
                callback.onCancle();
                break;
        }
    }
}
