/*
 * Copyright (C) 2020 The Android Open Source Project
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

package com.android.tv.settings.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;

import androidx.annotation.DrawableRes;

/**
 * A customized dynamic drawable span which aligns to the center of the text. This assumes that
 * the text is smaller than the image, which will be the case most of the time.
 * This class is mirrored from Setup.
 */
public class CenterAlignedDynamicDrawableSpan extends DynamicDrawableSpan {

    private final Context mContext;
    private final int mResourceId;

    public CenterAlignedDynamicDrawableSpan(Context context, @DrawableRes int resourceId) {
        super();
        this.mContext = context;
        this.mResourceId = resourceId;
    }

    @Override
    public int getSize(
            Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        super.getSize(paint, text, start, end, fm);

        Rect rect = getDrawable().getBounds();

        if (fm != null) {
            // Pad the bottom of the font metrics with enough space so that the drawable isn't cut
            // off by the bounds of the text.
            fm.bottom += rect.height() / 2;
        }

        return rect.right;
    }

    @Override
    public void draw(
            Canvas canvas,
            CharSequence text,
            int start,
            int end,
            float x,
            int top,
            int y,
            int bottom,
            Paint paint) {
        Drawable drawable = getDrawable();
        canvas.save();

        Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
        int fontHeight = fmPaint.descent - fmPaint.ascent;

        int transY = fmPaint.descent; // Align text descent (bottom most part) to bottom of drawable
        // Translate half of the extra space between the drawable and the font to center.
        transY += (drawable.getIntrinsicHeight() - fontHeight) / 2;

        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
    }

    @Override
    public Drawable getDrawable() {
        Drawable drawable = mContext.getDrawable(mResourceId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        return drawable;
    }
}
