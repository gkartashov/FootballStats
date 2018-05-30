package com.example.jg.footballstats.history;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.example.jg.footballstats.R;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

public class BetDetailsViewHolder extends AbstractExpandableItemViewHolder {
    public TextView dateTitle, betTypeTitle, pickTitle, realCoefficientTitle, coefficientTitle, statusTitle;
    public CardView cardView;

    public BetDetailsViewHolder(View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.history_odd_details_card_view);
        dateTitle = itemView.findViewById(R.id.history_odd_date);
        betTypeTitle = itemView.findViewById(R.id.history_odd_bet_type);
        pickTitle = itemView.findViewById(R.id.history_odd_pick);
        coefficientTitle = itemView.findViewById(R.id.history_odd_coefficient);
        realCoefficientTitle = itemView.findViewById(R.id.history_odd_real_coefficient);
        statusTitle = itemView.findViewById(R.id.history_odd_status);
    }
}
