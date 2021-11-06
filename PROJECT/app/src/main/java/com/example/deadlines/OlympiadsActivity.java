package com.example.deadlines;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

public class OlympiadsActivity extends AppCompatActivity {
    private RecyclerView olympsRV;
    private FirebaseAuth auth;
    private TextView olympsTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olympiads);
        auth = FirebaseAuth.getInstance();
        olympsTextView = findViewById(R.id.olympsTextView);
        olympsTextView.setText(Single.getInstance().credentialsOfUser.get("Olymps").toString()+ ". Олимпиады");
        parseOlymps();
        olympsRV = (RecyclerView)findViewById(R.id.olympsRV);
        olympsRV.setHasFixedSize(true);

        //подключение адаптера для списка олимпиад
        LinearLayoutManager llm = new LinearLayoutManager(this);
        olympsRV.setLayoutManager(llm);
        OlympiadsAdapter adapter = new OlympiadsAdapter(OlympiadsActivity.this, Single.getInstance().allCurrentOlympiads);
        olympsRV.setAdapter(adapter);
    }

    //парсинг списка олимпиад
    public void parseOlymps(){
        try {
            new ParserOlymps();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}