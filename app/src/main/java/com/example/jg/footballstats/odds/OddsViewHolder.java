package com.example.jg.footballstats.odds;

import android.view.View;
import android.widget.TextView;

import com.example.jg.footballstats.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class OddsViewHolder extends ChildViewHolder {
    public TextView oddType, oddCoefficient;
    public OddsViewHolder(View itemView) {
        super(itemView);
        oddType = itemView.findViewById(R.id.odd_type);
        oddCoefficient = itemView.findViewById(R.id.odd_coefficient);
    }
    public void onBind(Odd odd) {
        oddType.setText(odd.getType());
        oddCoefficient.setText(odd.getStringCoefficient());
    }
}
