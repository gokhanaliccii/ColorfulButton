package com.gokhanaliccii.colorfulbutton.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gokhanaliccii.colorfulbutton.R;

/**
 * Created by gokhan.alici on 19.10.2017.
 */

public class ColorfulButton extends ViewGroup {
    private static final String TAG = "ColorfulButton";
    private static int NONE = -1;

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

        if (attributeSet != null) {
            attribute = readAttribute(attributeSet);
            applyAttributes(attribute);
        }
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
        if (getChildCount() == 0) {
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        measureChild(mTitle, widthMeasureSpec, heightMeasureSpec);
        measureChild(mIcon, widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        width += mTitle.getMeasuredWidth();
        width += mIcon.getMeasuredWidth();

        height = Math.max(mTitle.getMeasuredHeight(), mIcon.getMeasuredHeight());

        int widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        setMeasuredDimension(widthSpec, heightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = r - l - getPaddingLeft() - getPaddingRight();
        int height = b - getPaddingTop() - getPaddingBottom();

        int childTotalWidth = 0;
        int childTotalHeight = 0;

        int center = (r - l) / 2;

//        mTitle.layout(l, t, l + mTitle.getWidth(), t + mTitle.getHeight());

        mTitle.layout(l, t, l + mTitle.getMeasuredWidth(), t + mTitle.getMeasuredHeight());
        mIcon.layout(center, t, center + mIcon.getMeasuredWidth(), t + mIcon.getMeasuredHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "onDraw: ");
    }

    private Attribute readAttribute(AttributeSet attributeSet) {
        Context context = getContext();

        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ColorfulButton);
        String title = typedArray.getString(R.styleable.ColorfulButton_android_text);
        int color = typedArray.getColor(R.styleable.ColorfulButton_android_color, NONE);
        int imageRes = typedArray.getResourceId(R.styleable.ColorfulButton_android_src, NONE);
        typedArray.recycle();

        return Attribute.createAttribute(title, imageRes, color);
    }

    private void applyAttributes(Attribute attribute) {
        Context context = getContext();
        Drawable image = ContextCompat.getDrawable(context, attribute.iconRes());
        String title = attribute.title();

        mTitle.setText(title);
        mIcon.setImageDrawable(image);
    }

    /*** important part for measure process ***/
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

    private static class Attribute {

        private String mTitle;
        private int mIconRes;
        private int mColorRes;

        private Attribute(String title, int iconRes, int color) {
            this.mTitle = title;
            this.mIconRes = iconRes;
            this.mColorRes = color;
        }

        static Attribute createAttribute(String title, int iconRes, int colorRes) {
            return new Attribute(title, iconRes, colorRes);
        }

        public String title() {
            return mTitle;
        }

        public int iconRes() {
            return mIconRes;
        }

        public int colorRes() {
            return mColorRes;
        }
    }
}
