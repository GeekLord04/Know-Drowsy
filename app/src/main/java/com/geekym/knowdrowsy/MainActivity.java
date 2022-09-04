package com.geekym.knowdrowsy;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import com.geekym.knowdrowsy.authentication.SignIn_Activity;
import com.geekym.knowdrowsy.authentication.Users;
import com.geekym.knowdrowsy.helpers.MLVideoHelperActivity;
import com.geekym.knowdrowsy.helpers.object.DriverDrowsinessDetectionActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.ncorti.slidetoact.SlideToActView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button location_btn,camera_btn;
    TextView location_txt,camera_txt,date_txt,username;
    Date currentTime;
    private TextClock textClock;
    FusedLocationProviderClient fusedLocationProviderClient;
    SlideToActView slider;
    CircularImageView profile;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialisation();

        //Firebase Fetching
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users userprofile = snapshot.getValue(Users.class);

                if (userprofile != null){
                    String fullname = userprofile.name;

                    username.setText("Hello "+fullname+"!"+"\nDrive safe");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //setting Date and Time
        currentTime = Calendar.getInstance().getTime();
        String formattedDate = DateFormat.getDateInstance().format(currentTime);

        date_txt.setText(formattedDate);
        textClock.setFormat24Hour("hh:mm:ss a  EEE MMM d");

        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            location_btn.setVisibility(View.GONE);
            LocationGet();

        }

        profile.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, SignIn_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });


        //Granting Location Permission
        location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    location_btn.setVisibility(View.GONE);
                    LocationGet();

                }
                else{
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }
            }
        });
        //end

        if (ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED){
            camera_btn.setVisibility(View.GONE);
            camera_txt.setText("Camera permission is granted");
        }
        else{
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA},44);
        }

        //Granting Location Permission
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED){
                    camera_btn.setVisibility(View.GONE);
                    camera_txt.setText("Camera permission is granted");
                }
                else{
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA},44);
                }
            }
        });
        //end

        //Slider
        slider.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NonNull SlideToActView slideToActView) {
                Intent intent = new Intent(MainActivity.this, DriverDrowsinessDetectionActivity.class);
                startActivity(intent);
                slider.resetSlider();
            }
        });


    }

    @SuppressLint("MissingPermission")
    private void LocationGet() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        location_txt.setText(Html.fromHtml(addresses.get(0).getAddressLine(0)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void Initialisation(){
        location_btn = findViewById(R.id.location_btn);
        camera_btn = findViewById(R.id.camera_btn);

        textClock = findViewById(R.id.timeClock);

        location_txt = findViewById(R.id.location_text);
        camera_txt = findViewById(R.id.camera_text);
        date_txt = findViewById(R.id.date);
        username = findViewById(R.id.user_name);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        slider = findViewById(R.id.slider);
        profile = findViewById(R.id.profile_img);

    }
}