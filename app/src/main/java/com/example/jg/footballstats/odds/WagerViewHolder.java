package com.example.jg.footballstats.odds;

import android.view.View;
import android.widget.TextView;

import com.example.jg.footballstats.R;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

public class WagerViewHolder extends AbstractExpandableItemViewHolder {
    public TextView wagerTitle;

    public WagerViewHolder(View itemView) {
        super(itemView);
        wagerTitle = itemView.findViewById(R.id.wager_type);
    }
}
