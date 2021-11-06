package com.example.deadlines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Objects;

public class MenuActivity extends AppCompatActivity {

    private Button settingsButton;
    private Button deadlineButton;
    private Button olympsButton;
    //private pl.droidsonroids.gif.GifImageView gifImageView;
    private FirebaseAuth auth;
    private SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_SELECTED_THEME = "selected_theme";
    public static final String APP_PREFERENCES_SELECTED_PRIORITY = "selected_priority";
    public static final String APP_PREFERENCES_SELECTED_SHARING = "selected_sharing";
    private com.applandeo.materialcalendarview.CalendarView mCalendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        olympsButton = findViewById(R.id.olympsButton);
        mCalendarView = (com.applandeo.materialcalendarview.CalendarView) findViewById(R.id.calendarView);
        mCalendarView.setSwipeEnabled(false);
        auth = FirebaseAuth.getInstance();
        currentMonthEvents(mCalendarView);
        Single.getInstance().credentialsOfUser.put("Name","");
        Single.getInstance().credentialsOfUser.put("Group", "");
        Single.getInstance().credentialsOfUser.put("Olymps", "");
        Single.getInstance().credentialsOfUser.put("Building", "");
        Single.getInstance().credentialsOfUser.put("Occupation", "");
        getCredentials();
        getAllUsersInfo();
        //обрабока нажатия на день в календаре
        mCalendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(@NotNull EventDay eventDay) {
                Single.getInstance().chosenDay = String.valueOf(eventDay.component1().getTime().getDate());
                Single.getInstance().chosenMonth = String.valueOf(eventDay.component1().getTime().getMonth());
                Single.getInstance().chosenYear = String.valueOf(eventDay.component1().getTime().getYear());
                getDeadlines();
                Intent intent=new Intent(MenuActivity.this,DayActivity.class);
                startActivity(intent);
            }
        });
        //переход к следующему/предыдущему месяцу
        mCalendarView.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                currentMonthEvents(mCalendarView);
            }
        });

        mCalendarView.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                currentMonthEvents(mCalendarView);
            }
        });



    }
