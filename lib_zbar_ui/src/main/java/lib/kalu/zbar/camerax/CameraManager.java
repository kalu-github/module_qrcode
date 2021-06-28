package lib.kalu.zbar.camerax;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.google.common.util.concurrent.ListenableFuture;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.Preview;
import androidx.camera.core.TorchState;
import androidx.camera.core.ZoomState;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import net.sourceforge.zbar.Symbol;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lib.kalu.zbar.analyze.AnalyzerQrcode;
import lib.kalu.zbar.impl.ICameraImpl;
import lib.kalu.zbar.listener.OnCameraStatusChangeListener;
import lib.kalu.zbar.sensor.LightSensorEventManager;
import lib.kalu.zbar.util.BeepUtil;
import lib.kalu.zbar.util.LogUtil;
import lib.kalu.zbar.util.MathUtils;
import lib.kalu.zbar.util.VibratorUtil;

/**
 * @description:
 * @date: 2021-05-07 14:55
 */
public final class CameraManager implements ICameraImpl {

    /**
     * true：正常
     * false: 暂停
     */
    private static final String INTENT_ZXING_STATUS = "intent_zxing_status";

    /**
     * Defines the maximum duration in milliseconds between a touch pad
     * touch and release for a given touch to be considered a tap (click) as
     * opposed to a hover movement gesture.
     */
    private static final int HOVER_TAP_TIMEOUT = 150;

    /**
     * Defines the maximum distance in pixels that a touch pad touch can move
     * before being released for it to be considered a tap (click) as opposed
     * to a hover movement gesture.
     */
    private static final int HOVER_TAP_SLOP = 20;

    private final CameraConfig CAMERA_CONFIG = new CameraConfig();

    private Camera mCamera;

//    private final MutableLiveData<Result> MUTABLE_LIVE_DATA = new MutableLiveData<>();

    private OnCameraStatusChangeListener mOnCameraStatusChangeListener;

//    private AmbientLightManager mAmbientLightManager;

    //    private int mOrientation;
    private long mLastAutoZoomTime;
    private long mLastHoveTapTime;
    private boolean isClickTap;
    private float mDownX;
    private float mDownY;


    /*******************/

    private CameraManager() {
    }

    private static class Holder {
        static final CameraManager instance = new CameraManager();
    }

    public static CameraManager build() {
        return Holder.instance;
    }

    /*******************/

