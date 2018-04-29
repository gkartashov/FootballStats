package com.example.jg.footballstats;


import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private EventAdapter eventAdapter;
    private APIController apiController = APIController.getInstance();
    private long since;

    private List<EventEntry> eventsList = new ArrayList<>();

    public EventsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);

        eventAdapter = new EventAdapter(eventsList, new IOnItemClickListener() {
            @Override
            public void onItemClick(EventEntry item) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_out_right, R.anim.slide_in_right)
                        .replace(R.id.main_layout, new EventFragment(),"event_fragment")
                        .addToBackStack("event_fragment")
                        .commit();
                ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left_white);

            }
        });
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(eventAdapter);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        swipeRefreshLayout.setRefreshing(true);
        onRefreshListener.onRefresh();
        return rootView;
    }

    public OnRefreshListener onRefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh() {
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

    private void eventsListInitializer(EventsList eventsList) {
        //for(League l:eventsList.getLeague())
        //eventAdapter.clear();
        eventAdapter.addAll(eventsList.getLeague().get(0).getEvents());
        since = eventsList.getLast();
        /*eventList.add(new Event("team1 team1 team1 team1","team2 team1 team1"));
        eventList.add(new Event("team3 team1","team4 team1 team1 team1"));
        eventList.add(new Event("team5","team6"));
        eventList.add(new Event("team7 team1 team1","team8"));
        eventList.add(new Event("team9","team10team1"));
        eventList.add(new Event("team11","team12"));
        eventList.add(new Event("team13 team1","team14"));
        eventList.add(new Event("team15","team16"));
        eventList.add(new Event("team17","team18 team1team1"));
        eventList.add(new Event("team19team1","team20"));
        eventList.add(new Event("team21","team22"));
        eventList.add(new Event("team23","team24team1 team1"));*/
    }
}
