package com.example.deadlines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeadlineInfoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<HashMap<String,Object>> deadlines = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deadline_info);
        recyclerView = (RecyclerView)findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        //подключение адаптера
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        deadlines = Single.getInstance().dayDeadlines.get(Single.getInstance().chosenLesson);
        RecyclerAdapter adapter = new RecyclerAdapter(DeadlineInfoActivity.this, deadlines);
        recyclerView.setAdapter(adapter);
    }

}