package com.charming.weather.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.charming.weather.R;
import com.charming.weather.util.ApplicationUtil;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 56223 on 2016/11/27.
 */

public class AllDayWeatherTendencyView extends View {

    private int mWidth, mHeight;

    private static final int THIN_LINE_WIDTH = 1;
    private static final int THICK_LINE_WIDTH = 3;
    private String mSr, mSs;

    private List<Integer> mDegrees;
    private Paint mPaint;
    private Context mContext;
    private Calendar mCalendar;
    private int density;

    public AllDayWeatherTendencyView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public AllDayWeatherTendencyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    public AllDayWeatherTendencyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        density = ApplicationUtil.getDisplayDensity((Activity) mContext);
        mWidth = density * 1560;
        mHeight = density * 177;
        mPaint = new Paint();
        mCalendar = Calendar.getInstance();
    }

    public void setSrAndSs(String sr, String ss) {
        mSr = sr;
        mSs = ss;
    }

    public void setDegrees(List<Integer> degrees) {
        mDegrees = degrees;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(measureWidth(widthMode, widthSize), measureHeight(heightMode, heightSize));
    }

    private int measureHeight(int heightMode, int heightSize) {
        if (heightMode == MeasureSpec.EXACTLY) {
            return heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            return mHeight;
        } else {
            return mHeight;
        }
    }

    private int measureWidth(int widthMode, int widthSize) {
        if (widthMode == MeasureSpec.EXACTLY) {
            return widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            return mWidth;
        } else {
            return mWidth;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mDegrees != null) {
            mPaint.setColor(mContext.getResources().getColor(android.R.color.darker_gray));
            mPaint.setStrokeWidth(THIN_LINE_WIDTH);
            canvas.drawLine(density * 30, density * 137, density * 1530, density *137, mPaint);
            Object[] tmps = mDegrees.toArray();
            Arrays.sort(tmps);
            int delta = (Integer) tmps[tmps.length - 1] - (Integer) tmps[0];
            int sr = Integer.parseInt(mSr.substring(0, 2));
            int ss = Integer.parseInt(mSs.substring(0, 2));
            int currentHour = mCalendar.get(Calendar.HOUR_OF_DAY);
            int lastHour = mCalendar.get(Calendar.HOUR_OF_DAY);
            for (int i = 0; i < mDegrees.size(); i++) {
                int d = mDegrees.get(i) - (Integer) tmps[0];
                int startX = density * 30 + density * 60 * i;
                int startY = density * 137;
                int endX = density * 30 + density * 60 * i;
                int endY = density * 137 - (density * 40 + d * density * 40 / delta);

                //画竖线
                mPaint.setColor(mContext.getResources().getColor(android.R.color.darker_gray));
                mPaint.setStrokeWidth(THIN_LINE_WIDTH);
                canvas.drawLine(startX, startY, endX, endY, mPaint);

                //画圆圈
                mPaint.setPathEffect(null);
                mPaint.setColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(THICK_LINE_WIDTH);
                canvas.drawCircle(endX, endY - density * 3, density * 3, mPaint);

                mPaint.setColor(Color.BLACK);
                mPaint.setAntiAlias(true);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setStrokeWidth(THICK_LINE_WIDTH);
                mPaint.setTextSize(density * 14);

                //画时间
                int hourValue = currentHour % 24;
                if (i == 0) {
                    canvas.drawText("现在", density * 13 + i * density * 60, density * 157, mPaint);
                } else {
                    if (sr == hourValue && lastHour == currentHour) {
                        canvas.drawText(mSr, density * 13 + i * density * 60, density * 157, mPaint);
                    } else if (ss == hourValue && lastHour == currentHour) {
                        canvas.drawText(mSs, density * 13 + i * density * 60, density * 157, mPaint);
                    } else {
                        if (hourValue < 10) {
                            canvas.drawText("0" + hourValue + ":00", density * 13 + i * density * 60, density * 157, mPaint);
                        } else {
                            canvas.drawText(hourValue + ":00", density * 13 + i * density * 60, density * 157, mPaint);
                        }
                    }
                }

                //画度数
                if (sr == hourValue || ss == hourValue) {
                    if (currentHour != lastHour) {
                        canvas.drawText(mDegrees.get(i) + "°", endX - density * 7, endY - density * 17, mPaint);
                        lastHour = currentHour;
                    } else {
                        if (sr == hourValue) {
                            canvas.drawText("日出", endX - density * 7, endY - density * 17, mPaint);
                        } else {
                            canvas.drawText("日落", endX - density * 7, endY - density * 17, mPaint);
                        }
                        currentHour++;
                    }
                } else {
                    canvas.drawText(mDegrees.get(i) + "°", endX - density * 17, endY - density * 17, mPaint);
                    lastHour = currentHour;
                    currentHour++;
                }

                //画连线
                mPaint.setColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                if (i < mDegrees.size() - 1) {
                    int nextEndX = density * 30 + density * 60 * (i + 1);
                    d = mDegrees.get(i + 1) - (Integer) tmps[0];
                    int nextEndY = density * 137 - (density * 40 + d * density * 40 / delta) - density * 3;
                    canvas.drawLine(endX + density * 5, endY - density * 3, nextEndX - density * 5, nextEndY, mPaint);
                }
            }
        }
    }
}
