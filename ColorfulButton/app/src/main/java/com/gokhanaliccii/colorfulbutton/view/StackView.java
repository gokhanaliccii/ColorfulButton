package com.gokhanaliccii.colorfulbutton.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by gokhan.alici on 23.10.2017.
 */

public class StackView extends ViewGroup {

    private static final String TAG = "StackView";

    private StackAdapter stackAdapter;
    private ItemDecorator itemDecorator;

    public StackView(Context context) {
        super(context);
    }

    public StackView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        int sumOfHeight = 0;

        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view.getVisibility() != GONE) {
                view.measure(widthMeasureSpec, heightMeasureSpec);
                sumOfHeight += view.getMeasuredHeight();
            }
        }

        int childHeightSpec = MeasureSpec.makeMeasureSpec(sumOfHeight, MeasureSpec.EXACTLY);

        setMeasuredDimension(widthMeasureSpec, childHeightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int bottomEdgeOfView = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view != null) {
                int leftPoint =  getPaddingLeft();
                int topPoint = bottomEdgeOfView + itemDecorator.getDecorationSpace(i);
                int rightPoint = leftPoint + view.getMeasuredWidth();
                int bottomPoint = bottomEdgeOfView + view.getMeasuredHeight();

                view.layout(leftPoint, topPoint, rightPoint, bottomPoint);
            }
        }
    }

    public void setAdapter(StackAdapter adapter) {
        if (adapter == stackAdapter) {
            return;
        }

        this.stackAdapter = adapter;

        checkInternal();
        removeAllViewsInLayout();

        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            View newStackView = adapter.getView(i);
            LayoutParams layoutParams = newStackView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = generateDefaultLayoutParams();
            }

            addViewInLayout(stackAdapter.getView(i), -1, layoutParams);
        }

        requestLayout();
    }

    private void checkInternal() {
        if (itemDecorator == null) {
            itemDecorator = new ItemDecorator.Empty();
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }

    public interface StackAdapter {

        int getCount();

        View getView(int position);
    }

    public interface ItemDecorator {

        int getDecorationSpace(int position);

        class Empty implements ItemDecorator {

            @Override
            public int getDecorationSpace(int position) {
                return 0;
            }
        }

    }
}
