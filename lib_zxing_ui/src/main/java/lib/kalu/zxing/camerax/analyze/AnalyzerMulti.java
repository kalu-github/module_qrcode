package lib.kalu.zxing.camerax.analyze;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;

import lib.kalu.zxing.camerax.DecodeConfig;
import lib.kalu.zxing.util.LogUtil;


import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @description:
 * @date: 2021-05-07 14:57
 */
public final class AnalyzerMulti extends AnalyzerCore {

    MultiFormatReader mReader;

    public AnalyzerMulti() {
        this((DecodeConfig) null);
    }

    public AnalyzerMulti(@Nullable Map<DecodeHintType, Object> hints) {
        this(new DecodeConfig().setHints(hints));
    }

    public AnalyzerMulti(@Nullable DecodeConfig config) {
        super(config);
        initReader();
    }

    private void initReader() {
        mReader = new MultiFormatReader();
    }

    @Nullable
    @Override
    public Result analyze(byte[] data, int dataWidth, int dataHeight, int left, int top, int width, int height) {
        Result rawResult = null;
        try {
            long start = System.currentTimeMillis();
            mReader.setHints(mHints);
            PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, dataWidth, dataHeight, left, top, width, height, false);
            rawResult = decodeInternal(source, isMultiDecode);

            if (rawResult == null && mDecodeConfig != null) {
                if (rawResult == null && mDecodeConfig.isSupportVerticalCode()) {
                    byte[] rotatedData = new byte[data.length];
                    for (int y = 0; y < dataHeight; y++) {
                        for (int x = 0; x < dataWidth; x++) {
                            rotatedData[x * dataHeight + dataHeight - y - 1] = data[x + y * dataWidth];
                        }
                    }
                    rawResult = decodeInternal(new PlanarYUVLuminanceSource(rotatedData, dataHeight, dataWidth, top, left, height, width, false), mDecodeConfig.isSupportVerticalCodeMultiDecode());
                }

                if (rawResult == null && mDecodeConfig.isSupportLuminanceInvert()) {
                    rawResult = decodeInternal(source.invert(), mDecodeConfig.isSupportLuminanceInvertMultiDecode());
                }
            }
            if (rawResult != null) {
                long end = System.currentTimeMillis();
                LogUtil.log("Found barcode in " + (end - start) + " ms");
            }
        } catch (Exception e) {

        } finally {
            mReader.reset();
        }
        return rawResult;
    }

    /**
     * GlobalHistogramBinarizer的效率要高于HybridBinarizer，减少了图像二值化的处理。故优先固定为GlobalHistogramBinarizer方法进行解析
     *
     * @param source
     * @param isMultiDecode
     * @return
     */
    private Result decodeInternal(@NonNull LuminanceSource source, @NonNull boolean isMultiDecode) {

        try {

            // 优先采用GlobalHistogramBinarizer解析
            Result result = mReader.decodeWithState(new BinaryBitmap(new GlobalHistogramBinarizer(source)));

            if (null == result) {
                // 采用HybridBinarizer解析
                return mReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(source)));
            } else {
                return result;
            }

        } catch (Exception e) {
            return null;
        }
    }

//    private Result decodeInternal(LuminanceSource source,boolean isMultiDecode){
//        Result result = null;
//        try{
//            try{
//                //采用HybridBinarizer解析
//                result = mReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(source)));
//            }catch (Exception e){
//
//            }
//            if(isMultiDecode && result == null){
//                //如果没有解析成功，再采用GlobalHistogramBinarizer解析一次
//                result = mReader.decodeWithState(new BinaryBitmap(new GlobalHistogramBinarizer(source)));
//            }
//        }catch (Exception e){
//
//        }
//        return result;
//    }
}
