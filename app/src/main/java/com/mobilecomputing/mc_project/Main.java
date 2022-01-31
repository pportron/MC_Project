package com.mobilecomputing.mc_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class Main extends AppCompatActivity {

    ListView ListRmd;
    String RmdName[] = {"Reminder1", "Reminder2", "Reminder3"};
    String RmdDate[] = {"05/02/20022", "07/02/20022", "01/03/20022"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ListRmd = (ListView) findViewById(R.id.ReminderList);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), RmdName, RmdDate);
        ListRmd.setAdapter(customAdapter);
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