package com.gokhanaliccii.colorfulbutton.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gokhanaliccii.colorfulbutton.R;

/**
 * Created by gokhan.alici on 19.10.2017.
 */

public class ColorfulButton extends ViewGroup {

    private static final String TAG = "ColorfulButton";

    private static final int NONE = -1;
    private static final int TWO_SIDE = 2;

    private ImageView mIcon;
    private TextView mTitle;
    private Attribute attribute;
    private Drawer shapeDrawer;
    private SpaceMode spaceMode;

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
        attribute = readAttribute(attributeSet);
        applyAttributes(attribute);
        createShapeDrawer();
    }

    private void createShapeDrawer() {
        Context context = getContext();
        shapeDrawer = new LeftColorDrawer(attribute.colorRes(), Color.WHITE);
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

        int innerPadding = attribute.padding() * TWO_SIDE;
        childWidth += (innerPadding);
        childHeight += (innerPadding);

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
        int width = r - l - getPaddingLeft() - getPaddingRight() - (2 * attribute.padding());
        int height = b - getPaddingTop() - getPaddingBottom() - t;
        int centerOfHeight = height / 2 + t + getPaddingTop();

        int lastPointOfLeftEdge = l + attribute.padding() + getPaddingLeft();
        int innerChildSpace = findChildInnerSpace(width);

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.getVisibility() != GONE) {

                int centerOfChild = view.getMeasuredHeight() / 2;
                int centerOnParentView = centerOfHeight - centerOfChild;
                int bottomOnParentView = centerOnParentView + view.getMeasuredHeight();

                view.layout(lastPointOfLeftEdge, centerOnParentView, lastPointOfLeftEdge + view.getMeasuredWidth(), bottomOnParentView);
                lastPointOfLeftEdge += view.getMeasuredWidth();
                lastPointOfLeftEdge += innerChildSpace;
            }
        }
    }

    private int findChildInnerSpace(int parentWidth) {
        int totalAvailableSpace = findAvailableSpace(parentWidth);
        int innerChildSpace = 0;

        if (totalAvailableSpace > 0) {
            int childCount = getChildCount() - 1;
            innerChildSpace = childCount > 0 ? totalAvailableSpace / childCount : 0;
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

    @Override
    protected void dispatchDraw(Canvas canvas) {
        shapeDrawer.draw(canvas);
        super.dispatchDraw(canvas);
    }


    private Attribute readAttribute(AttributeSet attributeSet) {
        Context context = getContext();
        Resources resources = context.getResources();

        int defaultInnerPadding = resources.getDimensionPixelSize(R.dimen.colorful_button_default_padding);

        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ColorfulButton);
        String title = typedArray.getString(R.styleable.ColorfulButton_android_text);
        int color = typedArray.getColor(R.styleable.ColorfulButton_android_color, NONE);
        int imageRes = typedArray.getResourceId(R.styleable.ColorfulButton_android_src, NONE);
        int padding = (int) typedArray.getDimension(R.styleable.ColorfulButton_innerpadding, defaultInnerPadding);
        typedArray.recycle();

        return Attribute.createAttribute(title, imageRes, color, padding);
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

    private static class Attribute {

        private String mTitle;
        private int mIconRes;
        private int mColorRes;
        private int mPadding;

        private Attribute(String title, int iconRes, int color, int padding) {
            this.mTitle = title;
            this.mIconRes = iconRes;
            this.mColorRes = color;
            this.mPadding = padding;
        }

        static Attribute createAttribute(String title, int iconRes, int colorRes, int padding) {
            return new Attribute(title, iconRes, colorRes, padding);
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

        public int padding() {
            return mPadding;
        }
    }

    private interface SpaceMode {

        int applySpace(int index);
    }

    private interface Drawer {

        void draw(Canvas canvas);
    }

    private static class LeftColorDrawer implements Drawer {

        private Paint foregroundPaint;
        private Paint backgroundPaint;
        private int radius = 5;
        private int thickness = 40;

        public LeftColorDrawer(int indicatorColor, int backGroundColor) {
            foregroundPaint = createPaint(indicatorColor);
            backgroundPaint = createPaint(backGroundColor);
        }

        private Paint createPaint(int color) {
            Paint backgroundPaint = new Paint();
            backgroundPaint.setColor(color);
            backgroundPaint.setAntiAlias(true);
            backgroundPaint.setColor(color);

            return backgroundPaint;
        }

        @Override
        public void draw(Canvas canvas) {
            int width = canvas.getWidth();
            int height = canvas.getHeight();

            RectF backGround = new RectF(0, 0, width, height);
            RectF foreGround = new RectF(0 + thickness, 0, width, height);

            canvas.drawRoundRect(backGround, radius, radius, foregroundPaint);
            canvas.drawRoundRect(foreGround, radius, radius, backgroundPaint);
        }
    }

    private static class TransparentDrawer implements Drawer {

        @Override
        public void draw(Canvas canvas) {

        }
    }
}