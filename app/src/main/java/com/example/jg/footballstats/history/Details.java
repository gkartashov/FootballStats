package com.example.jg.footballstats.history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Details {

    @SerializedName("correctTeam1Id")
    @Expose
    private String correctTeam1Id;
    @SerializedName("correctTeam2Id")
    @Expose
    private String correctTeam2Id;
    @SerializedName("correctListedPitcher1")
    @Expose
    private String correctListedPitcher1;
    @SerializedName("correctListedPitcher2")
    @Expose
    private String correctListedPitcher2;
    @SerializedName("correctSpread")
    @Expose
    private String correctSpread;
    @SerializedName("correctTotalPoints")
    @Expose
    private String correctTotalPoints;
    @SerializedName("correctTeam1TotalPoints")
    @Expose
    private String correctTeam1TotalPoints;
    @SerializedName("correctTeam2TotalPoints")
    @Expose
    private String correctTeam2TotalPoints;
    @SerializedName("correctTeam1Score")
    @Expose
    private String correctTeam1Score;
    @SerializedName("correctTeam2Score")
    @Expose
    private String correctTeam2Score;
    @SerializedName("correctTeam1TennisSetsScore")
    @Expose
    private String correctTeam1TennisSetsScore;
    @SerializedName("correctTeam2TennisSetsScore")
    @Expose
    private String correctTeam2TennisSetsScore;

    public String getCorrectTeam1Id() {
        return correctTeam1Id;
    }

    public void setCorrectTeam1Id(String correctTeam1Id) {
        this.correctTeam1Id = correctTeam1Id;
    }

    public String getCorrectTeam2Id() {
        return correctTeam2Id;
    }

    public void setCorrectTeam2Id(String correctTeam2Id) {
        this.correctTeam2Id = correctTeam2Id;
    }

    public String getCorrectListedPitcher1() {
        return correctListedPitcher1;
    }

    public void setCorrectListedPitcher1(String correctListedPitcher1) {
        this.correctListedPitcher1 = correctListedPitcher1;
    }

    public String getCorrectListedPitcher2() {
        return correctListedPitcher2;
    }

    public void setCorrectListedPitcher2(String correctListedPitcher2) {
        this.correctListedPitcher2 = correctListedPitcher2;
    }

    public String getCorrectSpread() {
        return correctSpread;
    }

    public void setCorrectSpread(String correctSpread) {
        this.correctSpread = correctSpread;
    }

    public String getCorrectTotalPoints() {
        return correctTotalPoints;
    }

    public void setCorrectTotalPoints(String correctTotalPoints) {
        this.correctTotalPoints = correctTotalPoints;
    }

    public String getCorrectTeam1TotalPoints() {
        return correctTeam1TotalPoints;
    }

    public void setCorrectTeam1TotalPoints(String correctTeam1TotalPoints) {
        this.correctTeam1TotalPoints = correctTeam1TotalPoints;
    }

    public String getCorrectTeam2TotalPoints() {
        return correctTeam2TotalPoints;
    }

    public void setCorrectTeam2TotalPoints(String correctTeam2TotalPoints) {
        this.correctTeam2TotalPoints = correctTeam2TotalPoints;
    }

    public String getCorrectTeam1Score() {
        return correctTeam1Score;
    }

    public void setCorrectTeam1Score(String correctTeam1Score) {
        this.correctTeam1Score = correctTeam1Score;
    }

    public String getCorrectTeam2Score() {
        return correctTeam2Score;
    }

    public void setCorrectTeam2Score(String correctTeam2Score) {
        this.correctTeam2Score = correctTeam2Score;
    }

    public String getCorrectTeam1TennisSetsScore() {
        return correctTeam1TennisSetsScore;
    }

    public void setCorrectTeam1TennisSetsScore(String correctTeam1TennisSetsScore) {
        this.correctTeam1TennisSetsScore = correctTeam1TennisSetsScore;
    }

    public String getCorrectTeam2TennisSetsScore() {
        return correctTeam2TennisSetsScore;
    }

    public void setCorrectTeam2TennisSetsScore(String correctTeam2TennisSetsScore) {
        this.correctTeam2TennisSetsScore = correctTeam2TennisSetsScore;
    }

}