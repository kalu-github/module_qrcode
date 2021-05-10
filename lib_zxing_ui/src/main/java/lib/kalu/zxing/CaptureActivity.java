package lib.kalu.zxing;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import lib.kalu.zxing.camerax.CameraScan;
import lib.kalu.zxing.camerax.DefaultCameraScan;
import lib.kalu.zxing.camerax.util.LogUtils;
import lib.kalu.zxing.camerax.util.PermissionUtils;
import lib.kalu.zxing.util.LogUtil;
import lib.kalu.zxing.util.ZxingUtil;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;

/**
 * @description: 二维码扫描UI
 * @date: 2021-05-07 10:50
 */
@Keep
public final class CaptureActivity extends AppCompatActivity implements CameraScan.OnCameraScanChangeListener {

    private static final String INTENT_DATA = "intent_data";
    private static final int RESULT_SUCC = 10890001;
    private static final int RESULT_FAIL = 10890002;
    private static final int RESULT_CANCLE = 10890003;

    private CameraScan mCameraScan;

    @Override
    public void onBackPressed() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setResult(RESULT_CANCLE);
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
        setContentView(R.layout.lib_zxing_ui_activity_capture);

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
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, !isTorch ? R.drawable.lib_zxing_ui_ic_flashlight_on : R.drawable.lib_zxing_ui_ic_flashlight_off, 0, 0);
                    textView.setText(!isTorch ? R.string.lib_zxing_string_light_off : R.string.lib_zxing_string_light_on);
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
        mCameraScan = new DefaultCameraScan(this, previewView);
        mCameraScan.setOnCameraScanChangeListener(this);
    }

    /**
     * 启动相机预览
     */
    public void startCamera() {
        if (mCameraScan != null) {
            if (PermissionUtils.checkPermission(this, Manifest.permission.CAMERA)) {
                mCameraScan.startCamera();
            } else {
                LogUtil.log("checkPermissionResult != PERMISSION_GRANTED");
                PermissionUtils.requestPermission(this, Manifest.permission.CAMERA, 0X86);
            }
        }
    }

    /**
     * 暂停相机预览
     */
    public void stopCamera() {
        if (mCameraScan != null) {
            mCameraScan.stopCamera();
        }
    }

    /**
     * 释放相机
     */
    private void releaseCamera() {
        if (mCameraScan != null) {
            mCameraScan.release();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.log("onActivityResult => requestCode = " + requestCode + ", resultCode = " + resultCode + ", data = " + data + ", uri = " + (null == data ? "null" : data.getData()));
        if (null == data || null == data.getData() || requestCode != 0X87)
            return;

        Uri uri = data.getData();
        if (null == uri) {
            Toast.makeText(getApplicationContext(), R.string.lib_zxing_string_null, Toast.LENGTH_SHORT).show();
            return;
        }

        String s = ZxingUtil.decodeQrcodeFromUrl(getApplicationContext(), uri);
        if (null == s || s.length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.lib_zxing_string_null, Toast.LENGTH_SHORT).show();
            return;
        }

        LogUtil.log("onActivityResult => s = " + s);
        Intent intent = new Intent();
        intent.putExtra(INTENT_DATA, s);
        setResult(RESULT_SUCC, intent);
        finish();
    }

    /**
     * 请求Camera权限回调结果
     *
     * @param permissions
     * @param grantResults
     */
    public void requestCameraPermissionResult(@NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtils.requestPermissionsResult(Manifest.permission.CAMERA, permissions, grantResults)) {
            startCamera();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        releaseCamera();
        super.onDestroy();
    }

    /**
     * Get {@link CameraScan}
     *
     * @return {@link #mCameraScan}
     */
    public CameraScan getCameraScan() {
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
            intent.putExtra(INTENT_DATA, result.getText());
            setResult(RESULT_SUCC, intent);
            finish();
        }
        return true;
    }
}