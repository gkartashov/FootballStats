package com.example.jg.footballstats;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.example.jg.footballstats.fixtures.EventEntry;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

public class EventViewHolder extends AbstractExpandableItemViewHolder {
    public TextView homeTextView, awayTextView, dateTextView, timeTextView;
    public CardView cardView;

    public EventViewHolder(View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.league_card_view);
        homeTextView = itemView.findViewById(R.id.home_team);
        awayTextView = itemView.findViewById(R.id.away_team);
        dateTextView = itemView.findViewById(R.id.date);
        timeTextView = itemView.findViewById(R.id.time);
    }

    public void bind(final EventEntry item, final IOnItemClickListener listener){
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item);
            }
        });
    }
}
