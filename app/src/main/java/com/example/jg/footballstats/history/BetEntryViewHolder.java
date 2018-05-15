package com.example.jg.footballstats.history;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jg.footballstats.R;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

public class BetEntryViewHolder extends AbstractExpandableItemViewHolder {
    public TextView homeTitle, awayTitle, homeScoreTitle, awayScoreTitle, homeScoreHTTitle, awayScoreHTTitle;
    public LinearLayout statusIndicator;

    public BetEntryViewHolder(View itemView) {
        super(itemView);
        statusIndicator = itemView.findViewById(R.id.history_event_status);
        homeTitle = itemView.findViewById(R.id.history_event_home);
        awayTitle = itemView.findViewById(R.id.history_event_away);
        homeScoreTitle = itemView.findViewById(R.id.history_event_home_score);
        awayScoreTitle = itemView.findViewById(R.id.history_event_away_score);
        homeScoreHTTitle = itemView.findViewById(R.id.history_event_homeHT_score);
        awayScoreHTTitle = itemView.findViewById(R.id.history_event_awayHT_score);
    }
}
