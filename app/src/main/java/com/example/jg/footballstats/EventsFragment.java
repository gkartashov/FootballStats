package com.example.jg.footballstats;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private android.support.v7.widget.RecyclerView mRecyclerView;

    private List<Event> eventList = new ArrayList<>();

    public EventsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);

        mRecyclerView = rootView.findViewById(R.id.recycler_view_layout_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(new EventAdapter(eventList));
        /*mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                Log.i("LOG_TAG", "TAPPED");
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                Log.i("LOG_TAG", "TAPPED2");
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                Log.i("LOG_TAG", "TAPPED3");
            }
        });*/
        /*mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), mRecyclerView, new ITapListener() {
            @Override
            public void onTap(View view, int position) {
                Log.i("LOG_TAG", "TAPPED");
            }

            @Override
            public void onLongTap(View view, int position) {
                Log.i("LOG_TAG", "TAPPED222");
            }
        }));*/

        eventList.add(new Event("team1","team2"));
        eventList.add(new Event("team3","team4"));
        eventList.add(new Event("team5","team6"));
        eventList.add(new Event("team7","team8"));
        eventList.add(new Event("team9","team10"));
        eventList.add(new Event("team11","team12"));
        eventList.add(new Event("team13","team14"));
        eventList.add(new Event("team15","team16"));
        eventList.add(new Event("team17","team18"));
        eventList.add(new Event("team19","team20"));
        eventList.add(new Event("team21","team22"));
        eventList.add(new Event("team23","team24"));

        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        return rootView;
    }

}
