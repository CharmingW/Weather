package com.charming.weather.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AlertDialog;
import android.util.ArraySet;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.charming.weather.R;
import com.charming.weather.adapter.CityNameListAdapter;
import com.charming.weather.util.ApplicationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AddCityActivity extends BaseSwipeBackActivity implements View.OnClickListener {

    private CityNameListAdapter mAdapter;
    private SharedPreferences mSp;
    private SharedPreferences.Editor mEditor;
    private FloatingActionButton mAddBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        ApplicationUtil.setMiuiStatusBarDarkMode(this, true);
        init();
    }

    private void init() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_edit_layout).setOnClickListener(this);
        findViewById(R.id.btn_cancel_layout).setOnClickListener(this);
        findViewById(R.id.btn_add).setOnClickListener(this);
        ListView cityList = (ListView) findViewById(R.id.city_list);
        mAddBtn = (FloatingActionButton) findViewById(R.id.btn_add);

        //初始化sp，向Set中添加一个默认的城市
        mSp = getSharedPreferences("city", MODE_PRIVATE);
        mEditor = mSp.edit();
        Set<String> cities = initCity();
        mEditor.putStringSet("city_name", cities);

        List<String> cityNames = getCityList();
        mAdapter = new CityNameListAdapter(this, cityNames);
        mAdapter.setOnDeleteCityCallback(new CityNameListAdapter.onDeleteCityCallback() {
            @Override
            public void onDeleteCitySuccess(String cityName) {
                List<String> cityList = mAdapter.getData();
                cityList.remove(cityName);
                mAdapter.notifyDataSetChanged();
            }
        });
        cityList.setAdapter(mAdapter);
        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!mAdapter.isEditing()) {
                    SharedPreferences spf = getSharedPreferences("location_city", MODE_PRIVATE);
                    SharedPreferences.Editor  editor = spf.edit();
                    editor.putString("city", mAdapter.getData().get(position) + "市");
                    SharedPreferencesCompat.EditorCompat editorCompat = SharedPreferencesCompat.EditorCompat.getInstance();
                    editorCompat.apply(editor);
                    Intent addCity = new Intent(AddCityActivity.this, WeatherOverviewActivity.class);
                    setResult(RESULT_OK, addCity);
                    finish();
                }
            }
        });
    }

    //从SharedPreferences中获取保存的城市
    @NonNull
    private List<String> getCityList() {
        List<String> cityNames = new ArrayList<>();
        Set<String> cities = mSp.getStringSet("city_name", null);
        if (cities == null) {
            cities = initCity();
            mEditor.putStringSet("city_name", cities);
        }
        for (String city : cities) {
            cityNames.add(city);
        }
        return cityNames;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                super.onBackPressed();
                break;
            case R.id.btn_edit_layout:
                if (!mAdapter.isEditing()) {    //按下编辑按钮
                    mAdapter.setEditing(true);
                    findViewById(R.id.back_title).setVisibility(View.GONE);
                    findViewById(R.id.btn_cancel_layout).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.btn_edit)).setText(R.string.sure_text);
                    Animation disappear = new TranslateAnimation(0, 0, 0, 300);
                    disappear.setDuration(300);
                    mAddBtn.startAnimation(disappear);
                    mAddBtn.setVisibility(View.GONE);
                } else {    //按下确定按钮
                    List<String> cityList = mAdapter.getData();
                    Set<String> cities = new ArraySet<>();
                    for (String s : cityList) {
                        cities.add(s);
                    }
                    mEditor.putStringSet("city_name", cities);
                    mEditor.putInt("city_count", cities.size());
                    mEditor.apply();
                    mAdapter.setEditing(false);
                    findViewById(R.id.back_title).setVisibility(View.VISIBLE);
                    findViewById(R.id.btn_cancel_layout).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.btn_edit)).setText(R.string.edit_text);
                    Animation disappear = new TranslateAnimation(0, 0, 300, 0);
                    disappear.setDuration(300);
                    mAddBtn.startAnimation(disappear);
                    mAddBtn.setVisibility(View.VISIBLE);
                }
                //更新数据

                mAdapter.setData(getCityList());
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_cancel_layout:   //按下取消按钮
                Set<String> cities = mSp.getStringSet("city_name", null);
                if (cities == null) {
                    cities = initCity();
                }
                List<String> cityList = mAdapter.getData();
                cityList.clear();
                for (String city : cities) {
                    cityList.add(city);
                }
                mAdapter.setEditing(false);
                findViewById(R.id.back_title).setVisibility(View.VISIBLE);
                findViewById(R.id.btn_cancel_layout).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.btn_edit)).setText(R.string.edit_text);

                Animation animation = new TranslateAnimation(0, 0, 300, 0);
                animation.setDuration(300);
                mAddBtn.startAnimation(animation);
                mAddBtn.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_add:
                displayAddCityDialog();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mAdapter.isEditing()) {
            findViewById(R.id.btn_cancel_layout).callOnClick();
        } else {
            super.onBackPressed();
        }
    }

    //显示添加城市对话框
    private void displayAddCityDialog() {

        final EditText et = new EditText(this);
        et.setHint("中文名/拼音，如广州/Guangzhou");
        et.setGravity(Gravity.CENTER);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("添加城市")
                .setView(et)
                .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String cityName = et.getText().toString();

                        if ("".equals(cityName)) {
                            Toast.makeText(AddCityActivity.this, "城市名不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            //添加城市到sp，并使更新城市数量
                            Set<String> cities = mSp.getStringSet("city_name", null);
                            boolean isAdded;
                            if (cities != null) {
                                isAdded = cities.add(cityName);
                            } else {
                                cities = initCity();
                                isAdded = cities.add(cityName);
                            }
                            mEditor.putStringSet("city_name", cities);
                            if (isAdded) {
                                int count = mSp.getInt("city_count", 1);
                                mEditor.putInt("city_count", ++count);
                            }
                            mEditor.apply();

                            SharedPreferences spf = getSharedPreferences("location_city", MODE_PRIVATE);
                            SharedPreferences.Editor  editor = spf.edit();
                            editor.putString("city", cityName + "市");
                            SharedPreferencesCompat.EditorCompat editorCompat = SharedPreferencesCompat.EditorCompat.getInstance();
                            editorCompat.apply(editor);

                            Intent addCity = new Intent(AddCityActivity.this, WeatherOverviewActivity.class);
                            setResult(RESULT_OK, addCity);
                            finish();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        Window window = dialog.getWindow(); //得到对话框
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setWindowAnimations(R.style.dialogAnimation);
        dialog.show();
    }

    private Set<String> initCity() {
        Set<String> cities = new ArraySet<>();
        cities.add("广州");
        mEditor.putInt("city_count", 1);
        return cities;
    }
}
