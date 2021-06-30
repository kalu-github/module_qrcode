package com.kalu.barcode;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import lib.kalu.zxing.QrcodeActivity;
import lib.kalu.zxing.qrcode.QrcodeTool;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String url = "http://umspos.caitc.cn/cajinfu-wx-web/#/user/registerQrcode?posterId=AD20210218155306&staffCode=17242a4d541ee348658c12ecde90103b346f05144805be07";
        String logo = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fs9.sinaimg.cn%2Fbmiddle%2F5ceba31bg5d6503750788&refer=http%3A%2F%2Fs9.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1616396178&t=68870ef9ba794e07c9db62b6f3365627";

        // 无LOGO
        findViewById(R.id.create_qr1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView imageView = findViewById(R.id.logo);
                imageView.setImageDrawable(null);

                String qrcode = QrcodeTool.createQrcode(getApplicationContext(), url);
                if (TextUtils.isEmpty(qrcode)) {
                    Toast.makeText(MainActivity.this, "生成二维码错误", Toast.LENGTH_SHORT).show();
                    return;
                }

                Uri parse = Uri.parse(qrcode);
                imageView.setImageURI(parse);
            }
        });

        // 有LOGO
        findViewById(R.id.create_qr2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView imageView = findViewById(R.id.logo);
                imageView.setImageDrawable(null);

                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        String qrcode = QrcodeTool.createQrcodeFromUrl(getApplicationContext(), url, 3, 10, 10, 10, 10, logo);
                        if (TextUtils.isEmpty(qrcode)) {
                            Looper.prepare();
                            Toast.makeText(MainActivity.this, "生成二维码错误", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                            return;
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                imageView.setImageURI(Uri.parse(qrcode));
                            }
                        });
                    }
                }).start();
            }
        });

        // 有LOGO
        findViewById(R.id.create_qr3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView imageView = findViewById(R.id.logo);
                imageView.setImageDrawable(null);

                String qrcode = QrcodeTool.createQrcodeFromRaw(getApplicationContext(), url, 10, 10, 10, 10, 10, R.raw.logo);
                if (TextUtils.isEmpty(qrcode)) {
                    Toast.makeText(MainActivity.this, "生成二维码错误", Toast.LENGTH_SHORT).show();
                    return;
                }

                imageView.setImageURI(Uri.parse(qrcode));
            }
        });

        // 有LOGO
        findViewById(R.id.create_qr4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView imageView = findViewById(R.id.logo);
                imageView.setImageDrawable(null);

                String qrcode = QrcodeTool.createQrcodeFromAssets(getApplicationContext(), url, 20, 1, 1, 1, 1, "logo.jpg");
                if (TextUtils.isEmpty(qrcode)) {
                    Toast.makeText(MainActivity.this, "生成二维码错误", Toast.LENGTH_SHORT).show();
                    return;
                }

                imageView.setImageURI(Uri.parse(qrcode));
            }
        });

        // 解析二维码
        findViewById(R.id.decode_qr1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
                intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
//        intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setAction(Intent.ACTION_PICK);
                /* 取得相片后返回本画面 */
                startActivityForResult(intent, 1000);
            }
        });

        // 默认扫描
        findViewById(R.id.decode_qr2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QrcodeActivity.class);
                startActivity(intent);
            }
        });

        // czxing_decode_qrcode
        findViewById(R.id.czxing_decode_qrcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), lib.kalu.czxing.QrcodeActivity.class);
                startActivityForResult(intent, 20000);
            }
        });

        // czxing_decode_all
        findViewById(R.id.czxing_decode_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), lib.kalu.czxing.QrcodeActivity.class);
                startActivityForResult(intent, 20000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != 1000 || null == data)
            return;

        new Thread(new Runnable() {
            @Override
            public void run() {

                Uri uri = data.getData();
                String qrcodeFromUrl = QrcodeTool.decodeQrcodeFromUrl(getApplicationContext(), uri);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (TextUtils.isEmpty(qrcodeFromUrl)) {
                            Toast.makeText(getApplicationContext(), "未发现二维码", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), qrcodeFromUrl, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }
}
