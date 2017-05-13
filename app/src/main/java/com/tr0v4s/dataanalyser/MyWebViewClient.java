package com.tr0v4s.dataanalyser;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by tr0v4s on 2/18/17.
 */

public class MyWebViewClient extends WebViewClient {

    private String username = "";
    private String password = "";
    private String link;
    private float width, height;


    public MyWebViewClient(String url, String username, String password) {
        this.username = username;
        this.password = password;
        this.link= url;
    }

    // When you click on any interlink on webview.
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.i("MyLog","Click on any interlink on webview that time you got url :-" + url);
        link = updateResolution(url);
        return super.shouldOverrideUrlLoading(view, link);
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view,
                                          HttpAuthHandler handler, String host, String realm) {
        handler.proceed(username.toString(), password.toString());
    }

    // When page loading
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        link = updateResolution(url);
        super.onPageStarted(view, url, favicon);
        Log.i("MyLog", "Your current url when webpage loading.." + url);
    }

    // When page load finish.
    @Override
    public void onPageFinished(WebView view, String url) {
        link = updateResolution(url);
        Log.i("MyLog", "Your current url when webpage loading.. finish" + url);
        super.onPageFinished(view, url);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
    }


    private String updateResolution(String url) {
        //paramwidth=1000&paramheight=400
        if (url != null) {
            url.replace("paramwidth=1000", "paramwidth="+getScreenWidth());
            url.replace("paramheight=400", "paramheight="+getScreenHeight());
        }

        return url;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}