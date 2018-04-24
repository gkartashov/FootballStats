package com.example.jg.footballstats;

import java.util.Date;

public class Event {
    private String homeTeam;
    private String awayTeam;
    private String handicap;
    private String total;
    private byte homeScore;
    private byte awayScore;
    private double[] moneylineCoef;
    private double[] totalCoef;
    private double[] handicapCoef;

    public Event(){
        moneylineCoef = handicapCoef = totalCoef = new double[2];
    }
    public Event(String homeTeam, String awayTeam){ this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;}
    public Event(String homeTeam, String awayTeam, String handicap, String total, byte homeScore, byte awayScore, double[] moneylineCoef, double[] totalCoef, double[] handicapCoef) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.handicap = handicap;
        this.total = total;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.moneylineCoef = moneylineCoef;
        this.totalCoef = totalCoef;
        this.handicapCoef = handicapCoef;
    }
    public String getScore(){
        return "" + homeScore + " - " + awayScore;
    }
    public byte getScore(byte team){
        return team == 0?homeScore:awayScore;
    }
    public void setScore(byte team, byte score){
        switch(team){
            case 0:
                homeScore = score;
                break;
            case 1:
                awayScore = score;
                break;
            default:
                break;
        }
    }
    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public String getHandicap() {
        return handicap;
    }

    public String getTotal() {
        return total;
    }

    public double[] getMoneylineCoef() {
        return moneylineCoef;
    }
    public double getMoneylineCoef(byte team) {
        return team == 0?moneylineCoef[0]:moneylineCoef[1];
    }

    public void setMoneylineCoef(byte team, double moneylineCoef) {
        this.moneylineCoef[team] = moneylineCoef;
    }

    public void setMoneylineCoef(double[] moneylineCoef) {
        this.moneylineCoef = moneylineCoef;
    }

    public double[] getTotalCoef() {
        return totalCoef;
    }

    public double getTotalCoef(byte team) {
        return team == 0?totalCoef[0]:totalCoef[1];
    }

    public void setTotalCoef(double[] totalCoef) {
        this.totalCoef = totalCoef;
    }

    public void setTotalCoef(byte team, double totalCoef) {
        this.totalCoef[team] = totalCoef;
    }

    public double[] getHandicapCoef() {
        return handicapCoef;
    }

    public double getHandicapCoef(byte team) {
        return team == 0?handicapCoef[0]:handicapCoef[1];
    }

    public void setHandicapCoef(double[] handicapCoef) {
        this.handicapCoef = handicapCoef;
    }

    public void setHandicapCoef(byte team, double handicapCoef) {
        this.handicapCoef[team] = handicapCoef;
    }
}
