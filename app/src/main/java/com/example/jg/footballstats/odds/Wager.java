package com.example.jg.footballstats.odds;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Wager implements Parcelable {
    private int id;
    private List<Odd> oddList;
    private String title;

    public Wager(String title, List<Odd> items) {
        this.title = title;
        oddList = items;
    }

    public List<Odd> getOddList() {
        return oddList;
    }

    public void setOddList(List<Odd> oddList) {
        this.oddList = oddList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addOdd(Odd odd) {
        oddList.add(odd);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.oddList);
        dest.writeString(this.title);
    }

    protected Wager(Parcel in) {
        this.oddList = in.createTypedArrayList(Odd.CREATOR);
        this.title = in.readString();
    }

    public static final Parcelable.Creator<Wager> CREATOR = new Parcelable.Creator<Wager>() {
        @Override
        public Wager createFromParcel(Parcel source) {
            return new Wager(source);
        }

        @Override
        public Wager[] newArray(int size) {
            return new Wager[size];
        }
    };
}
