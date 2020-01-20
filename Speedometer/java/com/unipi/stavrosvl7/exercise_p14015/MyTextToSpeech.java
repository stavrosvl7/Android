package com.unipi.stavrosvl7.exercise_p14015;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class MyTextToSpeech {
    private TextToSpeech textToSpeech;
    private TextToSpeech.OnInitListener initListener = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
            if(status==TextToSpeech.SUCCESS){
                textToSpeech.setLanguage(Locale.ENGLISH);
            }
        }
    };

    public MyTextToSpeech(Context context){
        textToSpeech = new TextToSpeech(context,initListener);
    }

    public void speak(String message){
        textToSpeech.speak(message,TextToSpeech.QUEUE_ADD,null,null);

    }

    public void stop(){
        if(textToSpeech.isSpeaking()){
            textToSpeech.stop();
        }
    }

    public void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
    }
}
