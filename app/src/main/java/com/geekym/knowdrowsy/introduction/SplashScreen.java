package com.geekym.knowdrowsy.introduction;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.geekym.knowdrowsy.MainActivity;
import com.geekym.knowdrowsy.R;
import com.geekym.knowdrowsy.authentication.SignIn_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        mAuth = FirebaseAuth.getInstance(); //initialize Firebase Auth

        FirebaseUser currentUser = mAuth.getCurrentUser(); //Get the current user

        if (currentUser == null)

            startActivity(new Intent(this, SignIn_Activity.class)); //If the user email is not verified
            //If the user has not logged in, send them to On-Boarding Activity

        else {
            //If user was logged in last time

            new Handler().postDelayed(() -> {

                Intent loginIntent;

                if (currentUser.isEmailVerified())
                    loginIntent = new Intent(this, MainActivity.class); //If the user email is verified
                else
                    loginIntent = new Intent(this, SignIn_Activity.class); //If the user email is not verified

                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
                finish();
            }, 2000);

        }
    }
}