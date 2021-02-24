package lib.kalu.qrcode.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

/**
 * description: surfaceview
 * create by kalu on 2019/1/25 17:01
 */
public class ZxingView extends SurfaceView implements SurfaceHolder.Callback {

    private Camera mCamera;

    /**********************************************************************************************/

    public ZxingView(Context context) {
        this(context, null, 0);
    }

    public ZxingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZxingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        getHolder().addCallback(this);
        getHolder().setKeepScreenOn(true);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int height = MeasureSpec.getSize(heightMeasureSpec);
        final int width = 480 * height / 640;
        setMeasuredDimension(width, height);

//        final int canvasHeight = MeasureSpec.getSize(heightMeasureSpec);
//        final int canvasWidth = MeasureSpec.getSize(widthMeasureSpec);
//
//        if (canvasWidth < canvasHeight) {
//            final int layerWidth = (int) (canvasWidth * 0.85f);
//            final int layerHeight = (int) (layerWidth / 1.6f);
//            setMeasuredDimension(layerWidth, layerHeight);
//        } else {
//            final int layerHeight = (int) (canvasHeight * 0.85f);
//            final int layerWidth = (int) (layerHeight * 1.6f);
//            setMeasuredDimension(layerWidth, layerHeight);
//        }
    }

    /**********************************************************************************************/

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        final PackageManager pm = getContext().getPackageManager();
        final boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        if (!hasACamera) {
            Log.e("kalu", "没有发现相机");
            return;
        }

        try {
            mCamera = Camera.open();

            Camera.Parameters parameters = mCamera.getParameters();//得到摄像头的参数
            parameters.setJpegQuality(100);//设置照片的质量
            parameters.setPreviewSize(640, 480);//设置预览尺寸
            parameters.setPictureSize(640, 480);//设置照片尺寸
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

            mCamera.setPreviewDisplay(getHolder());//通过SurfaceView显示取景画面
            mCamera.startPreview();//开始预览

            mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    //Log.e("kalu", "onPreviewFrame");

                   /* boolean alive1 = Thread1.isAlive();
                    if (!alive1) return;
                    //Log.e("kalu11", "setPreviewCallback ==> ");

                    final Message obtain = Message.obtain();
                    obtain.obj = data;
                    obtain.arg1 = 640;
                    obtain.arg2 = 480;*/
//                    mHandler.removeCallbacksAndMessages(null);
//                    mHandler.sendMessage(obtain);
                }
            });

        } catch (Exception e) {
            Log.e("kalu", e.getMessage(), e);
            if (mCamera != null) {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
}
