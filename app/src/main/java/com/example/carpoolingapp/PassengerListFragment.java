package com.example.carpoolingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class PassengerListFragment extends Fragment {

    RecyclerView passRecycler;
    DatabaseHelper databaseHelper;
    List<Userr> passengerList;
    OwnerListAdapter passengerAdapter;

    public PassengerListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_passenger_list, container, false);

        passRecycler = view.findViewById(R.id.passengerRecycler);

        databaseHelper = new DatabaseHelper(getActivity());
        passengerList = databaseHelper.getAllPassengers();

        passRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        passengerAdapter = new OwnerListAdapter(passengerList, getActivity());
        passRecycler.setAdapter(passengerAdapter);
        return view;
    }
}