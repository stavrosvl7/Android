package com.unipi.stavrosvl7.ChatApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.unipi.stavrosvl7.ChatApp.Adapters.MyTextToSpeech;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText username, email, password;

    Button buttonRegister;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    MyTextToSpeech myTextToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.textview_username);
        password = findViewById(R.id.textview_password);
        email = findViewById(R.id.textview_email);
        buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(RegisterActivity.this);

        myTextToSpeech = new MyTextToSpeech(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case(R.id.button_register):
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(RegisterActivity.this, "All fileds are required", Toast.LENGTH_SHORT).show();
                }
                else {
                    signUp();
                }
                break;
        }
    }

    public void signUp(){
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    savePreferences();
                    Toast.makeText(RegisterActivity.this, R.string.SuccessfulSignUp, Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userId = user.getUid();

                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                    HashMap<String , String> hashMap = new HashMap<>();
                    hashMap.put("id", userId);
                    hashMap.put("username", username.getText().toString());

                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                    myTextToSpeech.speak("Welcome");

                } else {
                    Toast.makeText(RegisterActivity.this,
                            task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void savePreferences(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email",mAuth.getCurrentUser().getEmail());
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        if (myTextToSpeech != null) {
            myTextToSpeech.stop();
            myTextToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
