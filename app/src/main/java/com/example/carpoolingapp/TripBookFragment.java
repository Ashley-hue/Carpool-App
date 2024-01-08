package com.example.carpoolingapp;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.List;

public class TripBookFragment extends Fragment implements BookedTripsAdapter.OnItemClickListener{

    RecyclerView bookedRecycler;
    DatabaseHelper helper;
    BookedTripsAdapter tripsAdapter;
    List<Trip> bookedTripp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip_book, container, false);

        bookedRecycler = view.findViewById(R.id.booked_trips);
        helper = new DatabaseHelper(getActivity());

        SharedPreferences sharedPreference = getActivity().getSharedPreferences("myPrefers", Context.MODE_PRIVATE);
        String currentUser = sharedPreference.getString("Email", "");

        bookedTripp = helper.getBookedTrips(currentUser);
        bookedRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        tripsAdapter = new BookedTripsAdapter(bookedTripp);
        bookedRecycler.setAdapter(tripsAdapter);

        tripsAdapter.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(int position) {
        Trip bookedTrip = bookedTripp.get(position);
        Intent intent = new Intent(getActivity(), MakePayment.class);
        intent.putExtra("BookedTrip", bookedTrip);
        startActivity(intent);
        tripsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreference = getActivity().getSharedPreferences("myPrefers", Context.MODE_PRIVATE);
        String userNow = sharedPreference.getString("Email", "");
        bookedTripp.clear();
        bookedTripp.addAll(helper.getBookedTrips(userNow));
        tripsAdapter.notifyDataSetChanged();
    }
}