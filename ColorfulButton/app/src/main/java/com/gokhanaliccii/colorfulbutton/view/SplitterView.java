package com.gokhanaliccii.colorfulbutton.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.gokhanaliccii.colorfulbutton.R;

import static android.graphics.Paint.Style.FILL;

/**
 * Created by gokhan.alici on 23.10.2017.
 */

public class SplitterView extends ViewGroup {

    private static final String TAG = "SplitterView";

    private int splitterColor;
    private float splitterWidth;
    private float splitterHeight;

    private Paint splitterPaint;

    public SplitterView(Context context) {
        super(context);
    }

    public SplitterView(Context context, AttributeSet attrs) {
        super(context, attrs);

        parseAttributes(attrs);
        initPaint();
    }

    private void parseAttributes(AttributeSet attrs) {
        Context context = getContext();

        int defColor = ContextCompat.getColor(context, R.color.splitter_view_split_color);
        int defWidth = context.getResources().getDimensionPixelSize(R.dimen.splitter_view_splitter_width);
        int defHeight = context.getResources().getDimensionPixelSize(R.dimen.splitter_view_splitter_height);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SplitterView);
        splitterWidth = typedArray.getDimension(R.styleable.SplitterView_splitterWidth, defWidth);
        splitterHeight = typedArray.getDimensionPixelSize(R.styleable.SplitterView_splitterHeight, defHeight);
        splitterColor = typedArray.getColor(R.styleable.SplitterView_splitterColor, defColor);
        typedArray.recycle();
    }

    private void initPaint() {
        splitterPaint = new Paint();
        splitterPaint.setColor(splitterColor);
        splitterPaint.setStyle(FILL);
        splitterPaint.setColor(splitterColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getChildCount() > 0) {

            int childCount = getChildCount();
            float totalSplitterSpace = (childCount - 1) * splitterWidth;
            float widthOfPerChild = (MeasureSpec.getSize(widthMeasureSpec) - totalSplitterSpace) / childCount;
            int childWidthSpec = MeasureSpec.makeMeasureSpec((int) widthOfPerChild, MeasureSpec.EXACTLY);

            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                if (view.getVisibility() != GONE) {
                    view.measure(childWidthSpec, view.getMeasuredHeightAndState());
                }
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int lastPointOfLeftEdge = 0;
        int height = b - t - getPaddingBottom() - getPaddingTop();

        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                if (view.getVisibility() != GONE) {
                                        int childHeight = view.getMeasuredHeight();
                    int startPointOfChild = (height - childHeight) / 2;
                    int rightPointOfChild = lastPointOfLeftEdge + view.getMeasuredWidth();
                    int bottomPointOfChild = startPointOfChild + view.getMeasuredHeight();

                    view.layout(lastPointOfLeftEdge, startPointOfChild, rightPointOfChild, bottomPointOfChild);
                    lastPointOfLeftEdge += view.getMeasuredWidth() + splitterWidth;
                }
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (getChildCount() == 0)
            return;

        int leftEdge = 0;
        int childCount = getChildCount();
        int center = (int) ((canvas.getHeight() - splitterHeight) / 2);

        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);

            leftEdge += childAt.getMeasuredWidth();
            canvas.drawRect(leftEdge, center, leftEdge + splitterWidth, center + splitterHeight, splitterPaint);
            leftEdge += splitterWidth;
        }
    }
}
