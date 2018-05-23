package com.example.jg.footballstats.db;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

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
    private int win;
    private int loss;
    private int total_events;

    public User() {

    }

    public User(String username, String password, String email, String name, int win, int loss, int total_events) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.win = win;
        this.loss = loss;
        this.total_events = total_events;
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

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getLoss() {
        return loss;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }

    public int getTotal_events() {
        return total_events;
    }

    public void setTotal_events(int total_events) {
        this.total_events = total_events;
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
