package com.example.jg.footballstats.odds;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class League {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("events")
    @Expose
    private List<Event> events = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public League() {
    }

    /**
     *
     * @param id
     * @param events
     */
    public League(int id, List<Event> events) {
        super();
        this.id = id;
        this.events = events;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

}