package com.unipi.stavrosvl7.exercise_p14015;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class Preferences_activity extends AppCompatActivity{

    SharedPreferences sharedPreferences;
    Button button_save;
    Switch aSwitch;

    boolean bool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences_activity);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        button_save = findViewById(R.id.button_save);
        aSwitch = findViewById(R.id.switch_tomiles);
        bool = true;

        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = findViewById(R.id.editText_preferences_speedlimit);
                bool = false;
            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                EditText editText = findViewById(R.id.editText_preferences_speedlimit);
                if(bool){
                    editor.putString("speedlimit", editText.getText().toString());
                    editText.getText().clear();
                }
                else{
                    editText.setText(String.valueOf(Float.parseFloat(editText.getText().toString())*1.6));
                    editor.putString("speedlimit", editText.getText().toString());
                    editText.getText().clear();
                    bool=true;
                }

                editor.commit();
                Toast.makeText(Preferences_activity.this,getString(R.string.speedlimit_saved),Toast.LENGTH_LONG).show();
            }
        });
    }

}
