package com.tr0v4s.dataanalyser;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by tr0v4s on 2/18/17.
 */

public class LoginActivity extends Activity {

    private String uri = "http://bi.2logical.pt/pentaho/";
    private Button login;
    private ImageView logo;
    private EditText username, password;
    private ProgressDialog pd;
    private Intent theintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        theintent = this.getIntent();

        login = (Button) findViewById(R.id.button_go);
        username = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        logo = (ImageView) findViewById(R.id.logo);

        logo.setImageResource(R.drawable.logo);
        ValueAnimator anime = ValueAnimator.ofFloat(0f, 1f);
        anime.setDuration(2000);
        anime.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val=Float.parseFloat(animation.getAnimatedValue().toString());
                logo.setAlpha(val);
                logo.setScaleX(val);
                logo.setScaleY(val);
            }
        });
        anime.start();

        username.setNextFocusDownId(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(LoginActivity.this);
                pd.setMessage("loading");
                pd.show();
                CheckLogin cl = new CheckLogin(username.getText().toString(), password.getText().toString(), uri);
                cl.execute();
            }
        });

    }

    private class CheckLogin extends AsyncTask<Void, Void, Void> {

        private String usernameStr, passwordStr, url;
        private Boolean checkLogin = false, finishtask = false;

        public CheckLogin(String username, String password, String url){
            this.usernameStr = username;
            this.passwordStr = password;
            this.url = url;
        }

        private boolean checkAuthentication() {
            try {
                URL urldata = new URL(url);
                HttpURLConnection c = (HttpURLConnection) urldata.openConnection();
                final String basicAuth = "Basic " + Base64.encodeToString((usernameStr+":"+passwordStr).getBytes(), Base64.NO_WRAP);
                c.setRequestProperty("Authorization", basicAuth);

                CookieManager cookieManager = CookieManager.getInstance();
                String cookie = cookieManager.getCookie(urldata.getHost());
                c.setDoOutput(true);
                c.setRequestProperty("Cookie", cookie);

                c.setUseCaches(false);
                c.setRequestMethod("POST");

                c.connect();

                InputStream in = new BufferedInputStream(c.getInputStream());

                String data = new Scanner(in).useDelimiter("\\A").next();

                if (data != null) {
                    return true;
                }
                //c.getContentType();
                //c.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected Void doInBackground(Void... params) {
            checkLogin = checkAuthentication();

            finishtask = true;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(finishtask){
                pd.dismiss();
                if (checkLogin){
                    Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
                    Bundle bundle = new Bundle();
                    intent.putExtra("info", bundle);
                    bundle.putString("username", usernameStr);
                    bundle.putString("password", passwordStr);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Error Login", Toast.LENGTH_LONG).show();
                }
                username.setText("");
                password.setText("");
                finishtask = false;
            }
        }
    }
}

