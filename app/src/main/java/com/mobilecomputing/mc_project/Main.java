package com.mobilecomputing.mc_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class Main extends AppCompatActivity {

    ListView ListRmd;
    String[] Message = new String[100];
    String[] reminder_time = new String[100];
    String[] creation_time = new String[100];
    String[] creator_id = new String[100] ;
    int[] reminder_seen = new int[100];
    double[] location_x = new double[100];
    double[] location_y = new double[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ListRmd = (ListView) findViewById(R.id.ReminderList);

        DBHandler dbHandler = new DBHandler(Main.this);

        Message = dbHandler.GetDataString("Message");
        reminder_time = dbHandler.GetDataString("reminder_time");
        creation_time = dbHandler.GetDataString("creation_time");
        creator_id = dbHandler.GetDataString("creator_id");
        reminder_seen = dbHandler.GetDataInt("reminder_seen");
        location_x = dbHandler.GetDataDouble("location_x");
        location_y = dbHandler.GetDataDouble("location_y");

        Reminder myreminder = new Reminder(getApplicationContext(), Message, reminder_time,creation_time,creator_id,reminder_seen,location_x,location_y);
        ListRmd.setAdapter(myreminder);
    }

    public void GoToLogin(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void GoToProfile(View view) {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    public void GoToAddReminder(View view) {
        Intent intent = new Intent(this, AddReminder.class);
        startActivity(intent);
    }
    }