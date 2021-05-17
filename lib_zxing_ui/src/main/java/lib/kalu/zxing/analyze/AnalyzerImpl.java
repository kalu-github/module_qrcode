package lib.kalu.zxing.analyze;

import com.google.zxing.Result;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.ImageProxy;

/**
 * @description: 分析器
 * @date: 2021-05-07 14:56
 */
public interface AnalyzerImpl {

    @Nullable
    Result analyze(@NonNull ImageProxy imageProxy, int orientation);

    @Nullable
    Result analyze(byte[] data, int width, int height);

    Result analyze(byte[] data, int dataWidth, int dataHeight, int left, int top, int width, int height);
}
