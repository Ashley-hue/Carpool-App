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


public class JourneyListFragment extends Fragment implements JourneyListAdapter.OnItemClickListener{

    RecyclerView journey;
    JourneyListAdapter listAdapter;
    List<Trip> trips;
    DatabaseHelper dataHelper;


    public JourneyListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_journey_list, container, false);

        journey = view.findViewById(R.id.recyclervieww);
        dataHelper = new DatabaseHelper(getContext());

        SharedPreferences sharedPreference = getActivity().getSharedPreferences("myPrefers", Context.MODE_PRIVATE);
        String currentUser = sharedPreference.getString("Email", "");

        trips = dataHelper.getTripList(currentUser);
        journey.setLayoutManager(new LinearLayoutManager(getActivity()));
        listAdapter = new JourneyListAdapter(getActivity(), trips);
        journey.setAdapter(listAdapter);

        listAdapter.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(int position) {
        Trip trip = trips.get(position);
        Intent intent = new Intent(getActivity(), UserRoles.class);
        intent.putExtra("Tricks", trip);
        startActivity(intent);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        trips.clear();
        SharedPreferences sharedPreference = getActivity().getSharedPreferences("myPrefers", Context.MODE_PRIVATE);
        String currentUser = sharedPreference.getString("Email", "");
        trips.addAll(dataHelper.getTripList(currentUser));
        listAdapter.notifyDataSetChanged();
    }

}