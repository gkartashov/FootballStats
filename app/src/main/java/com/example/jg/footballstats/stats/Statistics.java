package com.example.jg.footballstats.stats;

import java.text.DecimalFormat;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Statistics {

    @SerializedName("loss")
    @Expose
    private int loss;
    @SerializedName("realCoefficients")
    @Expose
    private List<Double> realCoefficients = null;
    @SerializedName("totalEvents")
    @Expose
    private int totalEvents;
    @SerializedName("coefficients")
    @Expose
    private List<Double> coefficients = null;
    @SerializedName("win")
    @Expose
    private int win;
    @SerializedName("inPlay")
    @Expose
    private long inPlay;

    public int getLoss() {
        return loss;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }

    public List<Double> getRealCoefficients() {
        return realCoefficients;
    }

    public void setRealCoefficients(List<Double> realCoefficients) {
        this.realCoefficients = realCoefficients;
    }

    public int getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(int totalEvents) {
        this.totalEvents = totalEvents;
    }

    public List<Double> getCoefficients() {
        return coefficients;
    }

    public void setCoefficients(List<Double> coefficients) {
        this.coefficients = coefficients;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public long getInPlay() {
        return inPlay;
    }

    public void setInPlay(long inPlay) {
        this.inPlay = inPlay;
    }

    public double calculateWPRatio() {
        double result = (double)win / (double)loss;
        return formattedDouble(result);
    }

    public double calculateROI(double betValue, double betsNumber) {
        double result = (rawIncome(betValue) / (betsNumber * betValue)) * 100;
        return formattedDouble(result) ;
    }

    public double calculateYield(double betValue, double betsNumber) {
        double result = ((income(betValue) - (betsNumber * betValue))/ (betsNumber * betValue)) * 100;
        return formattedDouble(result);
    }

    private double income(double betValue) {
        double result = 0;
        for (double d: realCoefficients)
            result += betValue * d;
        return result;
    }

    private double rawIncome(double betValue) {
        double result = 0;
        for (double d: realCoefficients)
            result += betValue * d - betValue;
        return result;
    }

    private double formattedDouble(double number) {
        return Double.isNaN(number) ? 0 : Double.parseDouble(new DecimalFormat(".##").format(number));
    }
}
