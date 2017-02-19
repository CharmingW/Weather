package com.charming.weather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.charming.weather.R;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what > 0) {
                ((TextView) findViewById(R.id.welcome_image)).setText("" + what);
                mHandler.sendEmptyMessage(what - 1);
            } else {
                Intent intent = new Intent(WelcomeActivity.this, WeatherOverviewActivity.class);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                for (int i = 3; i > 0; i--) {
                    mHandler.sendEmptyMessage(i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
