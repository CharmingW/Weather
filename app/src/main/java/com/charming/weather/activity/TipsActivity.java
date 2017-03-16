
package com.charming.weather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.charming.weather.R;
import com.charming.weather.util.ApplicationUtil;

public class TipsActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationUtil.FlymeSetStatusBarLightMode(getWindow(), true);
        setContentView(R.layout.activity_tips);
        Intent intent = getIntent();
        String title = intent.getStringExtra("index");
        String tips_title = intent.getStringExtra("title");
        String tips_content = intent.getStringExtra("content");
        int imageId = intent.getIntExtra("imageId", 0x0);
        ((TextView) findViewById(R.id.title_text)).setText(title);
        ((TextView) findViewById(R.id.tips_title)).setText(tips_title);
        ((TextView) findViewById(R.id.tips_content)).setText(tips_content);
        if (imageId != 0) {
            ((ImageView) findViewById(R.id.tips_icon)).setImageResource(imageId);
        }
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
