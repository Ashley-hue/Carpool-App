package com.example.carpoolingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;


import com.google.android.material.navigation.NavigationView;

public class Admin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawings;
    Toolbar toolie;
    NavigationView navView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        toolie = findViewById(R.id.toolybar);
        setSupportActionBar(toolie);

        drawings = findViewById(R.id.draweer_layout);
        navView = findViewById(R.id.adm_nav_view);
        navView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggles = new ActionBarDrawerToggle(this, drawings, toolie, R.string.open_nav, R.string.close_nav);
        drawings.addDrawerListener(toggles);
        toggles.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frames_containers, new AdminHomeFragment()).commit();
            navView.setCheckedItem(R.id.admin_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.admin_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.frames_containers, new AdminHomeFragment()).commit();
                break;
            case R.id.admin_cars:
                getSupportFragmentManager().beginTransaction().replace(R.id.frames_containers, new OwnerListFragment()).commit();
                break;
            case R.id.admin_pass:
                getSupportFragmentManager().beginTransaction().replace(R.id.frames_containers, new PassengerListFragment()).commit();
                break;
            case R.id.admin_pay:
                getSupportFragmentManager().beginTransaction().replace(R.id.frames_containers, new PaymentsFragment()).commit();
                break;
            case R.id.admin_report:
                getSupportFragmentManager().beginTransaction().replace(R.id.frames_containers, new ReportsFragment()).commit();
                break;
            case R.id.admin_view:
                getSupportFragmentManager().beginTransaction().replace(R.id.frames_containers, new UpdateProfileFragment()).commit();
                break;
            case R.id.admin_del:
                getSupportFragmentManager().beginTransaction().replace(R.id.frames_containers, new ViewProfileFragment()).commit();
                break;
            case R.id.adm_logout:
                getSupportFragmentManager().beginTransaction().replace(R.id.frames_containers, new LogoutFragment()).commit();
                break;
        }

        drawings.closeDrawer(GravityCompat.START);
        return true;
    }
}