package com.geekym.knowdrowsy.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.geekym.knowdrowsy.R;

public class SignIn_Activity extends AppCompatActivity {

    TextView createAccount;
    EditText Email, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initialization();


        createAccount.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SignUp_Activity.class);
            startActivity(intent);
        });

    }

    private void initialization() {
        createAccount = findViewById(R.id.createNew);
    }
}