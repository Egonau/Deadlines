package com.example.deadlines;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class AcceptanceActivity extends AppCompatActivity {
    private RecyclerView acceptanceRV;
    private FirebaseAuth auth;
    private TextView noSuggestionsTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptance);
        noSuggestionsTextView = findViewById(R.id.noSuggestionsTextView);
        noSuggestionsTextView.setVisibility(View.INVISIBLE);
        auth = FirebaseAuth.getInstance();
        acceptanceRV = (RecyclerView)findViewById(R.id.olympsRV);
        acceptanceRV.setHasFixedSize(true);
        acceptanceRV.setLayoutManager(null);
        acceptanceRV.setAdapter(null);
        //рассмотрел вариант, когда предложений нет
        if (Single.getInstance().suggestedDeadlines.isEmpty()){
            noSuggestionsTextView.setVisibility(View.VISIBLE);
            acceptanceRV.setVisibility(RecyclerView.INVISIBLE);
        }
        else{
            LinearLayoutManager llm = new LinearLayoutManager(this);
            acceptanceRV.setLayoutManager(llm);
            AcceptanceAdapter adapter = new AcceptanceAdapter(AcceptanceActivity.this, Single.getInstance().suggestedDeadlines);
            acceptanceRV.setAdapter(adapter);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}