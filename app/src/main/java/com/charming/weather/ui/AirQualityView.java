package com.charming.weather.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.charming.weather.R;
import com.charming.weather.util.ApplicationUtil;

import java.util.List;

/**
 * Created by CharmingWong on 2016/12/1.
 */

public class AirQualityView extends View {

    private List<String> mData;
    private Context mContext;

    public AirQualityView(Context context) {
        super(context);
        mContext = context;
    }

    public AirQualityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public AirQualityView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void setData(List<String> data) {
        mData = data;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int density = ApplicationUtil.getDisplayDensity((Activity) mContext);
        setMeasuredDimension(density * 100, density * 100);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int density = ApplicationUtil.getDisplayDensity((Activity) mContext);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(density * 2);
        paint.setColor(mContext.getResources().getColor(R.color.darker_gray));
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(density * 2, density * 2, density * 98, density * 98, 180, 225, false, paint);
        paint.setColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
        canvas.drawArc(density * 2, density * 2, density * 98, density * 98, 135, 45, false, paint);
        paint.setStyle(Paint.Style.FILL);
        if (mData != null) {
            paint.setTextSize(density * 37);
            if (mData.get(0).length() == 2) {
                canvas.drawText(mData.get(0), density * 28, density * 60, paint);
            } else {
                canvas.drawText(mData.get(0), density * 18, density * 60, paint);
            }

            paint.setColor(mContext.getResources().getColor(R.color.darker_gray));
            paint.setTextSize(mContext.getResources().getDimension(R.dimen.s_text_size));
            if (mData.get(1).length() == 3) {
                canvas.drawText(mData.get(1), density * 40, density * 73, paint);
            } else {
                canvas.drawText(mData.get(1), density * 33, density * 73, paint);
            }

            paint.setColor(mContext.getResources().getColor(R.color.black_gray));
            if (mData.get(2).length() == 6) {
                canvas.drawText(mData.get(2), density * 14, density * 98, paint);
            } else {
                canvas.drawText(mData.get(2), density * 20, density * 98, paint);
            }

        }
    }
}
