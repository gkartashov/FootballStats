package com.example.jg.footballstats.history;

import android.view.View;
import android.widget.TextView;

import com.example.jg.footballstats.R;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

public class BetDetailsViewHolder extends AbstractExpandableItemViewHolder {
    public TextView dateTitle, betTypeTitle, pickTitle, coefficientTitle, statusTitle;

    public BetDetailsViewHolder(View itemView) {
        super(itemView);
        dateTitle = itemView.findViewById(R.id.history_odd_date);
        betTypeTitle = itemView.findViewById(R.id.history_odd_bet_type);
        pickTitle = itemView.findViewById(R.id.history_odd_pick);
        coefficientTitle = itemView.findViewById(R.id.history_odd_coefficient);
        statusTitle = itemView.findViewById(R.id.history_odd_status);
    }
}
