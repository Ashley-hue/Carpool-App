package com.example.carpoolingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;


public class CarListFragment extends Fragment implements CarListAdapter.onCarClickListener, CarListAdapter.carDeleteListener{

    RecyclerView recyclerView;
    CarListAdapter carListAdapter;
    List<Car> carList;
    DatabaseHelper helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_car_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);

        SharedPreferences sharedPreference = getActivity().getSharedPreferences("myPrefers", Context.MODE_PRIVATE);
        String currentUser = sharedPreference.getString("Email", "");

        helper = new DatabaseHelper(getActivity());
        carList = helper.getCarsList(currentUser);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        carListAdapter = new CarListAdapter(carList, getActivity(), this, this);
        recyclerView.setAdapter(carListAdapter);

        return view;
    }

    @Override
    public void onCarClick(int position) {
        Toast.makeText(getContext(), "Item clicked!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCarDelete(int position) {
        SharedPreferences pref = getActivity().getSharedPreferences("myPrefers", Context.MODE_PRIVATE);
        String userCurrent = pref.getString("Email", "");
        Car car = carList.get(position);
        helper = new DatabaseHelper(getContext());
        boolean success = helper.deleteCar(getContext(), car.getReg(), userCurrent);

        if(success){
            carList.remove(position);
            carListAdapter.notifyItemRemoved(position);
            Toast.makeText(getContext(), "Car removed successfully", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(), "deletion failed!", Toast.LENGTH_SHORT).show();
        }
    }
}