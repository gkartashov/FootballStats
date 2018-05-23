package com.example.jg.footballstats;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jg.footballstats.db.User;
import com.example.jg.footballstats.fixtures.League;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class LaunchActivity extends AppCompatActivity {

    private class UserLoadHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    this.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(LaunchActivity.this,LoginActivity.class));
                            finish();
                        }
                    },300);
                    break;
                case 1:
                    startActivity(new Intent(LaunchActivity.this,MainActivity.class));
                    finish();
                    break;
                default:
                    break;
            }
        }
    }

    private UserLoadHandler mUserLoadHandler = new UserLoadHandler();
    private UserLoadAsyncTask mUserLoadAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_LauncherTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        mUserLoadAsyncTask = new UserLoadAsyncTask();
        mUserLoadAsyncTask.execute(getSharedPreferences("UserInfo", Context.MODE_PRIVATE));
    }

    public class UserLoadAsyncTask extends AsyncTask<SharedPreferences, Void, Void> {

        @Override
        protected Void doInBackground(SharedPreferences... sharedPreferences) {
            for (SharedPreferences sh : sharedPreferences) {
                Constants.USER = (User) ObjectSerializer.deserialize(sh.getString("User", null));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mUserLoadHandler.sendEmptyMessage(Constants.USER == null ? 0 : 1);
        }
    }
}
