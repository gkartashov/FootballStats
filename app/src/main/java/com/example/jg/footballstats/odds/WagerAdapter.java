package com.example.jg.footballstats.odds;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.jg.footballstats.IOnItemClickListener;
import com.example.jg.footballstats.R;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;

import java.util.List;

public class WagerAdapter extends AbstractExpandableItemAdapter<WagerViewHolder, OddViewHolder> {
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
    private WagerAdapter.IdGenerator mIdGenerator;
    private List<Wager> wagerList;
    private IOnItemClickListener listener;
    public WagerAdapter(List<Wager> wagerList, IOnItemClickListener listener) {
        this.wagerList = wagerList;
        this.listener = listener;
        mIdGenerator = new WagerAdapter.IdGenerator();
        setHasStableIds(true);
    }
    @Override
    public int getGroupCount() {
        return wagerList.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return wagerList.get(groupPosition).getOddList().size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return wagerList.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return wagerList.get(groupPosition).getOddList().get(childPosition).getId();
    }

    @Override
    public WagerViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        return new WagerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_wager_item, parent, false));
    }

    @Override
    public OddViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        return new OddViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_odd_item, parent, false));
    }

    @Override
    public void onBindGroupViewHolder(WagerViewHolder holder, int groupPosition, int viewType) {
        Wager wager = wagerList.get(groupPosition);
        holder.wagerTitle.setText(wager.getTitle());

    }

    @Override
    public void onBindChildViewHolder(OddViewHolder holder, int groupPosition, int childPosition, int viewType) {
        final Odd odd = wagerList.get(groupPosition).getOddList().get(childPosition);
        wagerList.get(groupPosition).getOddList().get(childPosition).setWagerType(wagerList.get(groupPosition).getTitle());
        holder.bind(odd,listener);
        holder.oddType.setText(odd.getType());
        holder.oddCoefficient.setText(odd.getStringCoefficient());
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(WagerViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        return true;
    }

    public void addListIdToChild(Wager w) {
        for (Odd o: w.getOddList())
            o.setId(mIdGenerator.nextChild());
    }
    public void addAllGroups(List<Wager> list) {
        for (Wager w: list) {
            w.setId(mIdGenerator.nextGroup());
            wagerList.add(w);
        }
    }
}
