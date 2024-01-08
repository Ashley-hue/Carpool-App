package com.example.carpoolingapp;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class CarDeatsFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ActivityResultLauncher<Intent> resultLauncher;

    ImageView profilepic;
    CardView cardView;
    EditText registrationNo, carName, modelYear;
    Button add;
    Uri imageUri;
    Login login = new Login();


    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_car_deats, container, false);

        registrationNo = view.findViewById(R.id.regno);
        carName = view.findViewById(R.id.carname);
        modelYear = view.findViewById(R.id.date);
        profilepic = view.findViewById(R.id.carOwnerprof);
        cardView = view.findViewById(R.id.cardview1);
        add = view.findViewById(R.id.btndetails);

        dbHelper = new DatabaseHelper(getContext());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myPrefers", Context.MODE_PRIVATE);
        String currentUser = sharedPreferences.getString("Email", "");

        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK){
                Intent data = result.getData();
                if (data != null && data.getData() != null) {
                    imageUri = data.getData();
                    profilepic.setImageURI(imageUri);

                }
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reg = registrationNo.getText().toString().trim();
                String vName = carName.getText().toString().trim();
                String mYear = modelYear.getText().toString().trim();
                String userEmail = currentUser;
                Log.d("CarDeatsFragment", "User Email:" + userEmail);

                if(TextUtils.isEmpty(reg)){
                    registrationNo.setError("Registration number is required!");
                    return;
                }
                if(TextUtils.isEmpty(vName)){
                    carName.setError("Name of car is required!");
                    return;
                }
                if(TextUtils.isEmpty(modelYear.getText().toString().trim())){
                    modelYear.setError("Year of model is required!");
                    return;
                }
                if (Integer.parseInt(mYear) < 1600 || Integer.parseInt(mYear) > 2023) {
                    modelYear.setError("Invalid model year");
                    return;
                }
                if(reg.length() < 7 || reg.length() > 7){
                    registrationNo.setError("Invalid registration number!");
                    return;
                }
                Car car = new Car(userEmail, reg, vName, Integer.parseInt(mYear), null);

                if(imageUri == null){
                    Toast.makeText(getContext(), "Upload car picture", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] image = stream.toByteArray();
                        car.setImage(image);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                boolean success = dbHelper.addCar(getContext(), car, userEmail);
                if(success){
                    registrationNo.setText("");
                    carName.setText("");
                    modelYear.setText("");
                    profilepic.setImageResource(R.drawable.deatsprof);

                    Toast.makeText(getContext(), "Car added successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    registrationNo.setText("");
                    carName.setText("");
                    modelYear.setText("");
                    profilepic.setImageResource(R.drawable.deatsprof);

                    Toast.makeText(getContext(), "Failed to add car", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        resultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }
}