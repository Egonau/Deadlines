package com.example.deadlines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class DeadlineActivity extends AppCompatActivity {

    private TextView deadlineText;
    private EditText deadlineNameTextEdit;
    private Spinner themeSpinner;
    private Spinner prioritySpinner;
    private Spinner sharingSpinner;
    private EditText deadlineDescriptionTextEdit;
    private Button deadlineSaveButton;
    private FirebaseAuth auth;
    private HashMap<String,Object> map = new HashMap<>();
    String[] theme = {"Уроки","Внеурочные мероприятия", "Внешкольные события"};
    String[] priority = {"Важный","Неважный"};
    String[] sharing  ={"Общелицейский дедлайн","Дедлайн группы","Личный дедлайн"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deadline);
        deadlineNameTextEdit = findViewById(R.id.deadlineNameTextEdit);
        themeSpinner = findViewById(R.id.themeSpinner);
        prioritySpinner  = findViewById(R.id.prioritySpinner);
        sharingSpinner = findViewById(R.id.sharingSpinner);
        deadlineDescriptionTextEdit = findViewById(R.id.deadlineDescriptionTextEdit);
        deadlineSaveButton = findViewById(R.id.deadlineSaveButton);
        auth = FirebaseAuth.getInstance();
        //наполнение списков данными
        //1
        ArrayAdapter<String> themeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, theme);
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(themeAdapter);
        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                map.put("DeadlineTheme", adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //2
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, priority);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(priorityAdapter);
        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                map.put("DeadlinePriority", adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //3
        ArrayAdapter<String> sharingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sharing);
        sharingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sharingSpinner.setAdapter(sharingAdapter);
        sharingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                map.put("DeadlineSharing", adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    @Override
    public void onBackPressed() {
    }
    public void SaveDeadline(View view){
        map.put("DeadlineName",  deadlineNameTextEdit.getText().toString());
        map.put("DeadlineDescription", deadlineDescriptionTextEdit.getText().toString());
        if (Single.getInstance().tags.size()!=0){
            for(HashMap.Entry<String,String> m : Single.getInstance().tags.entrySet())
            {
                map.put(m.getKey().toString(),m.getValue().toString());
            }
            Single.getInstance().tags.clear();
        }
        //загрузка дедлайна в БД
        FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(auth.getUid())).child("Deadlines").child("Accepted").child(String.valueOf(Single.getInstance().chosenYear)).child(String.valueOf(Single.getInstance().chosenMonth)).child(String.valueOf(Single.getInstance().chosenDay)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                map.put("DeadlineDateInfo",Single.getInstance().chosenYear+"_"+Single.getInstance().chosenMonth+"_"+Single.getInstance().chosenDay+"_"+snapshot.getChildrenCount());
                FirebaseDatabase.getInstance().getReference().child(auth.getUid()).child("Deadlines").child("Accepted").child(String.valueOf(Single.getInstance().chosenYear)).child(String.valueOf(Single.getInstance().chosenMonth)).child(String.valueOf(Single.getInstance().chosenDay)).child(String.valueOf(snapshot.getChildrenCount())).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DeadlineActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        Intent intent=new Intent(DeadlineActivity.this,MenuActivity.class);
        startActivity(intent);
        finish();
    }
}