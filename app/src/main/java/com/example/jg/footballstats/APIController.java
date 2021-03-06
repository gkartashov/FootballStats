package com.example.jg.footballstats;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIController {
    private static Retrofit retrofit;
    private static PinnacleAPI pinnacleAPI;
    private static DatabaseAPI databaseAPI;
    private static String url = "https://api.pinnacle.com";
    private static final APIController ourInstance = new APIController();

    private APIController() {

    }

    static APIController getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            pinnacleAPI = retrofit.create(PinnacleAPI.class);
        }
        return ourInstance;
    }

    public PinnacleAPI getAPI() {
        return pinnacleAPI;
    }
}
