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
public final class FinderView extends View {

    private final int TYPE_QRCODE = 0;
    private final int TYPE_BARCODE = 1;
    private final int COLOR_66000000 = Color.parseColor("#66000000");
    private final TextPaint CANVAS_PAINT = new TextPaint();

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
        CANVAS_PAINT.setAntiAlias(true);
        CANVAS_PAINT.setStrokeWidth(0);
        CANVAS_PAINT.setStyle(Paint.Style.FILL);
        CANVAS_PAINT.setPathEffect(null);

        // 直角
        int rectH = (int) mAngleWidth;
        int rectW = (int) mAngleHeight;
//        int rectH = (int) (2 * 4 + 0.5f);
//        int rectW = (int) (14 * 4 + 0.5f);

        // 阴影1
        CANVAS_PAINT.setColor(mShadowColor);
        canvas.drawRect(0, 0, width, top, CANVAS_PAINT);
        // canvas.drawRect(0, 0, width, top + rectH, paint);

        // 阴影2
        CANVAS_PAINT.setColor(mShadowColor);
        canvas.drawRect(0, bottom, width, height, CANVAS_PAINT);
        // canvas.drawRect(0, bottom, width, height, paint);

        // 阴影3
        CANVAS_PAINT.setColor(mShadowColor);
        canvas.drawRect(0, top, left, bottom, CANVAS_PAINT);
        // canvas.drawRect(0, top + rectH, left + rectH, bottom - rectH, paint);

        // 阴影4
        CANVAS_PAINT.setColor(mShadowColor);
        canvas.drawRect(right, top, width, bottom, CANVAS_PAINT);
        // canvas.drawRect(right - rectH, top + rectH, width, bottom - rectH, paint);

        //左上角
        CANVAS_PAINT.setColor(mAngleColor);
        canvas.drawRect(left, top, left + rectW, top + rectH, CANVAS_PAINT);
        canvas.drawRect(left, top, left + rectH, top + rectW, CANVAS_PAINT);
        //右上角
        CANVAS_PAINT.setColor(mAngleColor);
        canvas.drawRect(right - rectW, top, right + 1, top + rectH, CANVAS_PAINT);
        canvas.drawRect(right - rectH, top, right + 1, top + rectW, CANVAS_PAINT);
        //左下角
        CANVAS_PAINT.setColor(mAngleColor);
        canvas.drawRect(left, bottom - rectH, left + rectW, bottom + 1, CANVAS_PAINT);
        canvas.drawRect(left, bottom - rectW, left + rectH, bottom + 1, CANVAS_PAINT);
        //右下角
        CANVAS_PAINT.setColor(mAngleColor);
        canvas.drawRect(right - rectW, bottom - rectH, right + 1, bottom + 1, CANVAS_PAINT);
        canvas.drawRect(right - rectH, bottom - rectW, right + 1, bottom + 1, CANVAS_PAINT);

        // 文字
        if (null != mTextTip && mTextTip.length() > 0) {
            CANVAS_PAINT.setStrokeWidth(0);
            CANVAS_PAINT.setColor(mTextColor);
            CANVAS_PAINT.setTextSize(mTextSize);
            CANVAS_PAINT.setStyle(Paint.Style.FILL);
            CANVAS_PAINT.setPathEffect(null);
            CANVAS_PAINT.setTextAlign(Paint.Align.CENTER);
            StaticLayout layout = new StaticLayout(mTextTip, CANVAS_PAINT, (int) width, Layout.Alignment.ALIGN_NORMAL, 1.4F, 0.0F, true);
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
            CharSequence hint = (null == getTag(R.id.moudle_zxing_id_finderview_tag) ? null : getTag(R.id.moudle_zxing_id_finderview_tag).toString());
            float parseFloat = Float.parseFloat(hint.toString());
            displacement = Math.abs(parseFloat);

            if (parseFloat < 0) {
                displacement -= (mType == TYPE_BARCODE ? 2f : 5f);
            } else {
                displacement += (mType == TYPE_BARCODE ? 2f : 5f);
            }
            setTag(R.id.moudle_zxing_id_finderview_tag, String.valueOf(parseFloat < 0 ? -displacement : displacement));

        } catch (Exception e) {
            displacement = (mType == TYPE_BARCODE ? 2f : 5f);
            setTag(R.id.moudle_zxing_id_finderview_tag, String.valueOf(displacement));
        }

        float margin = Math.abs(right - left) * 0.1f;
        float lineHeight = margin * 0.05f;
        float rectLeft = left + margin;
        float rectTop = top + margin + displacement;
        float rectRight = right - margin;
        float rectBottom = rectTop + lineHeight;

        // 下移
        if (null == getTag(R.id.moudle_zxing_id_finderview_tag) || !getTag(R.id.moudle_zxing_id_finderview_tag).toString().startsWith("-")) {
            displacement += (mType == TYPE_BARCODE ? 2f : 5f);
        }
        // 上移
        else {
            displacement -= (mType == TYPE_BARCODE ? 2f : 5f);
        }

        // 底部
        if (rectBottom + margin > bottom) {
            setTag(R.id.moudle_zxing_id_finderview_tag, String.valueOf(-displacement));
        }
        // 顶部
        else if (rectTop - margin < top) {
            setTag(R.id.moudle_zxing_id_finderview_tag, String.valueOf(displacement));
        }

        CANVAS_PAINT.setStrokeWidth(0);
        CANVAS_PAINT.setColor(mLineColor);
        CANVAS_PAINT.setStyle(Paint.Style.STROKE);
        CANVAS_PAINT.setAntiAlias(true);
        CANVAS_PAINT.setPathEffect(new DashPathEffect(new float[]{4, 4}, 0));
        canvas.drawLine(rectLeft, rectTop, rectRight, rectBottom, CANVAS_PAINT);
//        canvas.drawRect(rectLeft, rectTop, rectRight, rectBottom, paint);
        postInvalidateDelayed(20, (int) left, (int) top, (int) right, (int) bottom);
        // LogUtil.log("onDraw[二维码扫描框] => lineDisplacement = " + getHint());
    }

    /***************/

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
}