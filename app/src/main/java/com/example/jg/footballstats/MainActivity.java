package com.example.jg.footballstats;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle menuToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(menuToggle);
        menuToggle.syncState();

        final Intent inner_intent = new Intent(this, InnerActivity.class);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_camera:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, new FirstFragment(), "fragment1").commit();
                        itemChecker(item);
                        break;
                    case R.id.nav_setting:
                        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_camera));
                        inner_intent.putExtra("caption",item.getTitle());
                        startActivity(inner_intent);
                        break;
                    case R.id.nav_events:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, new EventsFragment(), "events_fragment").commit();
                        itemChecker(item);
                        break;
                    default:
                        itemChecker(item);
                        for(Fragment f: getSupportFragmentManager().getFragments())
                            if (f != null)
                                getSupportFragmentManager().beginTransaction().remove(f).commit();
                        //Fragment fragment = getSupportFragmentManager().findFragmentByTag("fragment1");

                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }

    private void itemChecker(MenuItem item){
        item.setChecked(true);
        getSupportActionBar().setTitle(item.getTitle());
    }
}