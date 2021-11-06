package com.example.deadlines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private Button saveSettingsButton;
    private Button logOutButton;
    private EditText nameEditText;
    private Spinner buildingSpinner;
    private Spinner groupSpinner;
    private Spinner occupationSpinner;
    private Spinner olympiadSpinner;
    private FirebaseAuth auth;
    private EditText editTextTime;
    private ListView listView;
    private HashMap<String,Object> map = new HashMap<>();
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_NOTIFICATION_TIME = "notification_time";
    private SharedPreferences mSettings;
    String[] groups = {"Нет группы","9Ф1","9Ф2","9Ф3","9Ф4","9Ф5","9Ф6","9Ф7","9Ф8","9Ф9","9Ф10","9Ф11","9Ф12","9Ф13","9Ф14","9Ф15","9ФМ","10В1","10В2","10Г1","10Г2","10Г3","10Г4","10Г5","10Д1","10Д2","10Д3","10Е1","10Е2","10И1","10И2","10И3","10И4","10М","10П","10С1","10С2","10С3","10С4","10С5","10С6","10Э1","10Э2","10Э3","10Э4","10Э5","10Ю1","10Ю2","11В1","11В2","11Г1","11Г2","11Г3","11Г4","11Г5","11Д1","11Д2","11Е1","11Е2","11И1","11И2","11И3","11И4","11М","11П","11С1","11С2","11С3","11С4","11С5","11С6","11Э1","11Э2","11Э3","11Э4","11Э5","11Ю1","11Ю2"};
    String[] occupations = {"Преподаватель","Студент","Сторонний пользователь"};
    String[] buildings = {"Солянка","Большой Харитоньевский переулок","Колобовский переулок","Лялин переулок"};
    String[] olymps = {"Биология","География","Информатика","Математика","Физика","Химия","Астрономия","ИЗО","Искусство","История","Лингвистика","Литература","ОБЖ","Обществознание","Предпринимательство","Право","Психология","Робототехника","Русский язык","Технология","Физкультура","Черчение","Экология","Экология","Ин. языки"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        auth = FirebaseAuth.getInstance();
        getCredentials();
        olympiadSpinner = findViewById(R.id.olympiadSpinner);

        saveSettingsButton = findViewById(R.id.saveSettingsButton);
        nameEditText = findViewById(R.id.nameEditText);
        buildingSpinner = findViewById(R.id.buildingSpinner);
        groupSpinner = findViewById(R.id.groupSpinner);
        occupationSpinner = findViewById(R.id.occupationSpinner);
        editTextTime = findViewById(R.id.editTextTime);
        nameEditText.setText(Single.getInstance().credentialsOfUser.get("Name").toString());
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.contains(APP_PREFERENCES_NOTIFICATION_TIME)) {
            editTextTime.setText(mSettings.getString(APP_PREFERENCES_NOTIFICATION_TIME,""));
        }
        //наполнение списков данными
        //1
        ArrayAdapter<String> groupAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, groups);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSpinner.setAdapter(groupAdapter);
        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                map.put("Group", adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //2
        ArrayAdapter<String> olympiadAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, olymps);
        olympiadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        olympiadSpinner.setAdapter(olympiadAdapter);
        olympiadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                map.put("Olymps", adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //3
        ArrayAdapter<String> buildingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, buildings);
        buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingSpinner.setAdapter(buildingAdapter);
        buildingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                map.put("Building", adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //4
        ArrayAdapter<String> occupationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, occupations);
        occupationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        occupationSpinner.setAdapter(occupationAdapter);
        occupationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                map.put("Occupation", adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //установка текущих значений каждого параметра
        try {
            groupSpinner.setSelection(groupAdapter.getPosition(Single.getInstance().credentialsOfUser.get("Group").toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            groupSpinner.setSelection(groupAdapter.getPosition(Single.getInstance().credentialsOfUser.get("Group").toString()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            olympiadSpinner.setSelection(olympiadAdapter.getPosition(Single.getInstance().credentialsOfUser.get("Olymps").toString()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            buildingSpinner.setSelection(buildingAdapter.getPosition(Single.getInstance().credentialsOfUser.get("Building").toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            occupationSpinner.setSelection(occupationAdapter.getPosition(Single.getInstance().credentialsOfUser.get("Occupation").toString()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //получение данных пользователя
    public void getCredentials() {
        FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(auth.getUid())).child("Credentials").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    Single.getInstance().credentialsOfUser.put(ds.getKey().toString(),ds.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //сохранение дедлайна
    public void Save(View view){
        //HashMap<String,Object> map = new HashMap<>();
        map.put("Name", nameEditText.getText().toString());
        FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(auth.getUid())).child("Credentials").updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(SettingsActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_NOTIFICATION_TIME,String.valueOf(editTextTime.getText()));
        editor.apply();
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        Intent notifyIntent = new Intent(this, TimeNotification.class);
        String time = null;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (mSettings.contains(APP_PREFERENCES_NOTIFICATION_TIME)) {
            time = mSettings.getString(APP_PREFERENCES_NOTIFICATION_TIME,"");
            String[] notificationTime = new String[0];
            notificationTime = time.split(":");
            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),Integer.parseInt(notificationTime[0]),Integer.parseInt(notificationTime[1]));
            calendar.add(Calendar.SECOND,5);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        Intent intent=new Intent(SettingsActivity.this,MenuActivity.class);
        startActivity(intent);
        finish();
    }
    //выход из аккаунта
    public void LogOut(View view){
        auth.signOut();
        Intent intent=new Intent(SettingsActivity.this,StartActivity.class);
        startActivity(intent);
        finish();
    }
    //привязка верхнего меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_panel_settings, menu);
        return true;
    }
    //обработка кнопки помощь
    public void Help(MenuItem item){
        Intent intent=new Intent(SettingsActivity.this,AboutActivity.class);
        startActivity(intent);
    }
}