package lib.kalu.zxing.camerax;


import androidx.annotation.Nullable;
import androidx.camera.core.Camera;


/**
 * @description:
 * @date:  2021-05-07 14:55
 */
public interface ICamera {

    /**
     * 启动相机预览
     */
    void startCamera();

    /**
     * 停止相机预览
     */
    void stopCamera();

    /**
     * 获取{@link Camera}
     * @return
     */
    @Nullable Camera getCamera();

    /**
     * 释放
     */
    void release();

}
