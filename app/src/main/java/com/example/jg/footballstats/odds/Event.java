package com.example.jg.footballstats.odds;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event implements Parcelable {

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("awayScore")
    @Expose
    private int awayScore;
    @SerializedName("homeScore")
    @Expose
    private int homeScore;
    @SerializedName("awayRedCards")
    @Expose
    private int awayRedCards;
    @SerializedName("homeRedCards")
    @Expose
    private int homeRedCards;
    @SerializedName("periods")
    @Expose
    private List<Period> periods = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Event() {
    }

    /**
     *
     * @param awayRedCards
     * @param id
     * @param periods
     * @param homeScore
     * @param homeRedCards
     * @param awayScore
     */
    public Event(long id, int awayScore, int homeScore, int awayRedCards, int homeRedCards, List<Period> periods) {
        super();
        this.id = id;
        this.awayScore = awayScore;
        this.homeScore = homeScore;
        this.awayRedCards = awayRedCards;
        this.homeRedCards = homeRedCards;
        this.periods = periods;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayRedCards() {
        return awayRedCards;
    }

    public void setAwayRedCards(int awayRedCards) {
        this.awayRedCards = awayRedCards;
    }

    public int getHomeRedCards() {
        return homeRedCards;
    }

    public void setHomeRedCards(int homeRedCards) {
        this.homeRedCards = homeRedCards;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeInt(this.awayScore);
        dest.writeInt(this.homeScore);
        dest.writeInt(this.awayRedCards);
        dest.writeInt(this.homeRedCards);
        dest.writeList(this.periods);
    }

    protected Event(Parcel in) {
        this.id = in.readLong();
        this.awayScore = in.readInt();
        this.homeScore = in.readInt();
        this.awayRedCards = in.readInt();
        this.homeRedCards = in.readInt();
        this.periods = new ArrayList<Period>();
        in.readList(this.periods, Period.class.getClassLoader());
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