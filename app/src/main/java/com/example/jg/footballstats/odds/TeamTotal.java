package com.example.jg.footballstats.odds;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeamTotal {

    @SerializedName("home")
    @Expose
    private Home home;
    @SerializedName("away")
    @Expose
    private Away away;

    /**
     * No args constructor for use in serialization
     *
     */
    public TeamTotal() {
    }

    /**
     *
     * @param away
     * @param home
     */
    public TeamTotal(Home home, Away away) {
        super();
        this.home = home;
        this.away = away;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public Away getAway() {
        return away;
    }

    public void setAway(Away away) {
        this.away = away;
    }

}