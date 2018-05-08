package com.example.jg.footballstats;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DatabaseAPIController {
    private static Retrofit retrofit;
    private static DatabaseAPI databaseAPI;
    private static String url = "http://192.168.0.10:3000";
    private static final DatabaseAPIController ourInstance = new DatabaseAPIController();

    private DatabaseAPIController() {

    }

    static DatabaseAPIController getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            databaseAPI = retrofit.create(DatabaseAPI.class);
        }
        return ourInstance;
    }

    public DatabaseAPI getAPI() {
        return databaseAPI;
    }
}
