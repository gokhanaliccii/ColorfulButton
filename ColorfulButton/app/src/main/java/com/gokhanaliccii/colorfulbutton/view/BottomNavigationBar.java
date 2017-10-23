package com.gokhanaliccii.colorfulbutton.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.gokhanaliccii.colorfulbutton.R;

/**
 * Created by gokhan.alici on 23.10.2017.
 */

public class BottomNavigationBar extends LinearLayout implements View.OnClickListener {

    private int defaultColor;
    private int selectedColor;
    private int hoverColor;

    private View selectedView;
    private OnSelectionListener selectionListener;

    public BottomNavigationBar(Context context) {
        super(context);
    }

    public BottomNavigationBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        parseAttributes(attributeSet);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        setViewTags();
        super.onLayout(changed, l, t, r, b);
    }

    private void parseAttributes(AttributeSet attrs) {
        Context context = getContext();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BottomNavigationBar);
        defaultColor = typedArray.getColor(R.styleable.BottomNavigationBar_defaultColor, -1);
        selectedColor = typedArray.getColor(R.styleable.BottomNavigationBar_selectedColor, -1);
        hoverColor = typedArray.getColor(R.styleable.BottomNavigationBar_hoverColor, -1);
        typedArray.recycle();
    }

    private void setViewTags() {
        int childCount = getChildCount();
        if (childCount > 0) {

            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                child.setClickable(true);
                child.setBackground(createStateListDrawable());
                child.setTag(R.id.bottom_navigation_item_key, i);
                child.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (selectedView != null) {
            selectedView.setSelected(false);
        }

        selectedView = v;
        selectedView.setSelected(true);

        Integer index = getItemKey(v);
        if (selectionListener != null && index != null) {
            selectionListener.onItemSelected(index);
        }
    }

    private Integer getItemKey(View view) {
        if (view != null) {
            Object tag = view.getTag(R.id.bottom_navigation_item_key);
            if (tag != null) {
                return ((Integer) tag);
            }
        }

        return null;
    }

    private StateListDrawable createStateListDrawable() {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(hoverColor));
        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(selectedColor));
        stateListDrawable.addState(new int[]{}, new ColorDrawable(defaultColor));

        return stateListDrawable;
    }

    public interface OnSelectionListener {

        void onItemSelected(int index);
    }
}
