package lib.kalu.zxing.camerax.analyze;

import com.google.zxing.Result;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.ImageProxy;
import android.content.res.Configuration;

/**
 * @description: 分析器
 * @date:  2021-05-07 14:56
 */
public interface AnalyzerImpl {

    /**
     * Analyzes an image to produce a result.
     * @param image The image to analyze
     * @param orientation {@link Configuration#ORIENTATION_LANDSCAPE}, {@link Configuration#ORIENTATION_PORTRAIT}.
     * @return
     */
    @Nullable
    Result analyze(@NonNull ImageProxy image,int orientation);
}
