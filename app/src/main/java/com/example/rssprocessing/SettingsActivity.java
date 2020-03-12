package com.example.rssprocessing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat switch_1, switch_2, switch_3;
    private Boolean stateSwitch1, stateSwitch2, stateSwitch3;
    private SharedPreferences preferences;
    private final String PREF_FILE = "PREFS";
    private TextView tvPubDate2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();
        String text1 = intent.getStringExtra(MainActivity.EXTRA_TEXT1);

        preferences = getSharedPreferences(PREF_FILE, 0);
        stateSwitch1 = preferences.getBoolean("switch1", false);
        stateSwitch2 = preferences.getBoolean("switch2", false);
        stateSwitch3 = preferences.getBoolean("switch3", false);

        switch_1 = findViewById(R.id.switch_1);
        switch_2 = findViewById(R.id.switch_2);
        switch_3 = findViewById(R.id.switch_3);

        switch_1.setChecked(stateSwitch1);
        switch_2.setChecked(stateSwitch2);
        switch_3.setChecked(stateSwitch3);

        switch_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateSwitch1 = !stateSwitch1;
                switch_1.setChecked(stateSwitch1);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("switch1", stateSwitch1);
                Toast.makeText(SettingsActivity.this, "Changed Switch1", Toast.LENGTH_SHORT).show();
                editor.apply();
            }
        });

        switch_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateSwitch2 = !stateSwitch2;
                switch_2.setChecked(stateSwitch2);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("switch2", stateSwitch2);
                Toast.makeText(SettingsActivity.this, "Changed Switch2", Toast.LENGTH_SHORT).show();
                editor.apply();
                if ( stateSwitch2 == false){
                    editor.putInt("size", 10);
                    Toast.makeText(SettingsActivity.this, "Switch2 Off", Toast.LENGTH_SHORT).show();
                    //editor.apply();
                    editor.commit();
                }else{
                    editor.putInt("size", 20);
                    Toast.makeText(SettingsActivity.this, "Switch2 On", Toast.LENGTH_SHORT).show();
                    //editor.apply();
                    editor.commit();
                }
            }
        });

        switch_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateSwitch3 = !stateSwitch3;
                switch_3.setChecked(stateSwitch3);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("switch3", stateSwitch3);
                //editor.apply();
                if ( stateSwitch3 == false){
                    editor.putInt("color", 0XFFD81B60);
                    Toast.makeText(SettingsActivity.this, "Switch3 Off", Toast.LENGTH_SHORT).show();
                    //editor.apply();
                    editor.commit();
                }else{
                    editor.putInt("color", 0xfff67f21);
                    Toast.makeText(SettingsActivity.this, "Switch3 On", Toast.LENGTH_SHORT).show();
                    //editor.apply();
                    editor.commit();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("SC", "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("SC", "onResume()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("SC", "onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("SC", "onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("SC", "onDestroy()");
    }
}
