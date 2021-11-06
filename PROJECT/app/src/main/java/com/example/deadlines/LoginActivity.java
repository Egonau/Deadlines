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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class LoginActivity extends Activity {

    private TextView loginText;
    private EditText loginTextEdit;
    private EditText passwordTextEdit;
    private Button loginButton;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginText = (TextView) findViewById(R.id.loginText);
        loginTextEdit = (EditText) findViewById(R.id.loginTextEdit);
        passwordTextEdit = (EditText) findViewById(R.id.passwordTextEdit);
        loginButton = (Button) findViewById(R.id.loginButton);
        auth = FirebaseAuth.getInstance();
    }
    //вход в аккаунт
    public void Login(View view){
            if (!loginTextEdit.getText().toString().isEmpty() && !passwordTextEdit.getText().toString().isEmpty()) {
                loginUser(loginTextEdit.getText().toString(), passwordTextEdit.getText().toString());
            } else {
                Toast.makeText(getApplicationContext(), "Incorrect login or password", Toast.LENGTH_SHORT).show();
            }
    }
    //проверка наличия аккаунта в Firebase
    private void loginUser(String login, String password) {
        auth.signInWithEmailAndPassword(login, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Successfully logged in",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(LoginActivity.this,MenuActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Incorrect login or password",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //обработка кнопки сброса пароля
    public void Reset(View view) {
        Intent intent=new Intent(LoginActivity.this,ResetPasswordActivity.class);
        startActivity(intent);
    }


}