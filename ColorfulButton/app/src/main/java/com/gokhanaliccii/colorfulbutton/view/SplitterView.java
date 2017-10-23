package com.gokhanaliccii.colorfulbutton.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by gokhan.alici on 23.10.2017.
 */

public class SplitterView extends ViewGroup{

    private static final String TAG = "SplitterView";

    public SplitterView(Context context) {
        super(context);
    }

    public SplitterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }



}
