package com.example.jg.footballstats;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AboutFragment extends Fragment {


    public AboutFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        ImageView logo = rootView.findViewById(R.id.about_logo);
        logo.setImageResource(Constants.IS_THEME_DARK ? R.drawable.ic_logo_final_dark : R.drawable.ic_logo_final);
        return rootView;
    }

}
