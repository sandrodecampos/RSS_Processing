package com.example.rssprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OtherActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private final int STANDARD_REQUEST_CODE = 222;
    TextView textPubDate, textTitle, textLink, textDescription;
    private Button btnWebView;
    public static final String EXTRA_TEXT3 = "com.example.rssprocessing.EXTRA_TEXT3";
    public static final int DEFAULT_COLOR = 0XFFD81B60;
    public static final int DEFAULT_SIZE = 14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        Intent intent = getIntent();
        String text1 = intent.getStringExtra(MainActivity.EXTRA_TEXT1);
        String text2 = intent.getStringExtra(MainActivity.EXTRA_TEXT2);
        String text3 = intent.getStringExtra(MainActivity.EXTRA_TEXT3);
        String text4 = intent.getStringExtra(MainActivity.EXTRA_TEXT4);

        textPubDate = findViewById(R.id.tvPubDate);
        textPubDate.setText(text1);
        textTitle = (TextView) findViewById(R.id.tvTitle);
        textTitle.setText(text2);
        textLink = (TextView) findViewById(R.id.tvLink);
        textLink.setText(text3);
        textDescription = findViewById(R.id.tvDescription);
        textDescription.setText(text4);

        getSupportActionBar().setTitle("Item Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("test preferences", MODE_PRIVATE);

        btnWebView = (Button) findViewById(R.id.btnWebView);
        btnWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebView();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }



    public void openWebView(){
        Intent intent = new Intent(this, WebViewActivity.class);
        Intent intent2 = getIntent();
        String text3 = intent2.getStringExtra(MainActivity.EXTRA_TEXT3);
        intent.putExtra(EXTRA_TEXT3, text3);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(OtherActivity.this, MainActivity.class);
        //...and start it to get data back from OtherActivity
        startActivityForResult(intent, STANDARD_REQUEST_CODE);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences("PREFS", 0);
        Integer color = sp.getInt("color", DEFAULT_COLOR);
        Integer size = sp.getInt("size", DEFAULT_SIZE);

        if(size == DEFAULT_SIZE) {
            textPubDate.setTextSize(size);
            textDescription.setTextSize(size);
        }else {
            textPubDate.setTextSize(size);
            textDescription.setTextSize(size);
        }

        if(color == DEFAULT_COLOR) {
            textPubDate.setTextColor(color);
        }else {
            textPubDate.setTextColor(color);
        }
    }
}
