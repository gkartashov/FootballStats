package com.example.jg.footballstats;

import com.example.jg.footballstats.db.Bet;
import com.example.jg.footballstats.db.User;

import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DatabaseAPI {
    @GET("/footballbets/auth")
    Call<User> login(@Query("username") String username, @Query("password") String password);
    @FormUrlEncoded
    @POST("/footballbets/auth")
    Call<User> register(@Field("username") String username, @Field("password") String password, @Field("email") String email, @Field("name") String name);
    @POST("/footballbets/bet_accept")
    Call<ResponseBody> placeBet(@Body() Bet bet);
    @GET("/footballbets/bet_history")
    Call<List<Bet>> getUserBetHistory(@Query("username") String username);
    @POST("/footballbets/bet_history")
    Call<ResponseBody> updateDb(@Body() List<Bet> betList);
}
