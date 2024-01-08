package com.example.carpoolingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class PaymentsFragment extends Fragment {

    RecyclerView recyclerViewPays;
    DatabaseHelper helper;
    List<Paymeant> paymeantList;
    PaymentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payments, container, false);

        recyclerViewPays = view.findViewById(R.id.payRecycler);

        helper = new DatabaseHelper(getActivity());
        paymeantList = helper.getPayments();

        recyclerViewPays.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new PaymentAdapter(paymeantList, getActivity());
        recyclerViewPays.setAdapter(adapter);

        return view;
    }
}