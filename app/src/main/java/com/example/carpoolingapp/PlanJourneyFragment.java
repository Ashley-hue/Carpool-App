package com.example.carpoolingapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class PlanJourneyFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    EditText from, to, dat, tim;
    Spinner chooseCar, ratePerKm, noOfSeats;
    Button submit;
    Calendar calendar;
    int hour, minute;
    int month, day, year;

    List<String> regNumbers;

    DatabaseHelper helper;


    public PlanJourneyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_plan_journey, container, false);

        from = view.findViewById(R.id.city);
        to = view.findViewById(R.id.dest);
        dat = view.findViewById(R.id.when);
        tim = view.findViewById(R.id.whens);
        chooseCar = view.findViewById(R.id.carSelect);
        ratePerKm = view.findViewById(R.id.ratepkm);
        noOfSeats = view.findViewById(R.id.seatsNo);

        submit = view.findViewById(R.id.submitBtn);
        helper = new DatabaseHelper(getContext());

        dat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog pickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                        dat.setText(date);
                    }
                }, day, month, year);
                pickerDialog.show();
            }
        });

        tim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = hourOfDay + ":" + minute;
                        tim.setText(time);
                    }
                }, hour, minute, true);
                dialog.show();
            }
        });

        SharedPreferences preferences = getActivity().getSharedPreferences("myPrefers", Context.MODE_PRIVATE);
        String mail = preferences.getString("Email", "");

        regNumbers = helper.getRegistrations(mail);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_items, regNumbers);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseCar.setAdapter(arrayAdapter);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.rates, R.layout.spinner_items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ratePerKm.setAdapter(adapter);
        ratePerKm.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getContext(), R.array.seats, R.layout.spinner_items);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        noOfSeats.setAdapter(adapter1);
        noOfSeats.setOnItemSelectedListener(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String origin = from.getText().toString().trim();
                String destination = to.getText().toString().trim();
                String datt = dat.getText().toString().trim();
                String timm = tim.getText().toString().trim();
                String carRegistration = chooseCar.getSelectedItem().toString().trim();
                int raate = Integer.parseInt(ratePerKm.getSelectedItem().toString().trim());
                int seets = Integer.parseInt(noOfSeats.getSelectedItem().toString().trim());

                if(TextUtils.isEmpty(origin)){
                    from.setError("Origin is required!");
                    return;
                }
                if(TextUtils.isEmpty(destination)){
                    to.setError("Destination is required!");
                    return;
                }
                if(TextUtils.isEmpty(datt)){
                    dat.setError("Date is required!");
                    return;
                }
                if(TextUtils.isEmpty(timm)){
                    tim.setError("Time is required!");
                    return;
                }

                Trip trip = new Trip(origin, destination, datt, timm, carRegistration, raate, seets);
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myPrefers", Context.MODE_PRIVATE);
                String currentUser = sharedPreferences.getString("Email", "");



                boolean success = helper.addTrip(trip, currentUser);
                if(success){
                    from.setText("");
                    to.setText("");
                    dat.setText("");
                    tim.setText("");

                    Toast.makeText(getContext(), "Trip successfully added! ", Toast.LENGTH_SHORT).show();
                }
                else{
                    from.setText("");
                    to.setText("");
                    dat.setText("");
                    tim.setText("");

                    Toast.makeText(getContext(), "Failed to add trip! ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return  view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String txt = parent.getItemAtPosition(position).toString();
        String txt1 = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}