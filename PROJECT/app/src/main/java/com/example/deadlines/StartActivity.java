package com.example.deadlines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class StartActivity extends AppCompatActivity {

    private Button loginStartButton;
    private Button registrationStartButton;
    private FirebaseAuth auth;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_NOTIFICATION_TIME = "notification_time";
    private SharedPreferences mSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        loginStartButton = findViewById(R.id.loginStartButton);
        registrationStartButton = findViewById(R.id.registrationStartButton);
        auth = FirebaseAuth.getInstance();
    }
    @Override
    public void onBackPressed() {
    }
    //вход в систему
    public void StartLogin(View view){
        if (auth.getCurrentUser() != null) {
            Intent intent=new Intent(StartActivity.this,MenuActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
    //переход к регистрации
    public void StartRegistration(View view){
        Intent intent=new Intent(StartActivity.this,RegistrationActivity.class);
        startActivity(intent);
    }


}