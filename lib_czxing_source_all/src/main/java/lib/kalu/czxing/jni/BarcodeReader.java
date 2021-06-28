package lib.kalu.czxing.jni;

import android.graphics.Bitmap;
import android.util.Log;

public class BarcodeReader {

    private long _nativePtr;

    private BarcodeReader() {
        BarcodeFormat[] formats = new BarcodeFormat[]{BarcodeFormat.QR_CODE,
                BarcodeFormat.CODABAR,
                BarcodeFormat.CODE_128,
                BarcodeFormat.EAN_13,
                BarcodeFormat.UPC_A};
        _nativePtr = NativeSdk.getInstance().createInstance(getNativeFormats(formats));
    }

    public static final class Holder {
        private static BarcodeReader instance = new BarcodeReader();
    }

    public static BarcodeReader getInstance() {
        return Holder.instance;
    }

    public void setBarcodeFormat(BarcodeFormat... formats) {
        NativeSdk.getInstance().setFormat(_nativePtr, getNativeFormats(formats));
    }

    private int[] getNativeFormats(BarcodeFormat... formats) {
        int[] nativeFormats = new int[formats.length];
        for (int i = 0; i < formats.length; ++i) {
            nativeFormats[i] = formats[i].ordinal();
        }
        return nativeFormats;
    }

    public BarcodeResult read(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        // 避免某些情况无法获取图片格式的问题
        Bitmap newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false);
        int imgWidth = newBitmap.getWidth();
        int imgHeight = newBitmap.getHeight();

        Object[] result = new Object[2];
        int resultFormat = NativeSdk.getInstance().readBitmap(_nativePtr, newBitmap, 0, 0, imgWidth, imgHeight, result);
        bitmap.recycle();
        newBitmap.recycle();
        if (resultFormat >= 0) {
            BarcodeResult decodeResult = new BarcodeResult(BarcodeFormat.values()[resultFormat], (String) result[0]);
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
            int resultFormat = NativeSdk.getInstance().readByte(_nativePtr, data, cropLeft, cropTop, cropWidth, cropHeight, rowWidth, rowHeight, result);
            Log.e("czxing", "resultFormat = " + resultFormat);
            if (resultFormat >= 0) {
                Log.e("czxing", "result = " + result[0].toString());
                BarcodeResult decodeResult = new BarcodeResult(BarcodeFormat.values()[resultFormat], (String) result[0]);
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

    public void enableCVDetect(boolean enable) {
        if (enable) {
            NativeSdk.getInstance().openCVDetectValue(_nativePtr, 10);
        } else {
            NativeSdk.getInstance().openCVDetectValue(_nativePtr, 0);
        }
    }

    public void prepareRead() {
        NativeSdk.getInstance().prepareRead(_nativePtr);
    }

    public void stopRead() {
        NativeSdk.getInstance().stopRead(_nativePtr);
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            if (_nativePtr != 0) {
                NativeSdk.getInstance().destroyInstance(_nativePtr);
                _nativePtr = 0;
            }
        } finally {
            super.finalize();
        }
    }

    public void setReadCodeListener(ReadCodeListener readCodeListener) {
        NativeSdk.getInstance().setReadCodeListener(readCodeListener);
    }

    public interface ReadCodeListener {
        void onReadCodeResult(BarcodeResult result);

        void onFocus();

        void onAnalysisBrightness(boolean isDark);
    }
}
