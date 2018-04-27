package com.example.jg.footballstats;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class EventFragment extends Fragment {

    protected DrawerLayout drawerLayout;
    protected ActionBarDrawerToggle toolbarDrawerToggle;

    public EventFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    public void setUp(ActionBarDrawerToggle toolbarDrawerToggle, DrawerLayout drawerLayout) {
        this.toolbarDrawerToggle = toolbarDrawerToggle;
        this.drawerLayout = drawerLayout;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home://this will handle back navigation click
                getActivity().onBackPressed();
                //MainActivity.HamburgerArrowAnimator.startAnimation(drawerLayout,toolbarDrawerToggle,1.0f,0.0f);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