    public void init(@NonNull FragmentActivity activity, @NonNull PreviewView previewView, boolean touchScale, boolean sensorEvent) {
//        MUTABLE_LIVE_DATA.observe(activity, new Observer<Result>() {
//            @Override
//            public void onChanged(Result result) {
//                callback(activity, result);
//            }
//        });

        Context context = activity.getApplicationContext();

        // 手势缩放
        if (touchScale) {
            ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                @Override
                public boolean onScale(ScaleGestureDetector detector) {
                    float scale = detector.getScaleFactor();
                    if (mCamera != null) {
                        float ratio = mCamera.getCameraInfo().getZoomState().getValue().getZoomRatio();
                        zoomTo(ratio * scale);
                    }
                    return true;
                }

                @Override
                public boolean onScaleBegin(ScaleGestureDetector detector) {
                    activity.getIntent().putExtra(INTENT_ZXING_STATUS, false);
                    return true;
                }

                @Override
                public void onScaleEnd(ScaleGestureDetector detector) {
                    activity.getIntent().putExtra(INTENT_ZXING_STATUS, true);
                }
            });
            previewView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getPointerCount() == 1) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                activity.getIntent().putExtra(INTENT_ZXING_STATUS, false);
                                isClickTap = true;
                                mDownX = event.getX();
                                mDownY = event.getY();
                                mLastHoveTapTime = System.currentTimeMillis();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                activity.getIntent().putExtra(INTENT_ZXING_STATUS, false);
                                isClickTap = MathUtils.distance(mDownX, mDownY, event.getX(), event.getY()) < HOVER_TAP_SLOP;
                                break;
                            case MotionEvent.ACTION_UP:
                                if (isClickTap && mLastHoveTapTime + HOVER_TAP_TIMEOUT > System.currentTimeMillis()) {
                                    MeteringPoint point = previewView.getMeteringPointFactory().createPoint(event.getX(), event.getY());
                                    mCamera.getCameraControl().startFocusAndMetering(new FocusMeteringAction.Builder(point).build());
                                }
                                activity.getIntent().putExtra(INTENT_ZXING_STATUS, true);
                                break;
                        }
                    }

                    return isZoom() ? scaleGestureDetector.onTouchEvent(event) : false;
                }
            });
        }

        // 光纤传感器
        if (sensorEvent) {
            LightSensorEventManager.build().register();
            LightSensorEventManager.build().setOnLightSensorEventListener(new LightSensorEventManager.OnLightSensorEventListener() {
                @Override
                public void onSensorChanged(boolean dark, float lightLux) {

                    if (null != mOnCameraStatusChangeListener) {
                        mOnCameraStatusChangeListener.onSensor(dark, lightLux);
                    }
                }
            });
        }
    }

    @Override
    public void start(@NonNull FragmentActivity activity, @NonNull PreviewView previewView) {

        Context context = activity.getApplicationContext();
        ListenableFuture<ProcessCameraProvider> instance = ProcessCameraProvider.getInstance(context);

        instance.addListener(new Runnable() {
            @Override
            public void run() {

                try {
                    Preview preview = CAMERA_CONFIG.options(new Preview.Builder());

                    // 相机选择器
                    CameraSelector.Builder requireLensFacing = new CameraSelector.Builder().requireLensFacing(LENS_FACING_BACK);
                    CameraSelector cameraSelector = CAMERA_CONFIG.options(requireLensFacing);
                    // 设置SurfaceProvider
                    preview.setSurfaceProvider(previewView.getSurfaceProvider());

                    // 图像分析
                    ImageAnalysis.Builder builder = new ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST);
                    ImageAnalysis imageAnalysis = CAMERA_CONFIG.options(builder);

                    // 分析
                    ExecutorService threadPool = Executors.newFixedThreadPool(2);
                    imageAnalysis.setAnalyzer(threadPool, new ImageAnalysis.Analyzer() {
                        @Override
                        public void analyze(@NonNull ImageProxy image) {
                            boolean analysis = analysis(activity, image);
                            if (analysis) {
                                threadPool.shutdownNow();
                            }
                            image.close();
                        }
                    });
                    if (mCamera != null) {
                        instance.get().unbindAll();
                    }
                    //绑定到生命周期
                    mCamera = instance.get().bindToLifecycle(activity, cameraSelector, preview, imageAnalysis);
                } catch (Exception e) {
                    LogUtil.log(e.getMessage());
                }
            }
        }, ContextCompat.getMainExecutor(context));
    }

    @Override
    public boolean analysis(@NonNull Activity activity, @NonNull ImageProxy image) {

        if (null == mOnCameraStatusChangeListener)
            return false;

        boolean status = activity.getIntent().getBooleanExtra(INTENT_ZXING_STATUS, true);
        LogUtil.log("analysis => status = " + status + ", thread = " + Thread.currentThread().getName());
        if (!status)
            return false;

        Context context = activity.getApplicationContext();
        AnalyzerQrcode analyzerQrcode = AnalyzerQrcode.getAnalyzer();
        Symbol symbol = analyzerQrcode.analyzeImage(context, image, context.getResources().getConfiguration().orientation);
        if (null == symbol)
            return false;

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                release(context);
                mOnCameraStatusChangeListener.onResult(symbol);
            }
        });
        return true;
    }

    @Override
    public boolean isZoom() {
        return true;
    }

    @Override
    public ICameraImpl setCameraConfig(@NonNull ImageAnalysis.Builder builder) {
        if (CAMERA_CONFIG != null) {
            this.CAMERA_CONFIG.options(builder);
        }
        return this;
    }

    private boolean handleAutoZoom(@NonNull Activity activity, @Nullable Symbol result, float distance) {

        Context context = activity.getApplicationContext();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int size = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
        if (distance * 4 < size) {
            mLastAutoZoomTime = System.currentTimeMillis();
            zoomIn();

            if (null != mOnCameraStatusChangeListener) {
                release(context);
                mOnCameraStatusChangeListener.onResult(result);
            }
            return true;
        }
        return false;
    }

    @Override
    public void zoomIn() {
        if (mCamera != null) {
            float ratio = mCamera.getCameraInfo().getZoomState().getValue().getZoomRatio() + 0.1f;
            float maxRatio = mCamera.getCameraInfo().getZoomState().getValue().getMaxZoomRatio();
            if (ratio <= maxRatio) {
                mCamera.getCameraControl().setZoomRatio(ratio);
            }
        }
    }

    @Override
    public void zoomOut() {
        if (mCamera != null) {
            float ratio = mCamera.getCameraInfo().getZoomState().getValue().getZoomRatio() - 0.1f;
            float minRatio = mCamera.getCameraInfo().getZoomState().getValue().getMinZoomRatio();
            if (ratio >= minRatio) {
                mCamera.getCameraControl().setZoomRatio(ratio);
            }
        }
    }


    @Override
    public void zoomTo(float ratio) {
        if (mCamera != null) {
            ZoomState zoomState = mCamera.getCameraInfo().getZoomState().getValue();
            float maxRatio = zoomState.getMaxZoomRatio();
            float minRatio = zoomState.getMinZoomRatio();
            float zoom = Math.max(Math.min(ratio, maxRatio), minRatio);
            mCamera.getCameraControl().setZoomRatio(zoom);
        }
    }

    @Override
    public void lineZoomIn() {
        if (mCamera != null) {
            float zoom = mCamera.getCameraInfo().getZoomState().getValue().getLinearZoom() + 0.1f;
            if (zoom <= 1f) {
                mCamera.getCameraControl().setLinearZoom(zoom);
            }
        }
    }

    @Override
    public void lineZoomOut() {
        if (mCamera != null) {
            float zoom = mCamera.getCameraInfo().getZoomState().getValue().getLinearZoom() - 0.1f;
            if (zoom >= 0f) {
                mCamera.getCameraControl().setLinearZoom(zoom);
            }
        }
    }

    @Override
    public void lineZoomTo(@FloatRange(from = 0.0, to = 1.0) float linearZoom) {
        if (mCamera != null) {
            mCamera.getCameraControl().setLinearZoom(linearZoom);
        }
    }

    @Override
    public void enableTorch(boolean torch) {
        if (mCamera != null && hasFlashUnit()) {
            mCamera.getCameraControl().enableTorch(torch);
        }
    }

    @Override
    public boolean isTorchEnabled() {
        if (mCamera != null) {
            return mCamera.getCameraInfo().getTorchState().getValue() == TorchState.ON;
        }
        return false;
    }

    /**
     * 是否支持闪光灯
     *
     * @return
     */
    @Override
    public boolean hasFlashUnit() {
        if (mCamera != null) {
            return mCamera.getCameraInfo().hasFlashUnit();
        }
        return false;
    }

    @Override
    public ICameraImpl setVibrate(boolean vibrate) {
        VibratorUtil.vibrate = vibrate;
        return this;
    }

    @Override
    public ICameraImpl setBeep(boolean beep) {
        BeepUtil.beep = beep;
        return this;
    }

    @Nullable
    @Override
    public Camera getCamera() {
        return mCamera;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void release(@NonNull Context context) {

        // 光线传感器
        LightSensorEventManager.build().unregister();

        try {
            ListenableFuture<ProcessCameraProvider> instance = ProcessCameraProvider.getInstance(context);
            ProcessCameraProvider provider = instance.get();
            provider.unbindAll();
            provider.shutdown();
        } catch (Exception e) {
            LogUtil.log(e.getMessage(), e);
        }

        // 蜂鸣
//        BeepUtil.beep();
//        BeepUtil.release();

        // 震动
//        VibratorUtil.vibrator(context);
    }

    @Override
    public void pause(@NonNull FragmentActivity activity) {
        activity.getIntent().putExtra(INTENT_ZXING_STATUS, false);
    }

    @Override
    public void resume(@NonNull FragmentActivity activity) {
        activity.getIntent().putExtra(INTENT_ZXING_STATUS, true);
    }

//    @Override
//    public ICameraImpl bindFlashlightView(@Nullable View v) {
////        if (mAmbientLightManager != null) {
////            mAmbientLightManager.setLightSensorEnabled(v != null);
////        }
//        return this;
//    }

    @Override
    public ICameraImpl setOnCameraScanChangeListener(@NonNull OnCameraStatusChangeListener callback) {
        this.mOnCameraStatusChangeListener = callback;
        return this;
    }
}
