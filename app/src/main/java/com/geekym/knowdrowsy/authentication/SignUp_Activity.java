package com.geekym.knowdrowsy.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.geekym.knowdrowsy.R;

public class SignUp_Activity extends AppCompatActivity {

    Button createAccount;
    TextView signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initialization();


        signIn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SignIn_Activity.class);
            startActivity(intent);
        });


    }

    private void initialization() {
        createAccount = findViewById(R.id.createAccount);
        signIn = findViewById(R.id.SignIn);
    }
}