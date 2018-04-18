package com.example.jg.footballstats;

public class Event {
    private String _homeTeam;
    private String _awayTeam;
    private String _handicap;
    private String _total;
    private byte _homeScore;
    private byte _awayScore;
    private double[] _moneylineCoef;
    private double[] _totalCoef;
    private double[] _handicapCoef;

    public Event(){
        _moneylineCoef = _handicapCoef = _totalCoef = new double[2];
    }
    public Event(String homeTeam, String awayTeam, String handicap, String total, byte homeScore, byte awayScore, double[] moneylineCoef, double[] totalCoef, double[] handicapCoef) {
        _homeTeam = homeTeam;
        _awayTeam = awayTeam;
        _handicap = handicap;
        _total = total;
        _homeScore = homeScore;
        _awayScore = awayScore;
        _moneylineCoef = moneylineCoef;
        _totalCoef = totalCoef;
        _handicapCoef = handicapCoef;
    }
    public String getScore(){
        return "" + _homeScore + " - " + _awayScore;
    }
    public byte getScore(byte team){
        return team == 0?_homeScore:_awayScore;
    }
    public void setScore(byte team, byte score){
        switch(team){
            case 0:
                _homeScore = score;
                break;
            case 1:
                _awayScore = score;
                break;
            default:
                break;
        }
    }
    public String getHomeTeam() {
        return _homeTeam;
    }

    public String getAwayTeam() {
        return _awayTeam;
    }

    public String getHandicap() {
        return _handicap;
    }

    public String getTotal() {
        return _total;
    }

    public double[] getMoneylineCoef() {
        return _moneylineCoef;
    }
    public double getMoneylineCoef(byte team) {
        return team == 0?_moneylineCoef[0]:_moneylineCoef[1];
    }

    public void setMoneylineCoef(byte team, double _moneylineCoef) {
        this._moneylineCoef[team] = _moneylineCoef;
    }

    public void setMoneylineCoef(double[] _moneylineCoef) {
        this._moneylineCoef = _moneylineCoef;
    }

    public double[] getTotalCoef() {
        return _totalCoef;
    }

    public double getTotalCoef(byte team) {
        return team == 0?_totalCoef[0]:_totalCoef[1];
    }

    public void setTotalCoef(double[] _totalCoef) {
        this._totalCoef = _totalCoef;
    }

    public void setTotalCoef(byte team, double _totalCoef) {
        this._totalCoef[team] = _totalCoef;
    }

    public double[] getHandicapCoef() {
        return _handicapCoef;
    }

    public double getHandicapCoef(byte team) {
        return team == 0?_handicapCoef[0]:_handicapCoef[1];
    }

    public void setHandicapCoef(double[] _handicapCoef) {
        this._handicapCoef = _handicapCoef;
    }

    public void setHandicapCoef(byte team, double _handicapCoef) {
        this._handicapCoef[team] = _handicapCoef;
    }
}
