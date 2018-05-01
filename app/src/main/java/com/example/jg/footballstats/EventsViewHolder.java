package com.example.jg.footballstats;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class EventsViewHolder extends RecyclerView.ViewHolder {
    public TextView homeTeam, awayTeam, date, time;
    public EventsViewHolder(View itemView) {
        super(itemView);
        homeTeam = itemView.findViewById(R.id.homeTeam);
        awayTeam = itemView.findViewById(R.id.awayTeam);
        date = itemView.findViewById(R.id.date);
        time = itemView.findViewById(R.id.time);
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
