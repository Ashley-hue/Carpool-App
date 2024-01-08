package com.example.carpoolingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class RateFragment extends Fragment implements JourneyListAdapter.OnItemClickListener{

    RecyclerView rati;
    DatabaseHelper helpa;
    List<Trip> rateList;
    JourneyListAdapter rateAdapter;

    public RateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_rate, container, false);

        rati = view.findViewById(R.id.rating_trips);
        helpa = new DatabaseHelper(getContext());

        SharedPreferences sharedPreference = getActivity().getSharedPreferences("myPrefers", Context.MODE_PRIVATE);
        String currentUser = sharedPreference.getString("Email", "");

        rateList = helpa.getPassengerTrips(currentUser);
        rati.setLayoutManager(new LinearLayoutManager(getActivity()));
        rateAdapter = new JourneyListAdapter(getActivity(), rateList);
        rati.setAdapter(rateAdapter);

        rateAdapter.setOnItemClickListener(this);
        return  view;
    }

    @Override
    public void onItemClick(int position) {
        Trip tree = rateList.get(position);
        Intent intent = new Intent(getActivity(), RateTrip.class);
        intent.putExtra("Tree", tree); // Pass the trip ID to RateTrip activity
        startActivity(intent);
    }
}