package lib.kalu.zxing.analyze;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.exception.ReaderException;
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

    @Override
    default Result analyzeRect(byte[] data, int dataWidth, int dataHeight, int left, int top, int outWidth, int outHeight) {

        try {

            /**
             * HybridBinarizer 识别效果优于 GlobalHistogramBinarizer
             */

            PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, dataWidth, dataHeight, left, top, outWidth, outHeight);
            Result resultH = createReader().decode(new BinaryBitmap(new HybridBinarizer(source)));
            if (null == resultH) {
                Result resultGH = createReader().decode(new BinaryBitmap(new GlobalHistogramBinarizer(source)));
                LogUtil.log("analyzeRect[succ] => type = GlobalHistogramBinarizer, result = " + resultGH.getText());
                return resultGH;
            } else {
                LogUtil.log("analyzeRect[succ] => type = HybridBinarizer, result = " + resultH.getText());
                return resultH;
            }

        } catch (ReaderException e) {
            LogUtil.log("analyzeRect[exception] => not find data");
            return null;
        } catch (Exception e) {
            LogUtil.log("analyzeRect[exception] => " + e.getMessage(), e);
            return null;
        }
    }
}
