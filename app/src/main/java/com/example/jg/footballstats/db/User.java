package com.example.jg.footballstats.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.example.jg.footballstats.Constants;
import com.example.jg.footballstats.ObjectSerializer;

import java.io.IOException;
import java.io.Serializable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class User extends RequestBody implements Parcelable, Serializable {
    private String username;
    private String password;
    private String email;
    private String name;

    private static User instance;

    private User() {

    }

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    public void sharedPrefToUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        instance = (User) ObjectSerializer.deserialize(prefs.getString("User", null));
    }

    public void setUser(String username, String password, String email, String name) {
        instance.username = username;
        instance.password = password;
        instance.email = email;
        instance.name = name;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return null;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.email);
        dest.writeString(this.name);
    }

    protected User(Parcel in) {
        this.username = in.readString();
        this.password = in.readString();
        this.email = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
