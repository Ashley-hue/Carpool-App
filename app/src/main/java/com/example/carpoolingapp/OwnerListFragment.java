package com.example.carpoolingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class OwnerListFragment extends Fragment {

    RecyclerView ownerRecycler;
    DatabaseHelper dbHelpper;
    List<Userr> ownerList;
    OwnerListAdapter ownerListAdapter;

    public OwnerListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_owner_list, container, false);

        ownerRecycler = view.findViewById(R.id.ownerRecycler);
        dbHelpper = new DatabaseHelper(getActivity());
        ownerList = dbHelpper.getAllCarOwners();

        ownerRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        ownerListAdapter = new OwnerListAdapter(ownerList, getActivity());
        ownerRecycler.setAdapter(ownerListAdapter);

        return view;
    }
}