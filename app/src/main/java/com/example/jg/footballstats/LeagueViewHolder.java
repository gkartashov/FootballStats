package com.example.jg.footballstats;

import android.view.View;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class LeagueViewHolder extends AbstractExpandableItemViewHolder {
    public TextView leagueTextView;

    public LeagueViewHolder(View itemView) {
        super(itemView);
        leagueTextView = itemView.findViewById(R.id.league_name);
    }
}
