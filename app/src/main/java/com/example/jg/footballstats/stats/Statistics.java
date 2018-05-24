package com.example.jg.footballstats.stats;

import java.text.DecimalFormat;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Statistics {

    @SerializedName("realCoefficients")
    @Expose
    private List<Double> realCoefficients = null;
    @SerializedName("coefficients")
    @Expose
    private List<Double> coefficients = null;
    @SerializedName("win")
    @Expose
    private int win;
    @SerializedName("loss")
    @Expose
    private int loss;
    @SerializedName("inPlay")
    @Expose
    private long inPlay;
    @SerializedName("total")
    @Expose
    private int total;
    @SerializedName("returned")
    @Expose
    private int returned;

    public List<Double> getRealCoefficients() {
        return realCoefficients;
    }

    public void setRealCoefficients(List<Double> realCoefficients) {
        this.realCoefficients = realCoefficients;
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

    public int getLoss() {
        return loss;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }

    public long getInPlay() {
        return inPlay;
    }

    public void setInPlay(long inPlay) {
        this.inPlay = inPlay;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getReturned() {
        return returned;
    }

    public void setReturned(int returned) {
        this.returned = returned;
    }

    public double calculateWPRatio() {
        double result = (double)win / (double)loss;
        return formattedDouble(result,".##");
    }

    public double calculateROI(double betValue) {
        double result = (rawIncome(betValue) / ((win + loss + returned) * betValue)) * 100;
        return formattedDouble(result,".##") ;
    }

    public double calculateYield(double betValue) {
        double result = ((income(betValue) - ((win + loss + returned) * betValue))/ ((win + loss + returned) * betValue)) * 100;
        return formattedDouble(result,".##");
    }

    private double income(double betValue) {
        double result = 0;
        for (double d: realCoefficients)
            result += d < 0 ? 0 : (d > 0 && d < 1 ? formattedDouble(-betValue * d,".####") : formattedDouble(betValue * d,".####"));
        return result;
    }

    private double rawIncome(double betValue) {
        double result = 0;
        for (double d: realCoefficients)
            result += d < 0 ? 0 : formattedDouble(betValue * d,".####") - betValue;
        return result;
    }

    private double formattedDouble(double number, String pattern) {
        return Double.isNaN(number) ? 0 : Double.parseDouble(new DecimalFormat(pattern).format(number));
    }
}
