package com.unipi.stavrosvl7.ChatApp;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    EditText email, password;
    Button buttonLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        password = findViewById(R.id.textview_password);
        email = findViewById(R.id.textview_email);
        buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(LoginActivity.this);
    }

    public void signIn(){
        mAuth.signInWithEmailAndPassword(
                email.getText().toString(),password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,
                                    "Successfull login!", Toast.LENGTH_SHORT).show();
                            Intent intentChat = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intentChat);
                        }else {
                            Toast.makeText(LoginActivity.this,
                                    task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case(R.id.button_login):
                signIn();
                break;
        }
    }
}
