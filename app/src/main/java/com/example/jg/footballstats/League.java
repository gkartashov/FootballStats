package com.example.jg.footballstats;

import com.example.jg.footballstats.fixtures.EventEntry;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class League extends ExpandableGroup<EventEntry> {
    public League(String title, List<EventEntry> items) {
        super(title, items);
    }
}
