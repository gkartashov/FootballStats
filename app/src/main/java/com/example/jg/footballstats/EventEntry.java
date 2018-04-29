package com.example.jg.footballstats;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventEntry {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("parentId")
    @Expose
    private int parentId;
    @SerializedName("starts")
    @Expose
    private String starts;
    @SerializedName("home")
    @Expose
    private String home;
    @SerializedName("away")
    @Expose
    private String away;
    @SerializedName("rotNum")
    @Expose
    private String rotNum;
    @SerializedName("liveStatus")
    @Expose
    private int liveStatus;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("parlayRestriction")
    @Expose
    private int parlayRestriction;
    @SerializedName("altTeaser")
    @Expose
    private boolean altTeaser;

    public EventEntry() {
    }

    public EventEntry(int id, int parentId, String starts, String home, String away, String rotNum, int liveStatus, String status, int parlayRestriction, boolean altTeaser) {
        super();
        this.id = id;
        this.parentId = parentId;
        this.starts = starts;
        this.home = home;
        this.away = away;
        this.rotNum = rotNum;
        this.liveStatus = liveStatus;
        this.status = status;
        this.parlayRestriction = parlayRestriction;
        this.altTeaser = altTeaser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getStarts() {
        return starts;
    }

    public void setStarts(String starts) {
        this.starts = starts;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getAway() {
        return away;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public String getRotNum() {
        return rotNum;
    }

    public void setRotNum(String rotNum) {
        this.rotNum = rotNum;
    }

    public int getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(int liveStatus) {
        this.liveStatus = liveStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getParlayRestriction() {
        return parlayRestriction;
    }

    public void setParlayRestriction(int parlayRestriction) {
        this.parlayRestriction = parlayRestriction;
    }

    public boolean isAltTeaser() {
        return altTeaser;
    }

    public void setAltTeaser(boolean altTeaser) {
        this.altTeaser = altTeaser;
    }

}