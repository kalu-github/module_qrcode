package lib.kalu.zxing.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import lib.kalu.zxing.R;
import lib.kalu.zxing.util.LogUtil;

/**
 * description: 二维码扫描框
 * created by kalu on 2021-02-26
 */
public final class FinderView extends AppCompatTextView {

    private final int TYPE_QRCODE = 0;
    private final int TYPE_BARCODE = 1;
    private final int COLOR_66000000 = Color.parseColor("#66000000");

    @IntRange(from = 0, to = 1)
    private int mType = TYPE_QRCODE;
    @Nullable
    private String mTextTip = null;
    @ColorInt
    private int mTextColor = Color.BLACK;
    @FloatRange(from = 10F, to = 40F)
    private float mTextSize = 10F;
    @ColorInt
    private int mShadowColor = COLOR_66000000;
    @ColorInt
    private int mLineColor = Color.BLACK;
    @ColorInt
    private int mAngleColor = Color.BLACK;
    @FloatRange(from = 1, to = 40)
    private float mAngleWidth = 4;
    @FloatRange(from = 1, to = 400)
    private float mAngleHeight = 40;

    public FinderView(Context context) {
        super(context);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    public FinderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);

        try {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FinderView);
            mType = typedArray.getInt(R.styleable.FinderView_fv_type, TYPE_QRCODE);
            mLineColor = typedArray.getColor(R.styleable.FinderView_fv_line_color, Color.BLACK);
            mAngleColor = typedArray.getColor(R.styleable.FinderView_fv_angle_color, Color.BLACK);
            mShadowColor = typedArray.getColor(R.styleable.FinderView_fv_shadow_color, COLOR_66000000);
            mTextTip = typedArray.getString(R.styleable.FinderView_fv_text_tip);
            mTextColor = typedArray.getColor(R.styleable.FinderView_fv_text_color, Color.BLACK);
            mTextSize = typedArray.getDimension(R.styleable.FinderView_fv_text_size, 10);
            mAngleWidth = typedArray.getDimension(R.styleable.FinderView_fv_angle_width, 4);
            mAngleHeight = typedArray.getDimension(R.styleable.FinderView_fv_angle_height, 40);
        } catch (Exception e) {
        }
    }

    public FinderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        try {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FinderView);
            mType = typedArray.getInt(R.styleable.FinderView_fv_type, TYPE_QRCODE);
            mLineColor = typedArray.getColor(R.styleable.FinderView_fv_line_color, Color.BLACK);
            mAngleColor = typedArray.getColor(R.styleable.FinderView_fv_angle_color, Color.BLACK);
            mShadowColor = typedArray.getColor(R.styleable.FinderView_fv_shadow_color, COLOR_66000000);
            mTextTip = typedArray.getString(R.styleable.FinderView_fv_text_tip);
            mTextColor = typedArray.getColor(R.styleable.FinderView_fv_text_color, Color.BLACK);
            mTextSize = typedArray.getDimension(R.styleable.FinderView_fv_text_size, 10);
            mAngleWidth = typedArray.getDimension(R.styleable.FinderView_fv_angle_width, 4);
            mAngleHeight = typedArray.getDimension(R.styleable.FinderView_fv_angle_height, 40);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = 1080 * height / 1920;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);

        float height = getHeight();
        float width = getWidth();


        float left, top, right, bottom;

        // 二维码
        if (mType == TYPE_QRCODE) {
            float min = Math.min(height, width) * 0.6f;
            left = width * 0.5f - min * 0.5f;
            top = height * 0.5f - min * 0.5f;
            right = left + min;
            bottom = top + min;
        }
        // 条形码
        else {

            float minW = Math.min(height, width) * 0.8f;
//        float minH = minW * 2626 / 3729;
            float minH = minW * 1 / 3;

            left = width * 0.5f - minW * 0.5f;
            top = height * 0.5f - minH * 0.5f;
            right = left + minW;
            bottom = top + minH;
        }

        // 画笔
        TextPaint paint = getPaint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.FILL);
        paint.setPathEffect(null);

        // 直角
        int rectH = (int) mAngleWidth;
        int rectW = (int) mAngleHeight;
