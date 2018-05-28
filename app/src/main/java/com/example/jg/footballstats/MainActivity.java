package com.example.jg.footballstats;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.jg.footballstats.fixtures.EventEntry;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements EventsFragment.OnItemSelectListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private Fragment mFragment = null;
    private android.support.v4.app.FragmentManager fragmentManager;

    int currentDrawerItem = -1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setTheme(Constants.IS_THEME_DARK ? R.style.AppTheme : R.style.AppTheme_Light);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getColor(Constants.IS_THEME_DARK ? R.color.primaryColorDark : R.color.primaryLightColorLight));
        setSupportActionBar(toolbar);
        setHomeIconEnabled();

        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed
        };

        int[] colors = new int[] {
                Color.BLACK,
                Color.RED,
                Color.GREEN,
                Color.BLUE
        };
        ColorStateList c = new ColorStateList(states,colors);

        ColorStateList textState = new ColorStateList(new int[][] {new int[] {android.R.attr.state_checked}, new int[] {-android.R.attr.state_checked}},
                new int[]{ColorUtils.setAlphaComponent(getColor(R.color.lossColor), 255), ColorUtils.setAlphaComponent(getColor(R.color.winColor), 255)});

        ColorStateList iconState = new ColorStateList(new int[][] {new int[] {android.R.attr.state_checked}, new int[] {-android.R.attr.state_checked}},
                new int[] {Color.rgb(255,46,84), (Color.BLACK)});

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);
        navigationView.setItemTextColor(c);
       // navigationView.setItemIconTintList(iconState);
        //navigationView.setBackgroundTintList(c);
        navigationView.setBackgroundColor(getColor(Constants.IS_THEME_DARK ? R.color.primaryLightColorDark : R.color.primaryColorLight));
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


        Switch themeSwitch = findViewById(R.id.drawer_switch);
        themeSwitch.setChecked(Constants.IS_THEME_DARK);
        themeSwitch.setTextColor(getColor(Constants.IS_THEME_DARK ? android.R.color.primary_text_dark : android.R.color.primary_text_light));
        themeSwitch.setText(Constants.IS_THEME_DARK ? R.string.switch_theme_light : R.string.switch_theme_dark);
        themeSwitch.setBackgroundColor(getColor(Constants.IS_THEME_DARK ? R.color.primaryLightColorDark : R.color.primaryColorLight));
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences = getSharedPreferences("Settings",MODE_PRIVATE);
                Constants.IS_THEME_DARK = isChecked;
                sharedPreferences.edit().putBoolean("isThemeDark",isChecked).apply();
                recreate();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("isDrawerOpened"))
                drawerLayout.openDrawer(GravityCompat.START);
            currentDrawerItem = savedInstanceState.getInt("drawerCheckedItem");
            if (currentDrawerItem > -1)
                navigationView.setCheckedItem(currentDrawerItem);
            ArrayList<String> fragmentTags = savedInstanceState.getStringArrayList("fragmentsTags");
            if (fragmentTags != null && fragmentTags.size() > 0) {
                for (String fragmentTag : fragmentTags) {
                    mFragment = fragmentManager.getFragment(savedInstanceState, fragmentTag);
                    if (mFragment != null) {
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_layout, mFragment, fragmentTag)
                                .commit();
                    }
                }
            }
        }

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
            switch (item.getItemId()) {
                case R.id.nav_events:
                    setHomeIconEnabled();
                    getSupportActionBar().setTitle(item.getTitle());
                    setBackStackEmpty(EventsFragment.class.getSimpleName());
                    if (fragmentManager.findFragmentByTag(EventsFragment.class.getSimpleName()) == null)
                        mFragment = new EventsFragment();
                        fragmentManager.beginTransaction()
                                .add(R.id.main_layout, mFragment, EventsFragment.class.getSimpleName())
                                .addToBackStack(EventsFragment.class.getSimpleName())
                                .commit();
                    break;
                case R.id.nav_profile:
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    break;
                case R.id.nav_history:
                    setHomeIconEnabled();
                    getSupportActionBar().setTitle(item.getTitle());
                    setBackStackEmpty(BetHistoryFragment.class.getSimpleName());
                    if (fragmentManager.findFragmentByTag(BetHistoryFragment.class.getSimpleName()) == null)
                        mFragment = new BetHistoryFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_layout, mFragment, BetHistoryFragment.class.getSimpleName())
                                .addToBackStack(BetHistoryFragment.class.getSimpleName())
                                .commit();
                    break;
                case R.id.nav_stack:
                    setHomeIconEnabled();
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
                    SharedPreferences preferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove("User");
                    editor.apply();
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
            currentDrawerItem = item.getItemId();
            drawerLayout.closeDrawers();
            return true;
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isDrawerOpened", drawerLayout.isDrawerOpen(GravityCompat.START));
        outState.putInt("drawerCheckedItem",currentDrawerItem);
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null && fragments.size() > 0) {
            ArrayList<String> fragmentsTags = new ArrayList<>();
            for (Fragment f : fragments) {
                fragmentManager.putFragment(outState,f.getTag(),f);
                fragmentsTags.add(f.getTag());
            }
            outState.putStringArrayList("fragmentsTags", fragmentsTags);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                if (fragmentManager.findFragmentByTag("EventFragment") != null) {
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
        if (fragmentManager.findFragmentByTag("EventFragment") != null) {
            setHomeIconEnabled();
        }
        if (fragmentManager.getBackStackEntryCount() == 1) {
            toolbar.setTitle("Football Bets");
            currentDrawerItem = -1;
            setNavigationViewUnchecked();
        }
        super.onBackPressed();
    }

    @Override
    public void onItemSelect(EventEntry eventEntry) {
        mFragment = new EventFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("event",eventEntry);
        mFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
                .replace(R.id.main_layout, mFragment,EventFragment.class.getSimpleName())
                .addToBackStack(EventFragment.class.getSimpleName())
                .commit();
        toolbar.setTitle("Event details");
        setBackArrowIconEnabled();
    }

    private void setHomeIconEnabled() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(Constants.IS_THEME_DARK ? R.drawable.ic_menu_white_24px : R.drawable.ic_menu_black_24px);
    }

    private void setBackArrowIconEnabled() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(Constants.IS_THEME_DARK ? R.drawable.ic_arrow_left_white : R.drawable.ic_arrow_left_black);
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
                    fragmentManager.popBackStack();
                else
                    break;
            }
    }
}