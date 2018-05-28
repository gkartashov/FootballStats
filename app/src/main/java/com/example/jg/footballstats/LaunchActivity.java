package com.example.jg.footballstats;

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

import java.util.List;

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
        getThemeFromSharedPref();
        setTheme(Constants.IS_THEME_DARK ? R.style.AppTheme_LauncherTheme_Dark : R.style.AppTheme_LauncherTheme_Light);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        mUserLoadAsyncTask = new UserLoadAsyncTask(getSharedPreferences("UserInfo", Context.MODE_PRIVATE));
        mUserLoadAsyncTask.execute();
    }

    private void getThemeFromSharedPref() {
        SharedPreferences preferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        Constants.IS_THEME_DARK = preferences.getBoolean("isThemeDark",true);
    }

    private boolean sharedPrefToList() {
        SharedPreferences prefs = getSharedPreferences("EventsList", Context.MODE_PRIVATE);
        if (Constants.EVENTS_LIST.size() == 0) {
            if (Constants.EVENTS_LIST.size() == 0) {
                String prefString = prefs.getString("Events", null);
                List<League> tmpArray = (List<League>) ObjectSerializer.deserialize(prefString);
                if (tmpArray != null)
                    Constants.EVENTS_LIST = tmpArray;
            }
            if (Constants.EVENTS_LIST.size() > 0)
                Constants.EVENTS_LIST_SINCE = prefs.getLong("Since", Constants.EVENTS_LIST_SINCE);
        }
        else
            Constants.EVENTS_LIST_SINCE = prefs.getLong("Since",Constants.EVENTS_LIST_SINCE);
        return Constants.EVENTS_LIST.size() > 0 && Constants.EVENTS_LIST_SINCE != 0;
    }

    public class UserLoadAsyncTask extends AsyncTask<Void, Void, Void> {

        private SharedPreferences sharedPreferences;

        UserLoadAsyncTask(SharedPreferences sharedPreferences) {
            this.sharedPreferences = sharedPreferences;
        }

        @Override
        protected Void doInBackground(Void... nothing) {
                Constants.USER = (User) ObjectSerializer.deserialize(sharedPreferences.getString("User", null));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            sharedPrefToList();
            mUserLoadHandler.sendEmptyMessage(Constants.USER == null ? 0 : 1);
        }
    }
}
