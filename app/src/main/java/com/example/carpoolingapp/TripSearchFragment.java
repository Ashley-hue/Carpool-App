package com.example.carpoolingapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TripSearchFragment extends Fragment implements JourneyListAdapter.OnItemClickListener{
    RecyclerView tripRecyclerView;
    List<Trip> tripses;
    JourneyListAdapter journeyListAdapter;
    DatabaseHelper databees;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_trip_search, container, false);

        searchView = view.findViewById(R.id.searchView);
        searchView.clearFocus();

        tripRecyclerView = view.findViewById(R.id.tripRecycler);
        databees = new DatabaseHelper(getContext());

        tripses = databees.getTripsList();
        tripRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        journeyListAdapter = new JourneyListAdapter(getActivity(), tripses);
        tripRecyclerView.setAdapter(journeyListAdapter);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                journeyListAdapter.getFilter().filter(newText);
                return false;
            }

        });

        journeyListAdapter.setOnItemClickListener(this::onItemClick);

        return view;
    }


    @Override
    public void onItemClick(int position) {
        Trip trip = tripses.get(position);
        Intent intent = new Intent(getActivity(), PassengerRecyclerDetails.class);
        intent.putExtra("Trip", trip);
        startActivity(intent);
        journeyListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        tripses.clear();
        tripses.addAll(databees.getTripsList());
        journeyListAdapter.notifyDataSetChanged();
    }

}