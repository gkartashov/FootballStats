package com.example.jg.footballstats.history;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.jg.footballstats.R;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;

import java.util.List;

public class BetEntryAdapter extends AbstractExpandableItemAdapter<BetEntryViewHolder, BetDetailsViewHolder> {
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

    private BetEntryAdapter.IdGenerator mIdGenerator;
    private List<BetEntry> betList;

    public BetEntryAdapter(List<BetEntry> betList) {
        this.betList = betList;
        mIdGenerator = new BetEntryAdapter.IdGenerator();
        setHasStableIds(true);
    }

    @Override
    public int getGroupCount() {
        return betList.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return 1;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return betList.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return betList.get(groupPosition).getBetDetails().getId();
    }

    @Override
    public BetEntryViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        return new BetEntryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.history_odd_item, parent, false));
    }

    @Override
    public BetDetailsViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        return new BetDetailsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.history_odd_details, parent, false));
    }

    @Override
    public void onBindGroupViewHolder(BetEntryViewHolder holder, int groupPosition, int viewType) {
        BetEntry betEntry = betList.get(groupPosition);
        holder.homeTitle.setText(betEntry.getHome());
        holder.awayTitle.setText(betEntry.getAway());
        holder.homeScoreTitle.setText(betEntry.getStringHomeScore());
        holder.awayScoreTitle.setText(betEntry.getStringHomeScore());
    }

    @Override
    public void onBindChildViewHolder(BetDetailsViewHolder holder, int groupPosition, int childPosition, int viewType) {
        BetDetails betDetails = betList.get(groupPosition).getBetDetails();
        holder.dateTitle.setText(betDetails.getTime());
        holder.betTypeTitle.setText(betDetails.getBetType());
        holder.pickTitle.setText(betDetails.getPick());
        holder.coefficientTitle.setText(betDetails.getStringCoefficient());
        holder.statusTitle.setText(betDetails.getStatus());
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(BetEntryViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        return true;
    }

    public void addListIdToChild(BetEntry betEntry) {
        betEntry.getBetDetails().setId(mIdGenerator.nextChild());
    }

    public void addAllGroups(List<BetEntry> list) {
        for (BetEntry b : list) {
            b.setId(mIdGenerator.nextGroup());
            betList.add(b);
        }
    }
}
