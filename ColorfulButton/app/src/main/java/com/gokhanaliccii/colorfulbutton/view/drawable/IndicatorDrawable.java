package com.gokhanaliccii.colorfulbutton.view.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by gokhan on 22/10/17.
 */

public class IndicatorDrawable extends Drawable {

    private Paint mIndicatorPaint;
    private Paint mForegroundPaint;

    private float mRadius;
    private int mThickness;

    public IndicatorDrawable(int indicatorColor, int backGroundColor, int thickness, float radius) {
        mRadius = radius;
        mThickness = thickness;

        mIndicatorPaint = createPaint(indicatorColor);
        mForegroundPaint = createPaint(backGroundColor);
    }

    private Paint createPaint(int color) {
        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(color);
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(color);

        return backgroundPaint;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        RectF backGround = new RectF(0, 0, width, height);
        RectF foreGround = new RectF(0 + mThickness, 0, width, height);


        canvas.drawRoundRect(backGround, mRadius, mRadius, mIndicatorPaint);
        canvas.drawRoundRect(foreGround, mRadius, mRadius, mForegroundPaint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
