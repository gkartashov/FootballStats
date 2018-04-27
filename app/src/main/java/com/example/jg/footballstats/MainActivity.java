package com.example.jg.footballstats;

import android.animation.ValueAnimator;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.util.Log;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    /*public static class HamburgerArrowAnimator {
        public static void startAnimation(final DrawerLayout drawerLayout,final ActionBarDrawerToggle toggle, float start, float end) {
            ValueAnimator anim = ValueAnimator.ofFloat(start, end);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float slideOffset = (Float) valueAnimator.getAnimatedValue();
                    toggle.onDrawerSlide(drawerLayout, slideOffset);
                    toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
            });
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(500);
            anim.start();
        }
    }*/

    private NavigationViewFragment navigationViewFragment;
    private DrawerLayout drawerLayout;
    private com.example.jg.footballstats.ActionBarDrawerToggle.DrawerToggle drawerToggle;
    private android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
    private Toolbar toolbar;
    private NavigationView navigationView;
    private LinearLayout contentLayout;
    public static String nextFragment = "MainActivity";
    private String currentFragment = "MainActivity";
    private Runnable[] runnables = new Runnable[NavigationViewFragment.ANIMATION_DURATION + 1];

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);

        navigationViewFragment = (NavigationViewFragment) fragmentManager.findFragmentByTag("navigation_drawer_fragment");
        navigationViewFragment.setUp(drawerLayout, toolbar);
        //navigationViewFragment.getActionBarDrawerToggle().setToolbarNavigationClickListener(navigationClickListener);
        drawerToggle = navigationViewFragment.getActionBarDrawerToggle().getSlider();

        for (int i = 0; i < runnables.length; i++) {
            final float position = i / (float) NavigationViewFragment.ANIMATION_DURATION;
            runnables[i] = new Runnable() {
                @Override
                public void run() {
                    drawerToggle.setPosition(position);
                }
            };
        }
        navigationView = findViewById(R.id.navigation_view);
        contentLayout = findViewById(R.id.content_layout);


        contentLayout.addOnLayoutChangeListener(layoutChangeListener);


                /*if (currentFragment.equals(firstFragment.getClass().getSimpleName())) {
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_left_exit)
                            .replace(R.id.content_layout, secondFragment)
                            .commit();
                    current_fragment = secondFragment.getClass().getSimpleName();
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    for (int i = 0; i < runnables.length; i++)
                        drawerLayout.postDelayed(runnables[i], i);
                    drawerLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            navigationDrawerFragment.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
                        }
                    }, NavigationDrawerFragment.ANIMATION_DURATION);
                } else {
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_right_enter, R.anim.slide_right_exit)
                            .replace(R.id.content_layout, firstFragment)
                            .commit();
                    current_fragment = firstFragment.getClass().getSimpleName();
                    navigationDrawerFragment.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
                    for (int i = 0; i < runnables.length; i++)
                        drawerLayout.postDelayed(runnables[runnables.length - i - 1], i);
                    drawerLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        }
                    }, NavigationDrawerFragment.ANIMATION_DURATION);
                }
            }*/
        //getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, new EventsFragment(), "events_fragment").addToBackStack("events_fragment").commit();
        //navigationView.getMenu().findItem(R.id.nav_events).setChecked(true);

        //this is if you are using fragments
        /*setSupportActionBar(toolbar);
        drawerDrawable = new DrawerArrowAnimation.DrawerArrowDrawableToggle(this, getSupportActionBar().getThemedContext());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawerDrawable);*/

        /*ActionBarDrawerToggle menuToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(menuToggle);
        menuToggle.setDrawerIndicatorEnabled(true);
        menuToggle.syncState();*/

        fragmentManager.addOnBackStackChangedListener(new android.support.v4.app.FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Log.d("test", "backStackEntryCount: " + getSupportFragmentManager().getBackStackEntryCount());
            }
        });

        final Intent inner_intent = new Intent(this, InnerActivity.class);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_events:
                        getSupportActionBar().setTitle(item.getTitle());
                        nextFragment = "EventsFragment";
                        fragmentManager.popBackStackImmediate("events_fragment", 0);
                        if (fragmentManager.findFragmentByTag("events_fragment") == null) {
                            EventsFragment f = new EventsFragment();
                            //f.setUp(toggle, drawerLayout);
                            fragmentManager.beginTransaction()
                                    .replace(R.id.content_layout, f, "events_fragment")
                                    .addToBackStack("events_fragment")
                                    .commit();
                        }
                        break;
                    case R.id.nav_logout:
                        getSupportActionBar().setTitle(item.getTitle());
                        nextFragment = "FirstFragment";
                        fragmentManager.popBackStackImmediate("sample_fragment", 0);
                        if (fragmentManager.findFragmentByTag("sample_fragment") == null)
                            fragmentManager.beginTransaction()
                                    .replace(R.id.content_layout, new FirstFragment(), "sample_fragment")
                                    .addToBackStack("sample_fragment")
                                    .commit();
                        break;
                    case R.id.nav_settings:
                        nextFragment = "SettingsActivity";
                        inner_intent.putExtra("caption",item.getTitle());
                        startActivity(inner_intent);
                        break;
                    case R.id.nav_stack:
                        getSupportActionBar().setTitle(item.getTitle());
                        Log.i("Fragment Manager fragments list size ", Integer.toString(fragmentManager.getFragments().size()));
                        Log.i("Entries ", Integer.toString(fragmentManager.getBackStackEntryCount()));;
                        for(android.support.v4.app.Fragment f:fragmentManager.getFragments())
                            Log.i("FM entries", f.getTag());
                        break;
                    default:
                        nextFragment = "MainActivity";
                        for(Fragment f: fragmentManager.getFragments())
                            if (f != null)
                                fragmentManager.beginTransaction().remove(f).commit();
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawers();
        int size = fragmentManager.getFragments().size();
        if (size > 1) {
            fragmentManager.popBackStackImmediate();
            --size;
            if (size != 1) {
                currentFragment = fragmentManager.getFragments().get(size - 1).getClass().getSimpleName();
            }
        }
        else
            super.onBackPressed();
    }
    public View.OnLayoutChangeListener layoutChangeListener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            if (currentFragment != nextFragment)
            switch (nextFragment) {
                case "EventFragment":
                    currentFragment = "EventFragment";
                    for (int i = 0; i < runnables.length; i++)
                        drawerLayout.postDelayed(runnables[i], i);
                    drawerLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            navigationViewFragment.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
                        }
                    }, NavigationViewFragment.ANIMATION_DURATION);
                    break;
                default:
                    navigationViewFragment.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
                    for (int i = 0; i < runnables.length; i++)
                        drawerLayout.postDelayed(runnables[runnables.length - i - 1], i);
                    drawerLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        }
                    }, NavigationViewFragment.ANIMATION_DURATION);
            }
        }
            /*if (currentFragment.equals(firstFragment.getClass().getSimpleName())) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_left_exit)
                        .replace(R.id.content_layout, secondFragment)
                        .commit();
                current_fragment = secondFragment.getClass().getSimpleName();
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                for (int i = 0; i < runnables.length; i++)
                    drawerLayout.postDelayed(runnables[i], i);
                drawerLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        navigationDrawerFragment.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
                    }
                }, NavigationDrawerFragment.ANIMATION_DURATION);
            } else {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_right_enter, R.anim.slide_right_exit)
                        .replace(R.id.content_layout, firstFragment)
                        .commit();
                current_fragment = firstFragment.getClass().getSimpleName();
                navigationDrawerFragment.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
                for (int i = 0; i < runnables.length; i++)
                    drawerLayout.postDelayed(runnables[runnables.length - i - 1], i);
                drawerLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    }
                }, NavigationDrawerFragment.ANIMATION_DURATION);
            }*/
    };
}