package com.charming.weather.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.charming.weather.R;
import com.charming.weather.adapter.NewsListAdapter;
import com.charming.weather.comminterface.OnResponseCallback;
import com.charming.weather.parser.NewsGsonParser;
import com.charming.weather.presenter.NewsPresenter;
import com.charming.weather.util.ApplicationUtil;

import java.util.Map;

public class NewsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "NewsActivity";
    private int mWhichTips;
    private boolean isLight = true;
    private Map<String, Object> mNewsData;
    private NewsListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new NewsListAdapter(this);

        NewsPresenter presenter = NewsPresenter.getInstance(this);
        presenter.setDataParser(new NewsGsonParser());
        presenter.setOnResponseCallback(new OnResponseCallback() {
            @Override
            public void onResponseSuccess(Object response) {
                setContentView(R.layout.activity_news);
                init();
                mNewsData = (Map<String, Object>) response;
                mAdapter.setData(mNewsData);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onResponseError(Exception e) {
                setContentView(R.layout.news_error_refresh);
                findViewById(R.id.refresh).setOnClickListener(NewsActivity.this);
            }
        });
        mWhichTips = getIntent().getIntExtra("tips", 1);
        presenter.setChannelId(mWhichTips);
        presenter.startPresent();
    }

    private void init() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        Intent intent = getIntent();
        TextView titleText = (TextView) findViewById(R.id.title_text);
        titleText.setText(intent.getStringExtra("index"));
        ListView listView = (ListView) findViewById(R.id.news_list);
        //设置表首表尾
        final View header = getLayoutInflater().inflate(R.layout.activity_news_list_header, null);
        ((ImageView) header.findViewById(R.id.header_background)).setImageResource(getHeaderIconId(mWhichTips));
        ((ImageView) header.findViewById(R.id.header_icon)).setImageResource(intent.getIntExtra("imageId", R.drawable.ic_comfortable));
        ((TextView) header.findViewById(R.id.tips_title)).setText(intent.getStringExtra("title"));
        ((TextView) header.findViewById(R.id.tips_content)).setText(intent.getStringExtra("content"));
        listView.addHeaderView(header);
        listView.addFooterView(getLayoutInflater().inflate(R.layout.activity_foot_on_divider, null));

        listView.setDivider(null);

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0 && position != mAdapter.getCount() + 1) {
                    Log.i(TAG, "onItemClick: " + mAdapter.getCount());
                    Intent intent = new Intent(NewsActivity.this, NewsDetailsActivity.class);
                    startActivity(intent);
                }
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int[] location = new int[2];
                header.getLocationOnScreen(location);
                float alpha = location[1] / -300.0f;
                if (alpha <= 1f) {
                    findViewById(R.id.title_bar).setBackgroundColor(Color.argb((int) (alpha * 256), 0xea, 0xea, 0xea));
                    if (alpha >= 0.4f) {
                        if (isLight) {
                            ((ImageView) findViewById(R.id.back_icon)).setImageResource(R.drawable.ic_back);
                            ((TextView) findViewById(R.id.title_text)).setTextColor(getResources().getColor(R.color.black));
                            isLight = false;
                        }
                        header.findViewById(R.id.header_bar).setAlpha(0);
                        findViewById(R.id.title_bar_divider).setVisibility(View.VISIBLE);
                    } else if (alpha < 0.4f) {
                        float a = ((0.4f - alpha) / 0.4f);
                        header.findViewById(R.id.header_bar).setAlpha(a);
                        if (!isLight) {
                            ((ImageView) findViewById(R.id.back_icon)).setImageResource(R.drawable.ic_back_white);
                            ((TextView) findViewById(R.id.title_text)).setTextColor(getResources().getColor(android.R.color.white));
                            isLight = true;
                        }
                        findViewById(R.id.title_bar_divider).setVisibility(View.GONE);
                    }
                } else {
                    findViewById(R.id.title_bar).setBackgroundColor(Color.argb(0xff, 0xea, 0xea, 0xea));
                }

                if (location[1] < 0) {
                    ApplicationUtil.setMiuiStatusBarDarkMode(NewsActivity.this, true);
                } else {
                    ApplicationUtil.setMiuiStatusBarDarkMode(NewsActivity.this, false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                super.onBackPressed();
                break;
            case R.id.refresh:
                NewsPresenter presenter = NewsPresenter.getInstance(this);
                presenter.startPresent();
                break;
            default:
                break;
        }
    }

    private int getHeaderIconId(int i) {
        switch (i) {
            case 1:
                return R.drawable.ic_b1;
            case 2:
                return R.drawable.ic_b2;
            case 3:
                return R.drawable.ic_b3;
            case 4:
                return R.drawable.ic_b4;
            case 5:
                return R.drawable.ic_b5;
            case 6:
                return R.drawable.ic_b6;
            case 7:
                return R.drawable.ic_b7;
            default:
                return 1;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
