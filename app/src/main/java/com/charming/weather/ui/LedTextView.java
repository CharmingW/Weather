package com.charming.weather.ui;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by 56223 on 2016/11/14.
 */

public class LedTextView extends TextView {

    public LedTextView(Context context) {
        super(context);
        initView();
    }

    public LedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public LedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        AssetManager assetManager = getContext().getAssets();
        Typeface font = Typeface.createFromAsset(assetManager, "digital7.ttf");
        setTypeface(font);
    }
}
