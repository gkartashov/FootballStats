package com.example.jg.footballstats;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.jg.footballstats.fixtures.EventEntry;
import com.example.jg.footballstats.fixtures.League;
import com.example.jg.footballstats.odds.WagerViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class LeagueAdapter extends AbstractExpandableItemAdapter<LeagueViewHolder, EventViewHolder> {
    private List<League> leaguesList;

    public LeagueAdapter(List<League> leaguesList) {
        this.leaguesList = leaguesList;
        setHasStableIds(true);
    }

    @Override
    public int getGroupCount() {
        return leaguesList.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return leaguesList.get(groupPosition).getEvents().size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return leaguesList.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return leaguesList.get(groupPosition).getEvents().get(childPosition).getId();
    }

    @Override
    public LeagueViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        return new LeagueViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.events_league_item, parent, false));
    }

    @Override
    public EventViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_element, parent, false));
    }

    @Override
    public void onBindGroupViewHolder(LeagueViewHolder holder, int groupPosition, int viewType) {
        League league = leaguesList.get(groupPosition);
        holder.leagueTextView.setText(league.getName());
    }

    @Override
    public void onBindChildViewHolder(EventViewHolder holder, int groupPosition, int childPosition, int viewType) {
        EventEntry eventEntry = leaguesList.get(groupPosition).getEvents().get(childPosition);
        holder.homeTextView.setText(eventEntry.getHome());
        holder.awayTextView.setText(eventEntry.getAway());
        holder.dateTextView.setText(eventEntry.getDate());
        holder.timeTextView.setText(eventEntry.getTime());
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(LeagueViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        return true;
    }
}
