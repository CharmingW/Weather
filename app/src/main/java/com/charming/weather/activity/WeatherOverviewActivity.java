package com.charming.weather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.charming.weather.R;
import com.charming.weather.adapter.NewsTypeAdapter;
import com.charming.weather.comminterface.OnResponseCallback;
import com.charming.weather.entity.weather.Aqi;
import com.charming.weather.entity.weather.Basic;
import com.charming.weather.entity.weather.City;
import com.charming.weather.entity.weather.Comf;
import com.charming.weather.entity.weather.Cond;
import com.charming.weather.entity.weather.Cw;
import com.charming.weather.entity.weather.Daily_forecast;
import com.charming.weather.entity.weather.Drsg;
import com.charming.weather.entity.weather.Flu;
import com.charming.weather.entity.weather.Hourly_forecast;
import com.charming.weather.entity.weather.Now;
import com.charming.weather.entity.weather.Sport;
import com.charming.weather.entity.weather.Suggestion;
import com.charming.weather.entity.weather.Tmp;
import com.charming.weather.entity.weather.Trav;
import com.charming.weather.entity.weather.Uv;
import com.charming.weather.entity.weather.Wind;
import com.charming.weather.presenter.WeatherOverviewPresenter;
import com.charming.weather.ui.AirQualityView;
import com.charming.weather.ui.AllDayWeatherTendencyView;
import com.charming.weather.ui.ObservableScrollView;
import com.charming.weather.util.ApplicationUtil;
import com.charming.weather.util.NetworkUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class WeatherOverviewActivity
        extends Activity
        implements View.OnClickListener{

    private static final String TAG = "WeatherOverviewActivity";
    private static final String DEGREE = "°";
    private static Map<String, Object> mWeatherData;

    private Handler mHandler = new Handler();
    private WeatherOverviewPresenter mPresenter;
    private String locationDescription;

    public static Map<String, Object> getWeatherData() {
        return mWeatherData;
    }

    private LocationClient mLocationClient;
    private BDLocationListener mLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            mLocationClient.stop();
            int locType = bdLocation.getLocType();
            String city;
            SharedPreferences spf = getSharedPreferences("location_city", MODE_PRIVATE);
            final SharedPreferences.Editor editor = spf.edit();
            if (locType == BDLocation.TypeGpsLocation
                    || locType == BDLocation.TypeNetWorkLocation
                    || locType == BDLocation.TypeOffLineLocation
                    || locType == BDLocation.TypeCacheLocation) {
                city = bdLocation.getCity();
                String oldCity = spf.getString("city", null);
                List<Poi> list = bdLocation.getPoiList();
                locationDescription = list.get(0).getName();
                if (oldCity != null && oldCity.equals(city)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView location
                                    = (TextView) findViewById(R.id.specific_location);
                            location.setText(locationDescription);
                        }
                    });
                    return;
                }
                editor.putString("city", city);
                SharedPreferencesCompat.EditorCompat editorCompat = SharedPreferencesCompat.EditorCompat.getInstance();
                editorCompat.apply(editor);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.startPresent();
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherOverviewActivity.this, "定位失败", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new WeatherOverviewPresenter(getApplicationContext());
        startLocation();
    }

    private void startLocation() {
        mLocationClient = new LocationClient(getApplicationContext());

        initLocation();
        initPresenter();

        SharedPreferences spf = getSharedPreferences("location_city", MODE_PRIVATE);
        String city = spf.getString("city", null);

        if (city == null) {

            SharedPreferences.Editor editor = spf.edit();
            editor.putString("city", "广州");
            SharedPreferencesCompat.EditorCompat editorCompat
                    = SharedPreferencesCompat.EditorCompat.getInstance();
            editorCompat.apply(editor);
            mPresenter.startPresent();
            //开始定位
            mLocationClient.start();
        } else {
            //初始化数据主导器
            mPresenter.startPresent();

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLocationClient.start();
                }
            }, 2000);
        }
    }

    private void initLocation() {

        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 10000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(false);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }


    private void init() {

        setContentView(R.layout.activity_weather_overview);

        setupTitleLocation();

        //设置刷新动作
        setRefreshAction();

        //填充数据
        fillDataIntoView();

        //滚动时标题透明度变化
        setTitleAlphaWithScrolling();

        //设置导航菜单动作
        setNvMenuAction();

        //为控件设置监听器
        setOnClickListenerForView();

        //设置新闻列表
        setNewsList();
    }

    private void setupTitleLocation() {
        TextView location
                = (TextView) findViewById(R.id.specific_location);
        location.setText(locationDescription);
    }

    private void setOnClickListenerForView() {
        findViewById(R.id.weather_tendency_entrance).setOnClickListener(this);
        findViewById(R.id.air_quality_details_entrance).setOnClickListener(this);
    }

    private void initPresenter() {
        mPresenter.setOnResponseCallback(new OnResponseCallback() {
            @Override
            public void onResponseSuccess(Object response) {
                mWeatherData = ((Map) response);
                init();
            }

            @Override
            public void onResponseError(Exception e) {
                Log.i(TAG, "onResponseError: " + e.getMessage());
                Toast.makeText(WeatherOverviewActivity.this,
                        R.string.no_data_text,
                        Toast.LENGTH_SHORT).show();
                setContentView(R.layout.news_error_refresh);

                findViewById(R.id.error_refresh).setOnClickListener(WeatherOverviewActivity.this);
            }
        });
    }

    private void setRefreshAction() {
        final SwipeRefreshLayout weatherRefresh = (SwipeRefreshLayout) findViewById(R.id.weather_refresh);
        weatherRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter.checkDataUpdate()) {
                    mPresenter.startPresent();
                    return;
                }
                Toast.makeText(WeatherOverviewActivity.this, R.string.no_update, Toast.LENGTH_SHORT).show();
                if (weatherRefresh.isRefreshing()) {
                    weatherRefresh.setRefreshing(false);
                }
            }
        });
    }

    private void fillDataIntoView() {
        SwipeRefreshLayout weatherRefresh = (SwipeRefreshLayout) findViewById(R.id.weather_refresh);
        if (weatherRefresh.isRefreshing()) {
            weatherRefresh.setRefreshing(false);
            Toast.makeText(WeatherOverviewActivity.this, R.string.refresh_success, Toast.LENGTH_SHORT).show();
        }
        if (mWeatherData != null) {
            showDailyInfo((List<Daily_forecast>) mWeatherData.get("daily_forecast"));
            showAllDayTemperature(
                    (List<Hourly_forecast>) mWeatherData.get("hourly_forecast")
                    , ((List<Daily_forecast>) mWeatherData.get("daily_forecast")));
            showBasic((Basic) mWeatherData.get("basic"));
            showNowInfo((Now) mWeatherData.get("now"));
            showAirQuality((Aqi) mWeatherData.get("aqi"));
            showTips((Suggestion) mWeatherData.get("suggestion"));
        }
    }

    private void setNvMenuAction() {
        ((DrawerLayout) findViewById(R.id.activity_weather_overview)).addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });


        View menu = findViewById(R.id.btn_menu);
        menu.setOnClickListener(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_menu);
