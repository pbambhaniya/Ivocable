package com.multipz.dictionary.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.multipz.dictionary.R;

public class MyWordActivity extends AppCompatActivity {

    WebView webView;
    ImageView back;
    TextView title;

    String gethtml,gettitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_word);

        webView = (WebView) findViewById(R.id.webview);
        back = (ImageView) findViewById(R.id.nav_back);
        title = (TextView) findViewById(R.id.detailstitle);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        gethtml = getIntent().getStringExtra("html string");
        gettitle = getIntent().getStringExtra("title");

        webView.loadData(gethtml,"text/html", "UTF-8");
        title.setText(gettitle);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
