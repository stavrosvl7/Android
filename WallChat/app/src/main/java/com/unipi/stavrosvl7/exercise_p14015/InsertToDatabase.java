package com.unipi.stavrosvl7.exercise_p14015;

import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InsertToDatabase implements Runnable {
    private SQLiteDatabase database;
    Location location;
    SimpleDateFormat formatter;

    public InsertToDatabase(SQLiteDatabase db, Location loc){
        database = db;
        location = loc;
        formatter = new SimpleDateFormat("dd.MM.yyyy  hh:mm:ss aaa");
    }

    @Override
    public void run() {
        database.execSQL("INSERT INTO SpeedDatabase values" + "('" + location.getLongitude() + "','" + location.getLatitude() + "','" + location.getSpeed()* 3600 / 1000 + "','" + formatter.format(new Date(location.getTime())) + "','" + location.getTime() + "');");
    }

}
