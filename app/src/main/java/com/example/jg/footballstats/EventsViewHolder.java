package com.example.jg.footballstats;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.jg.footballstats.fixtures.EventEntry;

public class EventsViewHolder extends RecyclerView.ViewHolder {
    public TextView homeTeam, awayTeam, date, time;
    public EventsViewHolder(View itemView) {
        super(itemView);
        homeTeam = itemView.findViewById(R.id.home_team);
        awayTeam = itemView.findViewById(R.id.away_team);
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
