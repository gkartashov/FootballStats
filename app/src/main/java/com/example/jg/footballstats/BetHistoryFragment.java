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
import com.example.jg.footballstats.history.BetDetails;
import com.example.jg.footballstats.history.BetEntry;
import com.example.jg.footballstats.history.BetEntryAdapter;
import com.example.jg.footballstats.history.BetResult;
import com.example.jg.footballstats.history.Period;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Response;

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
    private View rootView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mWrappedAdapter;
    private BetEntryAdapter mAdapter;
    private RecyclerViewExpandableItemManager mExpandableItemManager;
    private APIController apiController = APIController.getInstance();
    private DatabaseAPIController dbController = DatabaseAPIController.getInstance();
    private BetHistoryFragment.HistoryRefreshTask mHistoryRefreshTask;
    private BetHistoryFragment.DbRefreshTask mUpdateDbTask;

    private List<BetEntry> betsList = new ArrayList<>();
    private List<BetEntry> updateList = new ArrayList<>();
    private long since;

    public BetHistoryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_bet_history, container, false);
        setHasOptionsMenu(true);

        mRecyclerView = rootView.findViewById(R.id.history_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();

        animator.setSupportsChangeAnimations(false);
        mRecyclerView.setItemAnimator(animator);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));

        mExpandableItemManager = new RecyclerViewExpandableItemManager(null);
        mAdapter = new BetEntryAdapter(getContext(), betsList);
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

        mAdapter = new BetEntryAdapter(getContext(), new ArrayList<BetEntry>());
        mAdapter.addAllGroups(betsList);

        mWrappedAdapter = mExpandableItemManager.createWrappedAdapter(mAdapter);
        mRecyclerView.setAdapter(mWrappedAdapter);
        mWrappedAdapter.notifyDataSetChanged();
        mExpandableItemManager.attachRecyclerView(mRecyclerView);
    }

    private List<BetEntry> betListToBetEntryList(List<Bet> betArrayList, BetResult betResult) {
        List<BetEntry> betEntries = new ArrayList<>();
        if (betArrayList.size() != 0) {

            for (Bet b:betArrayList) {
                Event event = b.getEvent();
                BetEntry betEntry = new BetEntry(b.getBetId(),
                        event.getLeagueId(),
                        event.getEventId(),
                        event.getHome(),
                        event.getAway(),
                        event.getHomeScore(),
                        event.getAwayScore(),
                        event.getHomeScoreHT(),
                        event.getAwayScoreHT(),
                        new BetDetails(event.getStarts(),b.getBetType(),b.getBetName(),b.getPick(),b.getCoefficient(),b.getStatus()));
                if (betResult != null && betResult.getLeagues() != null) {
                    int leagueIndex, eventIndex;
                    if ((leagueIndex = betResult.getLeagues().indexOf(new com.example.jg.footballstats.history.League(event.getLeagueId()))) >= 0 &&
                            (eventIndex = betResult.getLeagues().get(leagueIndex).getEvents().indexOf(new com.example.jg.footballstats.history.Event(event.getEventId()))) >= 0) {
                        List<Period> periods = betResult.getLeagues().get(leagueIndex).getEvents().get(eventIndex).getPeriods();
                        switch (periods.size()) {
                            case 1: {
                                Period p = periods.get(0);
                                if (p.getNumber() == 1 && (p.getStatus() == 1 || p.getStatus() == 2) && p.getCancellationReason() == null) {
                                    betEntry.setHomeScore(p.getTeam1Score());
                                    betEntry.setHomeScoreHT(p.getTeam1Score());
                                    betEntry.setAwayScore(p.getTeam2Score());
                                    betEntry.setAwayScoreHT(p.getTeam2Score());
                                }
                                break;
                            }
                            case 2:
                                for (Period p : periods)
                                    if ((p.getStatus() == 1 || p.getStatus() == 2) && p.getCancellationReason() == null) {
                                        if (p.getNumber() == 0) {
                                            betEntry.setHomeScore(p.getTeam1Score());
                                            betEntry.setAwayScore(p.getTeam2Score());
                                        } else {
                                            betEntry.setHomeScoreHT(p.getTeam1Score());
                                            betEntry.setAwayScoreHT(p.getTeam2Score());
                                        }

                                    }
                                break;
                            default:
                                break;
                        }

                    }
                }
                betEntries.add(betEntry);
            }

        }
        return betEntries;
    }

    private List<Bet> betEntryListToBetList(List<BetEntry> betEntries) {
        List<Bet> betList = new ArrayList<>();
        if (betEntries.size() != 0) {
            for (BetEntry be : betEntries) {
                betList.add(new Bet(be.getBetId(),
                        Constants.USER,
                        new Event(be.getEventId(),
                                be.getLeagueId(),
                                be.getBetDetails().getStarts(),
                                be.getHome(),
                                be.getAway(),
                                be.getHomeScore(),
                                be.getAwayScore(),
                                be.getHomeScoreHT(),
                                be.getAwayScoreHT()),
                        be.getBetDetails().getBetType(),
                        be.getBetDetails().getBetName(),
                        be.getBetDetails().getPick(),
                        be.getBetDetails().getCoefficient(),
                        be.getBetDetails().getStatus()));
            }
        }
        return betList;
    }

    private void refreshHistory(List<Bet> betArrayList, BetResult betResult) {
        if (betsList.size() == 0) {
            betsList = betListToBetEntryList(betArrayList, betResult);
            for (BetEntry b: betsList)
                if (b.getBetDetails().getStatus() == 0 && (b.isFirstHalfFinished() || b.isFinished())) {
                    BetsCalculator.CalculateBet(b);
                    updateList.add(b);
                }
        }
        else {
            int betIndex;
            for(BetEntry b:betListToBetEntryList(betArrayList,betResult)) {
                if (b.getBetDetails().getStatus() == 0 && (b.isFirstHalfFinished() || b.isFinished())) {
                    BetsCalculator.CalculateBet(b);
                    updateList.add(b);
                }
                if ((betIndex = betsList.indexOf(b)) >= 0) {
                    if (betsList.get(betIndex).getBetDetails().getStatus() <= b.getBetDetails().getStatus() ||
                            betsList.get(betIndex).getHomeScore() + betsList.get(betIndex).getAwayScore() != b.getHomeScore() + b.getAwayScore() ||
                            betsList.get(betIndex).getHomeScoreHT() + betsList.get(betIndex).getAwayScoreHT() != b.getHomeScoreHT() + b.getAwayScoreHT())
                        betsList.set(betIndex, b);
                }
                else
                    betsList.add(b);
            }
            since = betResult == null ? 0 :betResult.getLast();
        }
    }

    private Set<Integer> getLeaguesIdArray(List<Bet> bets) {
        HashSet<Integer> leagues = new HashSet<>();
        if (bets.size() > 0)
            for (Bet b:bets)
                if (!b.isFinished() || b.getStatus() == 0)
                    leagues.add(b.getEvent().getLeagueId());
        return leagues;
    }

    public class HistoryRefreshTask extends AsyncTask<Void, Void, Void> {
        public HistoryRefreshTask() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Response responseDb = dbController.getAPI().getUserBetHistory(Constants.USER.getUsername()).execute();
                Response responsePinnacle = null;
                Set<Integer> leaguesId = getLeaguesIdArray((List<Bet>) responseDb.body());
                if (leaguesId.size() > 0)
                    responsePinnacle = apiController.getAPI().getSettledFixtures(Constants.SPORT_ID,leaguesId).execute();
                if (responseDb != null) {
                    List<Bet> betList = (List<Bet>) responseDb.body();
                    BetResult betResult = responsePinnacle == null ? null :(BetResult) responsePinnacle.body();
                    refreshHistory(betList,betResult);
                }
                mUpdateDbTask = new BetHistoryFragment.DbRefreshTask();
                mUpdateDbTask.execute((Void) null);
            } catch (IOException e) {
                e.printStackTrace();
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
    public class DbRefreshTask extends AsyncTask<Void, Void, Void> {
        public DbRefreshTask() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                dbController.getAPI().updateDb(betEntryListToBetList(updateList)).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Void nothing) {
            updateList.clear();
        }
    }
}
