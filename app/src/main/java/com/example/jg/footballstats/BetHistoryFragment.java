package com.example.jg.footballstats;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
                case -1:
                    betsAcceptingRestrictionInitialization();
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

    private List<BetEntry> updateList = new ArrayList<>();
    private long since;

    public BetHistoryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_bet_history, container, false);
        setHasOptionsMenu(true);

        ((AppCompatActivity)getActivity()).getSupportActionBar().getTitle();
        mRecyclerView = rootView.findViewById(R.id.history_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();

        animator.setSupportsChangeAnimations(false);
        mRecyclerView.setItemAnimator(animator);

        //mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));

        mExpandableItemManager = new RecyclerViewExpandableItemManager(null);
        mAdapter = new BetEntryAdapter(getContext(), Constants.BETS_LIST);
        mWrappedAdapter = mExpandableItemManager.createWrappedAdapter(mAdapter);
        mRecyclerView.setAdapter(mWrappedAdapter);
        mExpandableItemManager.attachRecyclerView(mRecyclerView);

        mHandler = new BetHistoryFragment.RefreshingHandler();
        mSwipeRefreshLayout = rootView.findViewById(R.id.history_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        if (Constants.BETS_LIST.size() == 0)
            onRefreshListener.onRefresh();

        return rootView;
    }

    private void betsAcceptingRestrictionInitialization() {
        LinearLayout linearLayout = rootView.findViewById(R.id.history_recycler_view_layout);
        mRecyclerView.setVisibility(View.GONE);
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        textView.setText("No bets yet");
        textView.setGravity(Gravity.CENTER);
        linearLayout.addView(textView);
        linearLayout.invalidate();
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
        mAdapter.addAllGroups(Constants.BETS_LIST);

        mWrappedAdapter = mExpandableItemManager.createWrappedAdapter(mAdapter);
        mRecyclerView.setAdapter(mWrappedAdapter);
        mWrappedAdapter.notifyDataSetChanged();
        mExpandableItemManager.attachRecyclerView(mRecyclerView);
        mRecyclerView.setVisibility(View.VISIBLE);
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
                        new BetDetails(event.getStarts(),b.getBetType(),b.getBetName(),b.getPick(),b.getCoefficient(),b.getRealCoefficient(),b.getStatus()));
                if (betResult != null && betResult.getLeagues() != null) {
                    int leagueIndex, eventIndex;
                    if ((leagueIndex = betResult.getLeagues().indexOf(new com.example.jg.footballstats.history.League(event.getLeagueId()))) >= 0 &&
                            (eventIndex = betResult.getLeagues().get(leagueIndex).getEvents().indexOf(new com.example.jg.footballstats.history.Event(event.getEventId()))) >= 0) {
                        List<Period> periods = betResult.getLeagues().get(leagueIndex).getEvents().get(eventIndex).getPeriods();
                        switch (periods.size()) {
                            case 1: {
                                Period p = periods.get(0);
                                if (p.getNumber() == 1 && (p.getStatus() == 1 || p.getStatus() == 2) && p.getCancellationReason() == null) {
                                    //betEntry.setHomeScore(p.getTeam1Score());
                                    betEntry.setHomeScoreHT(p.getTeam1Score());
                                    //betEntry.setAwayScore(p.getTeam2Score());
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
                                        } else if (p.getNumber() == 1) {
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
                        be.getBetDetails().getRealCoefficient(),
                        be.getBetDetails().getStatus()));
            }
        }
        return betList;
    }

    private void refreshHistory(List<Bet> betArrayList, BetResult betResult) {
        if (Constants.BETS_LIST.size() == 0) {
            Constants.BETS_LIST = betListToBetEntryList(betArrayList, betResult);
            for (BetEntry b: Constants.BETS_LIST)
                if (b.getBetDetails().getStatus() == 0 && (b.isFirstHalfFinished() || b.isFinished())) {
                    BetsCalculator.CalculateBet(b);
                    updateList.add(b);
                }
        } else {
            int betIndex;
            for(BetEntry b:betListToBetEntryList(betArrayList,betResult)) {
                if (b.getBetDetails().getStatus() == 0 && (b.isFirstHalfFinished() || b.isFinished())) {
                    BetsCalculator.CalculateBet(b);
                    updateList.add(b);
                }
                if ((betIndex = Constants.BETS_LIST.indexOf(b)) >= 0) {
                    if (Constants.BETS_LIST.get(betIndex).getBetDetails().getStatus() <= b.getBetDetails().getStatus() ||
                            Constants.BETS_LIST.get(betIndex).getHomeScore() + Constants.BETS_LIST.get(betIndex).getAwayScore() != b.getHomeScore() + b.getAwayScore() ||
                            Constants.BETS_LIST.get(betIndex).getHomeScoreHT() + Constants.BETS_LIST.get(betIndex).getAwayScoreHT() != b.getHomeScoreHT() + b.getAwayScoreHT())
                        Constants.BETS_LIST.set(betIndex, b);
                }
                else
                    Constants.BETS_LIST.add(0,b);
            }
            since = betResult == null ? 0 :betResult.getLast();
        }
    }



    public class HistoryRefreshTask extends AsyncTask<Void, Void, Boolean> {
        public HistoryRefreshTask() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean isRefreshed = true;
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
                    mUpdateDbTask = new BetHistoryFragment.DbRefreshTask();
                    mUpdateDbTask.execute((Void) null);
                } else
                    isRefreshed = false;
            } catch (IOException e) {
                Snackbar.make(mSwipeRefreshLayout,e.getMessage().substring(0, 1).toUpperCase() + e.getMessage().substring(1),Snackbar.LENGTH_LONG);
            }
            return isRefreshed;
        }

        @Override
        protected void onPostExecute(final Boolean isRefreshed) {
            mHistoryRefreshTask = null;
            if (isRefreshed)
                if (Constants.BETS_LIST.isEmpty())
                    mHandler.sendEmptyMessage(-1);
                else
                    mHandler.sendEmptyMessage(0);
            else
                mHandler.sendEmptyMessage(-1);
            mSwipeRefreshLayout.setRefreshing(false);
        }

        private Set<Integer> getLeaguesIdArray(List<Bet> bets) {
            HashSet<Integer> leagues = new HashSet<>();
            if (bets.size() > 0)
                for (Bet b:bets)
                    if (!b.isFinished() || b.getStatus() == 0)
                        leagues.add(b.getEvent().getLeagueId());
            return leagues;
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
