package lib.kalu.zxing;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import lib.kalu.zxing.impl.ICameraImpl;
import lib.kalu.zxing.camerax.CameraManager;
import lib.kalu.zxing.listener.OnCameraScanChangeListener;
import lib.kalu.zxing.util.LogUtil;
import lib.kalu.zxing.qrcode.QrcodeTool;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;

/**
 * @description: 二维码扫描UI
 * @date: 2021-05-07 10:50
 */
@Keep
public final class QrcodeActivity extends AppCompatActivity implements OnCameraScanChangeListener {

    public static final int RESULT_CODE_SUCC = 10890001;
    public static final int RESULT_CODE_FAIL = 10890002;
    public static final int RESULT_CODE_CANCLE = 10890003;
    public static final String RESULT_INTENT_DATA = "result_intent_data";

    private ICameraImpl mCameraScan;

    @Override
    public void onBackPressed() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setResult(RESULT_CODE_CANCLE);
        super.onBackPressed();
    }

    @Override
    public void finish() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.moudle_zxing_ui_activity_qrcode);

        // 关闭
        findViewById(R.id.lib_zxing_ui_id_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // 相册
        findViewById(R.id.lib_zxing_ui_id_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent();
                    /* 开启Pictures画面Type设定为image */
                    intent.setType("image/*");
                    /* 使用Intent.ACTION_GET_CONTENT这个Action */
                    // intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setAction(Intent.ACTION_PICK);
                    /* 取得相片后返回本画面 */
                    startActivityForResult(intent, 0X87);
                } catch (Exception e) {
                }
            }
        });

        // 手电筒
        findViewById(R.id.lib_zxing_ui_id_flashlight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    boolean isTorch = mCameraScan.isTorchEnabled();
                    mCameraScan.enableTorch(!isTorch);

                    TextView textView = findViewById(R.id.lib_zxing_ui_id_flashlight);
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, !isTorch ? R.drawable.moudle_zxing_ic_flashlight_on : R.drawable.moudle_zxing_ic_flashlight_off, 0, 0);
                    textView.setText(!isTorch ? R.string.moudle_zxing_string_light_off : R.string.moudle_zxing_string_light_on);
                } catch (Exception e) {
                }
            }
        });

        // 预览
        initCamera();

        // 相机
        startCamera();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startCamera();
        TextView textView = findViewById(R.id.lib_zxing_ui_id_flashlight);
        textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.moudle_zxing_ic_flashlight_off, 0, 0);
        textView.setText(R.string.moudle_zxing_string_light_on);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopCamera();
    }

    /**
     * 初始化相机
     */
    public void initCamera() {
        PreviewView previewView = findViewById(R.id.lib_zxing_ui_id_preview);
        mCameraScan = new CameraManager(this, previewView);
        mCameraScan.setOnCameraScanChangeListener(this);
    }

    /**
     * 启动相机预览
     */
    public void startCamera() {
        if (mCameraScan != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                mCameraScan.start(getApplicationContext());
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0X86);
            }
        }
    }

    /**
     * 暂停相机预览
     */
    public void stopCamera() {
        if (mCameraScan != null) {
            mCameraScan.start(getApplicationContext());
        }
    }

    /**
     * 释放相机
     */
    private void releaseCamera() {
        if (mCameraScan != null) {
            mCameraScan.release(getApplicationContext());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != 0X86)
            return;
        requestCameraPermissionResult(permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.log("onActivityResult => requestCode = " + requestCode + ", resultCode = " + resultCode + ", data = " + data + ", uri = " + (null == data ? "null" : data.getData()));
        if (null == data || null == data.getData() || requestCode != 0X87)
            return;

        Uri uri = data.getData();
        if (null == uri) {
            Toast.makeText(getApplicationContext(), R.string.moudle_zxing_string_null, Toast.LENGTH_SHORT).show();
            return;
        }

        String s = QrcodeTool.decodeQrcodeFromUrl(getApplicationContext(), uri);
        if (null == s || s.length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.moudle_zxing_string_null, Toast.LENGTH_SHORT).show();
            return;
        }

        LogUtil.log("onActivityResult => s = " + s);
        Intent intent = new Intent();
        intent.putExtra(RESULT_INTENT_DATA, s);
        setResult(RESULT_CODE_SUCC, intent);
        finish();
    }

    /**
     * 请求Camera权限回调结果
     *
     * @param permissions
     * @param grantResults
     */
    public void requestCameraPermissionResult(@NonNull String[] permissions, @NonNull int[] grantResults) {

        int length = permissions.length;
        for (int i = 0; i < length; i++) {
            if (Manifest.permission.CAMERA.equals(permissions[i])) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    startCamera();
                    return;
                }
            }
        }

        finish();
    }

    @Override
    protected void onDestroy() {
        releaseCamera();
        super.onDestroy();

        try {
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            System.exit(0);
        }
    }

    /**
     * Get {@link ICameraImpl}
     *
     * @return {@link #mCameraScan}
     */
    public ICameraImpl getCameraScan() {
        return mCameraScan;
    }

    /**
     * 接收扫码结果回调
     *
     * @param result 扫码结果
     * @return 返回true表示拦截，将不自动执行后续逻辑，为false表示不拦截，默认不拦截
     */
    @Override
    public boolean onResult(Result result) {
        if (null != result && null != result.getText() && result.getText().length() > 0) {
            LogUtil.log("onScanResultCallback => text = " + result.getText());
            releaseCamera();

            // 震动
            try {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(40);
            } catch (Exception e) {
                LogUtil.log("onScanResultCallback => msg = " + e.getMessage(), e);
            }

            Intent intent = new Intent();
            intent.putExtra(RESULT_INTENT_DATA, result.getText());
            setResult(RESULT_CODE_SUCC, intent);
            finish();
        }
        return true;
    }
}