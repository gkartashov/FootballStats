package com.example.jg.footballstats.odds;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OddsList {

    @SerializedName("sportId")
    @Expose
    private int sportId;
    @SerializedName("last")
    @Expose
    private long last;
    @SerializedName("leagues")
    @Expose
    private List<League> leagues = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public OddsList() {
    }

    /**
     *
     * @param sportId
     * @param leagues
     * @param last
     */
    public OddsList(int sportId, long last, List<League> leagues) {
        super();
        this.sportId = sportId;
        this.last = last;
        this.leagues = leagues;
    }

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public long getLast() {
        return last;
    }

    public void setLast(long last) {
        this.last = last;
    }

    public List<League> getLeagues() {
        return leagues;
    }

    public void setLeagues(List<League> leagues) {
        this.leagues = leagues;
    }

}