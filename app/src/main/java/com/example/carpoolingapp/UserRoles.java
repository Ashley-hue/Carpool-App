package com.example.carpoolingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class UserRoles extends AppCompatActivity {
    EditText starts, stops, daterr, timerr, carKind, theRate, theSeats;
    ListView listView;
    DatabaseHelper dbHelper;
    List<Trip> trippy;
    Trip tuhrip;
    ArrayList<String> passengerEmails;
    ArrayAdapter<String> passAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_roles);

        starts = findViewById(R.id.cities);
        stops = findViewById(R.id.dests);
        daterr = findViewById(R.id.datez);
        timerr = findViewById(R.id.timz);
        carKind = findViewById(R.id.carz);
        theRate = findViewById(R.id.rates);
        theSeats = findViewById(R.id.noseats);

//        passengerEmails = new ArrayList<>();
//        passAdapter = new ArrayAdapter<>(this, R.layout.list_view_item, R.id.listText, passengerEmails);
        listView = findViewById(R.id.listvview);
//        listView.setAdapter(passAdapter);

        dbHelper = new DatabaseHelper(this);

        starts.setEnabled(false);
        stops.setEnabled(false);
        daterr.setEnabled(false);
        timerr.setEnabled(false);
        carKind.setEnabled(false);
        theRate.setEnabled(false);
        theSeats.setEnabled(false);

        SharedPreferences sharedPreferences = this.getSharedPreferences("myPrefers", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("Email", "");

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey("Tricks")){
            tuhrip = (Trip) bundle.getSerializable("Tricks");
        }

        trippy = dbHelper.getTripList(userEmail);

        if(tuhrip != null){
            starts.setText(tuhrip.getSource());
            stops.setText(tuhrip.getDestination());
            daterr.setText(tuhrip.getDating());
            timerr.setText(tuhrip.getTiming());
            carKind.setText(tuhrip.getCarRegNo());
            theRate.setText(String.valueOf(tuhrip.getRatings()));
            theSeats.setText(String.valueOf(tuhrip.getSeatings()));
        }
    }
}