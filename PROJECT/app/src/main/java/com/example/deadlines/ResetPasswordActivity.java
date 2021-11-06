package com.example.deadlines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private Button resetterButton;
    private EditText editTextLoginResetter;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        resetterButton = findViewById(R.id.resetterButton);
        editTextLoginResetter = findViewById(R.id.editTextLoginResetter);
        auth = FirebaseAuth.getInstance();
    }
    //сброс пароля
    public void Resetter(View view) {
        String login = editTextLoginResetter.getText().toString();
        if (login.contains("@")){
            auth.sendPasswordResetEmail(login).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(ResetPasswordActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        else{
            Toast.makeText(this, "Incorrect credentials", Toast.LENGTH_SHORT).show();
        }
    }
}