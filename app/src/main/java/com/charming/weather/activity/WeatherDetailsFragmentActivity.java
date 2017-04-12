package com.charming.weather.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.charming.weather.R;
import com.charming.weather.adapter.WeatherDetailsPagerAdapter;
import com.charming.weather.util.ApplicationUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherDetailsFragmentActivity extends BaseSwipeBackActivity {

    private int mLastPage, mCurrentPage;
    private ViewPager mViewPager;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_tendency_fragment);
        ApplicationUtil.setMiuiStatusBarDarkMode(this, true);
        init();
    }

    private void init() {
        mLinearLayout = (LinearLayout) findViewById(R.id.daily_date);
        mViewPager = (ViewPager) findViewById(R.id.weather_tendency_page);
        mViewPager.setOffscreenPageLimit(0);
        setDateView();
        setDailyView();
        setDailyViewBackground();
        setOnClickListenerForDailyView();
        initViewPager();
    }

    private void initViewPager() {
        WeatherDetailsPagerAdapter adapter = new WeatherDetailsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mCurrentPage = getIntent().getIntExtra("day", 0);
        mViewPager.setCurrentItem(mCurrentPage);
        TextView textView = (TextView) mLinearLayout.getChildAt(mCurrentPage);
        textView.setBackground(getDrawable(R.drawable.daily_view_selected_bg));
        textView.setTextColor(Color.WHITE);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                mLastPage = mCurrentPage;
                mCurrentPage = position;
                TextView child = (TextView) mLinearLayout.getChildAt(mLastPage);
                child.setBackground(getDrawable(R.drawable.daily_view_bg));
                child.setTextColor(getResources().getColor(R.color.black_gray));
                child = (TextView) mLinearLayout.getChildAt(mCurrentPage);
                child.setBackground(getDrawable(R.drawable.daily_view_selected_bg));
                child.setTextColor(Color.WHITE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void setDateView() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MM月dd日");
        String start = dateFormat.format(date);
        date.setTime(date.getTime() + 60 * 60 * 24 * 7 * 1000);
        String end = dateFormat.format(date);
        ((TextView) findViewById(R.id.weather_forecast_date))
                .setText(start + " - " + end);
    }

    private void setDailyView() {

        int density = ApplicationUtil.getDisplayDensity(this);
        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(40 * density, 40 * density);
        lp.gravity = Gravity.CENTER;
        lp.weight = 1;
        lp.leftMargin = 6 * density;
        lp.rightMargin = 6 * density;
        TextView textView = new TextView(this);
        textView.setText(R.string.today_text);
        mLinearLayout.addView(textView, lp);
        textView = new TextView(this);
        textView.setText(R.string.tomorrow_text);
        mLinearLayout.addView(textView, lp);
        textView = new TextView(this);
        textView.setText(R.string.the_day_after_tomorrow_text);
        mLinearLayout.addView(textView, lp);
        String s;
        DateFormat dateFormat = new SimpleDateFormat("EEEE");
        Date date = new Date();
        date.setTime(date.getTime() + 60 * 60 * 24 * 3 * 1000);
        for (int i = 0; i < 4; i++) {
            textView = new TextView(this);
            s = dateFormat.format(date);
            s = s.substring(2, 3);
            textView.setText("周" + s);
            mLinearLayout.addView(textView, lp);
            date.setTime(date.getTime() + 60 * 60 * 24 * 1000);
        }

        for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
            TextView child = (TextView) mLinearLayout.getChildAt(i);
            child.setGravity(Gravity.CENTER);
        }
    }

    private void setDailyViewBackground() {
        for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
            TextView child = ((TextView) mLinearLayout.getChildAt(i));
            child.setTextColor(getResources().getColor(R.color.black_gray));
            child.setBackgroundResource(R.drawable.daily_view_bg);
        }
    }

    private void setOnClickListenerForDailyView() {
        for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
            TextView child = (TextView) mLinearLayout.getChildAt(i);
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout ll = (LinearLayout) findViewById(R.id.daily_date);
                    int index = ll.indexOfChild(v);
                    mViewPager.setCurrentItem(index, false);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
