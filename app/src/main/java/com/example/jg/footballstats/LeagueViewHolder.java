package com.example.jg.footballstats;

import android.view.View;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class LeagueViewHolder extends GroupViewHolder {
    private TextView leagueTextView;

    public LeagueViewHolder(View itemView) {
        super(itemView);
        itemView.findViewById(R.id.league_name);
    }

    public void setGenreTitle(ExpandableGroup group) {
        leagueTextView.setText(group.getTitle());
    }
}
