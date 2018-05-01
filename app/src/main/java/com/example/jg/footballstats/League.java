package com.example.jg.footballstats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class League {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("events")
    @Expose
    private List<EventEntry> events = new ArrayList<>();

    public League() {
    }

    public League(int id, String name, List<EventEntry> events) {
        super();
        this.id = id;
        this.name = name;
        this.events = events;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EventEntry> getEvents() {
        return events;
    }

    public void setEvents(List<EventEntry> events) {
        this.events = events;
    }

    public EventEntry getEvent(int index) {
        return events.get(index);
    }

    public static void sort(List<EventEntry> events) {
        if (events.size() > 0)
            Collections.sort(events, new Comparator<EventEntry>() {
                @Override
                public int compare(EventEntry o1, EventEntry o2) {
                    return o1.toLocalTime().compareTo(o2.toLocalTime());
                }
            });
    }
}