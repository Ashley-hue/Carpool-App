package com.example.carpoolingapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.List;

public class PassengerRecyclerDetails extends AppCompatActivity {

    private static final String TAG = "PassengerRecyclerDeats";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    EditText driver1, phony, city1, dest1, date1, time1, car1, rate1, seats1, pricy;
    Button checkP, book;
    DatabaseHelper debs;
    List<Trip> trippin;
    Trip selectTrip;

    private ActivityResultLauncher<Intent> leMapLauncher = registerForActivityResult(new
            ActivityResultContracts.StartActivityForResult(), result -> {
        if(result.getResultCode() == Activity.RESULT_OK){
            Intent deats = result.getData();
            float resultData = deats.getFloatExtra("distance", 0);
            Log.d("Passenger Recycler Deat", "Data received " + resultData);
            int rates = Integer.parseInt(rate1.getText().toString());
            int price = (int) (resultData * rates);
            pricy.setText(String.valueOf(price));
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_recycler_details);

        driver1 = findViewById(R.id.drivername);
        phony = findViewById(R.id.phonies);
        city1 = findViewById(R.id.passcity);
        dest1 = findViewById(R.id.passdests);
        date1 = findViewById(R.id.passdatez);
        time1 = findViewById(R.id.passtimz);
        car1 = findViewById(R.id.passcarz);
        rate1 = findViewById(R.id.passrates);
        seats1 = findViewById(R.id.passnoseats);
        pricy = findViewById(R.id.price);
        checkP = findViewById(R.id.dapricin);
        book = findViewById(R.id.dabookin);

        debs = new DatabaseHelper(this);

        driver1.setEnabled(false);
        phony.setEnabled(false);
        city1.setEnabled(false);
        dest1.setEnabled(false);
        date1.setEnabled(false);
        time1.setEnabled(false);
        car1.setEnabled(false);
        rate1.setEnabled(false);
        seats1.setEnabled(false);
        pricy.setEnabled(false);
        book.setEnabled(false);
        book.setAlpha(0.5f);

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("Trip")) {
            selectTrip = (Trip) extras.getSerializable("Trip");
        }

        if(selectTrip != null){
            driver1.setText(selectTrip.getMerlin());
            phony.setText(selectTrip.getPhoon());
            city1.setText(selectTrip.getSource());
            dest1.setText(selectTrip.getDestination());
            date1.setText(selectTrip.getDating());
            time1.setText(selectTrip.getTiming());
            car1.setText(selectTrip.getCarRegNo());
            rate1.setText(String.valueOf(selectTrip.getRatings()));
            seats1.setText(String.valueOf(selectTrip.getSeatings()));
        }


        if(isServicesOK()){
            checkP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sourceLoc = city1.getText().toString();
                    String destLoc = dest1.getText().toString();

                    Intent intent = new Intent(PassengerRecyclerDetails.this,LeMap.class);
                    Log.d("Passenger Recycler Deat", "Button has been clicked");
                    intent.putExtra("sourceLoc", sourceLoc);
                    intent.putExtra("destLoc", destLoc);
                    leMapLauncher.launch(intent);

                    checkP.setEnabled(false);
                    checkP.setAlpha(0.5f);

                    book.setEnabled(true);
                    book.setAlpha(1.0f);
                }
            });
        }

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String driver = driver1.getText().toString().trim();
                int price = Integer.parseInt(pricy.getText().toString().trim());
                String dat = date1.getText().toString().trim();
                String time = time1.getText().toString().trim();

                SharedPreferences sharedPreferences = getSharedPreferences("myPrefers", Context.MODE_PRIVATE);
                String currentUser = sharedPreferences.getString("Email", "");

                int tripId = debs.getTripId(driver, dat, time);

                Paymeant pay = new Paymeant(driver, price);
                boolean success = debs.addPayment(pay, currentUser, tripId);
                if(success){
                    Toast.makeText(PassengerRecyclerDetails.this, "Trip booked successfully", Toast.LENGTH_SHORT).show();
                    book.setEnabled(false);
                    book.setAlpha(0.5f);
                }
                else{
                    Toast.makeText(PassengerRecyclerDetails.this, "Failed to book trip", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available  = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(PassengerRecyclerDetails.this);
        if(available == ConnectionResult.SUCCESS){
            Log.d(TAG, "isServicesOK: Google play services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG, "isServicesOK: Error occured but is fixable");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(PassengerRecyclerDetails.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}