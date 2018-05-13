package com.example.jg.footballstats.history;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

public class BetDetails implements Parcelable {
    private int id;
    private String starts;
    private String betType;
    private String pick;
    private double coefficient;
    private String status;

    public BetDetails() {
    }

    public BetDetails(String starts, String betType, String pick, double coefficient, String status) {
        this.starts = starts;
        this.betType = betType;
        this.pick = pick;
        this.coefficient = coefficient;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStarts() {
        return starts;
    }

    public void setStarts(String date) {
        this.starts = date;
    }

    public String getBetType() {
        return betType;
    }

    public void setBetType(String betType) {
        this.betType = betType;
    }

    public String getPick() {
        return pick;
    }

    public void setPick(String pick) {
        this.pick = pick;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public String getStringCoefficient() {
        return Double.toString(coefficient);
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.starts);
        dest.writeString(this.betType);
        dest.writeString(this.pick);
        dest.writeDouble(this.coefficient);
        dest.writeString(this.status);
    }

    protected BetDetails(Parcel in) {
        this.id = in.readInt();
        this.starts = in.readString();
        this.betType = in.readString();
        this.pick = in.readString();
        this.coefficient = in.readDouble();
        this.status = in.readString();
    }

    public static final Parcelable.Creator<BetDetails> CREATOR = new Parcelable.Creator<BetDetails>() {
        @Override
        public BetDetails createFromParcel(Parcel source) {
            return new BetDetails(source);
        }

        @Override
        public BetDetails[] newArray(int size) {
            return new BetDetails[size];
        }
    };
}
