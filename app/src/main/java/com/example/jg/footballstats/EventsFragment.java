package com.example.jg.footballstats;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.jg.footballstats.fixtures.EventEntry;
import com.example.jg.footballstats.fixtures.EventsList;
import com.example.jg.footballstats.fixtures.League;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsFragment extends Fragment {

    public interface OnItemSelectListener {
        void onItemSelect(EventEntry eventEntry);
    }

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private EventAdapter eventAdapter;
    private APIController apiController = APIController.getInstance();
    private OnItemSelectListener onItemSelectListener;
    private long since;

    private List<EventEntry> eventsList = new ArrayList<>();

    public EventsFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onItemSelectListener = (OnItemSelectListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        setHasOptionsMenu(true);
        eventAdapter = new EventAdapter(eventsList, new IOnItemClickListener() {
            @Override
            public void onItemClick(EventEntry item) {
                onItemSelectListener.onItemSelect(item);
            }
        });
        mRecyclerView = rootView.findViewById(R.id.events_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(eventAdapter);
        swipeRefreshLayout = rootView.findViewById(R.id.events_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        swipeRefreshLayout.setRefreshing(true);
        onRefreshListener.onRefresh();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(R.id.menu_events, R.id.action_refresh, 1, "Refresh").setIcon(R.drawable.ic_refresh_white_24px).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
                onRefreshListener.onRefresh();
        }
        return super.onOptionsItemSelected(item);
    }

    public OnRefreshListener onRefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh() {
            swipeRefreshLayout.setRefreshing(true);
            fetchFixtures();
        }
    };

    public Callback<EventsList> apiCallback = new Callback<EventsList>() {
        @Override
        public void onResponse(Call<EventsList> call, Response<EventsList> response) {
            eventsListInitializer(response.body());
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onFailure(Call<EventsList> call, Throwable t) {
            t.printStackTrace();
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    private void fetchFixtures() {
        if (since == 0)
            apiController.getAPI().getFixtures(29).enqueue(apiCallback);
        else
            apiController.getAPI().getFixturesSince(29, since).enqueue(apiCallback);
    }

    private List<EventEntry> eventsFilter(EventsList eventsList) {
        List<EventEntry> finalList = new ArrayList<EventEntry>();
        for (League l:eventsList.getLeague()) {
            List<EventEntry> eventEntries = l.getEvents();
            List<EventEntry> eventEntriesForRemove = new ArrayList<EventEntry>();
            for (EventEntry e : eventEntries)
                for (ExclusionTags ex : ExclusionTags.values())
                    if (e.getHome().toLowerCase().contains(ex.getDescription().toLowerCase()) ||
                            e.getAway().toLowerCase().contains(ex.getDescription().toLowerCase()) ||
                            e.isStarted() && e.getParentId() == 0)// ||
                            //e.getStatus() == "H")// ||
                            //e.getParentId() == 0)
                        eventEntriesForRemove.add(e);
                    else
                        e.setLeagueId(l.getId());
            eventEntries.removeAll(eventEntriesForRemove);
            l.setEvents(eventEntries);
            finalList.addAll(eventEntries);
        }
        return finalList;
    }

    private void eventsListRefresh(EventsList eventsList) {
        List<EventEntry> finalList = eventsFilter(eventsList);
        //for (EventEntry e:finalList)
            //this.eventsList.stream().map(Event)
    }
    private void eventsListInitializer(EventsList eventsList) {
        //for(League l:eventsFilter(eventsList).getLeague())
           // for (EventEntry eventEntry: l.getEvents())
                //eventAdapter.addAll(l.getEvents());
                //if (eventEntry.getHome().toLowerCase().contains("real")&&eventEntry.getHome().toLowerCase().contains("madrid")||eventEntry.getAway().toLowerCase().contains("real")&&eventEntry.getAway().toLowerCase().contains("madrid"))
                /*if (eventEntry.getDate() == "Today") {
                    eventEntry.setLeagueId(l.getId());
                    eventAdapter.add(eventEntry);
                }*/
        eventAdapter.addAll(eventsFilter(eventsList));
        eventAdapter.sort();
        since = eventsList.getLast();
    }
}
