package com.example.jg.footballstats;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventsList {

    @SerializedName("sportId")
    @Expose
    private int sportId;
    @SerializedName("last")
    @Expose
    private int last;
    @SerializedName("league")
    @Expose
    private List<League> league = new ArrayList<>();;

    public EventsList() {
    }

    public EventsList(int sportId, int last, List<League> league) {
        super();
        this.sportId = sportId;
        this.last = last;
        this.league = league;
    }

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

    public List<League> getLeague() {
        return league;
    }

    public void setLeague(List<League> league) {
        this.league = league;
    }

}