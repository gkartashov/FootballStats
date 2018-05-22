package com.example.jg.footballstats.fixtures;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.Serializable;
import java.util.Objects;

public class EventEntry implements Parcelable, Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    private int listId;
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

    private int leagueId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventEntry that = (EventEntry) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getIntegerId() {
        return id;
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

    public int getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public DateTime toLocalTime() {
        return DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .parseDateTime(starts)
                .withZoneRetainFields(DateTimeZone.UTC)
                .withZone(DateTimeZone.getDefault());
    }
    public String getDate() {
        DateTime dateTime = toLocalTime().toDateTime();
        return (dateTime.getDayOfYear() == LocalDate.now().getDayOfYear())?
                "Today":
                dateTime.toString("yyyy-MM-dd");
    }

    public String getTime() {
        return toLocalTime().toDateTime().toString("HH:mm");
    }

    public String getStartDateTime() {
        return getDate() + " " + getTime();
    }

    public boolean isStarted() {
        return (toLocalTime().toDateTime().isBefore(LocalDateTime.now().toDateTime()));
    }

    public boolean isFinished() {
        return (toLocalTime().toDateTime().plusMinutes(130).isBefore(LocalDateTime.now().toDateTime()));
    }

    public boolean isLive() {
        return isStarted() && ! isFinished();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.parentId);
        dest.writeString(this.starts);
        dest.writeString(this.home);
        dest.writeString(this.away);
        dest.writeString(this.rotNum);
        dest.writeInt(this.liveStatus);
        dest.writeString(this.status);
        dest.writeInt(this.parlayRestriction);
        dest.writeByte(this.altTeaser ? (byte) 1 : (byte) 0);
        dest.writeInt(this.leagueId);
    }

    protected EventEntry(Parcel in) {
        this.id = in.readInt();
        this.parentId = in.readInt();
        this.starts = in.readString();
        this.home = in.readString();
        this.away = in.readString();
        this.rotNum = in.readString();
        this.liveStatus = in.readInt();
        this.status = in.readString();
        this.parlayRestriction = in.readInt();
        this.altTeaser = in.readByte() != 0;
        this.leagueId = in.readInt();
    }

    public static final Parcelable.Creator<EventEntry> CREATOR = new Parcelable.Creator<EventEntry>() {
        @Override
        public EventEntry createFromParcel(Parcel source) {
            return new EventEntry(source);
        }

        @Override
        public EventEntry[] newArray(int size) {
            return new EventEntry[size];
        }
    };
}