//        int rectH = (int) (2 * 4 + 0.5f);
//        int rectW = (int) (14 * 4 + 0.5f);

        // 阴影1
        paint.setColor(mShadowColor);
        canvas.drawRect(0, 0, width, top, paint);
        // canvas.drawRect(0, 0, width, top + rectH, paint);

        // 阴影2
        paint.setColor(mShadowColor);
        canvas.drawRect(0, bottom, width, height, paint);
        // canvas.drawRect(0, bottom, width, height, paint);

        // 阴影3
        paint.setColor(mShadowColor);
        canvas.drawRect(0, top, left, bottom, paint);
        // canvas.drawRect(0, top + rectH, left + rectH, bottom - rectH, paint);

        // 阴影4
        paint.setColor(mShadowColor);
        canvas.drawRect(right, top, width, bottom, paint);
        // canvas.drawRect(right - rectH, top + rectH, width, bottom - rectH, paint);

        //左上角
        paint.setColor(mAngleColor);
        canvas.drawRect(left, top, left + rectW, top + rectH, paint);
        canvas.drawRect(left, top, left + rectH, top + rectW, paint);
        //右上角
        paint.setColor(mAngleColor);
        canvas.drawRect(right - rectW, top, right + 1, top + rectH, paint);
        canvas.drawRect(right - rectH, top, right + 1, top + rectW, paint);
        //左下角
        paint.setColor(mAngleColor);
        canvas.drawRect(left, bottom - rectH, left + rectW, bottom + 1, paint);
        canvas.drawRect(left, bottom - rectW, left + rectH, bottom + 1, paint);
        //右下角
        paint.setColor(mAngleColor);
        canvas.drawRect(right - rectW, bottom - rectH, right + 1, bottom + 1, paint);
        canvas.drawRect(right - rectH, bottom - rectW, right + 1, bottom + 1, paint);

        // 文字
        if (null != mTextTip && mTextTip.length() > 0) {
            paint.setStrokeWidth(0);
            paint.setColor(mTextColor);
            paint.setTextSize(mTextSize);
            paint.setStyle(Paint.Style.FILL);
            paint.setPathEffect(null);
            paint.setTextAlign(Paint.Align.CENTER);
            StaticLayout layout = new StaticLayout(mTextTip, paint, (int) width, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
            canvas.save();
            canvas.translate(width / 2, height / 2 - layout.getHeight() / 2);
            layout.draw(canvas);
            canvas.restore();
            // canvas.drawText(mTextTip, width / 2, height / 2, paint);
            // super.onDraw(canvas);
        }

        if (null == mTextTip || mTextTip.length() == 0)
            return;

        // 扫描动画, 局部刷新
        float displacement;
        try {
            CharSequence hint = getHint();
            float parseFloat = Float.parseFloat(hint.toString());
            displacement = Math.abs(parseFloat);

            if (parseFloat < 0) {
                displacement -= (mType == TYPE_BARCODE ? 2f : 5f);
            } else {
                displacement += (mType == TYPE_BARCODE ? 2f : 5f);
            }
            setHint(String.valueOf(parseFloat < 0 ? -displacement : displacement));

        } catch (Exception e) {
            displacement = (mType == TYPE_BARCODE ? 2f : 5f);
            setHint(String.valueOf(displacement));
        }

        float margin = Math.abs(right - left) * 0.1f;
        float lineHeight = margin * 0.05f;
        float rectLeft = left + margin;
        float rectTop = top + margin + displacement;
        float rectRight = right - margin;
        float rectBottom = rectTop + lineHeight;

        // 下移
        if (null == getHint() || !getHint().toString().startsWith("-")) {
            displacement += (mType == TYPE_BARCODE ? 2f : 5f);
        }
        // 上移
        else {
            displacement -= (mType == TYPE_BARCODE ? 2f : 5f);
        }

        // 底部
        if (rectBottom + margin > bottom) {
            setHint(String.valueOf(-displacement));
        }
        // 顶部
        else if (rectTop - margin < top) {
            setHint(String.valueOf(displacement));
        }

        paint.setStrokeWidth(0);
        paint.setColor(mLineColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setPathEffect(new DashPathEffect(new float[]{4, 4}, 0));
        canvas.drawLine(rectLeft, rectTop, rectRight, rectBottom, paint);
//        canvas.drawRect(rectLeft, rectTop, rectRight, rectBottom, paint);
        postInvalidateDelayed(20, (int) left, (int) top, (int) right, (int) bottom);
        // LogUtil.log("onDraw[二维码扫描框] => lineDisplacement = " + getHint());
    }

    /***************/

//    @Override
//    public void setText(CharSequence text, BufferType type) {
//    }
//
//    @Override
//    public void setTextAppearance(int resId) {
//    }
//
//    @Override
//    public void setTextSize(float size) {
//    }
//
//    @Override
//    public void setTextColor(int color) {
//    }
//
//    @Override
//    public void setTextColor(ColorStateList colors) {
//    }
    @Override
    public void setTextCursorDrawable(int textCursorDrawable) {
    }

    @Override
    public void setBackgroundColor(int color) {
    }

    @Override
    public int getDrawingCacheBackgroundColor() {
        return Color.TRANSPARENT;
    }

    @Override
    public void setDrawingCacheBackgroundColor(int color) {
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
    }

    @Override
    public void setBackgroundResource(int resId) {
    }

    @Override
    public void setBackground(Drawable background) {
    }

    @Override
    public void setBackgroundTintBlendMode(@Nullable BlendMode blendMode) {
    }

    @Override
    public void setBackgroundTintList(@Nullable ColorStateList tint) {
    }

    @Override
    public void setBackgroundTintMode(@Nullable PorterDuff.Mode tintMode) {
    }

    @Override
    public void setSupportBackgroundTintList(@Nullable @org.jetbrains.annotations.Nullable ColorStateList tint) {
    }

    @Override
    public void setSupportBackgroundTintMode(@Nullable @org.jetbrains.annotations.Nullable PorterDuff.Mode tintMode) {
    }
}