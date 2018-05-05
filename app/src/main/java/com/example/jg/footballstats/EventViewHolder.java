package com.example.jg.footballstats;

import android.view.View;
import android.widget.TextView;

import com.example.jg.footballstats.fixtures.EventEntry;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class EventViewHolder extends ChildViewHolder {
    private TextView homeTextView, awayTextView, dateTextView, timeTextView;
    public EventViewHolder(View itemView) {
        super(itemView);
        itemView.findViewById(R.id.home_team);
        itemView.findViewById(R.id.away_team);
        itemView.findViewById(R.id.date);
        itemView.findViewById(R.id.time);
    }

    public void onBind(EventEntry eventEntry) {
        homeTextView.setText(eventEntry.getHome());
        homeTextView.setText(eventEntry.getAway());
        dateTextView.setText(eventEntry.getDate());
        timeTextView.setText(eventEntry.getTime());
    }
}
