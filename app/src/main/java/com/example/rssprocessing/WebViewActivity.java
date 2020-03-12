package com.example.rssprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = (WebView) findViewById(R.id.webview);
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        Intent intent = getIntent();
        String text3 = intent.getStringExtra(OtherActivity.EXTRA_TEXT3);

//        String customHTML = "<html><body style=\"margin-top:100px; text-align:center\">"
//                + "<h1>Hello Sandro!</h1><input type=\"button\" value=\"Say hello\" onClick=\"showAndroidToast('Hello Sandro!')\" /></body></html>\n"
//                + "\n"
//                + "<script type=\"text/javascript\">\n"
//                + "    function showAndroidToast(toast) {\n"
//                + "        Android.showToast(toast);\n"
//                + "    }\n"
//                + "</script>";

        //String encodeHtml = Base64.encodeToString(customHTML.getBytes(), Base64.NO_PADDING);
        //webView.loadData(encodeHtml, "text/html", "base64");
        //webView.loadUrl("https://developer.android.com/guide/webapps/webview.html");
        webView.loadUrl(text3);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (webView != null){
            webView.destroy();
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
