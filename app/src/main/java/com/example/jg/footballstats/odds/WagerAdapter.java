package com.example.jg.footballstats.odds;

import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class WagerAdapter extends ExpandableRecyclerViewAdapter<WagerViewHolder,OddsViewHolder> {
    public WagerAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public WagerViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public OddsViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindChildViewHolder(OddsViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {

    }

    @Override
    public void onBindGroupViewHolder(WagerViewHolder holder, int flatPosition, ExpandableGroup group) {

    }
}
