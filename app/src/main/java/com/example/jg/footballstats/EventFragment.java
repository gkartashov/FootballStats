package com.example.jg.footballstats;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);

        recyclerView = rootView.findViewById(R.id.event_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));


        EventEntry item = getArguments().getParcelable("event");
        ((TextView) rootView.findViewById(R.id.event_home)).setText(item.getHome());
        ((TextView) rootView.findViewById(R.id.event_away)).setText(item.getAway());
        ((TextView) rootView.findViewById(R.id.event_start_time)).setText(item.getDate() + " " + item.getTime());
        final TextView score = rootView.findViewById(R.id.event_score);
        apiController.getAPI().getOdds(29, item.getLeagueId(),"Decimal",item.getId()).enqueue(new Callback<OddsList>() {
            @Override
            public void onResponse(Call<OddsList> call, Response<OddsList> response) {
                if (response.body().getLeagues().size() != 1)
                    onFailure(call, new Throwable("Invalid leagues number"));
                else if (response.body().getLeagues().get(0).getEvents().size() != 1)
                    onFailure(call, new Throwable("Invalid events number"));
                else {
                    String eventScore = response.body().getLeagues().get(0).getEvents().get(0).getHomeScore() + "-" + response.body().getLeagues().get(0).getEvents().get(0).getAwayScore();
                    score.setText(eventScore);

                    refreshData(response.body().getLeagues().get(0).getEvents().get(0));

                    recyclerAdapter = new WagerAdapter(wagers);
                    recyclerView.setAdapter(recyclerAdapter);
                }
            }

            @Override
            public void onFailure(Call<OddsList> call, Throwable t) {
                String s = "govno";
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
                    handicapList.add(new Odd("First team " + s.getStringHdp(), s.getHome()));
                    handicapList.add(new Odd("Second team " + s.getStringHdp(), s.getAway()));
                }
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
