package com.example.deadlines;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;

public class AboutActivity extends AppCompatActivity {
    TextView versionTextView;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        auth = FirebaseAuth.getInstance();
        versionTextView = findViewById(R.id.versionTextView);
        versionTextView.setText("ВЕРСИЯ ПРИЛОЖЕНИЯ: "+ BuildConfig.VERSION_NAME);
    }
    //открытие почтового клиента
    public void Support(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:egonau2012@gmail.com?subject=" + Uri.encode("User report:" + auth.getUid()));
        intent.setData(data);
        startActivity(intent);
    }
}