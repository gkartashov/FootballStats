package com.example.jg.footballstats;

import com.example.jg.footballstats.history.BetEntry;

public class BetsCalculator {
    public static void CalculateBet(BetEntry betEntry) {
        if (betEntry.isFinished()) {
            String betType = betEntry.getBetDetails().getBetType();
            String betName = betEntry.getBetDetails().getBetName();
            String pick = betEntry.getBetDetails().getPick();
            int homeScore = betEntry.getHomeScore();
            int awayScore = betEntry.getAwayScore();
            int homeScoreHT = betEntry.getHomeScoreHT();
            int awayScoreHT = betEntry.getAwayScoreHT();
            switch (betType) {
                case "Moneyline":
                    betEntry.getBetDetails().setStatus(BetsCalculator.betMoneyLineCalculation(homeScore,awayScore,pick));
                    break;
                case "1st half Moneyline":
                    betEntry.getBetDetails().setStatus(BetsCalculator.betMoneyLineCalculation(homeScoreHT,awayScoreHT,pick));
                    break;
                case "2nd half Moneyline":
                    betEntry.getBetDetails().setStatus(BetsCalculator.betMoneyLineCalculation(homeScore - homeScoreHT,awayScore - awayScoreHT,pick));
                    break;
                case "Total":
                    BetsCalculator.betTotalsCalculation(betEntry,
                            Double.parseDouble(betEntry.getBetDetails().getPick()),
                            homeScore+awayScore,
                            BetsCalculator.getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                            Double.parseDouble(betEntry.getBetDetails().getPick()) / 10.0);
                    break;
                case "1st half Total":
                    BetsCalculator.betTotalsCalculation(betEntry,
                            Double.parseDouble(betEntry.getBetDetails().getPick()),
                            homeScoreHT+awayScoreHT,
                            BetsCalculator.getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                            Double.parseDouble(betEntry.getBetDetails().getPick()) / 10.0);
                    break;
                case "2nd half Total":
                    BetsCalculator.betTotalsCalculation(betEntry,
                            Double.parseDouble(betEntry.getBetDetails().getPick()),
                            homeScore - homeScoreHT + awayScore - awayScoreHT,
                            BetsCalculator.getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                            Double.parseDouble(betEntry.getBetDetails().getPick()) / 10.0);
                    break;
                case "First team total":
                    BetsCalculator.betTotalsCalculation(betEntry,
                            Double.parseDouble(betEntry.getBetDetails().getPick()),
                            homeScore,
                            BetsCalculator.getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                            Double.parseDouble(betEntry.getBetDetails().getPick()) / 10.0);
                    break;
                case "1st half First team total":
                    BetsCalculator.betTotalsCalculation(betEntry,
                            Double.parseDouble(betEntry.getBetDetails().getPick()),
                            homeScoreHT,
                            BetsCalculator.getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                            Double.parseDouble(betEntry.getBetDetails().getPick()) / 10.0);
                    break;
                case "2nd half First team total":
                    BetsCalculator.betTotalsCalculation(betEntry,
                            Double.parseDouble(betEntry.getBetDetails().getPick()),
                            homeScore - homeScoreHT,
                            BetsCalculator.getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                            Double.parseDouble(betEntry.getBetDetails().getPick()) / 10.0);
                    break;
                case "Second team total":
                    BetsCalculator.betTotalsCalculation(betEntry,
                            Double.parseDouble(betEntry.getBetDetails().getPick()),
                            awayScore,
                            BetsCalculator.getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                            Double.parseDouble(betEntry.getBetDetails().getPick()) / 10.0);
                    break;
                case "1st half Second team total":
                    BetsCalculator.betTotalsCalculation(betEntry,
                            Double.parseDouble(betEntry.getBetDetails().getPick()),
                            awayScoreHT,
                            BetsCalculator.getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                            Double.parseDouble(betEntry.getBetDetails().getPick()) / 10.0);
                    break;
                case "2nd half Second team total":
                    BetsCalculator.betTotalsCalculation(betEntry,
                            Double.parseDouble(betEntry.getBetDetails().getPick()),
                            awayScore - awayScoreHT,
                            BetsCalculator.getDigitsAfterDot(Double.parseDouble(betEntry.getBetDetails().getPick())),
                            Double.parseDouble(betEntry.getBetDetails().getPick()) / 10.0);
                    break;
                default:
                    break;
            }
        }
    }

    public static int betMoneyLineCalculation(int homeScore, int awayScore, String pick) {
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

    public static void betTotalsCalculation(BetEntry betEntry, double totalPick, int totalGoals, double totalPickType, double totalPickGoals) {
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

    public static double getDigitsAfterDot(double a){
        return (Math.round(a*100) % 100) / 10.0;
    }
}
