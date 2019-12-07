package com.ming.weidushop.activity;

import android.webkit.WebView;

import com.abner.ming.base.BaseAppCompatActivity;
import com.abner.ming.base.utils.Utils;
import com.ming.weidushop.R;

/**
 * author:AbnerMing
 * date:2019/9/12
 * webView展示
 */
public class WebViewActivity extends BaseAppCompatActivity {
    private String mWebUrl,mWebTitle;
    private WebView mWebView;

    @Override
    protected void initData() {
        setShowTitle(false);
        setTitle(mWebTitle);
        isShowBack(true);
        setWindowTitleBlack(true);
        Utils.setttingWebView(mWebView);
        mWebView.loadUrl(mWebUrl);
    }

    @Override
    protected void initView() {
        mWebUrl = getIntent().getStringExtra("web_url");
        mWebTitle = getIntent().getStringExtra("web_title");
        mWebView = (WebView) get(R.id.webview);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }
}
