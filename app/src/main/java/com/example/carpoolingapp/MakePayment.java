package com.example.carpoolingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class MakePayment extends AppCompatActivity {

    EditText des, tty, ppr;
    Button pay;
    DatabaseHelper helper;
    List<Trip> myTrip;
    Trip bTrips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);

        des = findViewById(R.id.arriving);
        tty = findViewById(R.id.timeofArrive);
        ppr = findViewById(R.id.priceRide);
        pay = findViewById(R.id.payTrip);

        des.setEnabled(false);
        tty.setEnabled(false);
        ppr.setEnabled(false);

        helper = new DatabaseHelper(this);

        Bundle bnd = getIntent().getExtras();
        if(bnd != null && bnd.containsKey("BookedTrip")){
            bTrips = (Trip) bnd.getSerializable("BookedTrip");
        }

        if (bTrips != null){
            des.setText(bTrips.getDestination());
            tty.setText(bTrips.getTiming());
            ppr.setText(bTrips.getPayment());
        }

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}