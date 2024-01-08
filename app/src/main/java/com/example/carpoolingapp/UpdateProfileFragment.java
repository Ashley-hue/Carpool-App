package com.example.carpoolingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;


public class UpdateProfileFragment extends Fragment {
    ImageView pic;
    EditText email, dob, phone, address;
    Spinner gender;
    Button updateButton;
    Userr currentUser;

    private boolean isEditable = false;
    DatabaseHelper databaseHelper;

    public UpdateProfileFragment(){};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);

        pic = view.findViewById(R.id.imageView);
        email = view.findViewById(R.id.editTextTextEmailAddress2);
        gender = view.findViewById(R.id.genger);
        dob = view.findViewById(R.id.editTextDate);
        phone = view.findViewById(R.id.editTextPhone2);
        address = view.findViewById(R.id.hmaddress1);
        updateButton = view.findViewById(R.id.cree1);
        databaseHelper = new DatabaseHelper(getContext());

        email.setEnabled(false);
        gender.setEnabled(false);
        dob.setEnabled(false);
        phone.setEnabled(false);
        address.setEnabled(false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myPrefers", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("Email", "");

        currentUser = databaseHelper.getUser(userEmail);

        email.setText(currentUser.getEmail());
        String gen = currentUser.getGender();

        if(gen != null){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.genders, R.layout.spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            gender.setAdapter(adapter);
            int position = adapter.getPosition(gen);
            gender.setSelection(position);
        }
        dob.setText(currentUser.getDob());
        phone.setText(currentUser.getPhone());
        address.setText(currentUser.getAddress());

        updateButton.setText(isEditable ? "Save Details" : "Update Details");

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEditable){

                    String ph = phone.getText().toString();
                    String ad = address.getText().toString();
                    String g = gender.getSelectedItem().toString();

                    if(ph.isEmpty()){
                        phone.setError("Phone number is required!");
                        return;
                    }
                    if(phone.length() < 10 || phone.length() > 10){
                        phone.setError("Phone number must be 10 digits");
                        return;
                    }
                    if(ad.isEmpty()){
                        address.setError("Address is required!");
                        return;
                    }

                    currentUser.setPhone(ph);
                    currentUser.setAddress(ad);
                    currentUser.setGender(g);

                    databaseHelper.updateUser(currentUser);

                    gender.setEnabled(false);
                    phone.setEnabled(false);
                    address.setEnabled(false);

                    phone.setText(currentUser.getPhone());
                    address.setText(currentUser.getAddress());
                    String gen = currentUser.getGender();
                    if(gen != null){
                        ArrayAdapter<CharSequence> adapterr = ArrayAdapter.createFromResource(getContext(), R.array.genders, R.layout.spinner_item);
                        adapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        gender.setAdapter(adapterr);
                        int position = adapterr.getPosition(gen);
                        gender.setSelection(position);
                    }

                    updateButton.setText("Update Details");

                    Snackbar.make(getView(), "Details updated successfully!", Snackbar.LENGTH_SHORT).show();

                    isEditable = false;
                }
                else{
                    gender.setEnabled(true);
                    phone.setText("");
                    phone.setEnabled(true);
                    address.setText("");
                    address.setEnabled(true);

                    updateButton.setText("Save Details");

                    isEditable = true;
                }
            }
        });


        return  view;
    }
}