//верхнее меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_panel, menu);
        return true;
    }
    //получение ивентов текущего месяца
    public void currentMonthEvents(com.applandeo.materialcalendarview.CalendarView mCalendarView){
        mCalendarView.clearSelectedDays();
        getEvents();
    }
    //функция для отбора дедлайнов по параметрам сортировки
    public boolean equalify(Object a,Object b, Object c){
        if (Objects.equals(b,c)||Objects.equals(b,"")){
            return true;
        }
        else{
            if (Objects.equals(a,b)){
                return true;
            }
            else{
                return false;
            }
        }
    }
    //получение дедлайнов текущего месяца из БД
    public void getEvents(){
        Single.getInstance().calEvents.clear();
        mCalendarView.clearSelectedDays();
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(auth.getUid())).child("Deadlines").child("Accepted").child(String.valueOf(mCalendarView.getCurrentPageDate().getTime().getYear())).child(String.valueOf(mCalendarView.getCurrentPageDate().getTime().getMonth())).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds:snapshot.getChildren()){
                        Integer day = Integer.parseInt(ds.getKey());
                        for (int i = (int) (ds.getChildrenCount()-1); i>=0; i--){
                            HashMap<String,String> sorter = ds.getValue(Single.getInstance().t).get(i);
                            String sorterPriority =sorter.get("DeadlinePriority");
                            String sorterTheme =sorter.get("DeadlineTheme");
                            String sorterSharing =sorter.get("DeadlineSharing");
                            mSettings.getString("ecewc","");
                            if (equalify(sorterTheme,mSettings.getString(APP_PREFERENCES_SELECTED_THEME,""),"Выбрать") && equalify(sorterPriority,mSettings.getString(APP_PREFERENCES_SELECTED_PRIORITY,""),"Выбрать") && equalify(sorterSharing,mSettings.getString(APP_PREFERENCES_SELECTED_SHARING,""),"Выбрать")){
                                Calendar calendar = new GregorianCalendar(mCalendarView.getCurrentPageDate().getTime().getYear() +1900, mCalendarView.getCurrentPageDate().getTime().getMonth(),day);
                                Single.getInstance().calEvents.add(new EventDay(calendar, R.drawable.ic_baseline_circle_24));
                                break;
                            }
                        }

                    }
                    mCalendarView.setEvents(Single.getInstance().calEvents);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    //обработка кнопки перехода к настройкам
    public void Settings(MenuItem item){
        Intent intent=new Intent(MenuActivity.this,SettingsActivity.class);
        startActivity(intent);
    }
    //обработка кнопки перехода к предложениям
    public void Suggestions(MenuItem item){
        getSuggestedDeadlines();
        Intent intent=new Intent(MenuActivity.this,AcceptanceActivity.class);
        startActivity(intent);
    }
    //получение дедлайнов на выбраный день
    public void getDeadlines(){
            Single.getInstance().dayDeadlines.clear();
            FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(auth.getUid())).child("Deadlines").child("Accepted").child(Single.getInstance().chosenYear).child(Single.getInstance().chosenMonth).child(Single.getInstance().chosenDay).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds:snapshot.getChildren()){
                        GenericTypeIndicator<HashMap<String, Object>> t = new GenericTypeIndicator<HashMap<String, Object>>(){};
                        HashMap<String, Object> event = ds.getValue(t);
                        if (event.get("Type").equals("School")){
                            if (Single.getInstance().dayDeadlines.containsKey(event.get("Lesson").toString())){
                                Single.getInstance().dayDeadlines.get(event.get("Lesson").toString()).add(event);
                            }
                            else{
                                Single.getInstance().dayDeadlines.put(event.get("Lesson").toString(),new ArrayList<>());
                                Single.getInstance().dayDeadlines.get(event.get("Lesson").toString()).add(event);
                            }
                        }
                        else{
                            if (Single.getInstance().dayDeadlines.containsKey("Not School")){
                                Single.getInstance().dayDeadlines.get("Not School").add(event);
                            }
                            else{
                                Single.getInstance().dayDeadlines.put("Not School",new ArrayList<>());
                                Single.getInstance().dayDeadlines.get("Not School").add(event);
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    //обработка кнопки перехода к олимпиадам
    public void Olymps(View view) {
        //gifImageView.setVisibility(view.VISIBLE);
        //mCalendarView.setVisibility(view.INVISIBLE);
        Intent intent=new Intent(getBaseContext(),OlympiadsActivity.class);
        startActivity(intent);
    }
    //обработка кнопки перехода к сортировке
    public void Sort(MenuItem item) {
        //gifImageView.setVisibility(view.VISIBLE);
        Intent intent=new Intent(getBaseContext(),SortingActivity.class);
        startActivity(intent);
    }

    //получение предложенных дедлайнов из БД
    public void getSuggestedDeadlines(){
        Single.getInstance().suggestedDeadlines.clear();
        FirebaseDatabase.getInstance().getReference().child(auth.getUid()).child("Deadlines").child("Suggested").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot sn:snapshot.getChildren()){
                    GenericTypeIndicator<HashMap<String,Object>> t = new GenericTypeIndicator<HashMap<String, Object>>() {};
                    Single.getInstance().suggestedDeadlines.add(sn.getValue(t));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //получение данных пользователя
    public void getCredentials(){
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

    //получение кратких данных всех пользователей
    public void getAllUsersInfo() {
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot sn:snapshot.getChildren()){
                    GenericTypeIndicator<HashMap<String,Object>> t = new GenericTypeIndicator<HashMap<String, Object>>() {};
                    Single.getInstance().allUsersCredentials.put(sn.getKey().toString(),sn.child("Credentials").getValue(t));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}