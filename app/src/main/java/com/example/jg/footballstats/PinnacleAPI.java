package com.example.jg.footballstats;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface PinnacleAPI {
    //https://api.pinnacle.com/v1/fixtures
    @Headers("Authorization: Basic R0s5MDcyOTU6IWpvemVmMjAwMA==")
    @GET("/v1/fixtures")
    Call<EventsList> getFixtures(@Query("sportId") int sportId);
    @Headers("Authorization: Basic R0s5MDcyOTU6IWpvemVmMjAwMA==")
    @GET("/v1/fixtures")
    Call<EventsList> getFixturesSince(@Query("sportId") int sportId,@Query("since") long since);
}
