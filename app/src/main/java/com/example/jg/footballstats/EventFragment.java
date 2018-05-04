package com.example.jg.footballstats;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jg.footballstats.fixtures.EventEntry;
import com.example.jg.footballstats.odds.Event;
import com.example.jg.footballstats.odds.Moneyline;
import com.example.jg.footballstats.odds.Odd;
import com.example.jg.footballstats.odds.OddsList;
import com.example.jg.footballstats.odds.Period;
import com.example.jg.footballstats.odds.Spread;
import com.example.jg.footballstats.odds.TeamTotal;
import com.example.jg.footballstats.odds.Total;
import com.example.jg.footballstats.odds.Wager;
import com.example.jg.footballstats.odds.WagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EventFragment extends Fragment {

    private APIController apiController = APIController.getInstance();
    private List<Wager> wagers = new ArrayList<>();
    private RecyclerView recyclerView;
    private WagerAdapter recyclerAdapter;
    public EventFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_event, container, false);

        recyclerView = rootView.findViewById(R.id.event_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));


        final EventEntry item = getArguments().getParcelable("event");
        //((Toolbar) rootView.findViewById(R.id.toolbar)).setTitle("Event details");
        ((TextView) rootView.findViewById(R.id.event_home)).setText(item.getHome());
        ((TextView) rootView.findViewById(R.id.event_away)).setText(item.getAway());
        ((TextView) rootView.findViewById(R.id.event_start_time)).setText(item.getDate() + " " + item.getTime());
        final TextView scoreHome = rootView.findViewById(R.id.event_home_score);
        final TextView scoreAway = rootView.findViewById(R.id.event_away_score);
        apiController.getAPI().getOdds(29, item.getLeagueId(),"Decimal",item.getId()).enqueue(new Callback<OddsList>() {
            @Override
            public void onResponse(Call<OddsList> call, Response<OddsList> response) {
                if (response.body().getLeagues().size() != 1)
                    onFailure(call, new Throwable("Invalid leagues number"));
                else if (response.body().getLeagues().get(0).getEvents().size() != 1)
                    onFailure(call, new Throwable("Invalid events number"));
                else {
                    String eventHomeScore = Integer.toString(response.body().getLeagues().get(0).getEvents().get(0).getHomeScore());
                    String eventAwayScore =  Integer.toString(response.body().getLeagues().get(0).getEvents().get(0).getAwayScore());
                    scoreHome.setText(eventHomeScore);
                    scoreAway.setText(eventAwayScore);
                    refreshData(response.body().getLeagues().get(0).getEvents().get(0));
                    recyclerAdapter = new WagerAdapter(wagers);
                    recyclerView.setAdapter(recyclerAdapter);
                }
            }

            @Override
            public void onFailure(Call<OddsList> call, Throwable t) {
                LinearLayout linearLayout = rootView.findViewById(R.id.event_recycler_view_layout);
                recyclerView.setVisibility(View.GONE);
                scoreHome.setText("");
                scoreAway.setText("");
                TextView textView = new TextView(getContext());
                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
                textView.setText("Bets accepting is temporarily stopped");
                textView.setGravity(Gravity.CENTER);
                linearLayout.addView(textView);
                linearLayout.invalidate();
            }
        });
        return rootView;
    }

    private void refreshData(Event event) {
        String periodString;
        List<Period> periods = event.getPeriods();
        int periodNumber = 0;
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
                wagers.add(new Wager(periodString + "Moneyline",moneylineList));
            }
            if (handicaps != null) {
                List<Odd> handicapList = new ArrayList<>();
                for(Spread s:handicaps) {
                    String homeTeam ="", awayTeam = "";
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
                wagers.add(new Wager(periodString + "Handicap", handicapList));
            }
            if (totals != null) {
                List<Odd> totalsList = new ArrayList<>();
                for(Total t:totals) {
                    totalsList.add(new Odd("Over " + t.getStringPoints(), t.getOver()));
                    totalsList.add(new Odd("Under " + t.getStringPoints(), t.getUnder()));
                }
                wagers.add(new Wager(periodString + "Total",totalsList));
            }
            if (teamTotals != null) {
                List<Odd> firstTeamTotal = new ArrayList<>();
                firstTeamTotal.add(new Odd("Over " + teamTotals.getHome().getStringPoints(), teamTotals.getHome().getOver()));
                firstTeamTotal.add(new Odd("Under " + teamTotals.getHome().getStringPoints(), teamTotals.getHome().getUnder()));
                wagers.add(new Wager(periodString + "First team total",firstTeamTotal));
                List<Odd> secondTeamTotal = new ArrayList<>();
                secondTeamTotal.add(new Odd("Over " + teamTotals.getAway().getStringPoints(), teamTotals.getAway().getOver()));
                secondTeamTotal.add(new Odd("Under " + teamTotals.getAway().getStringPoints(), teamTotals.getAway().getUnder()));
                wagers.add(new Wager(periodString + "Second team total",secondTeamTotal));
            }
            ++periodNumber;
        }
    }

    private void setData() {
        List<Odd> moneyline = new ArrayList<>();
        moneyline.add(new Odd("First team to win", 1.23));
        moneyline.add(new Odd("Draw", 4.6));
        moneyline.add(new Odd("Second team to win", 12.8));

        List<Odd> total = new ArrayList<>();
        total.add(new Odd("Over 2.5", 1.9));
        total.add(new Odd("Under 2.5", 1.85));

        List<Odd> handicap = new ArrayList<>();
        handicap.add(new Odd("First team -1.5", 1.9));
        handicap.add(new Odd("Second team +1.5", 1.85));

        wagers.add(new Wager("Moneyline",moneyline));
        wagers.add(new Wager("Total",total));
        wagers.add(new Wager("Handicap",handicap));
    }

}
