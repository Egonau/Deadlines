package com.example.deadlines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditingActivity extends AppCompatActivity {
    private EditText editNameView;
    private Spinner themeEditSpinner;
    private Spinner priorityEditSpinner;
    private Spinner sharingEditSpinner;
    private EditText editDescriptionView;
    String[] theme = {"Уроки","Внеурочные мероприятия", "Внешкольные события"};
    String[] priority = {"Важный","Неважный"};
    String[] sharing  ={"Общелицейский дедлайн","Дедлайн группы","Личный дедлайн"};
    HashMap<String,Object> editedDeadline = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing);
        editNameView = findViewById(R.id.editNameView);
        themeEditSpinner = findViewById(R.id.themeEditSpinner);
        priorityEditSpinner = findViewById(R.id.priorityEditSpinner);
        sharingEditSpinner = findViewById(R.id.sharingEditSpinner);
        editDescriptionView = findViewById(R.id.editDescriptionView);
        //наполение списков данными
        //1
        ArrayAdapter<String> themeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, theme);
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeEditSpinner.setAdapter(themeAdapter);
        themeEditSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                editedDeadline.put("DeadlineTheme", adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //2
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, priority);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priorityEditSpinner.setAdapter(priorityAdapter);
        priorityEditSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                editedDeadline.put("DeadlinePriority", adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //3
        ArrayAdapter<String> sharingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sharing);
        sharingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sharingEditSpinner.setAdapter(sharingAdapter);
        sharingEditSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                editedDeadline.put("DeadlineSharing", adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        editNameView.setText(Single.getInstance().editedDeadline.get("DeadlineName").toString());
        editDescriptionView.setText(Single.getInstance().editedDeadline.get("DeadlineDescription").toString());
        Integer currentTheme = 0;
        Integer currentPriority = 0;
        Integer currentSharing = 0;
        //установка текущих значений
        //1
        for (Integer i = 0;i<theme.length;++i){
            if (theme[i].equals(Single.getInstance().editedDeadline.get("DeadlineTheme").toString())){
                currentTheme = i;
                break;
            }
        }
        //2
        for (Integer i = 0;i<priority.length;++i){
            if (priority[i].equals(Single.getInstance().editedDeadline.get("DeadlinePriority").toString())){
                currentPriority = i;
                break;
            }
        }
        //3
        for (Integer i = 0;i<sharing.length;++i){
            if (sharing[i].equals(Single.getInstance().editedDeadline.get("DeadlineSharing").toString())){
                currentSharing = i;
                break;
            }
        }
        themeEditSpinner.setSelection(currentTheme);
        priorityEditSpinner.setSelection(currentPriority);
        sharingEditSpinner.setSelection(currentSharing);
    }
    @Override
    public void onBackPressed() {
    }

    public void Edit(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        String year = Single.getInstance().dateInfo[0];
        String month = Single.getInstance().dateInfo[1];
        String day = Single.getInstance().dateInfo[2];
        String number = Single.getInstance().dateInfo[3];
        Single.getInstance().editedDeadline.put("DeadlineTheme", editedDeadline.get("DeadlineTheme"));
        Single.getInstance().editedDeadline.put("DeadlinePriority", editedDeadline.get("DeadlinePriority"));
        Single.getInstance().editedDeadline.put("DeadlineSharing", editedDeadline.get("DeadlineSharing"));
        Single.getInstance().editedDeadline.put("DeadlineName",editNameView.getText().toString());
        Single.getInstance().editedDeadline.put("DeadlineDescription",editDescriptionView.getText().toString());
        editedDeadline.clear();
        //загрузка изменений в БД
        rootRef.child(auth.getUid()).child("Deadlines").child("Accepted").child(year).child(month).child(day).child(number).updateChildren(Single.getInstance().editedDeadline);
        Intent intent=new Intent(this,MenuActivity.class);
        startActivity(intent);
        finish();
    }
}