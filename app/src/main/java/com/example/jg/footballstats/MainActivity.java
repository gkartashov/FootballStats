package com.example.jg.footballstats;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.jg.footballstats.fixtures.EventEntry;


public class MainActivity extends AppCompatActivity implements EventsFragment.OnItemSelectListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private android.support.v4.app.FragmentManager fragmentManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setHomeIconEnabled();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);

        navigationView.getHeaderView(0).findViewById(R.id.navigation_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackStackEmpty("");
                Constants.USER = null;
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        });
        if (Constants.USER != null) {
            ((TextView)navigationView.getHeaderView(0).findViewById(R.id.navigation_name)).setText(Constants.USER.getName());
            ((TextView)navigationView.getHeaderView(0).findViewById(R.id.navigation_email)).setText(Constants.USER.getEmail());
        }

        fragmentManager = getSupportFragmentManager();

        getSupportFragmentManager().addOnBackStackChangedListener(new android.support.v4.app.FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Log.d("test", "backStackEntryCount: " + getSupportFragmentManager().getBackStackEntryCount());
            }
        });

    }

    public NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            setHomeIconEnabled();
            switch (item.getItemId()) {
                case R.id.nav_events:
                    getSupportActionBar().setTitle(item.getTitle());
                    setBackStackEmpty("events_fragment");
                    if (fragmentManager.findFragmentByTag("events_fragment") == null)
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_layout, new EventsFragment(), "events_fragment")
                                .addToBackStack("events_fragment")
                                .commit();
                    break;
                case R.id.nav_profile:
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    break;
                case R.id.nav_history:
                    getSupportActionBar().setTitle(item.getTitle());
                    setBackStackEmpty("history_fragment");
                    if (fragmentManager.findFragmentByTag("history_fragment") == null)
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_layout, new BetHistoryFragment(), "history_fragment")
                                .addToBackStack("history_fragment")
                                .commit();
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
                case R.id.nav_logout:
                    setBackStackEmpty("");
                    Constants.USER = null;
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                    break;
                case R.id.nav_exit:
                    finishAndRemoveTask();
                default:
                    for(Fragment f: fragmentManager.getFragments())
                        if (f != null)
                            fragmentManager.beginTransaction().remove(f).commit();
            }
            drawerLayout.closeDrawers();
            return true;
        }
    };

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
        if (fragmentManager.getBackStackEntryCount() == 1) {
            toolbar.setTitle("Football Stats");
            setNavigationViewUnchecked();
        }
        super.onBackPressed();
    }

    @Override
    public void onItemSelect(EventEntry eventEntry) {
        EventFragment eventFragment = new EventFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("event",eventEntry);
        eventFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_out_right, R.anim.slide_in_right)
                .replace(R.id.main_layout, eventFragment,"event_fragment")
                .addToBackStack("event_fragment")
                .commit();
        toolbar.setTitle("Event details");
        setBackArrowIconEnabled();
    }

    private void setHomeIconEnabled() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24px);
    }

    private void setBackArrowIconEnabled() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left_white);
    }
    private void setNavigationViewUnchecked() {
        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }
    private void setBackStackEmpty(String entryName) {
        int backStackSize = fragmentManager.getBackStackEntryCount();
        if (entryName == "")
            while (backStackSize-- > 0)
                fragmentManager.popBackStackImmediate();
        else
            while (backStackSize > 0) {
                if (fragmentManager.getBackStackEntryAt(--backStackSize).getName() != entryName)
                    fragmentManager.popBackStackImmediate();
                else
                    break;
            }
    }
}