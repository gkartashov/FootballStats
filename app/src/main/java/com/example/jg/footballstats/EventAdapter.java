package com.example.jg.footballstats;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.jg.footballstats.fixtures.EventEntry;
import com.example.jg.footballstats.fixtures.EventsList;
import com.example.jg.footballstats.fixtures.League;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class EventAdapter extends RecyclerView.Adapter<EventsViewHolder> {
    private List<EventEntry> events = new ArrayList<>();
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
        holder.date.setText(event.getDate());
        holder.time.setText(event.getTime());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
    public void clear() {
        events.clear();
        notifyDataSetChanged();
    }

    public  void sort() {
        if (events.size() > 0)
            Collections.sort(events, new Comparator<EventEntry>() {
                @Override
                public int compare(EventEntry o1, EventEntry o2) {
                    return o1.toLocalTime().compareTo(o2.toLocalTime());
                }
            });
    }

    public List<EventEntry> getList() {
        return events;
    }

    public static List<EventEntry> sortByTime(List<EventEntry> list) {
        if (list.size() > 0)
            Collections.sort(list, new Comparator<EventEntry>() {
                @Override
                public int compare(EventEntry o1, EventEntry o2) {
                    return o1.toLocalTime().compareTo(o2.toLocalTime());
                }
            });
        return list;
    }

    public static List<EventEntry> sortById(List<EventEntry> list) {
        if (list.size() > 0)
            Collections.sort(list, new Comparator<EventEntry>() {
                @Override
                public int compare(EventEntry o1, EventEntry o2) {
                    return o1.getIntegerId().compareTo(o2.getIntegerId());
                }
            });
        return list;
    }

    public void addAll(List<EventEntry> list) {
        events.addAll(list);
        notifyDataSetChanged();
    }

    public void add(EventEntry eventEntry) {
        events.add(eventEntry);
        notifyDataSetChanged();
    }
    public void set(int index, EventEntry eventEntry) {
        events.set(index,eventEntry);
        notifyDataSetChanged();
    }
}
