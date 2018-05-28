package com.example.jg.footballstats;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

public class LeagueViewHolder extends AbstractExpandableItemViewHolder {
    public TextView leagueTextView;
    public View liveDot;

    public LeagueViewHolder(View itemView) {
        super(itemView);
        leagueTextView = itemView.findViewById(R.id.league_name);
        liveDot = itemView.findViewById(R.id.events_live_dot);
    }
}
