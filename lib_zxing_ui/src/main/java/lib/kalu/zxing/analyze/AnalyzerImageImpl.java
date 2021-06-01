package lib.kalu.zxing.analyze;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.Result;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.ImageProxy;

import org.jetbrains.annotations.NotNull;

import lib.kalu.zxing.util.LogUtil;

/**
 * @description: 图像分析器
 * @date: 2021-05-07 14:57
 */
interface AnalyzerImageImpl extends AnalyzerBaseImpl {

//    Bitmap[] bi = new Bitmap[1];

    @Nullable
    @Override
    default Result analyzeImage(@NonNull Context context, @Nullable ImageView imageView, @NonNull ImageProxy imageProxy, int orientation) {
        LogUtil.log("analyzeImage => format =  " + imageProxy.getFormat() + ", orientation = " + (orientation == Configuration.ORIENTATION_PORTRAIT ? "竖屏" : "横屏"));

        if (imageProxy.getFormat() != ImageFormat.YUV_420_888)
            return null;

        ByteBuffer buffer = imageProxy.getPlanes()[0].getBuffer();
        byte[] original = new byte[buffer.remaining()];
        buffer.get(original);
        int originalWidth = imageProxy.getWidth();
        int originalHeight = imageProxy.getHeight();

        // ratio() > 1F ? 全屏扫描 : 区域扫描
        int cropWidth = ratio() >= 1F ? originalWidth : (int) (Math.min(originalWidth, originalHeight) * ratio());
        int cropHeight = ratio() >= 1F ? originalHeight : cropWidth;
        int cropLeft = ratio() >= 1F ? 0 : originalWidth / 2 - cropWidth / 2;
        int cropTop = ratio() >= 1F ? 0 : originalHeight / 2 - cropHeight / 2;
        byte[] crop = crop(original, originalWidth, originalHeight, cropWidth, cropHeight, cropLeft, cropTop, orientation, true, true);

//        if (null != imageView && imageView.getVisibility() == View.VISIBLE) {
//
//            if (null != bi[0]) {
//                bi[0].recycle();
//                bi[0] = null;
//            }
//
//            try {
//                YuvImage yuvimage = new YuvImage(crop, ImageFormat.NV21, cropWidth, cropHeight, null);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                yuvimage.compressToJpeg(new Rect(0, 0, cropWidth, cropHeight), 80, baos);
//                byte[] jdata = baos.toByteArray();
//                BitmapFactory.Options bitmapFatoryOptions = new BitmapFactory.Options();
//                bitmapFatoryOptions.inPreferredConfig = Bitmap.Config.RGB_565;
//                Bitmap bmp = BitmapFactory.decodeByteArray(jdata, 0, jdata.length, bitmapFatoryOptions);
//                bi[0] = bmp;
//                imageView.setImageBitmap(bi[0]);
//            } catch (Exception e) {
//            }
//        }

        return analyzeData(context, crop, cropWidth, cropHeight, cropLeft, cropTop, original, originalWidth, originalHeight);
    }
}
