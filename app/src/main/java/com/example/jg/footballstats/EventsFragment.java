package com.example.jg.footballstats;


import android.content.Context;
import android.os.Bundle;
import android.provider.CalendarContract;
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
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import java.util.ArrayList;
import java.util.Comparator;
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
    private LeagueAdapter leagueAdapter;
    private RecyclerViewExpandableItemManager expandableItemManager;
    private APIController apiController = APIController.getInstance();
    private OnItemSelectListener onItemSelectListener;
    private long since;

    private List<League> leaguesList = new ArrayList<>();

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
        /*eventAdapter = new EventAdapter(eventsList, new IOnItemClickListener() {
            @Override
            public void onItemClick(EventEntry item) {
                onItemSelectListener.onItemSelect(item);
            }
        });*/
        mRecyclerView = rootView.findViewById(R.id.events_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));
        expandableItemManager = new RecyclerViewExpandableItemManager(null);
        leagueAdapter = new LeagueAdapter(leaguesList);
        mRecyclerView.setAdapter(expandableItemManager.createWrappedAdapter(leagueAdapter));
        expandableItemManager.attachRecyclerView(mRecyclerView);
        swipeRefreshLayout = rootView.findViewById(R.id.events_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
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
            EventsList el = response.body();
            if (el != null)
                if (since == 0)
                    eventsListInitializer(el);
            //else
                //eventsListRefresh(response.body());

            //expandableItemManager.no;
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

    private List<EventEntry> eventsFilter(List<EventEntry> eventEntries, int leagueId) {
        List<EventEntry> finalList = new ArrayList<>();
        List<EventEntry> eventEntriesForRemove = new ArrayList<EventEntry>();
        for (EventEntry e : eventEntries)
            for (ExclusionTags ex : ExclusionTags.values())
                if (e.getHome().toLowerCase().contains(ex.getDescription().toLowerCase()) ||
                        e.getAway().toLowerCase().contains(ex.getDescription().toLowerCase()) ||
                        e.isStarted() && e.getParentId() == 0 ||
                        !e.isStarted() && e.getParentId() != 0 ||
                        e.isFinished())// ||
                        //e.getStatus() == "H")// ||
                        //e.getParentId() == 0)
                    eventEntriesForRemove.add(e);
                else
                    e.setLeagueId(leagueId);
        eventEntries.removeAll(eventEntriesForRemove);
        finalList.addAll(eventEntries);
        finalList.sort(new Comparator<EventEntry>() {
            @Override
            public int compare(EventEntry o1, EventEntry o2) {
                return o1.toLocalTime().compareTo(o2.toLocalTime());
            }
        });
        return finalList;
    }

    /*private void eventsListRefresh(EventsList eventsList) {
        List<EventEntry> finalList = eventsFilter(eventsList);
        List<EventEntry> targetList = eventAdapter.getList();
        for(EventEntry eventEntry:finalList)
            if (targetList.contains(eventEntry)) {
                int index = targetList.indexOf(eventEntry);
                eventAdapter.set(index,eventEntry);
            }
            else
                eventAdapter.add(eventEntry);
        eventAdapter.sort();
        since = eventsList.getLast();
    }*/

    private void eventsListInitializer(EventsList eventsList) {
        /*for(com.example.jg.footballstats.fixtures.League l:eventsList.getLeague()) {
            leaguesList.add(new League(l.getName(), eventsFilter(l.getEvents(),l.getId())));
        }
        leaguesList.sort(new Comparator<League>() {
            @Override
            public int compare(League o1, League o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });*/
        leaguesList = eventsList.getLeague();
        //eventAdapter.addAll(eventsFilter(eventsList));
        //eventAdapter.sort();
        since = eventsList.getLast();
    }

    private void sampleDataInit() {

    }
}
