package com.example.jg.footballstats;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class MainScreenFragment extends Fragment {

    public interface OnCardClickListener {
        void onCardClick(int id);
    }

    private OnCardClickListener onCardClickListener;

    public MainScreenFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onCardClickListener = (OnCardClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnCardClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_screen, container, false);

        CardView cardViewEvents = rootView.findViewById(R.id.main_screen_events_card_view);
        CardView cardViewStats = rootView.findViewById(R.id.main_screen_stats_card_view);
        CardView cardViewHistory = rootView.findViewById(R.id.main_screen_history_card_view);
        ((ImageView)rootView.findViewById(R.id.main_screen_events_image_view)).setImageResource(Constants.IS_THEME_DARK ? R.drawable.ic_soccer_ball_in_front_of_the_arch_white : R.drawable.ic_soccer_ball_in_front_of_the_arch);
        ((ImageView)rootView.findViewById(R.id.main_screen_stats_image_view)).setImageResource(Constants.IS_THEME_DARK ? R.drawable.ic_analytics_white : R.drawable.ic_analytics);
        ((ImageView)rootView.findViewById(R.id.main_screen_history_image_view)).setImageResource(Constants.IS_THEME_DARK ? R.drawable.ic_medical_result_white : R.drawable.ic_medical_result);
        cardViewEvents.setOnClickListener(clickListener);
        cardViewStats.setOnClickListener(clickListener);
        cardViewHistory.setOnClickListener(clickListener);
        return rootView;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.getId();
            onCardClickListener.onCardClick(v.getId());
        }
    };
}
