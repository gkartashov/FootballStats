package com.example.jg.footballstats.odds;

import android.view.View;
import android.widget.TextView;

import com.example.jg.footballstats.IOnItemClickListener;
import com.example.jg.footballstats.R;
import com.example.jg.footballstats.fixtures.EventEntry;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

public class OddViewHolder extends AbstractExpandableItemViewHolder {
    public TextView oddType, oddCoefficient;

    public OddViewHolder(View itemView) {
        super(itemView);
        oddType = itemView.findViewById(R.id.odd_type);
        oddCoefficient = itemView.findViewById(R.id.odd_coefficient);
    }

    public void bind(final EventEntry item, final IOnItemClickListener listener){
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item);
            }
        });
    }
}
