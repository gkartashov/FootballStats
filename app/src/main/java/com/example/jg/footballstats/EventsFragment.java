package com.example.jg.footballstats;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
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
        setHasOptionsMenu(true);
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

    private void eventsListInitializer(final EventsList eventsList) {
        eventAdapter.addAll(eventsList.getLeague().get(0).getEvents());
        eventAdapter.sort();
        since = eventsList.getLast();
    }
}
