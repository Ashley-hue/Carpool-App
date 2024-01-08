package com.example.carpoolingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.Toast;

public class RateTrip extends AppCompatActivity {

    RatingBar ratingBar;
    Trip rateTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_trip);

        ratingBar = findViewById(R.id.ratingBar);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey("Tree")){
            rateTrip = (Trip) bundle.getSerializable("Tree");
        }
    }
}