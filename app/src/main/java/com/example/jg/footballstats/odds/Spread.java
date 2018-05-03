package com.example.jg.footballstats.odds;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Spread {

    @SerializedName("altLineId")
    @Expose
    private long altLineId;
    @SerializedName("hdp")
    @Expose
    private double hdp;
    @SerializedName("home")
    @Expose
    private double home;
    @SerializedName("away")
    @Expose
    private double away;

    /**
     * No args constructor for use in serialization
     *
     */
    public Spread() {
    }

    /**
     *
     * @param away
     * @param home
     * @param hdp
     * @param altLineId
     */
    public Spread(long altLineId, double hdp, double home, double away) {
        super();
        this.altLineId = altLineId;
        this.hdp = hdp;
        this.home = home;
        this.away = away;
    }

    public long getAltLineId() {
        return altLineId;
    }

    public void setAltLineId(long altLineId) {
        this.altLineId = altLineId;
    }

    public double getHdp() {
        return hdp;
    }

    public String getStringHdp() {
        return Double.toString(hdp);
    }

    public void setHdp(double hdp) {
        this.hdp = hdp;
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

}