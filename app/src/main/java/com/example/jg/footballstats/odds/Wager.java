package com.example.jg.footballstats.odds;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class Wager extends ExpandableGroup<Odd> {
    public Wager(String title, List<Odd> items) {
        super(title, items);
    }
}
