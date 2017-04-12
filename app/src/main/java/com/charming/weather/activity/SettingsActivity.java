package com.charming.weather.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.charming.weather.R;
import com.charming.weather.util.ApplicationUtil;

public class SettingsActivity extends BaseSwipeBackActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ApplicationUtil.setMiuiStatusBarDarkMode(this, true);
        init();
    }

    private void init() {
        ((TextView) findViewById(R.id.title_text)).setText(getString(R.string.settings_text));
        findViewById(R.id.btn_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                super.onBackPressed();
                break;
        }
    }
}
