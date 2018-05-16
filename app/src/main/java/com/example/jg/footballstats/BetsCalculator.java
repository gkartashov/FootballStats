package com.example.jg.footballstats;

import android.util.Pair;

import com.example.jg.footballstats.history.BetEntry;

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
        switch (betType) {
            case "Moneyline":
                if (isFinished)
                    betEntry.getBetDetails().setStatus(BetsCalculator.betMoneyLineCalculation(homeScore,awayScore,pick));
                break;
            case "1st half Moneyline":
                if (isFirstHalfFinished)
                    betEntry.getBetDetails().setStatus(BetsCalculator.betMoneyLineCalculation(homeScoreHT,awayScoreHT,pick));
                break;
            case "2nd half Moneyline":
                if (isFinished)
                    betEntry.getBetDetails().setStatus(BetsCalculator.betMoneyLineCalculation(homeScore - homeScoreHT,awayScore - awayScoreHT,pick));
                break;
            case "Total":
                if (isFinished)
                    BetsCalculator.betTotalsCalculation(betEntry,
                        Double.parseDouble(betEntry.getBetDetails().getPick()),
                        homeScore+awayScore,
                        BetsCalculator.getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                        Double.parseDouble(betEntry.getBetDetails().getPick()) / 10.0);
                break;
            case "1st half Total":
                if (isFirstHalfFinished)
                    BetsCalculator.betTotalsCalculation(betEntry,
                        Double.parseDouble(betEntry.getBetDetails().getPick()),
                        homeScoreHT+awayScoreHT,
                        BetsCalculator.getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                        Double.parseDouble(betEntry.getBetDetails().getPick()) / 10.0);
                break;
            case "2nd half Total":
                if (isFinished)
                    BetsCalculator.betTotalsCalculation(betEntry,
                        Double.parseDouble(betEntry.getBetDetails().getPick()),
                        homeScore - homeScoreHT + awayScore - awayScoreHT,
                        BetsCalculator.getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                        Double.parseDouble(betEntry.getBetDetails().getPick()) / 10.0);
                break;
            case "First team total":
                if (isFinished)
                    BetsCalculator.betTotalsCalculation(betEntry,
                        Double.parseDouble(betEntry.getBetDetails().getPick()),
                        homeScore,
                        BetsCalculator.getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                        Double.parseDouble(betEntry.getBetDetails().getPick()) / 10.0);
                break;
            case "1st half First team total":
                if (isFirstHalfFinished)
                    BetsCalculator.betTotalsCalculation(betEntry,
                        Double.parseDouble(betEntry.getBetDetails().getPick()),
                        homeScoreHT,
                        BetsCalculator.getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                        Double.parseDouble(betEntry.getBetDetails().getPick()) / 10.0);
                break;
            case "2nd half First team total":
                if (isFinished)
                    BetsCalculator.betTotalsCalculation(betEntry,
                        Double.parseDouble(betEntry.getBetDetails().getPick()),
                        homeScore - homeScoreHT,
                        BetsCalculator.getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                        Double.parseDouble(betEntry.getBetDetails().getPick()) / 10.0);
                break;
            case "Second team total":
                if (isFinished)
                    BetsCalculator.betTotalsCalculation(betEntry,
                        Double.parseDouble(betEntry.getBetDetails().getPick()),
                        awayScore,
                        BetsCalculator.getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                        Double.parseDouble(betEntry.getBetDetails().getPick()) / 10.0);
                break;
            case "1st half Second team total":
                if (isFirstHalfFinished)
                    BetsCalculator.betTotalsCalculation(betEntry,
                        Double.parseDouble(betEntry.getBetDetails().getPick()),
                        awayScoreHT,
                        BetsCalculator.getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                        Double.parseDouble(betEntry.getBetDetails().getPick()) / 10.0);
                break;
            case "2nd half Second team total":
                if (isFinished)
                    BetsCalculator.betTotalsCalculation(betEntry,
                        Double.parseDouble(betEntry.getBetDetails().getPick()),
                        awayScore - awayScoreHT,
                        BetsCalculator.getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                        Double.parseDouble(betEntry.getBetDetails().getPick()) / 10.0);
                break;
            case "Handicap":
                if (isFinished)
                    BetsCalculator.betHandicapCalculation(betEntry,
                            Double.parseDouble(betEntry.getBetDetails().getPick()),
                            homeScore,
                            awayScore,
                            BetsCalculator.getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                            BetsCalculator.getDigitsBeforeDot(Double.parseDouble(betEntry.getBetDetails().getPick())));
                break;
            case "1st half Handicap":
                if (isFirstHalfFinished)
                    BetsCalculator.betHandicapCalculation(betEntry,
                            Double.parseDouble(betEntry.getBetDetails().getPick()),
                            homeScoreHT,
                            awayScoreHT,
                            BetsCalculator.getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                            BetsCalculator.getDigitsBeforeDot(Double.parseDouble(betEntry.getBetDetails().getPick())));
                break;
            case "2nd half Handicap":
                if (isFirstHalfFinished)
                    BetsCalculator.betHandicapCalculation(betEntry,
                            Double.parseDouble(betEntry.getBetDetails().getPick()),
                            homeScore - homeScoreHT,
                            awayScore - awayScoreHT,
                            BetsCalculator.getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                            BetsCalculator.getDigitsBeforeDot(Double.parseDouble(betEntry.getBetDetails().getPick())));
                break;
            default:
                break;
        }
    }

    private static int betMoneyLineCalculation(int homeScore, int awayScore, String pick) {
        switch (pick) {
            case "First team to win":
                return homeScore > awayScore ? 1 : 2;
            case "Draw":
                return homeScore == awayScore ? 1 : 2;
            case "Second team to win":
                return homeScore < awayScore ? 1 : 2;
            default:
                return 0;
        }
    }

    private static void betTotalsCalculation(BetEntry betEntry, double totalPick, int totalGoals, double totalPickType, double totalPickGoals) {
        switch (betEntry.getBetDetails().getBetName()) {
            case "Over":
                //total over .0
                if (totalPickType == 0.0)
                    if (totalGoals > totalPick)
                        betEntry.getBetDetails().setStatus(1);
                    else if (totalGoals == totalPick) {
                        betEntry.getBetDetails().setStatus(1);
                        betEntry.getBetDetails().setCoefficient(1.0);
                    } else
                        betEntry.getBetDetails().setStatus(2);
                //total over .5
                if (totalPickType == 5.0)
                    if (totalGoals > totalPick)
                        betEntry.getBetDetails().setStatus(1);
                    else
                        betEntry.getBetDetails().setStatus(2);
                //total over .25
                if (totalPickType == 2.5) {
                    boolean firstPart = true, secondPart = true;
                    double coef1 = betEntry.getBetDetails().getCoefficient(), coef2 = coef1;
                    if (totalGoals > totalPickGoals + 0.5)
                        secondPart = true;
                    else {
                        secondPart = false;
                        coef2 = 0.0;
                    }
                    if (secondPart)
                        betEntry.getBetDetails().setStatus(1);
                    else {
                        if (totalGoals > totalPickGoals)
                            firstPart = true;
                        else if (totalGoals == totalPickGoals) {
                            firstPart = true;
                            coef1 = 1.0;
                        } else {
                            firstPart = false;
                            coef1 = 0.0;
                        }
                    }
                    if (firstPart)
                        betEntry.getBetDetails().setStatus(1);
                    else
                        betEntry.getBetDetails().setStatus(2);
                }
                //total over .75
                if (totalPickType == 7.5) {
                    boolean firstPart = true, secondPart = true;
                    double coef1 = betEntry.getBetDetails().getCoefficient(), coef2 = coef1;
                    if (totalGoals > totalPickGoals + 1.0)
                        secondPart = true;
                    else if (totalGoals == totalPickGoals + 1.0) {
                        secondPart = true;
                        coef2 = 1.0;
                    } else {
                        secondPart = firstPart = false;
                        coef2 = coef1 = 0.0;
                    }

                    if (secondPart)
                        betEntry.getBetDetails().setStatus(1);
                    else
                        betEntry.getBetDetails().setStatus(2);
                }
                break;
            case "Under":
                //total under .0
                if (totalPickType == 0.0)
                    if (totalGoals < totalPick)
                        betEntry.getBetDetails().setStatus(1);
                    else if (totalGoals == totalPick) {
                        betEntry.getBetDetails().setStatus(1);
                        betEntry.getBetDetails().setCoefficient(1.0);
                    } else
                        betEntry.getBetDetails().setStatus(2);
                //total under .5
                if (totalPickType == 5.0)
                    if (totalGoals < totalPick)
                        betEntry.getBetDetails().setStatus(1);
                    else
                        betEntry.getBetDetails().setStatus(2);
                //total under .25
                if (totalPickType == 2.5) {
                    boolean firstPart = true, secondPart = true;
                    double coef1 = betEntry.getBetDetails().getCoefficient(), coef2 = coef1;
                    if (totalGoals < totalPickGoals + 0.5)
                        secondPart = true;
                    else {
                        secondPart = firstPart = false;
                        coef2 = coef1 = 0.0;
                    }
                    if (!secondPart)
                        betEntry.getBetDetails().setStatus(2);
                    else {
                        if (totalGoals < totalPickGoals)
                            firstPart = true;
                        else if (totalGoals == totalPickGoals) {
                            firstPart = true;
                            coef1 = 1.0;
                        }
                        betEntry.getBetDetails().setStatus(1);
                    }

                }
                //total under .75
                if (totalPickType == 7.5) {
                    boolean firstPart = true, secondPart = true;
                    double coef1 = betEntry.getBetDetails().getCoefficient(), coef2 = coef1;
                    if (totalGoals < totalPickGoals + 1.0)
                        secondPart = true;
                    else if (totalGoals == totalPickGoals + 1.0) {
                        secondPart = true;
                        coef2 = 1.0;
                    } else {
                        secondPart = firstPart = false;
                        coef2 = coef1 = 0.0;
                    }
                    if (!secondPart)
                        betEntry.getBetDetails().setStatus(2);
                    else {
                        if (totalGoals < totalPickGoals + 0.5)
                            firstPart = true;
                        else {
                            firstPart = false;
                            coef1 = 0.0;
                        }
                        betEntry.getBetDetails().setStatus(1);
                    }
                    break;
                }
            default:
                break;
        }
    }

    private static void betHandicapCalculation(BetEntry betEntry, double handicapPick, int homeScore, int awayScore, double handicapPickType, double totalPickGoals) {
        Pair<Boolean,Double> result;
        String name = betEntry.getBetDetails().getBetName();
        double hdpType = Math.abs(handicapPickType);
        //handicap .0
        if (hdpType == 0.0) {
            if (name.equals("First team"))
                result = asianHandicap00helper(handicapPick,homeScore,awayScore,betEntry.getBetDetails().getCoefficient());
            else
                result = asianHandicap00helper(handicapPick,awayScore,homeScore,betEntry.getBetDetails().getCoefficient());
            if (result.first) {
                betEntry.getBetDetails().setStatus(1);
                betEntry.getBetDetails().setCoefficient(result.second >= 1.0 ? result.second : betEntry.getBetDetails().getCoefficient());
            } else
                betEntry.getBetDetails().setStatus(2);
        }
        //handicap .5
        if (hdpType == 5.0) {
            if (name.equals("First team"))
                result = asianHandicap05helper(handicapPick,homeScore,awayScore,betEntry.getBetDetails().getCoefficient());
            else
                result = asianHandicap05helper(handicapPick,awayScore,homeScore,betEntry.getBetDetails().getCoefficient());
            if (result.first)
                betEntry.getBetDetails().setStatus(1);
            else
                betEntry.getBetDetails().setStatus(2);
        }
        //handicap .25
        if (hdpType == 2.5) {
            Pair<Boolean, Double> resultSecondPart;
            if (name.equals("First team"))
                resultSecondPart = asianHandicap05helper(totalPickGoals > 0 ? totalPickGoals + 0.5 : reverse(reverse(totalPickGoals) + 0.5), homeScore, awayScore, betEntry.getBetDetails().getCoefficient());
            else
                resultSecondPart = asianHandicap05helper(totalPickGoals > 0 ? totalPickGoals + 0.5 : reverse(reverse(totalPickGoals) + 0.5), awayScore, homeScore, betEntry.getBetDetails().getCoefficient());
            if (resultSecondPart.first)
                betEntry.getBetDetails().setStatus(1);
            else {
                if (name.equals("First team"))
                    result = asianHandicap00helper(totalPickGoals,homeScore,awayScore,betEntry.getBetDetails().getCoefficient());
                else
                    result = asianHandicap00helper(totalPickGoals,awayScore,homeScore,betEntry.getBetDetails().getCoefficient());
                if (result.first) {
                    betEntry.getBetDetails().setStatus(1);
                    betEntry.getBetDetails().setCoefficient(result.second >= 1.0 ? result.second : betEntry.getBetDetails().getCoefficient());
                } else
                    betEntry.getBetDetails().setStatus(2);
            }
        }
        //handicap .75
        if (hdpType == 7.5) {
            if (name.equals("First team"))
                result = asianHandicap00helper(totalPickGoals > 0 ? totalPickGoals + 1.0 : reverse(reverse(totalPickGoals) + 1.0), homeScore, awayScore, betEntry.getBetDetails().getCoefficient());
            else
                result = asianHandicap00helper(totalPickGoals > 0 ? totalPickGoals + 1.0 : reverse(reverse(totalPickGoals) + 1.0), awayScore, homeScore, betEntry.getBetDetails().getCoefficient());
            if (result.first) {
                if (result.second > 1.0)
                    betEntry.getBetDetails().setStatus(1);
                else if (result.second == 1.0) {
                    betEntry.getBetDetails().setStatus(1);
                    betEntry.getBetDetails().setCoefficient(1.0);
                }
            } else
                betEntry.getBetDetails().setStatus(2);
        }
    }

    private static Pair<Boolean,Double> asianHandicap00helper(double handicapPick, double homeScore, double awayScore, double coefficient) {
            if (homeScore + handicapPick > awayScore)
                return new Pair<>(true,coefficient);
            else if (homeScore + handicapPick == awayScore)
                return new Pair<>(true,1.0);
            else
                return new Pair<>(false,0.0);
    }

    private static Pair<Boolean,Double> asianHandicap05helper(double handicapPick, double homeScore, double awayScore, double coefficient) {
        if (homeScore + handicapPick > awayScore)
            return new Pair<>(true,coefficient);
        else
            return new Pair<>(false,0.0);
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
