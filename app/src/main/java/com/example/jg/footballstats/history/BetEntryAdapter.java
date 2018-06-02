package com.example.jg.footballstats.history;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.jg.footballstats.Constants;
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
    private Context context;
    private BetEntryAdapter.IdGenerator mIdGenerator;
    private List<BetEntry> betList;

    public BetEntryAdapter(Context context, List<BetEntry> betList) {
        this.context = context;
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
        if (betEntry.getBetDetails().getStatus() == 1)
            holder.statusIndicator.setBackgroundColor(ContextCompat.getColor(context,R.color.winColor));
        else if (betEntry.getBetDetails().getStatus() == 2)
            holder.statusIndicator.setBackgroundColor(ContextCompat.getColor(context,R.color.lossColor));
        else
            holder.statusIndicator.setBackgroundColor(ContextCompat.getColor(context,Constants.IS_THEME_DARK ? R.color.statusColor : R.color.primaryDarkColorLight));
        holder.homeTitle.setText(betEntry.getHome());
        holder.awayTitle.setText(betEntry.getAway());
        holder.homeScoreHTTitle.setText(betEntry.getHomeScoreHT() >= 0 ? betEntry.getStringHomeHTScore() : "0");
        holder.awayScoreHTTitle.setText(betEntry.getAwayScoreHT() >= 0 ? betEntry.getStringAwayHTScore() : "0");
        holder.homeScoreTitle.setText(betEntry.getHomeScore() >= 0 ? betEntry.getStringHomeScore() : (betEntry.getHomeScoreHT() >= 0 ? betEntry.getStringHomeHTScore() : "0"));
        holder.awayScoreTitle.setText(betEntry.getAwayScore() >= 0 ? betEntry.getStringAwayScore() : (betEntry.getAwayScoreHT() >= 0 ? betEntry.getStringAwayHTScore() : "0"));
        holder.homeScoreTitle.setTextColor(ContextCompat.getColor(context, Constants.IS_THEME_DARK ? R.color.primaryTextColorDark : R.color.primaryTextColorLight));
        holder.awayScoreTitle.setTextColor(ContextCompat.getColor(context, Constants.IS_THEME_DARK ? R.color.primaryTextColorDark : R.color.primaryTextColorLight));


    }

    @Override
    public void onBindChildViewHolder(BetDetailsViewHolder holder, int groupPosition, int childPosition, int viewType) {
        BetDetails betDetails = betList.get(groupPosition).getBetDetails();
        holder.dateTitle.setText(betDetails.getStartDateTime());
        holder.betTypeTitle.setText(betDetails.getBetType());
        holder.pickTitle.setText(betDetails.getBetName() == null ? betDetails.getPick() : betDetails.getBetName() + " " + betDetails.getPick());
        holder.coefficientTitle.setText(betDetails.getStringCoefficient());
        holder.realCoefficientTitle.setText(betDetails.getRealCoefficient() == -1.0 ? betDetails.getStringCoefficient() : betDetails.getStringRealCoefficient());
        holder.statusTitle.setText(betDetails.getStringStatus());
        holder.cardView.setBackgroundColor(ContextCompat.getColor(context, Constants.IS_THEME_DARK ? R.color.listItemsColorDark : R.color.primaryBackgroundColorLight));
        if (betDetails.getStatus() == 1)
            holder.statusTitle.setTextColor(ContextCompat.getColor(context, R.color.winColor));
        else if (betDetails.getStatus() == 2)
            holder.statusTitle.setTextColor(ContextCompat.getColor(context, R.color.lossColor));
        else
            holder.statusTitle.setTextColor(ContextCompat.getColor(context, Constants.IS_THEME_DARK ? R.color.primaryTextColorDark : R.color.primaryTextColorLight));

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
            b.getBetDetails().setId(mIdGenerator.nextChild());
            betList.add(b);
        }
    }
}
