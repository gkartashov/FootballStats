package com.example.jg.footballstats;

import android.util.Pair;

import com.example.jg.footballstats.history.BetEntry;

import java.text.DecimalFormat;

public class BetsCalculator {
    public static void CalculateBet(BetEntry betEntry) {
        boolean isFinished = betEntry.isFinished(), isFirstHalfFinished = betEntry.isFirstHalfFinished();
        String betType = betEntry.getBetDetails().getBetType();
        String betName = betEntry.getBetDetails().getBetName();
        String pick = betEntry.getBetDetails().getPick();
        int homeScore = betEntry.getHomeScore();
        int awayScore = betEntry.getAwayScore();
        int homeScoreHT = betEntry.getHomeScoreHT();
        int awayScoreHT = betEntry.getAwayScoreHT();
        Pair<Integer,Double> result = null;
        switch (betType) {
            case "Moneyline":
                if (isFinished)
                    result = betMoneyLineCalculation(homeScore, awayScore, pick, betEntry.getBetDetails().getCoefficient());
                break;
            case "1st half Moneyline":
                if (isFirstHalfFinished)
                    result = betMoneyLineCalculation(homeScoreHT, awayScoreHT, pick, betEntry.getBetDetails().getCoefficient());
                break;
            case "2nd half Moneyline":
                if (isFirstHalfFinished)
                    result = betMoneyLineCalculation(homeScore - homeScoreHT, awayScore - awayScoreHT, pick, betEntry.getBetDetails().getCoefficient());
                break;
            case "Total":
                if (isFinished) {
                    result = betTotalsCalculation(betName,
                            betEntry.getBetDetails().getCoefficient(),
                            Double.parseDouble(betEntry.getBetDetails().getPick()),
                            homeScore + awayScore,
                            getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                            getDigitsBeforeDot(Double.parseDouble(betEntry.getBetDetails().getPick())));
                }
                break;
            case "1st half Total":
                if (isFirstHalfFinished)
                    result = betTotalsCalculation(betName,
                        betEntry.getBetDetails().getCoefficient(),
                        Double.parseDouble(betEntry.getBetDetails().getPick()),
                        homeScoreHT+awayScoreHT,
                        getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                        getDigitsBeforeDot(Double.parseDouble(betEntry.getBetDetails().getPick())));
                break;
            case "2nd half Total":
                if (isFinished)
                    result = betTotalsCalculation(betName,
                        betEntry.getBetDetails().getCoefficient(),
                        Double.parseDouble(betEntry.getBetDetails().getPick()),
                        homeScore - homeScoreHT + awayScore - awayScoreHT,
                        getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                        getDigitsBeforeDot(Double.parseDouble(betEntry.getBetDetails().getPick())));
                break;
            case "First team total":
                if (isFinished)
                    result = betTotalsCalculation(betName,
                        betEntry.getBetDetails().getCoefficient(),
                        Double.parseDouble(betEntry.getBetDetails().getPick()),
                        homeScore,
                        getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                        getDigitsBeforeDot(Double.parseDouble(betEntry.getBetDetails().getPick())));
                break;
            case "1st half First team total":
                if (isFirstHalfFinished)
                    result = betTotalsCalculation(betName,
                        betEntry.getBetDetails().getCoefficient(),
                        Double.parseDouble(betEntry.getBetDetails().getPick()),
                        homeScoreHT,
                        BetsCalculator.getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                        BetsCalculator.getDigitsBeforeDot(Double.parseDouble(betEntry.getBetDetails().getPick())));
                break;
            case "2nd half First team total":
                if (isFinished)
                    result = betTotalsCalculation(betName,
                        betEntry.getBetDetails().getCoefficient(),
                        Double.parseDouble(betEntry.getBetDetails().getPick()),
                        homeScore - homeScoreHT,
                        getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                        getDigitsBeforeDot(Double.parseDouble(betEntry.getBetDetails().getPick())));
                break;
            case "Second team total":
                if (isFinished)
                    result = betTotalsCalculation(betName,
                        betEntry.getBetDetails().getCoefficient(),
                        Double.parseDouble(betEntry.getBetDetails().getPick()),
                        awayScore,
                        getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                        getDigitsBeforeDot(Double.parseDouble(betEntry.getBetDetails().getPick())));
                break;
            case "1st half Second team total":
                if (isFirstHalfFinished)
                    result = betTotalsCalculation(betName,
                        betEntry.getBetDetails().getCoefficient(),
                        Double.parseDouble(betEntry.getBetDetails().getPick()),
                        awayScoreHT,
                        getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                        getDigitsBeforeDot(Double.parseDouble(betEntry.getBetDetails().getPick())));
                break;
            case "2nd half Second team total":
                if (isFinished)
                    result = betTotalsCalculation(betName,
                        betEntry.getBetDetails().getCoefficient(),
                        Double.parseDouble(betEntry.getBetDetails().getPick()),
                        awayScore - awayScoreHT,
                        getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                        getDigitsBeforeDot(Double.parseDouble(betEntry.getBetDetails().getPick())));
                break;
            case "Handicap":
                if (isFinished)
                    result = betHandicapCalculation(betName,
                            betEntry.getBetDetails().getCoefficient(),
                            Double.parseDouble(betEntry.getBetDetails().getPick()),
                            homeScore,
                            awayScore,
                            getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                            getDigitsBeforeDot(Double.parseDouble(betEntry.getBetDetails().getPick())));
                break;
            case "1st half Handicap":
                if (isFirstHalfFinished)
                    result = betHandicapCalculation(betName,
                            betEntry.getBetDetails().getCoefficient(),
                            Double.parseDouble(betEntry.getBetDetails().getPick()),
                            homeScoreHT,
                            awayScoreHT,
                            getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                            getDigitsBeforeDot(Double.parseDouble(betEntry.getBetDetails().getPick())));
                break;
            case "2nd half Handicap":
                if (isFinished)
                    result = betHandicapCalculation(betName,
                            betEntry.getBetDetails().getCoefficient(),
                            Double.parseDouble(betEntry.getBetDetails().getPick()),
                            homeScore - homeScoreHT,
                            awayScore - awayScoreHT,
                            getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                            getDigitsBeforeDot(Double.parseDouble(betEntry.getBetDetails().getPick())));
                break;
            default:
                break;
        }
        if (result != null) {
            betEntry.getBetDetails().setStatus(result.first);
            betEntry.getBetDetails().setRealCoefficient(result.second);
        }
    }

