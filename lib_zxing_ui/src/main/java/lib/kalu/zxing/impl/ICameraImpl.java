package lib.kalu.zxing.impl;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.Result;
import com.google.zxing.qrcode.QRCodeReader;

import lib.kalu.zxing.analyze.AnalyzerBaseImpl;
import lib.kalu.zxing.listener.OnCameraStatusChangeListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

/**
 * @description: 相机生命周期Impl
 * @date: 2021-05-17 13:46
 */
public interface ICameraImpl extends ICameraBaseLifecycleImpl, ICameraBaseControlImpl {

    int LENS_FACING_FRONT = CameraSelector.LENS_FACING_FRONT;
    int LENS_FACING_BACK = CameraSelector.LENS_FACING_BACK;

    /**
     * 解析数据
     *
     * @param image
     * @return
     */
    boolean analysis(@NonNull Activity activity, @Nullable ImageView imageView, @NonNull ImageProxy image);

//    /**
//     * 设置是否需要支持自动缩放
//     *
//     * @param needAutoZoom
//     * @return
//     */
//    public ICameraImpl setNeedAutoZoom(boolean needAutoZoom) {
//        isNeedAutoZoom = needAutoZoom;
//        return this;
//    }

    boolean isZoom();

    /**
     * 设置相机配置，请在{@link #start(@NonNull Context context)}之前调用
     *
     * @param cameraConfig
     */
    ICameraImpl setCameraConfig(@NonNull ImageAnalysis.Builder builder);

//    /**
//     * 设置是否分析图像
//     *
//     * @param analyze
//     */
//   ICameraImpl setAnalyzeImage(boolean analyze);

    /**
     * 设置是否震动
     *
     * @param vibrate
     */
    ICameraImpl setVibrate(boolean vibrate);

    /**
     * 设置是否播放提示音
     *
     * @param playBeep
     */
    ICameraImpl setBeep(boolean beep);

//    /**
//     * 绑定手电筒，绑定后可根据光线传感器，动态显示或隐藏手电筒
//     *
//     * @param v
//     */
//    ICameraImpl bindFlashlightView(@Nullable View v);

//    /**
//     * 设置光线足够暗的阈值（单位：lux），需要通过{@link #bindFlashlightView(View)}绑定手电筒才有效
//     *
//     * @param lightLux
//     */
//    ICameraImpl setDarkLightLux(float lightLux);
//
//    /**
//     * 设置光线足够明亮的阈值（单位：lux），需要通过{@link #bindFlashlightView(View)}绑定手电筒才有效
//     *
//     * @param lightLux
//     */
//    ICameraImpl setBrightLightLux(float lightLux);

    /*********************/

    /**
     * 设置扫码结果回调
     *
     * @param callback
     */
    ICameraImpl setOnCameraScanChangeListener(@NonNull OnCameraStatusChangeListener callback);
}
