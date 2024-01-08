package com.example.carpoolingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateAcc extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView profile;
    Button signin;
    EditText email, password, confirm, dob, phone, address;
    private Spinner gender;
    Calendar myCalendar;
    RadioGroup radioGroup;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);

        profile = findViewById(R.id.imageView);
        email = findViewById(R.id.editTextTextEmailAddress2);
        password = findViewById(R.id.editTextTextPassword2);
        confirm = findViewById(R.id.editTextTextPassword3);
        gender = findViewById(R.id.editTextTextPersonName);
        dob = findViewById(R.id.editTextDate);
        phone = findViewById(R.id.editTextPhone2);
        address = findViewById(R.id.hmaddress);
        signin = findViewById(R.id.cree);
        radioGroup = findViewById(R.id.rdGrp);

        myCalendar = Calendar.getInstance();
        databaseHelper = new DatabaseHelper(this);


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateAcc.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.genders, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);
        gender.setOnItemSelectedListener(this);


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString().trim().toLowerCase();
                String pass = password.getText().toString().trim().toLowerCase();
                String con = confirm.getText().toString().trim().toLowerCase();
                String genders = gender.getSelectedItem().toString().toLowerCase();
                String dobs = dob.getText().toString().trim().toLowerCase();
                String phones = phone.getText().toString().trim().toLowerCase();
                String addresss = address.getText().toString().trim().toLowerCase();


                String role = "";
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == R.id.rdbtn) {
                    role = "Car_Owner";
                } else if (selectedId == R.id.rdbtn2) {
                    role = "Passenger";
                }

                if(radioGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(CreateAcc.this, "Select a role!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d("CreateAcc", "Registering User with email: " + mail + "Password: " + pass + ", Gender: " + genders + ", DOB: " + dobs + ", Phone: " + phones + ", Address: " + addresss + ", Role: " + role);

                if (phone.length() > 10 || phone.length() < 10) {
                    Toast.makeText(CreateAcc.this, "Incorrect phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(mail.isEmpty()){
                    email.setError("Email is required!");
                    email.requestFocus();
                }
                else if(pass.isEmpty()){
                    password.setError("Password is required!");
                    password.requestFocus();
                }
                else if(con.isEmpty()){
                    confirm.setError("Confirm your password!");
                    confirm.requestFocus();
                }
                else if(dobs.isEmpty()){
                    dob.setError("Date is required!");
                    dob.requestFocus();
                }
                else if(phones.isEmpty()){
                    phone.setError("Phone number is required!");
                    phone.requestFocus();
                }
                else if(addresss.isEmpty()){
                    address.setError("Address is required!");
                    address.requestFocus();
                }
                else{
                    if(databaseHelper.checkEmail(mail)){
                        Toast.makeText(CreateAcc.this, "Email already exists, login", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CreateAcc.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        databaseHelper.addUser(mail,pass,genders, dobs,phones, addresss,role);
                        Toast.makeText(CreateAcc.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CreateAcc.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd//yy EEEE";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        dob.setText(dateFormat.format(myCalendar.getTime()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String txt = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), txt, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}