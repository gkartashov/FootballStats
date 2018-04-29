package com.example.jg.footballstats;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;


public class EventAdapter extends RecyclerView.Adapter<EventsViewHolder> {
    private List<EventEntry> events = new ArrayList<>();;
    private final IOnItemClickListener listener;
    public EventAdapter(List<EventEntry> events, IOnItemClickListener listener){
        this.events = events;
        this.listener = listener;
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
        holder.bind(events.get(position),listener);
        EventEntry event = events.get(position);
        holder.homeTeam.setText(event.getHome());
        holder.awayTeam.setText(event.getAway());
        holder.time.setText(TimeZone.getDefault().getDisplayName(true,TimeZone.SHORT));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
    public void clear() {
        events.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<EventEntry> list) {
        events.addAll(list);
        notifyDataSetChanged();
    }
}
