package com.example.jg.footballstats.odds;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Total {

    @SerializedName("altLineId")
    @Expose
    private long altLineId;
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
    public Total() {
    }

    /**
     *
     * @param over
     * @param under
     * @param altLineId
     * @param points
     */
    public Total(long altLineId, double points, double over, double under) {
        super();
        this.altLineId = altLineId;
        this.points = points;
        this.over = over;
        this.under = under;
    }

    public long getAltLineId() {
        return altLineId;
    }

    public void setAltLineId(long altLineId) {
        this.altLineId = altLineId;
    }

    public double getPoints() {
        return points;
    }

    public String getStringPoints() {
        return Double.toString(points);
    }

    public void setPoints(double points) {
        this.points = points;
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