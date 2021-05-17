package lib.kalu.zxing.impl;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;

/**
 * @description: 相机生命周期Impl
 * @date: 2021-05-17 13:46
 */
interface ICameraBaseLifecycleImpl {

    /**
     * 启动相机预览
     */
    void start(@NonNull Context context);

    /**
     * 停止相机预览
     */
    void stop(@NonNull Context context);

    void release(@NonNull Context context);

    @Nullable
    Camera getCamera();
}
