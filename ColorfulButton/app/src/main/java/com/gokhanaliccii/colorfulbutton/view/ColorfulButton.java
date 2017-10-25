package com.gokhanaliccii.colorfulbutton.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gokhanaliccii.colorfulbutton.R;
import com.gokhanaliccii.colorfulbutton.view.drawable.IndicatorDrawable;

/**
 * Created by gokhan.alici on 19.10.2017.
 */

public class ColorfulButton extends ViewGroup {

    private static final String TAG = "ColorfulButton";

    private static final int NONE = -1;
    private static final int TWO_SIDE = 2;

    public static final int[] STATE_DEFAULT = {};
    public static final int[] STATE_PRESSED = {android.R.attr.state_pressed};

    private ImageView mIcon;
    private TextView mTitle;
    private Attribute attribute;

    public ColorfulButton(Context context) {
        this(context, null);
    }

    public ColorfulButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorfulButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        addChildViews();
        readAttribute(attributeSet);
        applyAttributes(attribute);
        createStateDrawable();
    }

    private void createStateDrawable() {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(STATE_PRESSED, createIndicatorDrawable(attribute.pressedColor()));
        stateListDrawable.addState(STATE_DEFAULT, createIndicatorDrawable(attribute.foregroundColor()));
        setBackground(stateListDrawable);
    }

    private IndicatorDrawable createIndicatorDrawable(int foregroundColor) {
        return new IndicatorDrawable(attribute.indicatorColor(),foregroundColor , attribute.indicatorWidth(), attribute.indicatorRadius());
    }

    private void addChildViews() {
        Context context = getContext();

        mTitle = new TextView(context);
        mIcon = new ImageView(context);

        addView(mTitle);
        addView(mIcon);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childWidth = 0;
        int childHeight = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.getVisibility() != GONE) {
                measureChild(view, widthMeasureSpec, heightMeasureSpec);
                childWidth += view.getMeasuredWidth();
                childHeight = Math.max(childHeight, view.getMeasuredHeight());
            }
        }

        childWidth += (attribute.horizontalPadding() * TWO_SIDE);
        childHeight += (attribute.verticalPadding() * TWO_SIDE);

        //limit sum of child width
        if (childWidth > MeasureSpec.getSize(widthMeasureSpec)) {
            childWidth = MeasureSpec.getSize(widthMeasureSpec);
        }

        int widthSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            widthSpec = widthMeasureSpec;
        }

        setMeasuredDimension(widthSpec, heightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int height = b - t - getPaddingTop() - getPaddingBottom();
        int centerOfHeight = height / 2 + getPaddingTop();
        int usableWidth = r - l - getPaddingLeft() - getPaddingRight() - (2 * attribute.horizontalPadding()) - attribute.indicatorWidth();

        int lastPointOfLeftEdge = attribute.horizontalPadding() + attribute.indicatorWidth() + getPaddingLeft();
        int innerChildSpace = findChildInnerSpace(usableWidth);

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.getVisibility() != GONE) {
                int centerHeightOfChild = view.getMeasuredHeight() / 2;
                int childTopPoint = centerOfHeight - centerHeightOfChild;
                int childBottomPoint = childTopPoint + view.getMeasuredHeight();

                view.layout(lastPointOfLeftEdge, childTopPoint, lastPointOfLeftEdge + view.getMeasuredWidth(), childBottomPoint);
                lastPointOfLeftEdge += (view.getMeasuredWidth() + innerChildSpace);
            }
        }
    }

    private int findChildInnerSpace(int drawableWidth) {
        int availableSpace = findAvailableSpace(drawableWidth);
        int innerChildSpace = 0;

        if (availableSpace > 0) {
            int childCount = getChildCount() - 1;
            innerChildSpace = childCount > 0 ? availableSpace / childCount : 0;
        }

        return innerChildSpace;
    }

    private int findAvailableSpace(int parentWidth) {
        int childWidth = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.getVisibility() != GONE) {

                childWidth += view.getMeasuredWidth();
            }
        }

        int space = parentWidth - childWidth;
        if (space < 0) {
            space = 0;
        }

        return space;
    }

    private void readAttribute(AttributeSet attributeSet) {
        Context context = getContext();
        Resources resources = context.getResources();

        int defaultHorizontalPadding = resources.getDimensionPixelSize(R.dimen.colorful_button_horizontal_padding);
        int defaultVerticalPadding = resources.getDimensionPixelSize(R.dimen.colorful_button_vertical_padding);
        int defaultIndicatorWidth = resources.getDimensionPixelSize(R.dimen.colorful_button_indicator_width);
        float defaultIndicatorRadius = resources.getDimensionPixelSize(R.dimen.colorful_button_indicator_radius);

        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ColorfulButton);
        String title = typedArray.getString(R.styleable.ColorfulButton_android_text);
        int pressedColor = typedArray.getColor(R.styleable.ColorfulButton_pressedColor, NONE);
        int defaultColor = typedArray.getColor(R.styleable.ColorfulButton_foregroundColor, NONE);
        int indicatorColor = typedArray.getColor(R.styleable.ColorfulButton_indicatorColor, NONE);
        int imageRes = typedArray.getResourceId(R.styleable.ColorfulButton_android_src, NONE);
        int vPadding = (int) typedArray.getDimension(R.styleable.ColorfulButton_verticalPadding, defaultVerticalPadding);
        int hPadding = (int) typedArray.getDimension(R.styleable.ColorfulButton_horizontalPadding, defaultHorizontalPadding);
        int indicatorWidth = (int) typedArray.getDimension(R.styleable.ColorfulButton_indicatorWidth, defaultIndicatorWidth);
        float indicatorRadius = typedArray.getFloat(R.styleable.ColorfulButton_radius, defaultIndicatorRadius);
        typedArray.recycle();

        attribute = Attribute.createAttribute(title, imageRes, defaultColor, indicatorColor,
                pressedColor, vPadding, hPadding, indicatorWidth, indicatorRadius);
    }

    private void applyAttributes(Attribute attribute) {
        Context context = getContext();
        Drawable image = ContextCompat.getDrawable(context, attribute.iconRes());
        String title = attribute.title();

        mTitle.setText(title);
        mIcon.setImageDrawable(image);
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

    public static class Attribute {

        private String mTitle;
        private int mIconRes;
        private int mIndicatorColor;
        private int mPressedColor;
        private int mForegroundColor;
        private int mHorizontalPadding;
        private int mVerticalPadding;
        private int mIndicatorWidth;
        private float mIndicatorRadius;

        private Attribute(String title, int iconRes, int foregroundColor, int indicatorColor, int pressedColor, int vPadding,
                          int hPadding, int indicatorWidth, float indicatorRadius) {
            this.mTitle = title;
            this.mIconRes = iconRes;
            this.mForegroundColor = foregroundColor;
            this.mIndicatorColor = indicatorColor;
            this.mPressedColor = pressedColor;
            this.mVerticalPadding = vPadding;
            this.mHorizontalPadding = hPadding;
            this.mIndicatorWidth = indicatorWidth;
            this.mIndicatorRadius = indicatorRadius;
        }

        static Attribute createAttribute(String title, int iconRes,
                                         int foregroundColor, int indicatorColor, int pressedColor, int vPadding, int hPadding,
                                         int indicatorWidth, float indicatorRadius) {
            return new Attribute(title, iconRes, foregroundColor, indicatorColor, pressedColor,
                    vPadding, hPadding, indicatorWidth, indicatorRadius);
        }

        public String title() {
            return mTitle;
        }

        public int iconRes() {
            return mIconRes;
        }

        public int foregroundColor() {
            return mForegroundColor;
        }

        public int indicatorColor() {
            return mIndicatorColor;
        }

        public int pressedColor() {
            return mPressedColor;
        }

        public int horizontalPadding() {
            return mHorizontalPadding;
        }

        public int verticalPadding() {
            return mVerticalPadding;
        }

        public int indicatorWidth() {
            return mIndicatorWidth;
        }

        public float indicatorRadius() {
            return mIndicatorRadius;
        }

    }

}