package com.charming.weather.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.charming.weather.R;
import com.charming.weather.entity.weather.Aqi;
import com.charming.weather.entity.weather.Basic;
import com.charming.weather.entity.weather.City;
import com.charming.weather.ui.AirQualityView;
import com.charming.weather.util.ApplicationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AirQualityActivity extends BaseSwipeBackActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_quality);
        ApplicationUtil.setMiuiStatusBarDarkMode(this, true);
        init();
        initShare();
    }

    private void init() {
        Map<String, Object> data = WeatherOverviewActivity.getWeatherData();
        Aqi aqi = (Aqi) data.get("aqi");
        City city = aqi.getCity();
        Basic basic = (Basic) data.get("basic");
        ((TextView) findViewById(R.id.pm25)).setText(city.getPm25());
        ((TextView) findViewById(R.id.no2)).setText(city.getNo2());
        ((TextView) findViewById(R.id.co)).setText(city.getCo());
        ((TextView) findViewById(R.id.pm10)).setText(city.getPm10());
        ((TextView) findViewById(R.id.so2)).setText(city.getSo2());
        ((TextView) findViewById(R.id.o3)).setText(city.getO3());
        ((TextView) findViewById(R.id.air_quality)).setText(city.getQlty());
        ((TextView) findViewById(R.id.location)).setText(basic.getCity());
        AirQualityView aqv = (AirQualityView) findViewById(R.id.air_quality_index);
        List<String> aqvData = new ArrayList<>();
        aqvData.add(city.getAqi());
        aqvData.add(getString(R.string.air_quality_index_en_text));
        aqvData.add(getString(R.string.air_quality_index_text));
        aqv.setData(aqvData);

        findViewById(R.id.btn_share).setOnClickListener(this);
    }

    private void initShare() {
        final View rootView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationUtil.shareWeatherIcon(rootView);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_share:
                ApplicationUtil.shareWeatherIcon(findViewById(R.id.activity_air_quality));
                break;
        }
    }
}
