package com.example.jg.footballstats;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.AnimRes;
import android.support.annotation.AnimatorRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.jg.footballstats.fixtures.EventEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class MainActivity extends AppCompatActivity implements EventsFragment.OnItemSelectListener, MainScreenFragment.OnCardClickListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private Fragment mFragment = null;
    private android.support.v4.app.FragmentManager fragmentManager;

    int currentDrawerItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().hasExtra("bundle") && savedInstanceState==null){
            savedInstanceState = getIntent().getExtras().getBundle("bundle");
        }

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
        //navigationView.setItemTextColor(c);
        //navigationView.setItemIconTintList(c);
        //navigationView.setBackgroundTintList(c);

        navigationView.setBackgroundColor(getColor(Constants.IS_THEME_DARK ? R.color.primaryLightColorDark : R.color.primaryColorLight));
        navigationView.setItemBackgroundResource(Constants.IS_THEME_DARK ? R.drawable.navigation_drawer_item_background_dark : R.drawable.navigation_drawer_item_background_light);
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.navigation_name)).setTextColor(getColor(Constants.IS_THEME_DARK ? R.color.primaryTextColorDark : R.color.primaryTextColorLight));
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.navigation_email)).setTextColor(getColor(Constants.IS_THEME_DARK ? R.color.primaryTextColorDark : R.color.primaryTextColorLight));
        navigationView.getHeaderView(0).setBackgroundColor(getColor(Constants.IS_THEME_DARK ? R.color.primaryDarkColorDark : R.color.background_light));
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
                Bundle temp_bundle = new Bundle();
                onSaveInstanceState(temp_bundle);
                Intent intent = getIntent();
                intent.putExtra("bundle", temp_bundle);
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });

        fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() == 0)
            createFragment(getString(R.string.app_name), MainScreenFragment.class.getName(),
                    MainScreenFragment.class.getSimpleName(), R.id.main_layout, 0,0, android.R.anim.slide_in_left,android.R.anim.slide_out_right);

        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("isDrawerOpened"))
                drawerLayout.openDrawer(GravityCompat.START);
            String title = savedInstanceState.getString("toolbarTitle");
            getSupportActionBar().setTitle(title);
            ArrayList<String> fragmentTags = savedInstanceState.getStringArrayList("fragmentsTags");
            if (fragmentTags != null && fragmentTags.size() > 0) {
                for (String fragmentTag : fragmentTags) {
                    mFragment = fragmentManager.getFragment(savedInstanceState, fragmentTag);
                    if (mFragment != null) {
                        if (fragmentTag.equals(EventFragment.class.getSimpleName()))
                            setBackArrowIconEnabled();
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_layout, mFragment, fragmentTag)
                                .commit();
                    }
                }
            }
            currentDrawerItem = savedInstanceState.getInt("drawerCheckedItem");
            if (currentDrawerItem > -1)
                navigationView.setCheckedItem(currentDrawerItem);
            //navigationView.setCheckedItem(R.id.nav_stack);
                //setNavigationViewUnchecked(navigationView.getMenu());
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
                    createFragment(item.getTitle(), EventsFragment.class.getName(), EventsFragment.class.getSimpleName(), R.id.main_layout,
                            android.R.anim.slide_in_left,android.R.anim.slide_out_right, android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    break;
                case R.id.nav_profile:
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    break;
                case R.id.nav_history:
                    createFragment(item.getTitle(), BetHistoryFragment.class.getName(), BetHistoryFragment.class.getSimpleName(), R.id.main_layout,
                            android.R.anim.slide_in_left,android.R.anim.slide_out_right, android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    break;
                /*case R.id.nav_stack:
                    setHomeIconEnabled();
                    getSupportActionBar().setTitle(item.getTitle());
                    Log.i("Fragment Manager fragments list size ", Integer.toString(fragmentManager.getFragments().size()));
                    Log.i("Entries ", Integer.toString(fragmentManager.getBackStackEntryCount()));;
                    for(android.support.v4.app.Fragment f:fragmentManager.getFragments())
                        Log.i("FM entries", f.getTag());
                    for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i)
                        Log.i("BackStack entries ",fragmentManager.getBackStackEntryAt(i).getName());
                    break;*/
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
                    setNavigationViewUnchecked(navigationView.getMenu());
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
        outState.putString("toolbarTitle", toolbar.getTitle().toString());
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
        else {
            if (fragmentManager.findFragmentByTag("EventFragment") != null) {
                setHomeIconEnabled();
            }
            if (fragmentManager.getBackStackEntryCount() == 2) {
                toolbar.setTitle("Football Bets");
                currentDrawerItem = -1;
                setNavigationViewUnchecked(navigationView.getMenu());
                setBackStackEmpty(MainScreenFragment.class.getSimpleName());
            } else if (fragmentManager.getBackStackEntryCount() == 1) {
                finish();
            } else
                super.onBackPressed();
        }
    }

    @Override
    public void onItemSelect(EventEntry eventEntry) {
        mFragment = new EventFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("event",eventEntry);
        mFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right, android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                .replace(R.id.main_layout, mFragment,EventFragment.class.getSimpleName())
                .addToBackStack(EventFragment.class.getSimpleName())
                .commit();
        toolbar.setTitle("Event details");
        setBackArrowIconEnabled();
    }

    private void createFragment(CharSequence title, String className, String simpleClassName, int layoutResId, @AnimatorRes @AnimRes int enter,
                                @AnimatorRes @AnimRes int exit, @AnimatorRes @AnimRes int popEnter,
                                @AnimatorRes @AnimRes int popExit) {
        setHomeIconEnabled();
        setBackStackEmpty(className);
        getSupportActionBar().setTitle(title);
        if (fragmentManager.findFragmentByTag(className) == null)
            try {
                mFragment = (Fragment) Class.forName(className).newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        fragmentManager.beginTransaction()
                .setCustomAnimations(enter, exit, popEnter, popExit)
                .replace(layoutResId, mFragment, simpleClassName)
                .addToBackStack(simpleClassName)
                .commit();
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
    private void setNavigationViewUnchecked(Menu menu) {
        int size = menu.size();
        for (int i = 0; i < size; i++) {
            MenuItem item = menu.getItem(i);
            if(item.hasSubMenu()) {
                setNavigationViewUnchecked(item.getSubMenu());
            } else {
                if (item.isChecked())
                    item.setChecked(false);
            }
        }
    }
    private void setBackStackEmpty(String entryName) {
        int backStackSize = fragmentManager.getBackStackEntryCount();
        if (entryName == "")
            while (backStackSize-- > 1)
                fragmentManager.popBackStackImmediate();
        else
            while (backStackSize > 1) {
                if (fragmentManager.getBackStackEntryAt(--backStackSize).getName() != entryName)
                    fragmentManager.popBackStack();
                else
                    break;
            }
    }

    @Override
    public void onCardClick(int id) {
        switch (id) {
            case R.id.main_screen_events_card_view:
                navigationItemSelectedListener.onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_events));
                navigationView.setCheckedItem(R.id.nav_events);
                break;
            case R.id.main_screen_stats_card_view:
                navigationItemSelectedListener.onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_profile));
                break;
            case R.id.main_screen_history_card_view:
                navigationItemSelectedListener.onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_history));
                navigationView.setCheckedItem(R.id.nav_history);
                break;
            default:
                break;
        }
    }
}