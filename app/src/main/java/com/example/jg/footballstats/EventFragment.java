package com.example.jg.footballstats;


import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import com.example.jg.footballstats.db.User;
import com.example.jg.footballstats.fixtures.EventEntry;
import com.example.jg.footballstats.odds.Moneyline;
import com.example.jg.footballstats.odds.Odd;
import com.example.jg.footballstats.odds.OddsList;
import com.example.jg.footballstats.odds.Period;
import com.example.jg.footballstats.odds.Spread;
import com.example.jg.footballstats.odds.TeamTotal;
import com.example.jg.footballstats.odds.Total;
import com.example.jg.footballstats.odds.Wager;
import com.example.jg.footballstats.odds.WagerAdapter;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;


public class EventFragment extends Fragment {

    private APIController apiController = APIController.getInstance();
    private List<Wager> wagers = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mWrappedAdapter;
    private WagerAdapter mAdapter;
    private RecyclerViewExpandableItemManager mExpandableItemManager;
    private TextView scoreHome, scoreAway, date, home, away;
    private View rootView;
    private EventEntry event;
    private List<Period> periods = new ArrayList<>();
    private long since = 0;
    private int homeScore = 0, awayScore = 0;
    public EventFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_event, container, false);
        viewInitialization();
        getOdds();
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
                getOdds();
        }
        return super.onOptionsItemSelected(item);
    }

    public Callback<OddsList> oddsListCallback = new Callback<OddsList>() {
            @Override
            public void onResponse(Call<OddsList> call, Response<OddsList> response) {
                OddsList oddsList = response.body();
                if (oddsList.getLeagues().size() != 1)
                    onFailure(call, new Throwable("Invalid leagues number"));
                else if (oddsList.getLeagues().get(0).getEvents().size() != 1)
                    onFailure(call, new Throwable("Invalid events number"));
                else {
                    if (isPeriodsRefreshed(oddsList.getLeagues().get(0).getEvents().get(0).getPeriods(), oddsList.getLast()))
                        refreshData();
                    oddsListInitialization(oddsList);
                }
            }

            @Override
            public void onFailure(Call<OddsList> call, Throwable t) {
                //if (since == 0) {
                    betsAcceptingRestrictionInitialization();
                //}
            }
    };

    public IOnItemClickListener clickListener = new IOnItemClickListener<Odd>() {
        @Override
        public void onItemClick(Odd item) {
            /*DatabaseAPIController.getInstance().getAPI().getUserBetHistory(Constants.USER.getUsername()).enqueue(new Callback<List<Bet>>() {
                @Override
                public void onResponse(Call<List<Bet>> call, Response<List<Bet>> response) {
                    Snackbar mSnackbar = Snackbar.make(rootView,response.body().toString(),Snackbar.LENGTH_LONG);
                    mSnackbar.show();
                }

                @Override
                public void onFailure(Call<List<Bet>> call, Throwable t) {
                    t.printStackTrace();
                }
            });*/
            /*APIController.getInstance().getAPI().getSettledFixtures(29,event.getLeagueId()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String govn = "";
                    try {
                        govn = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    govn += "govn";
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });*/
            String betName = null;
            String pick = item.getType();
            if (!item.getWagerType().contains("Moneyline")) {
                Matcher m = Constants.DOUBLE_REGEX.matcher(item.getType());
                if (m.find()) {
                    pick = m.group(0);
                    betName = item.getType().split(pick)[0].trim();
                }
            }
            Event e = new Event(event.getId(),event.getLeagueId(),event.getStarts(),event.getHome(),event.getAway(),0,0,0,0);
            Bet bet = new Bet(Constants.USER,e,item.getWagerType(),betName,pick,item.getCoefficient(),0);
            DatabaseAPIController.getInstance().getAPI().placeBet(bet).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Snackbar mSnackbar = Snackbar.make(rootView,"Done!",Snackbar.LENGTH_LONG);
                    mSnackbar.show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    };

    private void viewInitialization() {
        setHasOptionsMenu(true);

        recyclerView = rootView.findViewById(R.id.event_recycler_view);
        try {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        }
        catch (NullPointerException e) {
            throw new NullPointerException("Application context error: unable to get context from main activity.");
        }
        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();

        animator.setSupportsChangeAnimations(false);
        recyclerView.setItemAnimator(animator);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));

        mExpandableItemManager = new RecyclerViewExpandableItemManager(null);
        mAdapter = new WagerAdapter(wagers, clickListener);
        mWrappedAdapter = mExpandableItemManager.createWrappedAdapter(mAdapter);
        recyclerView.setAdapter(mWrappedAdapter);
        mExpandableItemManager.attachRecyclerView(recyclerView);

        try {
            event = getArguments().getParcelable("event");
        }
        catch (NullPointerException e) {
            throw new NullPointerException("Bundle restoring error: invalid arguments.");
        }
        home = rootView.findViewById(R.id.event_home);
        home.setText(event.getHome());
        away = rootView.findViewById(R.id.event_away);
        away.setText(event.getAway());
        date = rootView.findViewById(R.id.event_start_time);
        date.setText(event.getStartDateTime());
        scoreHome = rootView.findViewById(R.id.event_home_score);
        scoreAway = rootView.findViewById(R.id.event_away_score);
    }

    private void oddsListInitialization(OddsList oddsList) {
        homeScore = oddsList.getLeagues().get(0).getEvents().get(0).getHomeScore();
        awayScore = oddsList.getLeagues().get(0).getEvents().get(0).getAwayScore();
        scoreHome.setText(Integer.toString(homeScore));
        scoreAway.setText(Integer.toString(awayScore));
        recyclerView.setVisibility(View.VISIBLE);
        if (event.isStarted()) {
            setHasOptionsMenu(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setHasOptionsMenu(true);
                }
            }, 5000);
        }
        WrapperAdapterUtils.releaseAll(mWrappedAdapter);
        mExpandableItemManager.release();
        mExpandableItemManager = new RecyclerViewExpandableItemManager(null);

        mAdapter = new WagerAdapter(new ArrayList<Wager>(), clickListener);
        mAdapter.addAllGroups(wagers);

        mWrappedAdapter = mExpandableItemManager.createWrappedAdapter(mAdapter);
        recyclerView.setAdapter(mWrappedAdapter);
        mWrappedAdapter.notifyDataSetChanged();
        mExpandableItemManager.attachRecyclerView(recyclerView);
    }

    private void betsAcceptingRestrictionInitialization() {
        LinearLayout linearLayout = rootView.findViewById(R.id.event_recycler_view_layout);
        recyclerView.setVisibility(View.GONE);
        scoreHome.setText("");
        scoreAway.setText("");
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        textView.setText("Bets accepting is temporarily stopped");
        textView.setGravity(Gravity.CENTER);
        linearLayout.addView(textView);
        linearLayout.invalidate();
    }

    private void getOdds() {
        //if (since == 0)
            //apiController.getAPI().getOdds(29, event.getLeagueId(),"Decimal",event.getId()).enqueue(oddsListCallback);
        //else
            apiController.getAPI().getOddsSince(29, event.getLeagueId(),"Decimal", since, event.getId()).enqueue(oddsListCallback);
    }

    private boolean isPeriodsRefreshed(List<Period> newPeriods, long lastUpdate) {
        boolean isRefreshed = false;
        for (Period p: newPeriods)
            if (periods.contains(p)) {
                int index = periods.indexOf(p);
                if (periods.get(index).isOutdated(p.toLocalTime()) || p.getLineId() != periods.get(index).getLineId()) {
                    isRefreshed = true;
                    periods.set(index,p);
                }
            }
            else {
                periods.add(p);
                isRefreshed = true;
            }
        this.since = lastUpdate;
        return isRefreshed;
    }

    private void refreshData() {
        String periodString;
        int periodNumber = 0;
        wagers.clear();
        for(Period p:periods) {
            Moneyline moneyline = p.getMoneyline();
            List<Spread> handicaps = p.getSpreads();
            List<Total> totals = p.getTotals();
            TeamTotal teamTotals = p.getTeamTotal();

            switch(periodNumber){
                case 1: periodString = "1st half "; break;
                case 2: periodString = "2nd half "; break;
                default: periodString = "";
            }
            if (moneyline != null) {
                List<Odd> moneylineList = new ArrayList<>();
                moneylineList.add(new Odd("First team to win", moneyline.getHome()));
                moneylineList.add(new Odd("Draw", moneyline.getDraw()));
                moneylineList.add(new Odd("Second team to win", moneyline.getAway()));
                Wager wager = new Wager(periodString + "Moneyline",moneylineList);
                mAdapter.addListIdToChild(wager);
                wagers.add(wager);
            }
            /*if (handicaps != null) {
                List<Odd> handicapList = new ArrayList<>();
                int ballDifference = Math.abs(homeScore - awayScore);

                for(Spread s:handicaps) {
                    String homeTeam ="", awayTeam = "";
                    if (ballDifference != 0)
                        s.setHdp(s.getHdp() + ballDifference);
                    if (s.getHdp() < 0) {
                        homeTeam = "-";
                        s.setHdp(Math.abs(s.getHdp()));
                    }
                    else
                        awayTeam = "-";
                    handicapList.add(new Odd("First team " + homeTeam  + s.getStringHdp(), s.getHome()));
                    handicapList.add(new Odd("Second team " + awayTeam + s.getStringHdp(), s.getAway()));
                }
                Collections.sort(handicapList, new Comparator<Odd>() {
                    @Override
                    public int compare(Odd o1, Odd o2) {
                        return o1.getType().compareTo(o2.getType());
                    }
                });
                Wager wager = new Wager(periodString + "Handicap", handicapList);
                mAdapter.addListIdToChild(wager);
                wagers.add(wager);
            }*/
            if (totals != null) {
                List<Odd> totalsList = new ArrayList<>();
                for(Total t:totals) {
                    totalsList.add(new Odd("Over " + t.getStringPoints(), t.getOver()));
                    totalsList.add(new Odd("Under " + t.getStringPoints(), t.getUnder()));
                }
                Wager wager = new Wager(periodString + "Total",totalsList);
                mAdapter.addListIdToChild(wager);
                wagers.add(wager);
            }
            if (teamTotals != null) {
                if (teamTotals.getHome() != null) {
                    List<Odd> firstTeamTotal = new ArrayList<>();
                    firstTeamTotal.add(new Odd("Over " + teamTotals.getHome().getStringPoints(), teamTotals.getHome().getOver()));
                    firstTeamTotal.add(new Odd("Under " + teamTotals.getHome().getStringPoints(), teamTotals.getHome().getUnder()));
                    Wager wager = new Wager(periodString + "First team total", firstTeamTotal);
                    mAdapter.addListIdToChild(wager);
                    wagers.add(wager);
                }
                if (teamTotals.getAway() != null) {
                    List<Odd> secondTeamTotal = new ArrayList<>();
                    secondTeamTotal.add(new Odd("Over " + teamTotals.getAway().getStringPoints(), teamTotals.getAway().getOver()));
                    secondTeamTotal.add(new Odd("Under " + teamTotals.getAway().getStringPoints(), teamTotals.getAway().getUnder()));
                    Wager wager =new Wager(periodString + "Second team total", secondTeamTotal);
                    mAdapter.addListIdToChild(wager);
                    wagers.add(wager);
                }
            }
            ++periodNumber;
        }
    }
}
