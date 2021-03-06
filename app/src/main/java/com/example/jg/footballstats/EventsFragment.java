package com.example.jg.footballstats;


import android.content.Context;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
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
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import java.io.IOException;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import retrofit2.Response;

import static com.example.jg.footballstats.Constants.SPORT_ID;

public class EventsFragment extends Fragment {

    public interface OnItemSelectListener {
        void onItemSelect(EventEntry eventEntry);
    }

    private class RefreshingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    refreshExpandableRecyclerView();
                    break;
                default:
                    break;
            }
        }
    }

    private static RefreshingHandler mHandler;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mWrappedAdapter;
    private LeagueAdapter mAdapter;
    private RecyclerViewExpandableItemManager mExpandableItemManager;
    private OnItemSelectListener onItemSelectListener;
    private APIController apiController = APIController.getInstance();
    private EventsRefreshTask mEventsRefreshTask;

    public EventsFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onItemSelectListener = (OnItemSelectListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnItemSelectListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        setHasOptionsMenu(true);

        mRecyclerView = rootView.findViewById(R.id.events_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();

        animator.setSupportsChangeAnimations(false);
        mRecyclerView.setItemAnimator(animator);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));

        mExpandableItemManager = new RecyclerViewExpandableItemManager(null);
        mAdapter = new LeagueAdapter(getContext(),Constants.EVENTS_LIST, clickListener);
        mWrappedAdapter = mExpandableItemManager.createWrappedAdapter(mAdapter);
        mRecyclerView.setAdapter(mWrappedAdapter);
        mExpandableItemManager.attachRecyclerView(mRecyclerView);

        mHandler = new RefreshingHandler();
        swipeRefreshLayout = rootView.findViewById(R.id.events_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(getActivity().getColor(Constants.IS_THEME_DARK ? R.color.primaryColorLight : R.color.primaryColorDark));
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(getActivity().getColor(Constants.IS_THEME_DARK ? R.color.primaryColorDark : R.color.primaryColorLight));
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        if (Constants.EVENTS_LIST.size() == 0)
            onRefreshListener.onRefresh();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Upcoming games");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(R.id.menu_events, R.id.action_refresh, 1, "Refresh").setIcon(Constants.IS_THEME_DARK ? R.drawable.ic_refresh_white_24px : R.drawable.ic_refresh_black_24px).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
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

    public IOnItemClickListener clickListener = new IOnItemClickListener<EventEntry>() {
        @Override
        public void onItemClick(EventEntry item) {
            onItemSelectListener.onItemSelect(item);
        }
    };

    public OnRefreshListener onRefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh() {
            swipeRefreshLayout.setRefreshing(true);
            fetchFixtures();
        }
    };

    private void fetchFixtures() {
        if (mEventsRefreshTask == null) {
                mEventsRefreshTask = new EventsRefreshTask();
                mEventsRefreshTask.execute((Void) null);
        }
    }

    private boolean filterEvents(League l) {
        List<EventEntry> eventEntriesForRemove = new ArrayList<>();
        l.setLive(false);
        for (EventEntry e : l.getEvents()) {
            for (ExclusionTags ex : ExclusionTags.values())
                if (e.getHome().toLowerCase().contains(ex.getDescription().toLowerCase()) ||
                        e.getAway().toLowerCase().contains(ex.getDescription().toLowerCase()) ||
                        e.isStarted() && e.getParentId() == 0 ||
                        !e.isStarted() && e.getParentId() != 0 ||
                        e.isFinished())
                    eventEntriesForRemove.add(e);
                else {
                    e.setLeagueId(l.getId());
                    if (e.isLive())
                        l.setLive(true);
                }
        }
        l.getEvents().removeAll(eventEntriesForRemove);
        return l.getEvents().size() > 0;
    }

    private void filterLeagues() {
        List<League> leaguesForRemove = new ArrayList<>();
        for (League l : Constants.EVENTS_LIST)
            if (l.getName().contains(ExclusionTags.CORNERS.getDescription()) ||
                    l.getName().contains(ExclusionTags.BOOKINGS.getDescription()) ||
                    !filterEvents(l))
                leaguesForRemove.add(l);
        Constants.EVENTS_LIST.removeAll(leaguesForRemove);

    }

    private void sortLeagues() {
        Constants.EVENTS_LIST.sort(new Comparator<League>() {
            @Override
            public int compare(League o1, League o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    private void sortEvents() {
        for (League l : Constants.EVENTS_LIST)
            l.getEvents().sort(new Comparator<EventEntry>() {
                @Override
                public int compare(EventEntry o1, EventEntry o2) {
                    return o1.toLocalTime().compareTo(o2.toLocalTime());
                }
            });
    }

    private void refreshLeaguesList(EventsList eventsList) {
        List<League> updates = eventsList.getLeague();
        int leagueIndex, eventIndex;
        for(League l : updates) {
            l.setLive(false);
            if ((leagueIndex = Constants.EVENTS_LIST.indexOf(l)) >= 0) {
                List<EventEntry> updatesEvents = l.getEvents();
                for (EventEntry e : updatesEvents) {
                    if ((eventIndex = Constants.EVENTS_LIST.get(leagueIndex).getEvents().indexOf(e)) >= 0) {
                        e.setListId(Constants.EVENTS_LIST.get(leagueIndex).getEvents().get(eventIndex).getListId());
                        Constants.EVENTS_LIST.get(leagueIndex).getEvents().set(eventIndex, e);
                    } else
                        Constants.EVENTS_LIST.get(leagueIndex).addEvent(e);
                    if (e.isLive())
                        Constants.EVENTS_LIST.get(leagueIndex).setLive(true);
                }
            } else
                Constants.EVENTS_LIST.add(l);
        }
        Constants.EVENTS_LIST_SINCE = eventsList.getLast();
        eventListProcessing();
    }

    private void initializeLeaguesList(EventsList eventsList) {
        Constants.EVENTS_LIST_SINCE = eventsList.getLast();
        Constants.EVENTS_LIST = eventsList.getLeague();
        eventListProcessing();
    }

    private void eventListProcessing() {
        filterLeagues();
        sortLeagues();
        sortEvents();
    }

    private void refreshExpandableRecyclerView() {
        int pos = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        WrapperAdapterUtils.releaseAll(mWrappedAdapter);
        Parcelable expandableState = mExpandableItemManager.getSavedState();
        mExpandableItemManager.release();
        mExpandableItemManager = new RecyclerViewExpandableItemManager(expandableState);

        mAdapter = new LeagueAdapter(getContext(), new ArrayList<League>(),clickListener);
        mAdapter.addAllGroups(Constants.EVENTS_LIST);

        mWrappedAdapter = mExpandableItemManager.createWrappedAdapter(mAdapter);
        mRecyclerView.setAdapter(mWrappedAdapter);
        mWrappedAdapter.notifyDataSetChanged();
        mExpandableItemManager.attachRecyclerView(mRecyclerView);
        mRecyclerView.getLayoutManager().scrollToPosition(pos);
    }

    private void listToSharedPref() {
        if (getContext() != null) {
            SharedPreferences prefs = getContext().getSharedPreferences("EventsList", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            String tmp = ObjectSerializer.serialize(new ArrayList<>(Constants.EVENTS_LIST));
            editor.putString("Events", tmp);
            editor.putLong("Since", Constants.EVENTS_LIST_SINCE);
            editor.apply();
        }
    }

    public class EventsRefreshTask extends AsyncTask<Void, Void, Void> {
        public EventsRefreshTask() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            Response response = null;
            try {
                response = apiController.getAPI().getFixturesSince(SPORT_ID, Constants.EVENTS_LIST_SINCE).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response != null) {
                if (Constants.EVENTS_LIST_SINCE == 0)
                    initializeLeaguesList((EventsList) response.body());
                else
                    refreshLeaguesList((EventsList) response.body());
            }

            return null;
        }

        @Override
        protected void onPostExecute(final Void nothing) {
            mEventsRefreshTask = null;
            mHandler.sendEmptyMessage(0);
            swipeRefreshLayout.setRefreshing(false);
            listToSharedPref();
        }
    }
}

