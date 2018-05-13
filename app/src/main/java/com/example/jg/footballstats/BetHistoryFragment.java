package com.example.jg.footballstats;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.jg.footballstats.db.Bet;
import com.example.jg.footballstats.db.Event;
import com.example.jg.footballstats.fixtures.EventsList;
import com.example.jg.footballstats.fixtures.League;
import com.example.jg.footballstats.history.BetDetails;
import com.example.jg.footballstats.history.BetEntry;
import com.example.jg.footballstats.history.BetEntryAdapter;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static com.example.jg.footballstats.Constants.SPORT_ID;


public class BetHistoryFragment extends Fragment {
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

    private static BetHistoryFragment.RefreshingHandler mHandler;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mWrappedAdapter;
    private BetEntryAdapter mAdapter;
    private RecyclerViewExpandableItemManager mExpandableItemManager;
    private APIController apiController = APIController.getInstance();
    private DatabaseAPIController dbController = DatabaseAPIController.getInstance();
    private BetHistoryFragment.HistoryRefreshTask mHistoryRefreshTask;

    private List<BetEntry> betsList = new ArrayList<>();
    private long since;

    public BetHistoryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bet_history, container, false);
        setHasOptionsMenu(true);

        mRecyclerView = rootView.findViewById(R.id.history_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();

        animator.setSupportsChangeAnimations(false);
        mRecyclerView.setItemAnimator(animator);

        mExpandableItemManager = new RecyclerViewExpandableItemManager(null);
        mAdapter = new BetEntryAdapter(betsList);
        mWrappedAdapter = mExpandableItemManager.createWrappedAdapter(mAdapter);
        mRecyclerView.setAdapter(mWrappedAdapter);
        mExpandableItemManager.attachRecyclerView(mRecyclerView);

        mHandler = new BetHistoryFragment.RefreshingHandler();
        mSwipeRefreshLayout = rootView.findViewById(R.id.history_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        if (betsList.size() == 0)
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

    public SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mSwipeRefreshLayout.setRefreshing(true);
            fetchHistory();
        }
    };

    private void fetchHistory() {
        if (mHistoryRefreshTask == null) {
            mHistoryRefreshTask = new BetHistoryFragment.HistoryRefreshTask();
            mHistoryRefreshTask.execute((Void) null);
        }
    }

    private void refreshExpandableRecyclerView() {
        WrapperAdapterUtils.releaseAll(mWrappedAdapter);
        mExpandableItemManager.release();
        mExpandableItemManager = new RecyclerViewExpandableItemManager(null);

        mAdapter = new BetEntryAdapter(new ArrayList<BetEntry>());
        mAdapter.addAllGroups(betsList);

        mWrappedAdapter = mExpandableItemManager.createWrappedAdapter(mAdapter);
        mRecyclerView.setAdapter(mWrappedAdapter);
        mWrappedAdapter.notifyDataSetChanged();
        mExpandableItemManager.attachRecyclerView(mRecyclerView);
    }

    private List<BetEntry> betListToBetEntryList(List<Bet> betArrayList) {
        List<BetEntry> betEntries = new ArrayList<>();
        if (betArrayList.size() != 0) {

            for (Bet b:betArrayList) {
                Event event = b.getEvent();
                betEntries.add(new BetEntry(b.getBetId(),
                        event.getLeagueId(),
                        event.getHome(),
                        event.getAway(),
                        event.getHomeScore(),
                        event.getAwayScore(),
                        new BetDetails(event.getStarts(),b.getBetType(),b.getPick(),b.getCoefficient(),b.getStatus() == 0 ? "In play" : (b.getStatus() == 1 ? "Won" : "Lost"))));
            }

        }
        return betEntries;
    }
    private void refreshHistory(List<Bet> betArrayList) {
        if (betsList.size() == 0)
            betsList = betListToBetEntryList(betArrayList);
        else {
            int betIndex;
            for(BetEntry b:betListToBetEntryList(betArrayList))
                if ((betIndex = betsList.indexOf(b)) >= 0)
                    betsList.set(betIndex,b);
                else
                    betsList.add(b);
        }
    }

    public class HistoryRefreshTask extends AsyncTask<Void, Void, Void> {
        public HistoryRefreshTask() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            Response response = null;
            try {
                response = dbController.getAPI().getUserBetHistory(Constants.USER.getUsername()).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response != null) {
                refreshHistory((List<Bet>)response.body());
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Void nothing) {
            mHistoryRefreshTask = null;
            mHandler.sendEmptyMessage(0);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
