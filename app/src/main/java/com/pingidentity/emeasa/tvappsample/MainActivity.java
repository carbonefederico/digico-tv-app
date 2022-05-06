package com.pingidentity.emeasa.tvappsample;

import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * Main Activity class that loads {@link MainFragment}.
 */
public class MainActivity extends FragmentActivity {


    FrameLayout startLayout, loginLayout, userLayout, contentLayout;
    String givenName = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startLayout = (FrameLayout ) findViewById(R.id.startFrame);
        loginLayout = (FrameLayout ) findViewById(R.id.loginFrame);
        contentLayout = (FrameLayout ) findViewById(R.id.contentFrame);
        userLayout = (FrameLayout ) findViewById(R.id.userFrame);
       Button startButton = (Button) findViewById(R.id.startButton);
       startButton.setOnClickListener(new View.OnClickListener() {
           
           @Override
           public void onClick(View v) {
               startLayout.setVisibility(View.GONE);
               loadWebView();
               loginLayout.setVisibility(View.VISIBLE);
           }
       });
        Button logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startLayout.setVisibility(View.VISIBLE);
                loginLayout.setVisibility(View.GONE);
                contentLayout.setVisibility(View.GONE);
                userLayout.setVisibility(View.GONE);
                givenName = null;
            }
        });
    }

    private void loadWebView() {
        WebView loginWebView = (WebView) findViewById(R.id.loginWebView);
        loginWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // Ignore SSL certificate errors
            }
        });
        WebSettings webSettings = loginWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        loginWebView.addJavascriptInterface(this, "Android");
        loginWebView.loadUrl("https://digico-web-portal.glitch.me/tvlogin.html");
    }

    @JavascriptInterface
    public void handleLoginResponse(String payload) throws JSONException {
        Log.i("Main", payload);
        givenName = payload;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logInUser(givenName);
            }
        });
    }

    private void logInUser(String email) {
        TextView emailText = (TextView) findViewById(R.id.emailText);
        emailText.setText(email);
        loginLayout.setVisibility(View.GONE);
        startLayout.setVisibility(View.GONE);
        userLayout.setVisibility(View.VISIBLE);
        contentLayout.setVisibility(View.VISIBLE);
    }
}