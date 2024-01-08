package com.example.carpoolingapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;


public class ViewProfileFragment extends Fragment {
    LottieAnimationView bye;
    Button deleteAccount;
    DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_view_profile, container, false);

        bye = view.findViewById(R.id.goodbye);
        deleteAccount = view.findViewById(R.id.deletebtn);
        dbHelper = new DatabaseHelper(getContext());

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete Account");
                builder.setMessage("Are you sure you want to delete your account? This account will be deleted forever");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreference = getActivity().getSharedPreferences("myPrefers", Context.MODE_PRIVATE);
                        String currentUser = sharedPreference.getString("Email", "");

                        dbHelper.deleteUser(currentUser);
                        Toast.makeText(getContext(), " Account deleted successfully! ", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getActivity(), Login.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });

                builder.setNegativeButton("No", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }
}