package com.example.jg.footballstats.fixtures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class League implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    private int listId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("events")
    @Expose
    private List<EventEntry> events = new ArrayList<>();
    private boolean isLive = false;

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

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public void addEvent(EventEntry eventEntry) {
        events.add(eventEntry);
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        League league = (League) o;
        return id == league.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}