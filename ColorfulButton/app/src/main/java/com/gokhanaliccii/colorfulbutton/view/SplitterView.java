package com.gokhanaliccii.colorfulbutton.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import static android.graphics.Paint.Style.FILL;

/**
 * Created by gokhan.alici on 23.10.2017.
 */

public class SplitterView extends ViewGroup {

    private static final String TAG = "SplitterView";

    public SplitterView(Context context) {
        super(context);
    }

    public SplitterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() > 0) {

            int splitterWidth = 20;

            int childCount = getChildCount();
            int widthOfPerChild = (MeasureSpec.getSize(widthMeasureSpec) - splitterWidth) / childCount;
            int childWidthSpec = MeasureSpec.makeMeasureSpec(widthOfPerChild, MeasureSpec.EXACTLY);

            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                if (view.getVisibility() != GONE) {
                    view.measure(childWidthSpec, view.getMeasuredHeightAndState());
                }
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = r - l - getPaddingLeft() - getPaddingRight();
        int childCount = getChildCount();
        int lastPointOfLeftEdge = 0;

        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                if (view.getVisibility() != GONE) {
                    view.layout(lastPointOfLeftEdge, 0, lastPointOfLeftEdge + view.getMeasuredWidth(), view.getMeasuredHeight());
                    lastPointOfLeftEdge += view.getMeasuredWidth() + 20;
                }
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (getChildCount() == 0)
            return;

        Paint splitterPaint = new Paint();
        splitterPaint.setColor(Color.BLACK);
        splitterPaint.setStyle(FILL);

        int leftEdge = 0;
        int splitterWidth = 10;
        int childCount = getChildCount();


        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);

            leftEdge = childAt.getMeasuredWidth();

            canvas.drawRect(leftEdge, 0, leftEdge + splitterWidth, 50, splitterPaint);
            leftEdge += splitterWidth;
        }

    }
}
