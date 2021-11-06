package com.example.deadlines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DayActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private TableLayout tableView;
    private ArrayList<HashMap<String, String>> dayDeadlines = new ArrayList<>();
    private TextView textViewDayName;
    private Button buttonAddEventDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        tableView = findViewById(R.id.tableView);
        auth = FirebaseAuth.getInstance();
        textViewDayName = findViewById(R.id.textViewOlympsSelected);
        buttonAddEventDay = findViewById(R.id.buttonAddEventDay);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(Integer.parseInt(Single.getInstance().chosenYear)+1900,Integer.parseInt(Single.getInstance().chosenMonth),Integer.parseInt(Single.getInstance().chosenDay)));
        Integer dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        String dayOfWeekString;
        //парсинг расписания
        parse();
        //день недели по его номеру в Calendar
        switch(dayOfWeek){
            case (1):{
                dayOfWeekString = "Суббота";
                break;
            }
            case (2):{
                dayOfWeekString = "Воскресенье";
                break;
            }
            case (3):{
                dayOfWeekString = "Понедельник";
                break;
            }
            case (4):{
                dayOfWeekString = "Вторник";
                break;
            }
            case (5):{
                dayOfWeekString = "Среда";
                break;
            }
            case (6):{
                dayOfWeekString = "Четверг";
                break;
            }
            case (7):{
                dayOfWeekString = "Пятница";
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + dayOfWeek);
        }
        textViewDayName.setText(dayOfWeekString+ ", "+Single.getInstance().chosenDay+"."+Integer.parseInt(String.valueOf(Integer.parseInt(Single.getInstance().chosenMonth)+1))+"."+String.valueOf(Integer.parseInt(Single.getInstance().chosenYear)+1900));
        //наполнение расписания уроками
        if (dayOfWeekString.equals("Воскресенье") || (Single.getInstance().schedule.size()==0)|| (Single.getInstance().credentialsOfUser.get("Group").equals("Нет группы")) ) {
        }
        else{
            HashMap<String, String> daySchedule = Single.getInstance().schedule.get(dayOfWeekString);
            Integer lessonsAmount = daySchedule.size();
            for (String key : daySchedule.keySet()) {
                if (lessonsAmount<Integer.parseInt(key)){
                    lessonsAmount = Integer.parseInt(key);
                }
            }
            for (Integer i = 1;i<=lessonsAmount;++i){
                if (!daySchedule.containsKey(String.valueOf(i))){
                    daySchedule.put(i.toString(),"Нет урока");
                } else{
                    if (daySchedule.get(String.valueOf(i)).equals("")) {
                        daySchedule.put(i.toString(),"Нет урока");
                    }
                }

            }
            for (Integer i = 1; i <= daySchedule.size(); ++i) {
                String resourse = null;
                String str = daySchedule.get(String.valueOf(i));
                Boolean infoPresser = false;
                if (Single.getInstance().dayDeadlines.containsKey(str) ){
                    resourse = "Red";
                    infoPresser = true;
                }
                if (str!=null){
                    setTableView(String.valueOf(i), daySchedule.get(String.valueOf(i)), resourse, true, infoPresser);
                }

            }

        }
        //добавление в расписание внешкольных дедлайнов
        if (Single.getInstance().dayDeadlines.size()!=0 && Single.getInstance().dayDeadlines.containsKey("Not School")){
            for (Integer i = 0;i<Single.getInstance().dayDeadlines.get("Not School").size();++i){
                String resourse ="Red";
                setTableView("#",Single.getInstance().dayDeadlines.get("Not School").get(i).get("DeadlineName").toString(),resourse,false,true);
            }
        }

        
    }
    //логика одного элемента расписания
    public void setTableView(String numberText, String infoText,String resourse,Boolean statusPressable, Boolean infoPressable){
        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View row = (View) inflater.inflate(R.layout.standart_row,null);
        TextView numberTextView = (TextView) row.findViewById(R.id.numberTextView);
        TextView infoTextView = (TextView) row.findViewById(R.id.infoTextView);
        ImageView statusImageView = row.findViewById(R.id.statusImageView);
        ImageView infoImageView = row.findViewById(R.id.infoImageView);
        if (resourse!=null){
            //statusImageView.setImageResource(R.drawable.ic_baseline_add_circle_red_24);//resourse
            infoImageView.setImageResource(R.drawable.ic_baseline_info_red_24);
        }else
        {
            //statusImageView.setImageResource(R.drawable.ic_baseline_add_circle_24);
            infoImageView.setImageResource(R.drawable.ic_baseline_info);
        }
        //infoImageView.setImageResource(R.drawable.ic_baseline_info);
        statusImageView.setImageResource(R.drawable.ic_baseline_add_circle_24);
        numberTextView.setText(numberText);
        infoTextView.setText(infoText);
        //управление кнопкой Status
        if (statusPressable){
            statusImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Single.getInstance().tags.clear();
                    Single.getInstance().tags.put("Type","School");
                    Single.getInstance().tags.put("Lesson", infoText);
                    Intent intent=new Intent(DayActivity.this,DeadlineActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        //управление кнопкой Info
        if (infoPressable){
            infoImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (numberText.equals("#")){
                        Single.getInstance().chosenLesson = "Not School";
                    }
                    else{
                        Single.getInstance().chosenLesson = infoText;
                    }
                    Intent intent=new Intent(DayActivity.this,DeadlineInfoActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

        tableView.addView(row);
    }

    //парсинг
    public void parse(){
        try {
            new Parser();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //слушатель для кнопки внешкольного дедлайна
    public void AddEvent(View view) {
        Single.getInstance().tags.clear();
        Single.getInstance().tags.put("Type","NonSchool");
        Intent intent=new Intent(DayActivity.this,DeadlineActivity.class);
        startActivity(intent);
        finish();
    }
}