package com.example.jg.footballstats.odds;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.jg.footballstats.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableList;

import java.util.List;

public class WagerAdapter extends ExpandableRecyclerViewAdapter<WagerViewHolder,OddsViewHolder> {
    public WagerAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public WagerViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        return new WagerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_wager_item, parent, false));
    }

    @Override
    public OddsViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        return new OddsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_odd_item, parent, false));
    }

    @Override
    public void onBindChildViewHolder(OddsViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        Odd odd = ((Wager) group).getItems().get(childIndex);
        holder.onBind(odd);
    }

    @Override
    public void onBindGroupViewHolder(WagerViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setWagerTitle(group);
    }

    public void refresh(List<? extends ExpandableGroup> groups) {
        this.expandableList = new ExpandableList(groups);
        notifyDataSetChanged();
    }
}
