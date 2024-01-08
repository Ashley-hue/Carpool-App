package com.example.carpoolingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    EditText em, pass;
    TextView forgot, createAccs;
    Button login;
    DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        em = findViewById(R.id.editTextTextEmailAddress);
        pass = findViewById(R.id.editTextTextPassword);
        forgot = findViewById(R.id.textView);
        createAccs = findViewById(R.id.textView3);
        login = findViewById(R.id.button);


        helper = new DatabaseHelper(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ema = em.getText().toString().trim().toLowerCase();
                String ps = pass.getText().toString().trim();

                if(ema.isEmpty()){
                    em.setError("Email is required!");
                    em.requestFocus();
                }
                else if(ps.isEmpty()){
                    pass.setError("Enter password!");
                    pass.requestFocus();
                }
                else if(!helper.checkEmail(ema)){
                    em.setError("Email not found");
                    em.requestFocus();
                }
                else if(!helper.checkEmailPassword(ema, ps)){
                    pass.setError("Incorrect password!");
                    pass.requestFocus();
                }
                else{
                    SharedPreferences preferences = getSharedPreferences("myPrefers", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("Email", ema);
                    editor.apply();

                    String role = helper.getRole(ema);

                    if(role.equals("Car_Owner")){
                        helper.setCurrentUserEmail(getApplicationContext(), ema);
                        Intent intent = new Intent(Login.this, CarDash.class);
                        intent.putExtra("Email", ema);
                        startActivity(intent);
                        finish();
                    }else if(role.equals("Passenger")){
                        helper.setCurrentUserEmail(Login.this, ema);
                        Intent intent = new Intent(Login.this, PassDash.class);
                        intent.putExtra("Email", ema);
                        startActivity(intent);
                        finish();
                    }
                    else if(role.equals("")){
                        helper.setCurrentUserEmail(Login.this, ema);
                        Intent intent = new Intent(Login.this, Admin.class);
                        intent.putExtra("Email", ema);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        createAccs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, CreateAcc.class));
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}