    private static Pair<Integer,Double> betMoneyLineCalculation(int homeScore, int awayScore, String pick, double realCoef) {
        switch (pick) {
            case "First team to win":
                return homeScore > awayScore ? new Pair<>(1,realCoef) : new Pair<>(2,0.0);
            case "Draw":
                return homeScore > awayScore ? new Pair<>(1,realCoef) : new Pair<>(2,0.0);
            case "Second team to win":
                return homeScore > awayScore ? new Pair<>(1,realCoef) : new Pair<>(2,0.0);
            default:
                return new Pair<>(2,0.0);
        }
    }

    private static Pair<Integer,Double> totalHelper(String betName, int totalGoals, double totalPick, double coefficient) {
        if (totalGoals > totalPick)
            return betName.equals("Over") ? new Pair<>(1, coefficient) : new Pair<>(2, 0.0);
        else if (totalGoals == totalPick)
            return new Pair<>(1, 1.0);
        else
            return betName.equals("Over") ? new Pair<>(2, 0.0) : new Pair<>(1, coefficient);
    }

    private static Pair<Integer,Double> betTotalsCalculation(String betName, double coef, double totalPick, int totalGoals, double totalPickType, double totalPickGoals) {
        //total over .0 and total over .5
        if (totalPickType == 0.0 || totalPickType == 5.0) {
            return totalHelper(betName, totalGoals, totalPick, coef);
        //total over .25
        } else if (totalPickType == 2.5) {
            Pair<Integer,Double> firstPart, secondPart;
            firstPart = totalHelper(betName,totalGoals,totalPickGoals,coef);
            secondPart = totalHelper(betName, totalGoals, totalPickGoals + 0.5, coef);
            return new Pair<>((firstPart.first + secondPart.first) / 2, (firstPart.second + secondPart.second) / 2.0);
        //total over .75
        } else if (totalPickType == 7.5) {
            Pair<Integer,Double> firstPart, secondPart;
            firstPart = totalHelper(betName,totalGoals,totalPickGoals + 0.5,coef);
            secondPart = totalHelper(betName, totalGoals, totalPickGoals + 1.0, coef);
            return new Pair<>((firstPart.first + secondPart.first) / 2, Double.parseDouble(new DecimalFormat(".####").format((firstPart.second + secondPart.second) / 2.0)));
        } else
            return new Pair<>(2,0.0);
    }

    private static Pair<Integer,Double> betHandicapCalculation(String betName, double coef, double handicapPick, int homeScore, int awayScore, double handicapPickType, double handicapPickGoals) {
        double hdpType = Math.abs(handicapPickType);
        //handicap .0 and .5
        if (hdpType == 0.0 || hdpType == 5.0)
            return asianHandicapHelper(handicapPick, betName.equals("First team") ? homeScore : awayScore, betName.equals("First team") ? awayScore : homeScore, coef);
        //handicap .25
        else if (hdpType == 2.5) {
            Pair<Integer,Double> firstPart, secondPart;
            firstPart = asianHandicapHelper(handicapPickGoals, betName.equals("First team") ? homeScore : awayScore, betName.equals("First team") ? awayScore : homeScore, coef);
            secondPart = asianHandicapHelper(handicapPickGoals >= 0 ? handicapPickGoals + 0.5 : reverse(reverse(handicapPickGoals) + 0.5), betName.equals("First team") ? homeScore : awayScore, betName.equals("First team") ? awayScore : homeScore, coef);
            return new Pair<>((firstPart.first + secondPart.first) / 2, (firstPart.second + secondPart.second) / 2.0);
        //handicap .75
        } else if (hdpType == 7.5) {
            Pair<Integer, Double> firstPart, secondPart;
            firstPart = asianHandicapHelper(handicapPickGoals >= 0 ? handicapPickGoals + 0.5 : reverse(reverse(handicapPickGoals) + 0.5), betName.equals("First team") ? homeScore : awayScore, betName.equals("First team") ? awayScore : homeScore, coef);
            secondPart = asianHandicapHelper(handicapPickGoals >= 0 ? handicapPickGoals + 1.0 : reverse(reverse(handicapPickGoals) + 1.0), betName.equals("First team") ? homeScore : awayScore, betName.equals("First team") ? awayScore : homeScore, coef);
            return new Pair<>((firstPart.first + secondPart.first) / 2, (firstPart.second + secondPart.second) / 2.0);
        } else
            return new Pair<>(2,0.0);
    }

    private static Pair<Integer,Double> asianHandicapHelper(double handicapPick, double homeScore, double awayScore, double coefficient) {
            if (homeScore + handicapPick > awayScore)
                return new Pair<>(1,coefficient);
            else if (homeScore + handicapPick == awayScore)
                return new Pair<>(1,1.0);
            else
                return new Pair<>(2,0.0);
    }

    private static double getDigitsAfterDot(double a){
        return (Math.round(a*100) % 100) / 10.0;
    }
    private static double getDigitsBeforeDot(double a){
        return (int) a;
    }

    private static double reverse(double a) {
        return -a;
    }
}
