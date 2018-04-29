package com.example.jg.footballstats;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIController {
    private static Retrofit retrofit;
    private static PinnacleAPI pinnacleAPI;
    private static final APIController ourInstance = new APIController();

    private APIController() {

    }

    static APIController getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.pinnacle.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            pinnacleAPI = retrofit.create(PinnacleAPI.class);
        }
        return ourInstance;
    }

    public static PinnacleAPI getAPI() {
        return pinnacleAPI;
    }


}
