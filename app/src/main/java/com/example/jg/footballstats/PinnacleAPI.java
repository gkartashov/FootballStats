package com.example.jg.footballstats;

import com.example.jg.footballstats.fixtures.EventsList;
import com.example.jg.footballstats.history.BetResult;
import com.example.jg.footballstats.odds.OddsList;

import java.util.Collection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface PinnacleAPI {
    //https://api.pinnacle.com/v1/fixtures
    @Headers("Authorization: Basic R0s5MDcyOTU6IWpvemVmMjAwMA==")
    @GET("/v1/odds")
    Call<EventsList> getFixtures(@Query("sportId") int sportId);
    @Headers("Authorization: Basic R0s5MDcyOTU6IWpvemVmMjAwMA==")
    @GET("/v1/fixtures")
    Call<EventsList> getFixturesSince(@Query("sportId") int sportId, @Query("since") long since);
    @Headers("Authorization: Basic R0s5MDcyOTU6IWpvemVmMjAwMA==")
    @GET("/v1/odds")
    Call<OddsList> getOdds(@Query("sportId") int sportId, @Query("leagueIds") int leagueIds, @Query("oddsFormat") String oddsFormat, @Query("eventIds") int eventIds);
    @Headers("Authorization: Basic R0s5MDcyOTU6IWpvemVmMjAwMA==")
    @GET("/v1/odds")
    Call<OddsList> getOddsSince(@Query("sportId") int sportId, @Query("leagueIds") int leagueIds, @Query("oddsFormat") String oddsFormat, @Query("since") long since, @Query("eventIds") int eventIds);
    @Headers("Authorization: Basic R0s5MDcyOTU6IWpvemVmMjAwMA==")
    @GET("/v1/fixtures/settled")
    Call<BetResult> getSettledFixtures(@Query("sportId") int sportId, @Query("leagueIds") Collection<Integer> leagueIds);
    @Headers("Authorization: Basic R0s5MDcyOTU6IWpvemVmMjAwMA==")
    @GET("/v1/fixtures/settled")
    Call<BetResult> getSettledFixturesSince(@Query("sportId") int sportId, @Query("leagueIds") Collection<Integer> leagueIds, @Query("since") long since);
}
