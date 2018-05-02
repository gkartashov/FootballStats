package com.example.jg.footballstats.odds;

import android.view.View;
import android.widget.TextView;

import com.example.jg.footballstats.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;


public class WagerViewHolder extends GroupViewHolder {
    private TextView wagerTitle;

    public WagerViewHolder(View itemView) {
        super(itemView);
        wagerTitle = itemView.findViewById(R.id.wager_type);
    }
    public void setWagerTitle(ExpandableGroup group) {
        wagerTitle.setText(group.getTitle());
    }
}
