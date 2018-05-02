package com.example.jg.footballstats.odds;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class Wager extends ExpandableGroup<Event> {
    public Wager(String title, List<Event> items) {
        super(title, items);
    }
}
