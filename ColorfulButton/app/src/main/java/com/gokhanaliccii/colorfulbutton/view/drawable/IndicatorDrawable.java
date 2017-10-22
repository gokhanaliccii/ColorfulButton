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

    private Paint indicatorPaint;
    private Paint foregroundPaint;
    private float radius;
    private int thickness;

    public IndicatorDrawable(int indicatorColor, int backGroundColor, int thickness, float radius) {
        this.thickness = thickness;
        this.radius = radius;
        indicatorPaint = createPaint(indicatorColor);
        foregroundPaint = createPaint(backGroundColor);
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
        RectF foreGround = new RectF(0 + thickness, 0, width, height);


        canvas.drawRoundRect(backGround, radius, radius, indicatorPaint);
        canvas.drawRoundRect(foreGround, radius, radius, foregroundPaint);
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
