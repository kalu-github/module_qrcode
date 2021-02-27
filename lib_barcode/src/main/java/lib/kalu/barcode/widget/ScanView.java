/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lib.kalu.barcode.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import lib.kalu.barcode.util.LogUtil;

/**
 * description: 条形码扫描框 - 商品条形码的标准尺寸是37.29mmx26.26mm
 * created by kalu on 2021-02-26
 */
public final class ScanView extends TextView {

    // 颜色
    private final int COLOR_BACKGROUND = Color.parseColor("#66000000");
    private final int COLOR_LINE = Color.parseColor("#ffffff");

    // 位移
    private float lineDisplacement = 0f;
    private final PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

    public ScanView(Context context) {
        super(context);
    }

    public ScanView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = 1080 * height / 1920;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        float height = getHeight();
        float width = getWidth();
        float minW = Math.min(height, width) * 0.8f;
//        float minH = minW * 2626 / 3729;
        float minH = minW * 1 / 3;

        float left = width * 0.5f - minW * 0.5f;
        float top = height * 0.5f - minW * 0.5f;
        float right = left + minW;
        float bottom = top + minH;

        // 画笔
        TextPaint paint = getPaint();
        paint.setAntiAlias(true);

        paint.setXfermode(null);
        paint.setColor(COLOR_BACKGROUND);
        canvas.drawRect(0, 0, width, height, paint);

        // 背景
        paint.setXfermode(porterDuffXfermode);
        paint.setColor(Color.TRANSPARENT);
        canvas.drawRect(left, top, right, bottom, paint);

        // 直角
        int rectH = (int) (2 * 4 + 0.5f);
        int rectW = (int) (14 * 4 + 0.5f);

        //左上角
        paint.setXfermode(null);
        paint.setColor(Color.BLACK);
        canvas.drawRect(left, top, left + rectW, top + rectH, paint);
        canvas.drawRect(left, top, left + rectH, top + rectW, paint);
        //右上角
        canvas.drawRect(right - rectW, top, right + 1, top + rectH, paint);
        canvas.drawRect(right - rectH, top, right + 1, top + rectW, paint);
        //左下角
        canvas.drawRect(left, bottom - rectH, left + rectW, bottom + 1, paint);
        canvas.drawRect(left, bottom - rectW, left + rectH, bottom + 1, paint);
        //右下角
        canvas.drawRect(right - rectW, bottom - rectH, right + 1, bottom + 1, paint);
        canvas.drawRect(right - rectH, bottom - rectW, right + 1, bottom + 1, paint);

        // 文字
        paint.setXfermode(null);
        paint.setColor(Color.BLACK);
        super.onDraw(canvas);

        CharSequence text = getText();
        if (null == text || text.length() == 0)
            return;

        // 扫描动画, 局部刷新
        lineDisplacement += 10f;
        float margin = Math.abs(right - left) * 0.1f;
        float lineHeight = margin * 0.05f;
        paint.setColor(COLOR_LINE);
        float rectLeft = left + margin;
        float rectTop = top + margin + lineDisplacement;
        float rectRight = right - margin;
        float rectBottom = rectTop + lineHeight;
        if (rectBottom + margin > bottom) {
            lineDisplacement = 0f;
            rectTop = top + margin;
            rectBottom = rectTop + lineHeight;
        }

        canvas.drawRect(rectLeft, rectTop, rectRight, rectBottom, paint);
        postInvalidateDelayed(10, (int) left, (int) top, (int) right, (int) bottom);
        LogUtil.log("onDraw[二维码扫描框] => lineDisplacement = " + lineDisplacement);
    }
}
