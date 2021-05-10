package lib.kalu.zxing.camerax;

import android.content.Intent;
import android.view.View;

import com.google.zxing.Result;
import com.google.zxing.qrcode.QRCodeReader;

import lib.kalu.zxing.camerax.analyze.Analyzer;
import lib.kalu.zxing.camerax.analyze.AreaRectAnalyzer;
import lib.kalu.zxing.camerax.analyze.BarcodeFormatAnalyzer;
import lib.kalu.zxing.camerax.analyze.ImageAnalyzer;
import lib.kalu.zxing.camerax.analyze.MultiFormatAnalyzer;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;

/**
 * @description:
 * @date: 2021-05-07 14:55
 */
public abstract class CameraScan implements ICamera, ICameraControl {

    public static String SCAN_RESULT = "SCAN_RESULT";

    /**
     * A camera on the device facing the same direction as the device's screen.
     */
    public static int LENS_FACING_FRONT = CameraSelector.LENS_FACING_FRONT;
    /**
     * A camera on the device facing the opposite direction as the device's screen.
     */
    public static int LENS_FACING_BACK = CameraSelector.LENS_FACING_BACK;


    /**
     * 是否需要支持自动缩放
     */
    private boolean isNeedAutoZoom = false;

    /**
     * 是否需要支持触摸缩放
     */
    private boolean isNeedTouchZoom = true;

    /**
     * 是否需要支持触摸缩放
     *
     * @return
     */
    protected boolean isNeedTouchZoom() {
        return isNeedTouchZoom;
    }


    /**
     * 设置是否需要支持触摸缩放
     *
     * @param needTouchZoom
     * @return
     */
    public CameraScan setNeedTouchZoom(boolean needTouchZoom) {
        isNeedTouchZoom = needTouchZoom;
        return this;
    }

    /**
     * 是否需要支持自动缩放
     *
     * @return
     */
    protected boolean isNeedAutoZoom() {
        return isNeedAutoZoom;
    }

    /**
     * 设置是否需要支持自动缩放
     *
     * @param needAutoZoom
     * @return
     */
    public CameraScan setNeedAutoZoom(boolean needAutoZoom) {
        isNeedAutoZoom = needAutoZoom;
        return this;
    }

    /**
     * 设置相机配置，请在{@link #startCamera()}之前调用
     *
     * @param cameraConfig
     */
    public abstract CameraScan setCameraConfig(CameraConfig cameraConfig);

    /**
     * 设置是否分析图像
     *
     * @param analyze
     */
    public abstract CameraScan setAnalyzeImage(boolean analyze);

    /**
     * 设置分析器，内置了一些{@link Analyzer}的实现类如下
     *
     * @param analyzer
     * @see {@link MultiFormatAnalyzer}
     * @see {@link AreaRectAnalyzer}
     * @see {@link ImageAnalyzer}
     * @see {@link BarcodeFormatAnalyzer}
     * @see {@link QRCodeReader}
     */
    public abstract CameraScan setAnalyzer(Analyzer analyzer);

    /**
     * 设置是否震动
     *
     * @param vibrate
     */
    public abstract CameraScan setVibrate(boolean vibrate);

    /**
     * 设置是否播放提示音
     *
     * @param playBeep
     */
    public abstract CameraScan setPlayBeep(boolean playBeep);

    /**
     * 绑定手电筒，绑定后可根据光线传感器，动态显示或隐藏手电筒
     *
     * @param v
     */
    public abstract CameraScan bindFlashlightView(@Nullable View v);

    /**
     * 设置光线足够暗的阈值（单位：lux），需要通过{@link #bindFlashlightView(View)}绑定手电筒才有效
     *
     * @param lightLux
     */
    public abstract CameraScan setDarkLightLux(float lightLux);

    /**
     * 设置光线足够明亮的阈值（单位：lux），需要通过{@link #bindFlashlightView(View)}绑定手电筒才有效
     *
     * @param lightLux
     */
    public abstract CameraScan setBrightLightLux(float lightLux);

    @Nullable
    public static String parseScanResult(Intent data) {
        if (data != null) {
            return data.getStringExtra(SCAN_RESULT);
        }
        return null;
    }

    /*********************/

    /**
     * 设置扫码结果回调
     *
     * @param callback
     */
    public abstract CameraScan setOnCameraScanChangeListener(OnCameraScanChangeListener callback);

    public interface OnCameraScanChangeListener {
        boolean onResult(Result result);
    }
}
