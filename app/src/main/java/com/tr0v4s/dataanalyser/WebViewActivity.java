package com.tr0v4s.dataanalyser;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

/**
 * Created by tr0v4s on 2/18/17.
 */

public class WebViewActivity extends Activity {

    private WebView webView;
    //private String url= "http://bi.2logical.pt/pentaho/plugin/cgg/api/services/draw?script=/public/AO/NAT_DEV/AONat_AnaliseGeral1_a2.js&outputType=png&paramwidth=1000&paramheight=400&paramperiodo_param=Mat1&paramchartp_param=Mth&parammercado_param=Merc.%20Total&parammetricas_param=KZ&paramtipo_perfil_param=NAT&paramcountry_param=AO";
    private String url= "http://bi.2logical.pt/pentaho/api/repos/%3Ahome%3Adanielsantos%3AAO%202N%20RH%20DASH.wcdf/generatedContent";
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        webView = (WebView) findViewById(R.id.theWebView);
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);

        Bundle bundle = this.getIntent().getBundleExtra("info");
        if (bundle!=null){
            String username = bundle.getString("username");
            String password = bundle.getString("password");
            // Custom WebViewClient to handle event on WebView.
            webView.setWebViewClient( new MyWebViewClient(url, username, password));
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.loadUrl(url);
        }

    }
}
