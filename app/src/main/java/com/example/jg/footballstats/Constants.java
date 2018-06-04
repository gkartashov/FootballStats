package com.example.jg.footballstats;

import com.example.jg.footballstats.db.User;
import com.example.jg.footballstats.fixtures.League;
import com.example.jg.footballstats.history.BetEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class Constants {
    public static List<BetEntry> BETS_LIST = new ArrayList<>();
    public static List<League> EVENTS_LIST = new ArrayList<>();
    public static boolean IS_THEME_DARK = true;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    public static final Pattern VALID_USERNAME_REGEX =
            Pattern.compile("^[a-zA-Z0-9._-]{3,}$");
    public static final Pattern DOUBLE_REGEX =
            Pattern.compile("-?\\d+(\\.\\d*)?");
    public static final int SPORT_ID = 29;
    public static long EVENTS_LIST_SINCE = 0;
}
