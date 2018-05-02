package com.example.jg.footballstats.odds;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Moneyline {

    @SerializedName("home")
    @Expose
    private double home;
    @SerializedName("away")
    @Expose
    private double away;
    @SerializedName("draw")
    @Expose
    private double draw;

    /**
     * No args constructor for use in serialization
     *
     */
    public Moneyline() {
    }

    /**
     *
     * @param draw
     * @param away
     * @param home
     */
    public Moneyline(double home, double away, double draw) {
        super();
        this.home = home;
        this.away = away;
        this.draw = draw;
    }

    public double getHome() {
        return home;
    }

    public void setHome(double home) {
        this.home = home;
    }

    public double getAway() {
        return away;
    }

    public void setAway(double away) {
        this.away = away;
    }

    public double getDraw() {
        return draw;
    }

    public void setDraw(double draw) {
        this.draw = draw;
    }

}