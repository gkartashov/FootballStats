package com.example.jg.footballstats.odds;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Home {

    @SerializedName("points")
    @Expose
    private double points;
    @SerializedName("over")
    @Expose
    private double over;
    @SerializedName("under")
    @Expose
    private double under;

    /**
     * No args constructor for use in serialization
     *
     */
    public Home() {
    }

    /**
     *
     * @param over
     * @param under
     * @param points
     */
    public Home(double points, double over, double under) {
        super();
        this.points = points;
        this.over = over;
        this.under = under;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public String getStringPoints() {
        return Double.toString(points);
    }

    public double getOver() {
        return over;
    }

    public void setOver(double over) {
        this.over = over;
    }

    public double getUnder() {
        return under;
    }

    public void setUnder(double under) {
        this.under = under;
    }

}