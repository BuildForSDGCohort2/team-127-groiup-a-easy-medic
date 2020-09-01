package org.builgforsdg.team127a.easymedic.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import org.builgforsdg.team127a.easymedic.R;


public class OutputActivity extends AppCompatActivity {
    private static final String TAG = "OUTPUT";
    Button btnSeachAgain;
    WebView myWebView;
    private Bundle extras;
    String category,body,ext, fileName, path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);
        myWebView = findViewById(R.id.wvBody);
        btnSeachAgain = findViewById(R.id.btnSearchAgain);

        extras = getIntent().getExtras();
        if (extras != null) {
            Log.d(TAG, "<< EXTRAS NOT NULL >> ");

            category = extras.getString("CATEGORY");
            Log.d(TAG, "category >> " + category);

            body = extras.getString("BODY");
            Log.d(TAG, "body >> " + body);

            ext = extras.getString("EXT");
            Log.d(TAG, "ext >> " + ext);

            fileName = extras.getString("NAME");
            Log.d(TAG, "fileName >> " + fileName);

            path = extras.getString("PATH");
            Log.d(TAG, "path >> " + path);

        } else {
            Log.d(TAG, "<< EXTRAS IS NULL >> ");
        }

        btnSeachAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "About to Go Back");
                Intent it = new Intent(OutputActivity.this, SearchActivity.class);
                startActivity(it);            }
        });
        myWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);

        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                android.util.Log.d("WebView", consoleMessage.message());
                return true;
            }
        });

//        myWebView.loadDataWithBaseURL("", body, "text/html", "UTF-8", "");
        myWebView.loadUrl("file:///android_asset/"+path);
    }
}
