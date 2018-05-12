package com.example.jg.footballstats;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_LauncherTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               // if (AccountManager.get(LaunchActivity.this).getAccountsByType("com.example.jg.footballstats.auth").length == 0)
                 startActivity(new Intent(LaunchActivity.this,LoginActivity.class));
                //else
                    //startActivity(new Intent(LaunchActivity.this,MainActivity.class));
                finish();
            }
        }, 500);

    }
}
