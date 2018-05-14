package com.example.jg.footballstats.db;

public class Bet {
    private int betId;
    private User user;
    private Event event;
    private String betType;
    private String betName;
    private String pick;
    private double coefficient;
    private int status;

    public Bet() {
    }

    public Bet(User user, Event event, String betType, String betName, String pick, double coefficient, int status) {
        this.user = user;
        this.event = event;
        this.betType = betType;
        this.betName = betName;
        this.pick = pick;
        this.coefficient = coefficient;
        this.status = status;
    }

    public int getBetId() {
        return betId;
    }

    public void setBetId(int betId) {
        this.betId = betId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getBetType() {
        return betType;
    }

    public void setBetType(String betType) {
        this.betType = betType;
    }

    public String getBetName() {
        return betName;
    }

    public void setBetName(String betName) {
        this.betName = betName;
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

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
