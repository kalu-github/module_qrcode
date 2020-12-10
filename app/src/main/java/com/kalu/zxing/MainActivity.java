package com.kalu.zxing;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.android.MNScanManager;
import com.google.zxing.android.encode.EncodeUtil;
import com.google.zxing.android.model.MNScanConfig;
import com.google.zxing.android.other.MNScanCallback;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 无LOGO
        findViewById(R.id.create_qr1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap bitmap = EncodeUtil.encode("https://www.baidu.com/");
                if (null == bitmap)
                    return;

                ImageView imageView = findViewById(R.id.logo);
                imageView.setImageBitmap(bitmap);
            }
        });

        // 有LOGO
        findViewById(R.id.create_qr2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String path = EncodeUtil.encodeRaw(getApplicationContext(), "https://www.baidu.com/", R.raw.logo);
                if (TextUtils.isEmpty(path)) {
                    Toast.makeText(MainActivity.this, "生成二维码错误", Toast.LENGTH_SHORT).show();
                    return;
                }

                ImageView imageView = findViewById(R.id.logo);
                imageView.setImageURI(Uri.parse(path));
            }
        });

        // 默认扫描
        findViewById(R.id.scan_normal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestCameraPerm();
                MNScanManager.startScan(MainActivity.this, new MNScanCallback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        handlerResult(resultCode, data);
                    }
                });
            }
        });

        // 自定义扫描
        findViewById(R.id.scan_custom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestCameraPerm();
                MNScanConfig scanConfig = new MNScanConfig.Builder()
                        //设置完成震动
                        .isShowVibrate(false)
                        //扫描完成声音
                        .isShowBeep(true)
                        //显示相册功能
                        .isShowPhotoAlbum(true)
                        //打开扫描页面的动画
                        .setActivityOpenAnime(R.anim.activity_anmie_in)
                        //退出扫描页面动画
                        .setActivityExitAnime(R.anim.activity_anmie_out)
                        //自定义文案
                        .setScanHintText("请将二维码放入框中...")
                        //扫描线的颜色
                        .setScanColor("#FFFF00")
                        //是否显示缩放控制器
                        .isShowZoomController(true)
                        //显示缩放控制器位置
                        .setZoomControllerLocation(MNScanConfig.ZoomControllerLocation.Bottom)
                        .builder();
                MNScanManager.startScan(MainActivity.this, scanConfig, new MNScanCallback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        handlerResult(resultCode, data);
                    }
                });
            }
        });
    }

    public void requestCameraPerm() {
        //判断权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 10010);
            }
        }
    }

    private void handlerResult(int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        switch (resultCode) {
            case MNScanManager.RESULT_SUCCESS:
                String msg = data.getStringExtra(MNScanManager.INTENT_KEY_RESULT_SUCCESS);
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                break;
            case MNScanManager.RESULT_FAIL:
                String msg1 = data.getStringExtra(MNScanManager.INTENT_KEY_RESULT_ERROR);
                Toast.makeText(this, msg1, Toast.LENGTH_SHORT).show();
                break;
            case MNScanManager.RESULT_CANCLE:
                Toast.makeText(this, "取消扫码", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
