package com.gokhanaliccii.colorfulbutton.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gokhanaliccii.colorfulbutton.R;

/**
 * Created by gokhan.alici on 23.10.2017.
 */

public class ImageTextView extends LinearLayout {

    private static final String TAG = "ImageTextView";

    private TextView textView;
    private ImageView imageView;

    public ImageTextView(Context context) {
        this(context, null);
    }

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    private void initView(AttributeSet attributeSet) {
        attachView();
        parseAttributes(attributeSet);
    }

    private void attachView() {
        Context context = getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_image_textview, this, true);

        textView = (TextView) findViewById(R.id.imagetextview_text);
        imageView = (ImageView) findViewById(R.id.imagetextview_icon);
    }

    private void parseAttributes(AttributeSet attributeSet) {
        if (attributeSet != null) {
            Context context = getContext();
            TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ImageTextView);
            String text = typedArray.getString(R.styleable.ImageTextView_android_text);
            int res = typedArray.getResourceId(R.styleable.ImageTextView_android_src, -1);
            typedArray.recycle();

            textView.setText(text);
            imageView.setImageResource(res);
        }
    }
}