package com.cn.lv.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
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
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                if (s.contains("alipays:") || s.contains("alipay")) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    } catch (Exception e) {
                        new AlertDialog.Builder(WebViewActivity.this)
                                .setMessage("未检测到支付宝客户端，请安装后重试。")
                                .setPositiveButton("立即安装", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Uri alipayUrl = Uri.parse("https://d.alipay.com");
                                        startActivity(new Intent("android.intent.action.VIEW",
                                                alipayUrl));
                                    }
                                }).setNegativeButton("取消", null).show();
                    }
                    return true;
                }
                if (!(s.startsWith("http") || s.startsWith("https"))) {
                    return true;
                }
                webView.loadUrl(s);
                return true;
            }

        };
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(new WebChromeClient());
    }

    private void initWebViewSetting() {
        //指定的垂直滚动条有叠加样式
        webView.setVerticalScrollbarOverlay(true);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setJavaScriptEnabled(true);
        //设置页面默认缩放密度
        settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        //设置默认的文本编码名称，以便在解码html页面时使用
        settings.setDefaultTextEncodingName("UTF-8");
        //启动或禁用WebView内的内容URL访问
        settings.setAllowContentAccess(true);
        //设置是否应该启用应用程序缓存api
        settings.setAppCacheEnabled(false);
        //设置WebView是否应该使用其内置的缩放机制
        settings.setBuiltInZoomControls(false);
        //设置WebView是否应该支持viewport
        settings.setUseWideViewPort(true);
        //不管WebView是否在概述模式中载入页面，将内容放大适合屏幕宽度
        settings.setLoadWithOverviewMode(true);
        //重写缓存的使用方式
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //告知js自动打开窗口
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //设置WebView是否应该载入图像资源
        settings.setLoadsImagesAutomatically(true);
        //启用或禁用WebView内的文件访问
        settings.setAllowFileAccess(true);
        settings.setDomStorageEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(0);
        }
    }

    /**
     * 点击回退按钮不是退出应用程序，而是返回上一个页面
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
