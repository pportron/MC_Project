package com.mobilecomputing.mc_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class Main extends AppCompatActivity {

    ListView ListRmd;
    String Message[] = {"HW2 Mobile", "HW2 XR", "Project1 IA"};
    String reminder_time[] = {"13/02/20022", "15/02/20022", "11/03/20022"};
    String creation_time[] = {"01/02/20022", "01/02/20022", "01/02/20022"};
    String creator_id[] = {"admin", "admin", "admin"};
    int reminder_seen[] = {0, 0, 0};
    double location_x[] = {65.03440750807297, 66.54371260878337, 65.94350682745116};
    double location_y[] = {25.46302378505754, 25.84720802746948, 26.46699554092714};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ListRmd = (ListView) findViewById(R.id.ReminderList);
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
    }