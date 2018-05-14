package com.example.jg.footballstats.odds;

import android.os.Parcel;
import android.os.Parcelable;

public class Odd implements Parcelable {
    private int id;
    private String type;
    private String wagerType;
    private double coefficient;

    public Odd(String type, double coefficient) {
        this.type = type;
        this.coefficient = coefficient;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWagerType() {
        return wagerType;
    }

    public void setWagerType(String wagerType) {
        this.wagerType = wagerType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeDouble(this.coefficient);
    }

    protected Odd(Parcel in) {
        this.type = in.readString();
        this.coefficient = in.readDouble();
    }

    public static final Parcelable.Creator<Odd> CREATOR = new Parcelable.Creator<Odd>() {
        @Override
        public Odd createFromParcel(Parcel source) {
            return new Odd(source);
        }

        @Override
        public Odd[] newArray(int size) {
            return new Odd[size];
        }
    };
}
