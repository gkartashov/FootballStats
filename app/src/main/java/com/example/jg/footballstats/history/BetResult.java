package com.example.jg.footballstats.history;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BetResult {

    @SerializedName("sportId")
    @Expose
    private int sportId;
    @SerializedName("last")
    @Expose
    private int last;
    @SerializedName("leagues")
    @Expose
    private List<League> leagues = null;

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public List<League> getLeagues() {
        return leagues;
    }

    public void setLeagues(List<League> leagues) {
        this.leagues = leagues;
    }

}