package com.example.jg.footballstats;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jg.footballstats.fixtures.EventEntry;
import com.example.jg.footballstats.fixtures.League;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;

import java.util.List;

public class LeagueAdapter extends AbstractExpandableItemAdapter<LeagueViewHolder, EventViewHolder> {
    private static class IdGenerator {
        int mIdGroup;
        int mIdChild;

        public int nextGroup() {
            final int id = mIdGroup;
            mIdGroup += 1;
            return id;
        }

        public int nextChild() {
            final int id = mIdChild;
            mIdChild += 1;
            return id;
        }
    }
    private Context context;
    private IdGenerator mIdGenerator;
    private List<League> leaguesList;
    private final IOnItemClickListener listener;

    public LeagueAdapter(Context context, List<League> leaguesList, IOnItemClickListener listener) {
        this.context = context;
        this.leaguesList = leaguesList;
        this.listener = listener;
        mIdGenerator = new IdGenerator();
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
        return leaguesList.get(groupPosition).getEvents().get(childPosition).getListId();
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
        holder.liveDot.setVisibility(league.isLive() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onBindChildViewHolder(EventViewHolder holder, int groupPosition, int childPosition, int viewType) {
        final EventEntry eventEntry = leaguesList.get(groupPosition).getEvents().get(childPosition);
        holder.bind(eventEntry,listener);
        if (eventEntry.isStarted()) {
            holder.dateTextView.setText("LIVE");
            holder.dateTextView.setTextColor(ContextCompat.getColor(context,R.color.lossColor));
            holder.dateTextView.setTypeface(null, Typeface.BOLD);
        } else {
            holder.dateTextView.setText(eventEntry.getDate());
            holder.dateTextView.setTextColor(holder.timeTextView.getTextColors());
            holder.dateTextView.setTypeface(null, Typeface.NORMAL);
        }
        holder.homeTextView.setText(eventEntry.getHome());
        holder.awayTextView.setText(eventEntry.getAway());
        holder.timeTextView.setText(eventEntry.getTime());
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(LeagueViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        return true;
    }

    public void addAllGroups(List<League> list) {
        for (League l: list) {
            l.setListId(mIdGenerator.nextGroup());
            for (EventEntry e : l.getEvents())
                e.setListId(mIdGenerator.nextChild());
            leaguesList.add(l);
        }
    }
}
