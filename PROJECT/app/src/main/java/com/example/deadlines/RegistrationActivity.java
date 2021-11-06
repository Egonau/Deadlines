package com.example.deadlines;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegistrationActivity extends Activity{

    private  TextView registrationText;
    private  EditText passwordRegistrationTextEdit;
    private  EditText loginRegistrationEditText;
    private  Button registrationButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        registrationText = (TextView) findViewById(R.id.registrationText);
        passwordRegistrationTextEdit = (EditText) findViewById(R.id.passwordRegistrationTextEdit);
        loginRegistrationEditText = (EditText) findViewById(R.id.loginRegistrationEditText);
        registrationButton = (Button) findViewById(R.id.registrationButton);
        auth = FirebaseAuth.getInstance();
    }

    //рагистрация пользователя
    public void Registration(View view){
        if (!loginRegistrationEditText.getText().toString().isEmpty() && !passwordRegistrationTextEdit.getText().toString().isEmpty()){
            registerUser(loginRegistrationEditText.getText().toString(), passwordRegistrationTextEdit.getText().toString());
        }
        else {
            Toast.makeText(this, "Incorrect credentials", Toast.LENGTH_SHORT).show();
        }
    }
    //обработка запроса в БД
    private void registerUser(String login, String password) {
        auth.createUserWithEmailAndPassword(login, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegistrationActivity.this, "Registering successfully", Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(RegistrationActivity.this,MenuActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(RegistrationActivity.this, "Incorrect credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}