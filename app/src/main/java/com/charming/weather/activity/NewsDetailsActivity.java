package com.charming.weather.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.charming.weather.R;
import com.charming.weather.util.ApplicationUtil;

public class NewsDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String URL = "https://www.sina.cn";

    private WebView mNewsWebpage;
    private PopupMenu mPopupMenu;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        init();
    }

    private void init() {

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mNewsWebpage = (WebView) findViewById(R.id.news_webpage);
        mNewsWebpage.getSettings().setJavaScriptEnabled(true);
        mNewsWebpage.getSettings().setSupportZoom(true);
        mNewsWebpage.getSettings().setUseWideViewPort(true);
        mNewsWebpage.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mNewsWebpage.getSettings().setLoadWithOverviewMode(true);
        mNewsWebpage.getSettings().setAppCacheEnabled(true);

        mNewsWebpage.setWebViewClient(new WebViewClient());

        mNewsWebpage.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    mProgressBar.setProgress(newProgress);//设置进度值
                }

            }
        });
        mNewsWebpage.loadUrl(URL);
        findViewById(R.id.btn_news_details_back).setOnClickListener(this);
        findViewById(R.id.btn_news_details_menu).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_news_details_back:
                onBackPressed();
                break;
            case R.id.btn_news_details_menu:
                if (mPopupMenu == null) {
                    mPopupMenu = new PopupMenu(this, v, Gravity.TOP);
                    mPopupMenu.inflate(R.menu.news_details);
                    mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.share:
                                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                    shareIntent.putExtra(Intent.EXTRA_TEXT, URL);
                                    shareIntent.setType("text/plain");
                                    startActivity(Intent.createChooser(shareIntent, "分享"));
                                    break;
                                case R.id.open_in_browser:
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                                    browserIntent.setData(Uri.parse(URL));
                                    startActivity(browserIntent);
                                    break;
                            }
                            return false;
                        }
                    });
                }
                mPopupMenu.show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationUtil.setMiuiStatusBarDarkMode(this, true);
    }

    @Override
    public void onBackPressed() {
        if (mNewsWebpage.canGoBack()) {
            mNewsWebpage.goBack();
        } else {
            super.onBackPressed();
        }
    }


}
