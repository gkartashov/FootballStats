package com.example.jg.footballstats.odds;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.example.jg.footballstats.R;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

public class WagerViewHolder extends AbstractExpandableItemViewHolder {
    public TextView wagerTitle;
    public CardView cardView;

    public WagerViewHolder(View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.event_wager_card_view);
        wagerTitle = itemView.findViewById(R.id.wager_type);
    }
}
