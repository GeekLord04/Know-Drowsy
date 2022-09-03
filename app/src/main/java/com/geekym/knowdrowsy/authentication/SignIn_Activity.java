package com.geekym.knowdrowsy.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geekym.knowdrowsy.MainActivity;
import com.geekym.knowdrowsy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn_Activity extends AppCompatActivity {

    TextView createAccount, forgotPassword;
    EditText Email, Password;
    private FirebaseAuth mAuth;
    Button LogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initialization();

        createAccount.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SignUp_Activity.class);
            startActivity(intent);
        });

        forgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
            startActivity(intent);
        });

        LogIn.setOnClickListener(view -> {
            String email = Email.getText().toString().trim();      //EditText -> String
            String pass = Password.getText().toString().trim();    //EditText -> String

            //Checking entered text if it is valid or not
            if (email.isEmpty()) {
                Email.setError("Field can't be empty");
                Email.requestFocus();
                return;
            } else if (pass.isEmpty()) {
                Password.setError("Field can't be empty");
                Password.requestFocus();
                return;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Email.setError("Please enter a valid email address");
                Email.requestFocus();
                return;
            } else if (pass.length() < 8) {
                Password.setError("Password must be at least 8 characters");
                Password.requestFocus();
                return;
            }


            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                    assert user != null;
                    if (!user.isEmailVerified()) {      //If the User's email is already verified
                        user.sendEmailVerification();
                        Toast.makeText(this, "Check your email to verify your account and Login again", Toast.LENGTH_SHORT).show();
                    } else {
                        //Send a verification link to the user
                        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent2);
                        finishAffinity();
                    }
                } else {
                    //If the Login Fails
                    Toast.makeText(this, "Failed to Login! Please check your credentials", Toast.LENGTH_SHORT).show();
                }
            });
        });


    }

    private void initialization() {
        createAccount = findViewById(R.id.createNew);
        mAuth = FirebaseAuth.getInstance();
        LogIn = findViewById(R.id.loginButton);
        Email = findViewById(R.id.SignIn_Email);
        Password = findViewById(R.id.SignIn_Password);
        forgotPassword = findViewById(R.id.forgotPassword);
    }
}