package com.example.jg.footballstats;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jg.footballstats.fixtures.EventEntry;


public class EventFragment extends Fragment {


    public EventFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);
        EventEntry item = getArguments().getParcelable("event");
        ((TextView) rootView.findViewById(R.id.event_home)).setText(item.getHome());
        ((TextView) rootView.findViewById(R.id.event_away)).setText(item.getAway());
        return rootView;
    }

}
