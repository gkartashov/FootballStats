package com.example.jg.footballstats.odds;

import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

public class Period {

    @SerializedName("lineId")
    @Expose
    private long lineId;
    @SerializedName("number")
    @Expose
    private int number;
    @SerializedName("cutoff")
    @Expose
    private String cutoff;
    @SerializedName("maxSpread")
    @Expose
    private double maxSpread;
    @SerializedName("maxMoneyline")
    @Expose
    private double maxMoneyline;
    @SerializedName("maxTotal")
    @Expose
    private double maxTotal;
    @SerializedName("maxTeamTotal")
    @Expose
    private double maxTeamTotal;
    @SerializedName("spreads")
    @Expose
    private List<Spread> spreads = null;
    @SerializedName("moneyline")
    @Expose
    private Moneyline moneyline;
    @SerializedName("totals")
    @Expose
    private List<Total> totals = null;
    @SerializedName("teamTotal")
    @Expose
    private TeamTotal teamTotal;

    /**
     * No args constructor for use in serialization
     *
     */
    public Period() {
    }

    /**
     *
     * @param maxTotal
     * @param teamTotal
     * @param spreads
     * @param maxSpread
     * @param moneyline
     * @param number
     * @param totals
     * @param maxMoneyline
     * @param maxTeamTotal
     * @param cutoff
     * @param lineId
     */
    public Period(long lineId, int number, String cutoff, double maxSpread, double maxMoneyline, double maxTotal, double maxTeamTotal, List<Spread> spreads, Moneyline moneyline, List<Total> totals, TeamTotal teamTotal) {
        super();
        this.lineId = lineId;
        this.number = number;
        this.cutoff = cutoff;
        this.maxSpread = maxSpread;
        this.maxMoneyline = maxMoneyline;
        this.maxTotal = maxTotal;
        this.maxTeamTotal = maxTeamTotal;
        this.spreads = spreads;
        this.moneyline = moneyline;
        this.totals = totals;
        this.teamTotal = teamTotal;
    }

    public long getLineId() {
        return lineId;
    }

    public void setLineId(long lineId) {
        this.lineId = lineId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getCutoff() {
        return cutoff;
    }

    public void setCutoff(String cutoff) {
        this.cutoff = cutoff;
    }

    public double getMaxSpread() {
        return maxSpread;
    }

    public void setMaxSpread(double maxSpread) {
        this.maxSpread = maxSpread;
    }

    public double getMaxMoneyline() {
        return maxMoneyline;
    }

    public void setMaxMoneyline(double maxMoneyline) {
        this.maxMoneyline = maxMoneyline;
    }

    public double getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(double maxTotal) {
        this.maxTotal = maxTotal;
    }

    public double getMaxTeamTotal() {
        return maxTeamTotal;
    }

    public void setMaxTeamTotal(double maxTeamTotal) {
        this.maxTeamTotal = maxTeamTotal;
    }

    public List<Spread> getSpreads() {
        return spreads;
    }

    public void setSpreads(List<Spread> spreads) {
        this.spreads = spreads;
    }

    public Moneyline getMoneyline() {
        return moneyline;
    }

    public void setMoneyline(Moneyline moneyline) {
        this.moneyline = moneyline;
    }

    public List<Total> getTotals() {
        return totals;
    }

    public void setTotals(List<Total> totals) {
        this.totals = totals;
    }

    public TeamTotal getTeamTotal() {
        return teamTotal;
    }

    public void setTeamTotal(TeamTotal teamTotal) {
        this.teamTotal = teamTotal;
    }

    public DateTime toLocalTime() {
        return DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .parseDateTime(cutoff)
                .withZoneRetainFields(DateTimeZone.UTC)
                .withZone(DateTimeZone.getDefault());
    }

    public boolean isOutdated(DateTime cutoff) {
        return (toLocalTime().toDateTime().isBefore(cutoff));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Period period = (Period) o;
        return number == period.number;
    }

    @Override
    public int hashCode() {

        return Objects.hash(number);
    }
}