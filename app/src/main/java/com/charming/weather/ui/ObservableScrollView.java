package com.charming.weather.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by CharmingWong on 2016/12/7.
 */

public class ObservableScrollView extends ScrollView {

    private OnObservableScrollChangedListener mListener;

    public void setOnObservableScrollChangedListener(OnObservableScrollChangedListener listener) {
        mListener = listener;
    }

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mListener != null) {
            mListener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public interface OnObservableScrollChangedListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }
}
