package lib.kalu.qrcode.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.barcode.QRCodeReader;

import lib.kalu.qrcode.R;
import lib.kalu.qrcode.listener.OnCameraBytesChangeListener;
import lib.kalu.qrcode.util.LogUtil;

/**
 * description: 相机预览
 * created by kalu on 2021-02-26
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback, GestureDetector.OnGestureListener {

    private boolean isCall = true;
    private Camera mCamera = null;
    private OnCameraBytesChangeListener onCameraBytesChangeListener;
    private final GestureDetector mGestureDetector = new GestureDetector(this);

    public CameraView(Context context) {
        super(context);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // 延迟400ms
        postDelayed(new Runnable() {
            @Override
            public void run() {
                getHolder().addCallback(CameraView.this);
                getHolder().setKeepScreenOn(true);
                getHolder().setFormat(PixelFormat.TRANSPARENT);
                getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            }
        }, 400);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = 1080 * height / 1920;
        setMeasuredDimension(width, height);

//        int canvasHeight = MeasureSpec.getSize(heightMeasureSpec);
//        int canvasWidth = MeasureSpec.getSize(widthMeasureSpec);
//
//        if (canvasWidth < canvasHeight) {
//            int layerWidth = (int) (canvasWidth * 0.85f);
//            int layerHeight = (int) (layerWidth / 1.6f);
//            setMeasuredDimension(layerWidth, layerHeight);
//        } else {
//            int layerHeight = (int) (canvasHeight * 0.85f);
//            int layerWidth = (int) (layerHeight * 1.6f);
//            setMeasuredDimension(layerWidth, layerHeight);
//        }
    }

    /**********************************************************************************************/

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        // 没有发现相机
        Context context = getContext().getApplicationContext();
        PackageManager packageManager = context.getPackageManager();
        boolean hasSystemFeature = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        if (!hasSystemFeature) {
            Toast.makeText(context, R.string.lib_qrcode_warning, Toast.LENGTH_SHORT).show();
            return;
        }

        try {

            mCamera = Camera.open();

            Camera.Parameters parameters = mCamera.getParameters();//得到摄像头的参数
            parameters.setJpegQuality(100);//设置照片的质量
            parameters.setPreviewSize(1920, 1080);//设置预览尺寸
            parameters.setPictureSize(1920, 1080);//设置照片尺寸
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 连续对焦模式
            //Camera.Parameters.FOCUS_MODE_AUTO; //自动聚焦模式
            //Camera.Parameters.FOCUS_MODE_INFINITY;//无穷远
            //Camera.Parameters.FOCUS_MODE_MACRO;//微距
            //Camera.Parameters.FOCUS_MODE_FIXED;//固定焦距
            mCamera.setParameters(parameters);

            Camera.CameraInfo info = new Camera.CameraInfo();
            //获取摄像头信息
            Camera.getCameraInfo(0, info);
            WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            int rotation = manager.getDefaultDisplay().getRotation();
            //获取摄像头当前的角度
            int degrees = 0;
            switch (rotation) {
                case Surface.ROTATION_0:
                    degrees = 0;
                    break;
                case Surface.ROTATION_90:
                    degrees = 90;
                    break;
                case Surface.ROTATION_180:
                    degrees = 180;
                    break;
                case Surface.ROTATION_270:
                    degrees = 270;
                    break;
            }
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                // 前置摄像头
                int result = (360 - ((info.orientation + degrees) % 360)) % 360; // compensate the mirror
                mCamera.setDisplayOrientation(result);
            } else {
                // 后置摄像头
                int result = (info.orientation - degrees + 360) % 360;
                mCamera.setDisplayOrientation(result);
            }

            parameters.setPictureFormat(ImageFormat.JPEG);
            mCamera.setPreviewDisplay(getHolder());//通过SurfaceView显示取景画面
            mCamera.startPreview();//开始预览

            int height = getHeight();
            int width = getWidth();
            float min = Math.min(height, width) * 0.56f;

            int left = (int) (width / 2 - min / 2);
            int top = (int) (height / 2 - min / 2);
            int right = (int) (left + min);
            int bottom = (int) (top + min);
            Rect rect = new Rect(left, top, right, bottom);

            mCamera.setPreviewCallback(new Camera.PreviewCallback() {

                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {

                    LogUtil.log("surfaceCreated => data = " + data);
                    if (null == data)
                        return;

                    if (isCall) {
                        if (null != onCameraBytesChangeListener) {
                            onCameraBytesChangeListener.onOpen();
                        }
                    }
                    isCall = false;

                    PlanarYUVLuminanceSource source = QRCodeReader.obtain().source(rect, data, width, height);
                    Result result = QRCodeReader.obtain().decode(source);
                    LogUtil.log("surfaceCreated => result = " + result);

                    if (null == result)
                        return;

                    String text = result.getText();
                    LogUtil.log("surfaceCreated => text = " + text);

                    if (null != text && text.length() > 0) {
                        if (null != onCameraBytesChangeListener) {
                            surfaceDestroyed(null);
                            onCameraBytesChangeListener.onSucc(text);
                        }
                    }
                }
            });

        } catch (Exception e) {
            LogUtil.log("surfaceCreated => " + e.getMessage(), e);

            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // CameraManager.getInstance().releaseCamera();

        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    public void setPreviewCallback(@NonNull Camera.PreviewCallback cb) {

        if (null == mCamera)
            return;

        mCamera.setPreviewCallback(cb);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        LogUtil.log("onSingleTapUp => 11");
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                LogUtil.log("onSingleTapUp => " + success);
            }
        });
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public final String praseBytes(@Nullable byte[] bytes) {

        LogUtil.log("praseBytes => bytes = " + bytes);
        if (null == bytes)
            return null;

        int height = getHeight();
        int width = getWidth();
        float min = Math.min(height, width) * 0.56f;

        int left = (int) (width / 2 - min / 2);
        int top = (int) (height / 2 - min / 2);
        int right = (int) (left + min);
        int bottom = (int) (top + min);
        Rect rect = new Rect(left, top, right, bottom);

        PlanarYUVLuminanceSource source = QRCodeReader.obtain().source(rect, bytes, width, height);
        Result result = QRCodeReader.obtain().decode(source);
        LogUtil.log("praseBytes => result = " + result);

        if (null == result)
            return null;

        String text = result.getText();
        LogUtil.log("praseBytes => text = " + text);
        return text;
    }

    public void setOnCameraBytesChangeListener(@NonNull OnCameraBytesChangeListener listener) {
        this.onCameraBytesChangeListener = listener;
    }


    public void openLight() {

        if (null == mCamera)
            return;

        Camera.Parameters parameter = mCamera.getParameters();
        parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        mCamera.setParameters(parameter);
    }

    public void offLight() {

        if (null == mCamera)
            return;

        Camera.Parameters parameter = mCamera.getParameters();
        parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(parameter);
    }
}
