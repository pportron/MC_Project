package com.mobilecomputing.mc_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UpdateReminder extends AppCompatActivity {

    private SharedPreferences preferences;
    private static final String KEY_USERNAME = "username";
    private ArrayList<Reminder> arraylist;

    RecyclerView ListRmd;
    private RecyclerViewAdapter recyclerviewadapter;
    private String classname = "UpdateReminder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_reminder);

        ListRmd = (RecyclerView) findViewById(R.id.ReminderUpdateList);
        DBHandler dbHandler = new DBHandler(UpdateReminder.this);
        arraylist = new ArrayList<>();
        Cursor c = dbHandler.readReminder();
        while (c.moveToNext())
        {
            Reminder reminder = new Reminder(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getInt(5),c.getDouble(6),c.getDouble(7));
            arraylist.add(reminder);
        }

        recyclerviewadapter = new RecyclerViewAdapter(UpdateReminder.this,classname, arraylist, new ClickListener() {
            @Override
            public void onPositionClicked(int position) {
            }
        });
        ListRmd.setAdapter(recyclerviewadapter);
        ListRmd.setLayoutManager(new LinearLayoutManager(UpdateReminder.this));


        //********************************************************************************************************
        EditText IdMsgET = (EditText) findViewById(R.id.IdMessage);
        EditText MsgET = (EditText) findViewById(R.id.UpdtMessage);
        EditText DtET = (EditText) findViewById(R.id.UpdtDate);
        EditText LxET = (EditText) findViewById(R.id.UpdtLocationX);
        EditText LyET = (EditText) findViewById(R.id.UpdtLocationY);

        Button BtnUpdtRMD = (Button) findViewById(R.id.UpdateRmd);
        BtnUpdtRMD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DBHandler dbHandler = new DBHandler(UpdateReminder.this);

                String Id = IdMsgET.getText().toString();
                String Message = MsgET.getText().toString();
                String remember_time = DtET.getText().toString();
                String creation_time = java.text.DateFormat.getDateTimeInstance().format(new Date());
                preferences = getApplicationContext().getSharedPreferences("Log", 0);
                String creator_id = preferences.getString(KEY_USERNAME, "");
                int reminder_seen = 0;
                String Strlocation_x = LxET.getText().toString();
                double location_x = 0;
                if (Strlocation_x.length() > 0) {
                    location_x = Double.parseDouble(Strlocation_x);
                }
                String Strlocation_y = LyET.getText().toString();
                double location_y = 0;
                if (Strlocation_y.length() > 0){
                    location_y = Double.parseDouble(Strlocation_y);
                }

                if (Id.length() > 0 && (remember_time.length() > 0 || Message.length() > 0 || location_x != 0 || location_y != 0)) {

                    Toast.makeText(UpdateReminder.this, "Reminder Updated", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(UpdateReminder.this, "To update you need the old id and at least 1 component changed", Toast.LENGTH_SHORT).show();
                }

                if (Message.length() == 0)
                {
                    Message = dbHandler.GetAMessage(Id);
                }
                if (remember_time.length() == 0)
                {
                    remember_time = dbHandler.GetATime(Id);
                }
                if (Strlocation_x.length() == 0)
                {
                    location_x = dbHandler.GetALocX(Id);
                }
                if (Strlocation_y.length() == 0)
                {
                    location_y = dbHandler.GetALocY(Id);
                }

                dbHandler.updateReminder(Id,Message, remember_time, creation_time, creator_id, reminder_seen, location_x, location_y);
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });
    }

    public void BackToMain(View view) {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }
}