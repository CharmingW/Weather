/**
 * Generated by smali2java 1.0.0.558
 * Copyright (C) 2013 Hensence.com
 */

package com.charming.weather.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.charming.weather.R;
import com.charming.weather.util.ApplicationUtil;

public class NewsDetailsActivity extends BaseSwipeBackActivity implements View.OnClickListener {
    private static final String TAG = "NewsDetailsActivity";
    private PopupMenu mPopupMenu;
    private ProgressBar mProgressBar;
    private String mUrl;
    private WebView mWebView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationUtil.setStrictMode();
        setContentView(R.layout.activity_news_details);
        init();
    }

    private void init() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_news_details_menu).setOnClickListener(this);
        mUrl = getIntent().getData().toString();
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mWebView = (WebView) findViewById(R.id.news_webpage);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.setWebViewClient(new WebViewClient() {

            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                View errorView = findViewById(R.id.error_refresh);
                errorView.setOnClickListener(NewsDetailsActivity.this);
                errorView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });
        mWebView.loadUrl(mUrl);
        startProgressBarAnimation();
    }

    private void startProgressBarAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 900);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                mProgressBar.setProgress(progress);
            }
        });
        valueAnimator.start();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                onBackPressed();
                return;
            }
            case R.id.btn_news_details_menu: {
                if (mPopupMenu == null) {
                    mPopupMenu = new PopupMenu(this, v, 0x30);
                    mPopupMenu.inflate(R.menu.news_details);
                    mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.share: {
                                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                    shareIntent.putExtra(Intent.EXTRA_TEXT, mUrl);
                                    shareIntent.setType("text/plain");
                                    startActivity(Intent.createChooser(shareIntent, "\u5206\u4eab"));
                                    break;
                                }
                                case R.id.open_in_browser: {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                                    browserIntent.setData(Uri.parse(mUrl));
                                    startActivity(browserIntent);
                                    break;
                                }
                            }
                            return false;
                        }
                    });
                }
                mPopupMenu.show();
                return;
            }
            case R.id.error_refresh: {
                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                startProgressBarAnimation();
                mWebView.loadUrl(mUrl);
                break;
            }
        }
    }

    protected void onResume() {
        super.onResume();
        ApplicationUtil.setMiuiStatusBarDarkMode(this, true);
    }

    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }
        super.onBackPressed();
    }
}
