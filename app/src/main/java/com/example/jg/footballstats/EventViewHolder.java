package com.example.jg.footballstats;

import android.view.View;
import android.widget.TextView;

import com.example.jg.footballstats.fixtures.EventEntry;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class EventViewHolder extends AbstractExpandableItemViewHolder {
    public TextView homeTextView, awayTextView, dateTextView, timeTextView;
    public EventViewHolder(View itemView) {
        super(itemView);
        homeTextView = itemView.findViewById(R.id.home_team);
        awayTextView = itemView.findViewById(R.id.away_team);
        dateTextView = itemView.findViewById(R.id.date);
        timeTextView = itemView.findViewById(R.id.time);
    }
}