//        View header =  getLayoutInflater().inflate(R.layout.sliding_menu, navigationView, false);
//        navigationView.addView(header);
        View header = navigationView.getHeaderView(0);
        TextView close = (TextView) header.findViewById(R.id.close);
        TextView addOrDelete = (TextView) header.findViewById(R.id.add_or_delete);
        TextView share = (TextView) header.findViewById(R.id.share);
        TextView settings = (TextView) header.findViewById(R.id.settings);
        close.setOnClickListener(this);
        addOrDelete.setOnClickListener(this);
        share.setOnClickListener(this);
        settings.setOnClickListener(this);
    }


    private void setTitleAlphaWithScrolling() {
        final View title = findViewById(R.id.title);
        title.setAlpha(0);
        ObservableScrollView sv = (ObservableScrollView) findViewById(R.id.sv);
        sv.setOnObservableScrollChangedListener(new ObservableScrollView.OnObservableScrollChangedListener() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                int[] location = new int[2];
                findViewById(R.id.rl).getLocationOnScreen(location);
                float alpha = ((float) location[1] / -800);
                title.setAlpha(alpha);
                if (location[1] < 0) {
                    ApplicationUtil.setMiuiStatusBarDarkMode(WeatherOverviewActivity.this, true);
                } else {
                    ApplicationUtil.setMiuiStatusBarDarkMode(WeatherOverviewActivity.this, false);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationClient.registerLocationListener(mLocationListener);
        if (!NetworkUtil.checkNetworkStatus(this)) {
            Toast.makeText(this, getString(R.string.no_network_text), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationClient.unRegisterLocationListener(mLocationListener);
        if (mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.activity_weather_overview);
        if (drawerLayout.isDrawerOpen(Gravity.END)) {
            drawerLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawerLayout.closeDrawer(Gravity.END);
                }
            }, 500);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        Suggestion suggestion;
        switch (view.getId()) {
            //菜单事件
            case R.id.btn_menu:
                ((DrawerLayout) findViewById(R.id.activity_weather_overview)).openDrawer(Gravity.END);
                break;
            case R.id.close:
                ((DrawerLayout) findViewById(R.id.activity_weather_overview)).closeDrawer(Gravity.END);
                break;
            case R.id.add_or_delete:
                intent = new Intent(WeatherOverviewActivity.this, AddCityActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.share:
                View rootView = findViewById(R.id.sv);
                ApplicationUtil.shareWeatherIcon(rootView);
                break;
            case R.id.settings:
                intent = new Intent(WeatherOverviewActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            //天气细节
            case R.id.weather_details_item1:
                intent = new Intent(WeatherOverviewActivity.this, WeatherDetailsFragmentActivity.class);
                intent.putExtra("day", 0);
                startActivity(intent);

                break;
            case R.id.weather_details_item2:
                intent = new Intent(WeatherOverviewActivity.this, WeatherDetailsFragmentActivity.class);
                intent.putExtra("day", 1);
                startActivity(intent);

                break;
            case R.id.weather_details_item3:
                intent = new Intent(WeatherOverviewActivity.this, WeatherDetailsFragmentActivity.class);
                intent.putExtra("day", 2);
                startActivity(intent);

                break;

            //指数
            case R.id.comfortable_tips:
                intent = new Intent(WeatherOverviewActivity.this, TipsActivity.class);
                intent.putExtra("index", getString(R.string.comfortable_index_text));
                intent.putExtra("imageId", R.drawable.ic_comfortable);
                suggestion = (Suggestion) mWeatherData.get("suggestion");
                intent.putExtra("title", suggestion.getComf().getBrf());
                intent.putExtra("content", suggestion.getComf().getTxt());
                startActivity(intent);
                break;
            case R.id.dress_tips:
                intent = new Intent(WeatherOverviewActivity.this, TipsActivity.class);
                intent.putExtra("index", getString(R.string.dress_index_text));
                intent.putExtra("imageId", R.drawable.ic_dress);
                suggestion = (Suggestion) mWeatherData.get("suggestion");
                intent.putExtra("title", suggestion.getDrsg().getBrf());
                intent.putExtra("content", suggestion.getDrsg().getTxt());
                startActivity(intent);

                break;
            case R.id.cold_tips:
                intent = new Intent(WeatherOverviewActivity.this, TipsActivity.class);
                intent.putExtra("index", getString(R.string.cold_index_text));
                intent.putExtra("imageId", R.drawable.ic_cold);
                suggestion = (Suggestion) mWeatherData.get("suggestion");
                intent.putExtra("title", suggestion.getFlu().getBrf());
                intent.putExtra("content", suggestion.getFlu().getTxt());
                startActivity(intent);

                break;
            case R.id.ultraviolet_tips:
                intent = new Intent(WeatherOverviewActivity.this, TipsActivity.class);
                intent.putExtra("index", getString(R.string.ultraviolet_index_text));
                intent.putExtra("imageId", R.drawable.ic_ultraviolet);
                suggestion = (Suggestion) mWeatherData.get("suggestion");
                intent.putExtra("title", suggestion.getUv().getBrf());
                intent.putExtra("content", suggestion.getUv().getTxt());
                startActivity(intent);

                break;
            case R.id.exercise_tips:
                intent = new Intent(WeatherOverviewActivity.this, TipsActivity.class);
                intent.putExtra("index", getString(R.string.tips_exercise_text));
                intent.putExtra("imageId", R.drawable.ic_exercise);
                suggestion = (Suggestion) mWeatherData.get("suggestion");
                intent.putExtra("title", suggestion.getSport().getBrf());
                intent.putExtra("content", suggestion.getSport().getTxt());
                startActivity(intent);

                break;
            case R.id.travel_tips:
                intent = new Intent(WeatherOverviewActivity.this, TipsActivity.class);
                intent.putExtra("index", getString(R.string.tips_travel_text));
                intent.putExtra("imageId", R.drawable.ic_travel);
                suggestion = (Suggestion) mWeatherData.get("suggestion");
                intent.putExtra("title", suggestion.getTrav().getBrf());
                intent.putExtra("content", suggestion.getTrav().getTxt());
                startActivity(intent);

                break;
            case R.id.wash_car_tips:
                intent = new Intent(WeatherOverviewActivity.this, TipsActivity.class);
                intent.putExtra("index", getString(R.string.tips_wash_car_text));
                intent.putExtra("imageId", R.drawable.ic_wash_car);
                suggestion = (Suggestion) mWeatherData.get("suggestion");
                intent.putExtra("title", suggestion.getCw().getBrf());
                intent.putExtra("content", suggestion.getCw().getTxt());
                startActivity(intent);
                break;

            //入口
            case R.id.weather_tendency_entrance:
                intent = new Intent(WeatherOverviewActivity.this, WeatherDetailsFragmentActivity.class);
                startActivity(intent);
                break;
            case R.id.air_quality_details_entrance:
                intent = new Intent(WeatherOverviewActivity.this, AirQualityActivity.class);
                startActivity(intent);
                break;
            case R.id.air_quality:
                intent = new Intent(WeatherOverviewActivity.this, AirQualityActivity.class);
                startActivity(intent);
                break;
            case R.id.error_refresh:
                mPresenter.startPresent();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                mPresenter.startPresent();
            }
    }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_weather_overview);
        if (drawer.isDrawerOpen(Gravity.END)) {
            drawer.closeDrawer(Gravity.END);
        } else {
            super.onBackPressed();
        }
    }

    private void showDailyInfo(List<Daily_forecast> daily_forecast_list) {

        //遍历三天的天气视图
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.future_weather_overview);
        for (int i = 0; i < 3; i++) {
            Daily_forecast daily_forecast = daily_forecast_list.get(i);
            View itemView = viewGroup.getChildAt(i);
            TextView date = ((TextView) itemView.findViewById(R.id.daily_date));
            Cond cond = daily_forecast.getCond();
            Tmp tmp = daily_forecast.getTmp();

            //设置时间文字
            if (i == 0) {
                date.setText(getString(R.string.today_text));
            } else if (i == 1) {
                date.setText(getString(R.string.tomorrow_text));
            } else {
                date.setText(getString(R.string.the_day_after_tomorrow_text));
            }
            //按照白天的天气状况设置明天和后天的天气图标
            if (i != 0) {
                ((ImageView) itemView.findViewById(R.id.weather_s_icon)).setImageResource(getIconId(cond.getTxt_d()));
            }

            //设置当天的天气变化
            if (cond.getTxt_d().equals(cond.getTxt_n())) {
                ((TextView) itemView.findViewById(R.id.weather_details)).setText(cond.getTxt_d());
            } else {
                ((TextView) itemView.findViewById(R.id.weather_details)).setText(cond.getTxt_d() + "转" + cond.getTxt_n());
            }

            //日出日落时间
            ((TextView) itemView.findViewById(R.id.sun_rise_set_value))
                    .setText(daily_forecast.getAstro().getSr() + "/" + daily_forecast.getAstro().getSs());
            ((TextView) itemView.findViewById(R.id.high_low_degree)).setText(tmp.getMax() + DEGREE + " / " + tmp.getMin() + DEGREE);

            itemView.setOnClickListener(this);
        }


    }

    private void showAllDayTemperature(List<Hourly_forecast> hourly_forecasts, List<Daily_forecast> daily_forecasts) {
        AllDayWeatherTendencyView dayWeatherTendencyView = (AllDayWeatherTendencyView) findViewById(R.id.all_day_temperature);

        List<Integer> degrees = new ArrayList<>();
        if (hourly_forecasts != null && daily_forecasts != null) {
            Calendar calendar = Calendar.getInstance();
            //截取日出日落整点时间
            int sr = Integer.parseInt(daily_forecasts.get(0).getAstro().getSr().substring(0, 2));
            int ss = Integer.parseInt(daily_forecasts.get(0).getAstro().getSs().substring(0, 2));
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            if (currentHour > ss) {
                sr = Integer.parseInt(daily_forecasts.get(1).getAstro().getSr().substring(0, 2));
                ss = Integer.parseInt(daily_forecasts.get(1).getAstro().getSs().substring(0, 2));
                dayWeatherTendencyView.setSrAndSs(
                        daily_forecasts.get(1).getAstro().getSr(),
                        daily_forecasts.get(1).getAstro().getSs()
                );
            } else if (currentHour > sr && currentHour <= ss) {
                sr = Integer.parseInt(daily_forecasts.get(1).getAstro().getSr().substring(0, 2));
                ss = Integer.parseInt(daily_forecasts.get(0).getAstro().getSs().substring(0, 2));
                dayWeatherTendencyView.setSrAndSs(
                        daily_forecasts.get(1).getAstro().getSr(),
                        daily_forecasts.get(0).getAstro().getSs()
                );
            } else {
                sr = Integer.parseInt(daily_forecasts.get(0).getAstro().getSr().substring(0, 2));
                ss = Integer.parseInt(daily_forecasts.get(0).getAstro().getSs().substring(0, 2));
                dayWeatherTendencyView.setSrAndSs(
                        daily_forecasts.get(0).getAstro().getSr(),
                        daily_forecasts.get(0).getAstro().getSs()
                );
            }

            for (int i = 0; i < hourly_forecasts.size(); i++) {
                //整点温度
                degrees.add(Integer.parseInt(hourly_forecasts.get(i).getTmp()));
                if (sr == currentHour % 24 || ss == currentHour % 24) {
                    //日出温度 日落温度
                    degrees.add(Integer.parseInt(hourly_forecasts.get(i).getTmp()));
                }
                dayWeatherTendencyView.setDegrees(degrees);
                currentHour++;
            }
            dayWeatherTendencyView.invalidate();
        }
    }

    private void showAirQuality(Aqi aqi) {
        City city = aqi.getCity();
        AirQualityView airIndex = (AirQualityView) findViewById(R.id.air_quality_index);
        List<String> aqiData = new ArrayList<>();
        aqiData.add(city.getAqi());
        aqiData.add(getString(R.string.air_quality_index_en_text));
        aqiData.add(getString(R.string.air_quality_index_text));
        airIndex.setData(aqiData);
        airIndex.invalidate();

        List<String> ppData = new ArrayList<>();
        AirQualityView primaryPollutant = (AirQualityView) findViewById(R.id.primary_pollutant);
        ppData.add(city.getPm10());
        ppData.add(getString(R.string.primary_pollutant_en_text));
        ppData.add(getString(R.string.primary_pollutant_text));
        primaryPollutant.setData(ppData);
        primaryPollutant.invalidate();

        ((TextView) findViewById(R.id.location_weather_type)).append(" | " + city.getQlty());

        findViewById(R.id.air_quality).setOnClickListener(this);
    }

    private void showTips(Suggestion suggestion) {
        View tipsView = null;

        Comf comf = suggestion.getComf();
        tipsView = findViewById(R.id.comfortable_tips);
        ((ImageView) tipsView.findViewById(R.id.tips_icon)).setImageResource(R.drawable.ic_comfortable);
        ((TextView) tipsView.findViewById(R.id.tips_title)).setText(comf.getBrf());
        ((TextView) tipsView.findViewById(R.id.tips_content)).setText(comf.getTxt());
        tipsView.setOnClickListener(this);

        Drsg drsg = suggestion.getDrsg();
        tipsView = findViewById(R.id.dress_tips);
        ((ImageView) tipsView.findViewById(R.id.tips_icon)).setImageResource(R.drawable.ic_dress);
        ((TextView) tipsView.findViewById(R.id.tips_title)).setText(drsg.getBrf());
        ((TextView) tipsView.findViewById(R.id.tips_content)).setText(drsg.getTxt());
        tipsView.setOnClickListener(this);


        Flu flu = suggestion.getFlu();
        tipsView = findViewById(R.id.cold_tips);
        ((ImageView) tipsView.findViewById(R.id.tips_icon)).setImageResource(R.drawable.ic_cold);
        ((TextView) tipsView.findViewById(R.id.tips_title)).setText(flu.getBrf());
        ((TextView) tipsView.findViewById(R.id.tips_content)).setText(flu.getTxt());
        tipsView.setOnClickListener(this);


        Uv uv = suggestion.getUv();
        tipsView = findViewById(R.id.ultraviolet_tips);
        ((ImageView) tipsView.findViewById(R.id.tips_icon)).setImageResource(R.drawable.ic_ultraviolet);
        ((TextView) tipsView.findViewById(R.id.tips_title)).setText(uv.getBrf());
        ((TextView) tipsView.findViewById(R.id.tips_content)).setText(uv.getTxt());
        tipsView.setOnClickListener(this);


        Sport sport = suggestion.getSport();
        tipsView = findViewById(R.id.exercise_tips);
        ((ImageView) tipsView.findViewById(R.id.tips_icon)).setImageResource(R.drawable.ic_exercise);
        ((TextView) tipsView.findViewById(R.id.tips_title)).setText(sport.getBrf());
        ((TextView) tipsView.findViewById(R.id.tips_content)).setText(sport.getTxt());
        tipsView.setOnClickListener(this);


        Trav trav = suggestion.getTrav();
        tipsView = findViewById(R.id.travel_tips);
        ((ImageView) tipsView.findViewById(R.id.tips_icon)).setImageResource(R.drawable.ic_travel);
        ((TextView) tipsView.findViewById(R.id.tips_title)).setText(trav.getBrf());
        ((TextView) tipsView.findViewById(R.id.tips_content)).setText(trav.getTxt());
        tipsView.setOnClickListener(this);


        Cw cw = suggestion.getCw();
        tipsView = findViewById(R.id.wash_car_tips);
        ((ImageView) tipsView.findViewById(R.id.tips_icon)).setImageResource(R.drawable.ic_wash_car);
        ((TextView) tipsView.findViewById(R.id.tips_title)).setText(cw.getBrf());
        ((TextView) tipsView.findViewById(R.id.tips_content)).setText(cw.getTxt());
        tipsView.setOnClickListener(this);

    }

    private void showNowInfo(Now now) {
        Wind wind = now.getWind();
        ((TextView) findViewById(R.id.wind_direction)).setText(wind.getDir());
        ((TextView) findViewById(R.id.wind_level)).setText(wind.getSc());
        ((TextView) findViewById(R.id.relative_humidity_value)).setText(now.getHum());
        ((TextView) findViewById(R.id.rainfall_value)).setText(now.getPcpn());
        ((TextView) findViewById(R.id.location_weather_type)).append(" | " + now.getCond().getTxt());
        ((TextView) findViewById(R.id.degree)).setText(now.getTmp() + DEGREE);
        ((TextView) findViewById(R.id.title_text)).append(" " + now.getTmp() + DEGREE);

        ((TextView) findViewById(R.id.sendible_temperature)).setText(getString(R.string.sendible_temperature_text) + "   " + now.getFl() + DEGREE);
        ((TextView) findViewById(R.id.air_pressure)).setText(getString(R.string.air_pressure_text) + "   " + now.getPres().substring(0, 3) + "kPa");
        ((TextView) findViewById(R.id.visibility)).setText(getString(R.string.visibility_text) + "   " + now.getVis() + "km");
        ((TextView) findViewById(R.id.wind_degree)).setText(getString(R.string.wind_degree_text) + "   " + wind.getDeg() + DEGREE);
        ((TextView) findViewById(R.id.wind_speed)).setText(getString(R.string.wind_speed_text) + "   " + wind.getSpd() + "km/h");

        ((ImageView) findViewById(R.id.title_icon)).setImageResource(getIconId(now.getCond().getTxt()));
        ((ImageView) findViewById(R.id.weather_l_icon)).setImageResource(getIconId(now.getCond().getTxt()));
        ((ImageView) findViewById(R.id.weather_details_item1)
                .findViewById(R.id.weather_s_icon))
                .setImageResource(getIconId(now.getCond().getTxt()));
    }

    private int getIconId(String s) {
        switch (s) {
            case "晴":
                return R.drawable.ic_sunny;
            case "阴":
                return R.drawable.ic_shade;
            case "多云":
                return R.drawable.ic_cloudy;
            case "小雨":
                return R.drawable.ic_light_rain;
            case "中雨":
                return R.drawable.ic_moderate_rain;
            case "大雨":
                return R.drawable.ic_heavy_rain;
            case "阵雨":
                return R.drawable.ic_light_rain;
            case "暴雨":
                return R.drawable.ic_rainstorm;
            case "特大暴雨":
                return R.drawable.ic_extraordinary_rainstorm;
            case "雷阵雨":
                return R.drawable.ic_thundershower;
            case "小雪":
                return R.drawable.ic_light_snow;
            case "中雪":
                return R.drawable.ic_moderate_snow;
            case "大雪":
                return R.drawable.ic_heavy_snow;
            case "雨夹雪":
                return R.drawable.ic_sleet;
            case "风":
                return R.drawable.ic_wind;
            case "大风":
                return R.drawable.ic_heavy_wind;
            case "雾":
                return R.drawable.ic_fog;
            case "霾":
                return R.drawable.ic_haze;
            case "沙尘":
                return R.drawable.ic_dust;
        }
        return 0;
    }

    private void showBasic(Basic basic) {
        ((TextView) findViewById(R.id.location_weather_type)).setText(basic.getCity());
        ((TextView) findViewById(R.id.title_text)).setText(basic.getCity());
    }

    private void setNewsList() {
        GridView newsList = (GridView) findViewById(R.id.news_list);
        newsList.setAdapter(new NewsTypeAdapter(this));
        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WeatherOverviewActivity.this, NewsActivity.class);
                String type = ((TextView) view.findViewById(R.id.type_text)).getText().toString();
                intent.putExtra("news_type", type);
                startActivity(intent);
            }
        });
    }


}
