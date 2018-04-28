package com.example.jg.footballstats;

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

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private android.support.v4.app.FragmentManager fragmentManager;
    ActionBarDrawerToggle mDrawerToggle;

    private boolean mToolBarNavigationListenerIsRegistered = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        fragmentManager = getSupportFragmentManager();

        //getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, new EventsFragment(), "events_fragment").addToBackStack("events_fragment").commit();
        //navigationView.getMenu().findItem(R.id.nav_events).setChecked(true);





        //this is if you are using fragments
        /*setSupportActionBar(toolbar);
        drawerDrawable = new DrawerArrowAnimation.DrawerArrowDrawableToggle(this, getSupportActionBar().getThemedContext());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawerDrawable);*/
        setSupportActionBar(toolbar);
        /*mDrawerToggle = new ActionBarDrawerToggle (this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        // Setting the actionbarToggle to drawer layout
        drawerLayout.addDrawerListener(mDrawerToggle);
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });*/
        // Calling sync state is necessary to show your hamburger icon...
        // or so I hear. Doesn't hurt including it even if you find it works
        // without it on your test device(s)
        //mDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24px);

        getSupportFragmentManager().addOnBackStackChangedListener(new android.support.v4.app.FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Log.d("test", "backStackEntryCount: " + getSupportFragmentManager().getBackStackEntryCount());
            }
        });

        final Intent inner_intent = new Intent(this, InnerActivity.class);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                setHomeIconEnabled();
                switch (item.getItemId()) {
                    case R.id.nav_events:
                        getSupportActionBar().setTitle(item.getTitle());
                        //fragmentManager.popBackStackImmediate("events_fragment", 0);
                        setBackStackEmpty("events_fragment");
                        if (fragmentManager.findFragmentByTag("events_fragment") == null)
                            fragmentManager.beginTransaction()
                                    .replace(R.id.main_layout, new EventsFragment(), "events_fragment")
                                    .addToBackStack("events_fragment")
                                    .commit();
                        break;
                    case R.id.nav_logout:
                        getSupportActionBar().setTitle(item.getTitle());
                        //fragmentManager.popBackStackImmediate("sample_fragment", 0);
                        setBackStackEmpty("sample_fragment");
                        if (fragmentManager.findFragmentByTag("sample_fragment") == null)
                            fragmentManager.beginTransaction()
                                    .replace(R.id.main_layout, new FirstFragment(), "sample_fragment")
                                    .addToBackStack("sample_fragment")
                                    .commit();
                        break;
                    case R.id.nav_settings:
                        inner_intent.putExtra("caption",item.getTitle());
                        startActivity(inner_intent);
                        break;
                    case R.id.nav_stack:
                        getSupportActionBar().setTitle(item.getTitle());
                        Log.i("Fragment Manager fragments list size ", Integer.toString(fragmentManager.getFragments().size()));
                        Log.i("Entries ", Integer.toString(fragmentManager.getBackStackEntryCount()));;
                        for(android.support.v4.app.Fragment f:fragmentManager.getFragments())
                            Log.i("FM entries", f.getTag());
                        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i)
                            Log.i("BackStack entries ",fragmentManager.getBackStackEntryAt(i).getName());
                        break;
                    default:
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                if (fragmentManager.findFragmentByTag("event_fragment") != null) {
                    setHomeIconEnabled();
                    fragmentManager.popBackStackImmediate();
                }
                else
                    drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawers();
        if (fragmentManager.findFragmentByTag("event_fragment") != null) {
            setHomeIconEnabled();
        }
        super.onBackPressed();
    }
    private void setHomeIconEnabled() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24px);
    }
    private void setBackStackEmpty(String entryName) {
        int backStackSize = fragmentManager.getBackStackEntryCount();
        while (backStackSize > 0) {
            if (fragmentManager.getBackStackEntryAt(--backStackSize).getName() != entryName)
                fragmentManager.popBackStackImmediate();
            else
                break;
        }
    }
}