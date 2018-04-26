package com.example.jg.footballstats;


import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
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

import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private List<Event> eventList = new ArrayList<>();

    public EventsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);

        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(new EventAdapter(eventList, new IOnItemClickListener() {
            @Override
            public void onItemClick(Event item) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_out_right, R.anim.slide_in_right)
                        .replace(R.id.main_layout, new EventFragment(),"event_fragment")
                        .addToBackStack("event_fragment")
                        .commit();
                /*Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
                DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawer_layout);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.app_name, R.string.app_name);
                drawerLayout.addDrawerListener(toggle);
                toggle.getDrawerArrowDrawable();
                toggle.syncState();*/
                //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }));

        /*mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                EventFragment ef = new EventFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, ef).addToBackStack(null).commit();
                Integer tmp = new Integer( getActivity().getSupportFragmentManager().getBackStackEntryCount());
                Log.i("TAG ", tmp.toString());
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                //Log.i("LOG_TAG", "TAPPED3");
            }
        });*/
        /*mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), mRecyclerView, new ITapListener() {
            @Override
            public void onTap(View view, int position) {
                EventFragment ef = new EventFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, ef).addToBackStack(null).commit();
                Integer tmp = new Integer( getActivity().getSupportFragmentManager().getBackStackEntryCount());
                Log.i("TAG ", tmp.toString());
            }

            @Override
            public void onLongTap(View view, int position) {
                Log.i("LOG_TAG", "TAPPED222");
            }
        }));*/

        eventListInitializer();

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
                }, 1000);
            }
        });
        return rootView;
    }

    private void eventListInitializer() {
        eventList.add(new Event("team1 team1 team1 team1","team2 team1 team1"));
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
        eventList.add(new Event("team23","team24team1 team1"));
    }
}
