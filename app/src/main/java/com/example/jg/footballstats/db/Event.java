package com.example.jg.footballstats.db;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {
    private long eventId;
    private int leagueId;
    private String starts;
    private String home;
    private String away;
    private int homeScore;
    private int awayScore;

    public Event() {
    }

    public Event(long eventId, int leagueId, String starts, String home, String away, int homeScore, int awayScore) {
        this.eventId = eventId;
        this.leagueId = leagueId;
        this.starts = starts;
        this.home = home;
        this.away = away;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public int getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

    public String getStarts() {
        return starts;
    }

    public void setStarts(String starts) {
        this.starts = starts;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public String getAway() {
        return away;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.eventId);
        dest.writeInt(this.leagueId);
        dest.writeString(this.starts);
        dest.writeInt(this.homeScore);
        dest.writeInt(this.awayScore);
    }

    protected Event(Parcel in) {
        this.eventId = in.readLong();
        this.leagueId = in.readInt();
        this.starts = in.readString();
        this.homeScore = in.readInt();
        this.awayScore = in.readInt();
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
