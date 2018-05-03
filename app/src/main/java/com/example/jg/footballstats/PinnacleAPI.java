package com.example.jg.footballstats;

import com.example.jg.footballstats.fixtures.EventsList;
import com.example.jg.footballstats.odds.OddsList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface PinnacleAPI {
    //https://api.pinnacle.com/v1/fixtures
    @Headers("Authorization: Basic R0s5MDcyOTU6IWpvemVmMjAwMA==")
    @GET("/v1/fixtures")
    Call<EventsList> getFixtures(@Query("sportId") int sportId);
    @Headers("Authorization: Basic R0s5MDcyOTU6IWpvemVmMjAwMA==")
    @GET("/v1/fixtures")
    Call<EventsList> getFixturesSince(@Query("sportId") int sportId, @Query("since") long since);
    @Headers("Authorization: Basic R0s5MDcyOTU6IWpvemVmMjAwMA==")
    @GET("/v1/odds")
    Call<OddsList> getOdds(@Query("sportId") int sportId, @Query("leagueIds") int leagueIds, @Query("oddsFormat") String oddsFormat, @Query("eventIds") int eventIds);
}
