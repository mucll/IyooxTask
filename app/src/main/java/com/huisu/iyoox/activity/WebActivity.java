package com.huisu.iyoox.activity;

import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.util.LogUtil;
import com.huisu.iyoox.views.Loading;

/**
 * Function: 加载网页
 * Date: 2016-08-20
 *
 * @author zhaojin
 * @since JDK 1.7
 */
public class WebActivity extends BaseActivity {
    private Loading loading;
    private WebView webView;
    private String url;
    private String title;
    private LinearLayout contentView;

    public static void start(Context context, String title, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        webView = findViewById(R.id.web_view);
        contentView = findViewById(R.id.web_activity_content_view);
        loading = Loading.show(loading, this, "正在加载...");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);// 设置适应Html5  重点是这个设置
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                LogUtil.e(url);
                return false;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (loading != null) {
                    try {
                        loading.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        webView.loadUrl(url);
    }

    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }
        setBack();
    }

    @Override
    protected void setEvent() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_web;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.removeAllViews();
            webView.destroy();
        }
    }

}
