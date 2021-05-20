package lib.kalu.zxing.analyze;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;

import com.google.zxing.qrcode.QrcodeDecodeConfig;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @description:
 * @date: 2021-05-07 14:57
 */
abstract class AnalyzerFormat extends AnalyzerCore {

    private Reader mReader;

    public AnalyzerFormat() {
        super();
        initReader();
    }

    public AnalyzerFormat(@Nullable QrcodeDecodeConfig config) {
        super(config);
        initReader();
    }

    private void initReader() {
        mReader = createReader();
    }

    @Nullable
    @Override
    public Result analyze(byte[] data, int dataWidth, int dataHeight, int left, int top, int width, int height) {
        Result rawResult = null;
        if (mReader != null) {
            try {
                PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, dataWidth, dataHeight, left, top, width, height, false);
                rawResult = decodeInternal(source);
            } catch (Exception e) {
            } finally {
                mReader.reset();
            }
        }
        return rawResult;
    }

    private Result decodeInternal(@NonNull LuminanceSource source) {

        try {

            // 优先采用GlobalHistogramBinarizer解析
            Result result = mReader.decode(new BinaryBitmap(new GlobalHistogramBinarizer(source)));

            if (null == result) {
                // 采用HybridBinarizer解析
                return mReader.decode(new BinaryBitmap(new HybridBinarizer(source)));
            } else {
                return result;
            }

        } catch (Exception e) {
            return null;
        }
    }

    public abstract Reader createReader();

}
