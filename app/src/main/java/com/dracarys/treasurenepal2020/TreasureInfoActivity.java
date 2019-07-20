package com.dracarys.treasurenepal2020;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class TreasureInfoActivity extends AppCompatActivity {

    WebView informationWebView;

    ProgressBar webViewProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure_info);

        informationWebView.setWebViewClient(new WebViewClient());    //the lines of code added
        informationWebView.setWebChromeClient(new WebChromeClient()); //same as above
        informationWebView.getSettings().setJavaScriptEnabled(true);
        informationWebView.canGoBack();

        informationWebView.loadUrl("https://www.welcomenepal.com/places-to-see/rara.html");


        webViewProgressBar.setVisibility(View.VISIBLE);


        informationWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

            }

            public void onPageFinished(WebView view, String url) {
                webViewProgressBar.setVisibility(View.GONE);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
               webViewProgressBar.setVisibility(View.GONE);
            }
        });


        //To handle Webpage back in fragment
        informationWebView.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == MotionEvent.ACTION_UP
                        && informationWebView.canGoBack()) {
                    informationWebView.goBack();
                    return true;
                }
                return false;
            }
        });
    }


}
