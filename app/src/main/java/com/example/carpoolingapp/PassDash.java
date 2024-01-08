package com.example.carpoolingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class PassDash extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout layout;
    Toolbar toolbarr;
    NavigationView navView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_dash);

        toolbarr = findViewById(R.id.pass_toolbar);
        setSupportActionBar(toolbarr);

        layout = findViewById(R.id.pass_layout);
        navView = findViewById(R.id.pass_nav_view);
        navView .setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle barDrawerToggle = new ActionBarDrawerToggle(this, layout, toolbarr, R.string.open_nav, R.string.close_nav);
        layout.addDrawerListener(barDrawerToggle);
        barDrawerToggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomePassFragment()).commit();
            navView.setCheckedItem(R.id.pass_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pass_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomePassFragment()).commit();
                break;
            case R.id.trip_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TripSearchFragment()).commit();
                break;
            case R.id.book_trip:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TripBookFragment()).commit();
                break;
            case R.id.rate_trip:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RateFragment()).commit();
                break;
            case R.id.upd_prof:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UpdateProfileFragment()).commit();
                break;
            case R.id.pass_view_prof:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ViewProfileFragment()).commit();
                break;
            case R.id.pass_logout:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LogoutFragment()).commit();
                break;
        }
        layout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if(layout.isDrawerOpen(GravityCompat.START)){
            layout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }
}