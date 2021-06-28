package lib.kalu.zbar.impl;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import lib.kalu.zbar.listener.OnCameraStatusChangeListener;

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
    boolean analysis(@NonNull Activity activity, @NonNull ImageProxy image);

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
     * @param beep
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
