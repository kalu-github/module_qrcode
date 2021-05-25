package lib.kalu.zxing.analyze;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.source.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import lib.kalu.zxing.util.LogUtil;

/**
 * @description:
 * @date: 2021-05-07 14:57
 */
interface AnalyzerRectImpl extends AnalyzerDataImpl {


    /**
     * 解析策略
     * step1. HybridBinarizer解析, 精确度高于GlobalHistogramBinarizer
     * step2. GlobalHistogramBinarizer解析, 性能最快
     *
     * @param data       图片原始帧数据
     * @param dataWidth  原始宽度
     * @param dataHeight 原始高度
     * @param left       裁剪左起始位置
     * @param top        裁剪上起始位置
     * @param outWidth   裁剪宽度
     * @param outHeight  裁剪高度
     * @return
     */
    @Override
    default Result analyzeRect(byte[] data, int dataWidth, int dataHeight, int left, int top, int outWidth, int outHeight) {

        PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, dataWidth, dataHeight, left, top, outWidth, outHeight);
        try {
            Result result = createReader().decode(new BinaryBitmap(new HybridBinarizer(source)));
            LogUtil.log("analyzeRect[succ] => HybridBinarizer");
            return result;
        } catch (Exception e1) {
            LogUtil.log("analyzeRect[fail] => HybridBinarizer");
            try {
                Result result = createReader().decode(new BinaryBitmap(new GlobalHistogramBinarizer(source)));
                LogUtil.log("analyzeRect[succ] => GlobalHistogramBinarizer");
                return result;
            } catch (Exception e2) {
                LogUtil.log("analyzeRect[fail] => GlobalHistogramBinarizer");
                return null;
            }
        }
    }
}