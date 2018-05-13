package com.example.jg.footballstats.history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Period {

    @SerializedName("number")
    @Expose
    private int number;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("settlementId")
    @Expose
    private int settlementId;
    @SerializedName("settledAt")
    @Expose
    private String settledAt;
    @SerializedName("team1Score")
    @Expose
    private int team1Score;
    @SerializedName("team2Score")
    @Expose
    private int team2Score;
    @SerializedName("cancellationReason")
    @Expose
    private CancellationReason cancellationReason;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(int settlementId) {
        this.settlementId = settlementId;
    }

    public String getSettledAt() {
        return settledAt;
    }

    public void setSettledAt(String settledAt) {
        this.settledAt = settledAt;
    }

    public int getTeam1Score() {
        return team1Score;
    }

    public void setTeam1Score(int team1Score) {
        this.team1Score = team1Score;
    }

    public int getTeam2Score() {
        return team2Score;
    }

    public void setTeam2Score(int team2Score) {
        this.team2Score = team2Score;
    }

    public CancellationReason getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(CancellationReason cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

}