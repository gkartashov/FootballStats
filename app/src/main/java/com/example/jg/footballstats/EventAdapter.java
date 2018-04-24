package com.example.jg.footballstats;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;
import java.util.TimeZone;


public class EventAdapter extends RecyclerView.Adapter<EventsViewHolder> {
    private List<Event> events;
    public EventAdapter(List<Event> events){
        this.events = events;
    }
    @NonNull
    @Override
    public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_element,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull EventsViewHolder holder, int position) {
        Event event = events.get(position);
        holder.homeTeam.setText(event.getHomeTeam());
        holder.awayTeam.setText(event.getAwayTeam());
        holder.time.setText(TimeZone.getDefault().getDisplayName(true,TimeZone.SHORT));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
