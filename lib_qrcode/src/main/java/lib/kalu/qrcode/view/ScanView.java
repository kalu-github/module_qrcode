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

package lib.kalu.qrcode.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import lib.kalu.qrcode.util.LogUtil;

/**
 * description: 二维码扫描框
 * created by kalu on 2021-02-26
 */
public final class ScanView extends TextView {

    // 位移
    private float lineDisplacement = 0f;
    private int lineMargin = 10;
    private int lineHeight = 4;

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
        super.onDraw(canvas);

        float height = getHeight();
        float width = getWidth();
        float min = Math.min(height, width) * 0.56f;

        float left = width * 0.5f - min * 0.5f;
        float top = height * 0.5f - min * 0.5f;
        float right = left + min;
        float bottom = top + min;

        // 画笔
        TextPaint paint = getPaint();
        paint.setAntiAlias(true);

        // 背景
        paint.setColor(Color.parseColor("#aa000000"));
        canvas.drawRect(0, 0, width, top, paint);
        canvas.drawRect(0, bottom, width, height, paint);
        canvas.drawRect(0, top, left, bottom, paint);
        canvas.drawRect(right, top, width, bottom, paint);

        // 直角
        int rectH = (int) (2 * 4 + 0.5f);
        int rectW = (int) (14 * 4 + 0.5f);

        //左上角
        paint.setColor(Color.parseColor("#000000"));
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

        CharSequence text = getText();
        if (null == text || text.length() == 0)
            return;

        // 扫描动画, 局部刷新
        lineDisplacement += 18;
        if (lineDisplacement + 18 <= top) {
            lineDisplacement = top;
        } else if (lineDisplacement + 18 >= bottom) {
            lineDisplacement = top;
        }
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawRect(left + lineMargin, lineDisplacement, right - lineMargin, lineDisplacement + lineHeight, paint);
        postInvalidateDelayed(10, (int) left, (int) top, (int) right, (int) bottom);
        LogUtil.log("onDraw[二维码扫描框] => lineDisplacement = " + lineDisplacement);
    }
}
