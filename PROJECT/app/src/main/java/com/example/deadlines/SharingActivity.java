package com.example.deadlines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SharingActivity extends AppCompatActivity {
    private Spinner groupSharingSpinner;
    private Spinner buildingSharingSpinner;
    private Spinner occupationSharingSpinner;
    private EditText nameSharingEditText;
    private Button sharingButton;
    private Button sharingAcceptButton;
    private ListView sharingListView;
    private FirebaseAuth auth;
    private List<String> users = new ArrayList<>();
    private List<String> usersEqual = new ArrayList<>();
    private SparseBooleanArray selected = new SparseBooleanArray();
    String[] groups = {"Выбрать","Нет группы","9Ф1","9Ф2","9Ф3","9Ф4","9Ф5","9Ф6","9Ф7","9Ф8","9Ф9","9Ф10","9Ф11","9Ф12","9Ф13","9Ф14","9Ф15","9ФМ","10В1","10В2","10Г1","10Г2","10Г3","10Г4","10Г5","10Д1","10Д2","10Д3","10Е1","10Е2","10И1","10И2","10И3","10И4","10М","10П","10С1","10С2","10С3","10С4","10С5","10С6","10Э1","10Э2","10Э3","10Э4","10Э5","10Ю1","10Ю2","11В1","11В2","11Г1","11Г2","11Г3","11Г4","11Г5","11Д1","11Д2","11Е1","11Е2","11И1","11И2","11И3","11И4","11М","11П","11С1","11С2","11С3","11С4","11С5","11С6","11Э1","11Э2","11Э3","11Э4","11Э5","11Ю1","11Ю2"};
    String[] occupations = {"Выбрать","Преподаватель","Студент","Сторонний пользователь"};
    String[] buildings = {"Выбрать","Солянка","Большой Харитоньевский переулок","Колобовский переулок","Лялин переулок"};
    private HashMap<String,String> chosenOptions = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);
        auth = FirebaseAuth.getInstance();
        groupSharingSpinner = findViewById(R.id.groupSharingSpinner);
        buildingSharingSpinner = findViewById(R.id.buildingSharingSpinner);
        occupationSharingSpinner = findViewById(R.id.occupationSharingSpinner);
        nameSharingEditText = findViewById(R.id.nameSharingEditText);
        sharingButton = findViewById(R.id.sharingButton);
        sharingAcceptButton = findViewById(R.id.sharingAcceptButton);
        sharingListView = findViewById(R.id.sharingListView);
        Single.getInstance().allUsersCredentials.remove(auth.getUid());
        //получение ников пользователей
        for(Map.Entry entry:Single.getInstance().allUsersCredentials.entrySet()){
            HashMap<String,String> user  = (HashMap<String, String>) entry.getValue();
                users.add(user.get("Name"));
        }
        //наполнение списков данными
        //1
        ArrayAdapter<String> groupAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, groups);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSharingSpinner.setAdapter(groupAdapter);
        groupSharingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosenOptions.put("Group", (String) adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //2
        ArrayAdapter<String> buildingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, buildings);
        buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingSharingSpinner.setAdapter(buildingAdapter);
        buildingSharingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosenOptions.put("Building", (String) adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //3
        ArrayAdapter<String> occupationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, occupations);
        occupationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        occupationSharingSpinner.setAdapter(occupationAdapter);
        occupationSharingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosenOptions.put("Occupation", (String) adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //установка адаптера
        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_multiple_choice, users);
        sharingListView.setAdapter(adapter);

        //поиск по нику пользователя
        nameSharingEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                usersEqual.clear();
                for(Integer i = 0;i<users.size();++i){
                    if (!users.get(i).isEmpty()&&users.get(i).contains(nameSharingEditText.getText().toString())){
                        usersEqual.add(users.get(i));
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter(getBaseContext(),
                        android.R.layout.simple_list_item_multiple_choice, usersEqual);
                sharingListView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //обработка выбора элемента списка
        sharingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected=sharingListView.getCheckedItemPositions();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Single.getInstance().suggestedDeadlines.clear();
        Intent intent=new Intent(SharingActivity.this,MenuActivity.class);
        startActivity(intent);
        finish();
    }

    //создание списка пользователей на основе выбранных критериев
    public void Accept(View view) {
        nameSharingEditText.getText().clear();
        users.clear();
        HashMap<String,HashMap<String,Object>> selection = new HashMap<>();
        for(Map.Entry entry:Single.getInstance().allUsersCredentials.entrySet()){
            HashMap<String,String> user  = (HashMap<String, String>) entry.getValue();
            if (equalify(user.get("Group"),chosenOptions.get("Group"),"Выбрать")&&equalify(user.get("Occupation"),chosenOptions.get("Occupation"),"Выбрать")&&equalify(user.get("Building"),chosenOptions.get("Building"),"Выбрать")){//&&!user.get("Occupation").isEmpty()&&user.get("Occupation").equals(occupationSharingEditText.getText())&&!user.get("Building").isEmpty()&&user.get("Building").equals(buildingSharingEditText.getText())&&!user.get("Group").isEmpty()&&user.get("Group").equals(groupSharingEditText.getText())){
                users.add(user.get("Name"));
           }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_multiple_choice, users);
        sharingListView.setAdapter(adapter);
    }
    //функция сортировки
    public boolean equalify(Object a,Object b, Object c){
        if (Objects.equals(b,c)){
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
    //отправка дедлайна выбранным пользователям
    public void sendDeadlines(){
        for(Integer i=0;i < users.size();i++)
        {
            if(selected.get(i)){
                users.get(i);
                for(Map.Entry entry:Single.getInstance().allUsersCredentials.entrySet()){
                    HashMap<String,String> user  = (HashMap<String, String>) entry.getValue();
                    if (Objects.equals(user.get("Name"),users.get(i))){
                        FirebaseDatabase.getInstance().getReference().child(entry.getKey().toString()).child("Deadlines").child("Suggested").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                Single.getInstance().sharingDeadline.put("DeadlineDateInfo",Single.getInstance().chosenYear+"_"+Single.getInstance().chosenMonth+"_"+Single.getInstance().chosenDay+"_"+String.valueOf(snapshot.getChildrenCount()));
                                FirebaseDatabase.getInstance().getReference().child(entry.getKey().toString()).child("Deadlines").child("Suggested").child(String.valueOf(snapshot.getChildrenCount())).updateChildren(Single.getInstance().sharingDeadline).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        //Toast.makeText(SharingActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(SharingActivity.this,MenuActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        }
    }
    public void Share(View view) {
        sendDeadlines();
    }
}