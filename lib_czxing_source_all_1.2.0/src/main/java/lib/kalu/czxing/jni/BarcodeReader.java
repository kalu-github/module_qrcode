package lib.kalu.czxing.jni;

import android.graphics.Bitmap;

import androidx.annotation.Keep;

@Keep
public class BarcodeReader {

    private BarcodeReader() {
    }

    public static final class Holder {
        private static BarcodeReader instance = new BarcodeReader();
    }

    public static BarcodeReader getInstance() {
        return Holder.instance;
    }

    public BarcodeResult read(Bitmap bitmap) {

        if (null == bitmap)
            return null;

        // 避免某些情况无法获取图片格式的问题
        Bitmap copy = bitmap.copy(Bitmap.Config.ARGB_8888, false);
        if (null != bitmap) {
            bitmap.recycle();
        }
        int width = copy.getWidth();
        int height = copy.getHeight();

        Object[] result = new Object[2];
        int code = Native.getInstance().readBitmap(copy, 0, 0, width, height, result);
        LogUtil.log("BarcodeReader > read => code = " + code);

        if (null != copy) {
            copy.recycle();
        }

        if (code >= 0) {
            BarcodeFormat type = BarcodeFormat.valueOf(code);
            String content = result[0].toString();
            LogUtil.log("BarcodeReader > read => type = " + type + ", content = " + content);
            BarcodeResult decodeResult = new BarcodeResult(type, content);
            if (result[1] != null) {
                decodeResult.setPoint((float[]) result[1]);
            }
            return decodeResult;
        } else {
            return null;
        }
    }

    public BarcodeResult read(byte[] data, int cropLeft, int cropTop, int cropWidth, int cropHeight, int rowWidth, int rowHeight) {
        try {
            Object[] result = new Object[2];
            int code = Native.getInstance().readBytes(data, cropLeft, cropTop, cropWidth, cropHeight, rowWidth, rowHeight, result);
            LogUtil.log("BarcodeReader > read => code = " + code);

            if (code >= 0) {
                BarcodeFormat type = BarcodeFormat.valueOf(code);
                String content = result[0].toString();
                LogUtil.log("BarcodeReader > read => type = " + type + ", content = " + content);
                BarcodeResult decodeResult = new BarcodeResult(type, content);
                if (result[1] != null) {
                    decodeResult.setPoint((float[]) result[1]);
                }
                return decodeResult;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
