package com.example.jg.footballstats;

import com.example.jg.footballstats.db.User;

import retrofit2.Call;
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
}
