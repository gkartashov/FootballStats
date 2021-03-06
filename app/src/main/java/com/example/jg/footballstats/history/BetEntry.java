package com.example.jg.footballstats.history;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.LocalDateTime;

import java.util.Objects;

public class BetEntry implements Parcelable {
    private int id;
    private int leagueId;
    private long eventId;
    private int betId;
    private String home;
    private String away;
    private int homeScore;
    private int awayScore;
    private int homeScoreHT;
    private int awayScoreHT;
    private BetDetails betDetails;

    public BetEntry() {
    }

    public BetEntry(int betId, int leagueId, long eventId, String home, String away, int homeScore, int awayScore, int homeScoreHT, int awayScoreHT, BetDetails betDetails) {
        this.betId = betId;
        this.leagueId = leagueId;
        this.eventId = eventId;
        this.home = home;
        this.away = away;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.homeScoreHT = homeScoreHT;
        this.awayScoreHT = awayScoreHT;
        this.betDetails = betDetails;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getHomeScore() {
        return homeScore;
    }

    public String getStringHomeScore() {
        return Integer.toString(homeScore);
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public String getStringAwayScore() {
        return Integer.toString(awayScore);
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public BetDetails getBetDetails() {
        return betDetails;
    }

    public void setBetDetails(BetDetails betDetails) {
        this.betDetails = betDetails;
    }

    public int getHomeScoreHT() {
        return homeScoreHT;
    }

    public String getStringHomeHTScore() {
        return Integer.toString(homeScoreHT);
    }

    public String getStringAwayHTScore() {
        return Integer.toString(awayScoreHT);
    }

    public void setHomeScoreHT(int homeScoreHT) {
        this.homeScoreHT = homeScoreHT;
    }

    public int getAwayScoreHT() {
        return awayScoreHT;
    }

    public void setAwayScoreHT(int awayScoreHT) {
        this.awayScoreHT = awayScoreHT;
    }

    public boolean isExpired() {
        return (betDetails.toLocalTime().toDateTime().plusHours(24).isBefore(LocalDateTime.now().toDateTime()));
    }

    public boolean isFinished() {
        return betDetails.toLocalTime().toDateTime().plusMinutes(120).isBefore(LocalDateTime.now().toDateTime()) && homeScore >= 0 && awayScore >= 0;
    }

    public boolean isFirstHalfFinished() {
        return betDetails.toLocalTime().toDateTime().plusMinutes(55).isBefore(LocalDateTime.now().toDateTime()) && homeScoreHT >= 0 && awayScoreHT >= 0;
    }

    public int getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

    public int getBetId() {
        return betId;
    }

    public void setBetId(int betId) {
        this.betId = betId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BetEntry betEntry = (BetEntry) o;
        return betId == betEntry.betId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(betId, betDetails);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.home);
        dest.writeString(this.away);
        dest.writeInt(this.homeScore);
        dest.writeInt(this.awayScore);
        dest.writeParcelable(this.betDetails, flags);
    }

    protected BetEntry(Parcel in) {
        this.id = in.readInt();
        this.home = in.readString();
        this.away = in.readString();
        this.homeScore = in.readInt();
        this.awayScore = in.readInt();
        this.betDetails = in.readParcelable(BetDetails.class.getClassLoader());
    }

    public static final Parcelable.Creator<BetEntry> CREATOR = new Parcelable.Creator<BetEntry>() {
        @Override
        public BetEntry createFromParcel(Parcel source) {
            return new BetEntry(source);
        }

        @Override
        public BetEntry[] newArray(int size) {
            return new BetEntry[size];
        }
    };
}
