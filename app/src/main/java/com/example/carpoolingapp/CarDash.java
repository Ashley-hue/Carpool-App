package com.example.carpoolingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class CarDash extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_dash);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();
                break;
            case R.id.nav_car_detas:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new CarDeatsFragment()).commit();
                break;
            case R.id.nav_car_list:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new CarListFragment()).commit();
                break;
            case R.id.nav_journ:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new PlanJourneyFragment()).commit();
                break;
            case R.id.nav_journ_list:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new JourneyListFragment()).commit();
                break;
            case R.id.nav_upd:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new UpdateProfileFragment()).commit();
                break;
            case R.id.nav_view_prof:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new ViewProfileFragment()).commit();
                break;
            case R.id.nav_logout:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new LogoutFragment()).commit();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }
}