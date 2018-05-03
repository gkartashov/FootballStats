package com.example.jg.footballstats;

public enum ExclusionTags {
    TEST_A(0, "Test A"),
    TEST_B(1, "Test B"),
    CORNERS(2, "Corners"),
    TO_ADVANCE(3, "To advance"),
    BOOKINGS(4, "Bookings"),
    AWAY_TEAMS(5,"Away teams"),
    HOME_TEAMS(5,"Home teams");

    private final int code;
    private final String description;

    ExclusionTags(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }
}
