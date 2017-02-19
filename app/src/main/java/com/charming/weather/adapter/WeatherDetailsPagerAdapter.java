package com.charming.weather.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.charming.weather.R;
import com.charming.weather.activity.WeatherOverviewActivity;
import com.charming.weather.entity.Basic;
import com.charming.weather.entity.Cond;
import com.charming.weather.entity.Daily_forecast;
import com.charming.weather.entity.Now;
import com.charming.weather.entity.Wind;
import com.charming.weather.util.ApplicationUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by CharmingWong on 2016/12/6.
 */

public class WeatherDetailsPagerAdapter extends FragmentPagerAdapter {

    private static final String DEGREE = "°";

    private FragmentManager mFragmentManager;

    public WeatherDetailsPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        return WeatherDetailsFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return 7;
    }

    public static class WeatherDetailsFragment extends Fragment {

        private int mPage;

        public static WeatherDetailsFragment newInstance(int page) {
            WeatherDetailsFragment fragment = new WeatherDetailsFragment();
            Bundle args = new Bundle();
            args.putInt("page", page);
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_weather_details, container, false);
            rootView.findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApplicationUtil.shareWeatherIcon(rootView);
                }
            });

            Map<String, Object> data = WeatherOverviewActivity.getWeatherData();
            showBasic(rootView, (Basic) data.get("basic"));
            showDailyInfo(rootView, ((List<Daily_forecast>) data.get("daily_forecast")).get(mPage), (Now) data.get("now"));
            return rootView;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mPage = getArguments().getInt("page");

        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }


        private int getIconId(String s) {
            switch (s) {
                case "晴":
                    return R.drawable.ic_sunny;
                case "晴间":
                    return R.drawable.ic_sunny;
                case "阴":
                    return R.drawable.ic_cloudy;
                case "多云":
                    return R.drawable.ic_cloudy;
                case "小雨":
                    return R.drawable.ic_light_rain;
                case "中雨":
                    return R.drawable.ic_moderate_rain;
                case "大雨":
                    return R.drawable.ic_heavy_rain;
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
                case "雾霾":
                    return R.drawable.ic_haze;
                case "沙尘":
                    return R.drawable.ic_dust;
            }
            return 0;
        }

        private void showDailyInfo(View rootView, Daily_forecast daily_forecast, Now now) {
            ((TextView) rootView.findViewById(R.id.rainfall_probability)).append("  " + daily_forecast.getPop() + "%");


            //非当天则设置未来预报值
            if (mPage != 0) {
                Cond cond = daily_forecast.getCond();
                Wind wind = daily_forecast.getWind();

                ((TextView) rootView.findViewById(R.id.wind_direction)).setText(wind.getDir());
                ((TextView) rootView.findViewById(R.id.wind_level)).setText(wind.getSc());
                if (wind.getSc().equals("微风")) {
                    rootView.findViewById(R.id.unit).setVisibility(View.GONE);
                }
                ((TextView) rootView.findViewById(R.id.relative_humidity_value)).setText(daily_forecast.getHum());
                ((TextView) rootView.findViewById(R.id.rainfall_value)).setText(daily_forecast.getPcpn());
                ((TextView) rootView.findViewById(R.id.degree))
                        .setText(daily_forecast.getTmp().getMax() + DEGREE + "/" + daily_forecast.getTmp().getMin() + DEGREE);

                ((TextView) rootView.findViewById(R.id.rainfall_probability)).setText(getString(R.string.rainfall_probability_text) + "   " + daily_forecast.getPop());
                ((TextView) rootView.findViewById(R.id.air_pressure)).setText(getString(R.string.air_pressure_text) + "    " + daily_forecast.getPres().substring(0, 3) + "kPa");
                ((TextView) rootView.findViewById(R.id.visibility)).setText(getString(R.string.visibility_text) + "  " + daily_forecast.getVis() + "km");
                ((TextView) rootView.findViewById(R.id.wind_degree)).setText(getString(R.string.wind_degree_text) + "   " + wind.getDeg() + DEGREE);
                ((TextView) rootView.findViewById(R.id.wind_speed)).setText(getString(R.string.wind_speed_text) + "   " + wind.getSpd() + "km/h");
                ((ImageView) rootView.findViewById(R.id.weather_l_icon)).setImageResource(getIconId(daily_forecast.getCond().getTxt_d()));


                if (cond.getTxt_d().equals(cond.getTxt_n())) {
                    ((TextView) rootView.findViewById(R.id.location_weather_type)).append(" | " + cond.getTxt_d());
                } else {
                    ((TextView) rootView.findViewById(R.id.location_weather_type)).append(" | " + cond.getTxt_d() + "转" + cond.getTxt_n());
                }
                //日出日落间
                ((TextView) rootView.findViewById(R.id.sun_rise_set_value))
                        .append("  " + daily_forecast.getAstro().getSr() + "/" + daily_forecast.getAstro().getSs());

            } else { //当天则设置实时值
                Cond cond = now.getCond();
                Wind wind = now.getWind();
                ((TextView) rootView.findViewById(R.id.wind_direction)).setText(wind.getDir());
                ((TextView) rootView.findViewById(R.id.wind_level)).setText(wind.getSc());
                if (wind.getSc().equals("微风")) {
                    rootView.findViewById(R.id.unit).setVisibility(View.GONE);
                }
                ((TextView) rootView.findViewById(R.id.relative_humidity_value)).setText(daily_forecast.getHum());
                ((TextView) rootView.findViewById(R.id.rainfall_value)).setText(daily_forecast.getPcpn());
                ((TextView) rootView.findViewById(R.id.degree))
                        .setText(daily_forecast.getTmp().getMax() + DEGREE + "/" + daily_forecast.getTmp().getMin() + DEGREE);

                ((TextView) rootView.findViewById(R.id.rainfall_probability)).setText(getString(R.string.rainfall_probability_text) + "   " + daily_forecast.getPop());
                ((TextView) rootView.findViewById(R.id.air_pressure)).setText(getString(R.string.air_pressure_text) + "   " + now.getPres().substring(0, 3) + "kPa");
                ((TextView) rootView.findViewById(R.id.visibility)).setText(getString(R.string.visibility_text) + "   " + now.getVis() + "km");
                ((TextView) rootView.findViewById(R.id.wind_degree)).setText(getString(R.string.wind_degree_text) + "   " + wind.getDeg() + DEGREE);
                ((TextView) rootView.findViewById(R.id.wind_speed)).setText(getString(R.string.wind_speed_text) + "   " + wind.getSpd() + "km/h");
                ((ImageView) rootView.findViewById(R.id.weather_l_icon)).setImageResource(getIconId(daily_forecast.getCond().getTxt_d()));

                ((TextView) rootView.findViewById(R.id.location_weather_type)).append(" | " + cond.getTxt());
            }
        }

        private void showBasic(View rootView, Basic basic) {
            ((TextView) rootView.findViewById(R.id.location_weather_type)).setText(basic.getCity());
        }
    }
}
