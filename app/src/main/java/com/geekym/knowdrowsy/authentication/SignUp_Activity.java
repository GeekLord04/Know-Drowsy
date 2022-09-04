package com.geekym.knowdrowsy.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geekym.knowdrowsy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUp_Activity extends AppCompatActivity {

    Button createAccount;
    TextView signIn;
    EditText Name, Email, Password;
    boolean passwordVisible;
    private FirebaseAuth mAuth;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initialization();


        // Hide & Show Password
        Password.setOnTouchListener((v, event) -> {
            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= Password.getRight() - Password.getCompoundDrawables()[Right].getBounds().width()) {
                    int selection = Password.getSelectionEnd();
                    //Handles Multiple option popups
                    if (passwordVisible) {
                        //set drawable image here
                        Password.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility_off, 0);
                        //for hide password
                        Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordVisible = false;
                    } else {
                        //set drawable image here
                        Password.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility, 0);
                        //for show password
                        Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordVisible = true;
                    }
                    Password.setLongClickable(false); //Handles Multiple option popups
                    Password.setSelection(selection);
                    return true;
                }
            }
            return false;
        });

        signIn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SignIn_Activity.class);
            startActivity(intent);
        });


        createAccount.setOnClickListener(view -> {
            //Get all the input from the user
            String name = Name.getText().toString().trim();
            String email = Email.getText().toString().trim();
            String pass = Password.getText().toString().trim();


            //Check if the details entered are valid or not
            if (name.isEmpty()) {
                Name.setError("Field can't be empty");
                Name.requestFocus();
                return;
            }
            if (email.isEmpty()) {
                Email.setError("Field can't be empty");
                Email.requestFocus();
                return;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Email.setError("Please enter a valid email address");
                Email.requestFocus();
                return;
            } else if (pass.isEmpty()) {
                Password.setError("Field can't be empty");
                Password.requestFocus();
                return;
            } else if (pass.length() < 8) {
                Password.setError("Password must be at least 8 characters");
                Password.requestFocus();
                return;
            }


            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(SignUp_Activity.this, task -> {
                        if (task.isSuccessful()) {
                            //Successfully Created a new account

                            Users users = new Users(name, email); //Creating a User Object with the inputs by user

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                    .setValue(users).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(this, "Verification mail sent", Toast.LENGTH_SHORT).show(); //Send a verification link
                                            intentNow();
                                            assert user != null;
                                            user.sendEmailVerification();
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        });


    }

    private void intentNow() {
        startActivity(new Intent(getApplicationContext(), SignIn_Activity.class));
    }

    private void initialization() {
        createAccount = findViewById(R.id.createAccount);
        signIn = findViewById(R.id.SignIn);
        Name = findViewById(R.id.SignUp_Name);
        Email = findViewById(R.id.SignUp_Email);
        Password = findViewById(R.id.SignUp_Password);
        mAuth = FirebaseAuth.getInstance();
    }
}