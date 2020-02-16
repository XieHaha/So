package com.cn.lv.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.frame.data.CommonData;
import com.cn.frame.ui.BaseActivity;
import com.cn.frame.utils.SweetLog;
import com.cn.lv.R;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;

public class WebViewActivity extends BaseActivity {
    @BindView(R.id.layout_title_root)
    RelativeLayout layoutTitleRoot;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    /**
     * title
     */
    private String title, content;
    /**
     * 源
     */
    private String url;
    /**
     * 是否是登录协议
     */
    private boolean isProtocol;
    private boolean isError;

    @Override
    protected boolean isInitStatusBar() {
        return false;
    }

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_webview;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initWebViewSetting();
        loadViewClient();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            title = getIntent().getStringExtra(CommonData.KEY_TITLE);
            content = getIntent().getStringExtra(CommonData.KEY_PUBLIC_STRING);
            url = getIntent().getStringExtra(CommonData.KEY_PUBLIC);
            isProtocol = getIntent().getBooleanExtra(CommonData.KEY_IS_PROTOCOL, false);
        }
        publicTitleBarTitle.setText(title);
        SweetLog.i(TAG, "url:" + url);
        if (isProtocol) {
            webView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);
        } else {
            webView.loadUrl(url);
        }
    }

    private void loadViewClient() {
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
    }

    private void initWebViewSetting() {
        //指定的垂直滚动条有叠加样式
        webView.setVerticalScrollbarOverlay(true);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setAppCacheEnabled(false);
        settings.setBuiltInZoomControls(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(0);
        }
    }
}
