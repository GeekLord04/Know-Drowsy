package com.geekym.knowdrowsy.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.geekym.knowdrowsy.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText email;
    Button Reset;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initialization();

        Reset.setOnClickListener(view -> {
            ForgotPassword();
        });

    }

    //Validating text input by the User
    @SuppressLint("NotConstructor")
    private void ForgotPassword() {
        String Email = email.getText().toString().trim();
        if (Email.isEmpty()) {
            email.setError("Field can't be empty");
            email.requestFocus();

        } else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            email.setError("Please enter a valid Email id");
            email.requestFocus();

        } else { //If there user entered a valid email
            mAuth.sendPasswordResetEmail(Email).addOnCompleteListener(task -> {
                try {
                    if (task.isSuccessful()) { //Password Reset link is sent to user's Email

                        Toast.makeText(this, "Password reset email sent!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), SignIn_Activity.class));
                    } else {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }


    private void initialization() {
        email = findViewById(R.id.editTextTextEmailAddress);
        Reset = findViewById(R.id.send);
        mAuth = FirebaseAuth.getInstance();
    }
}