package com.mobilecomputing.mc_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class Main extends AppCompatActivity {

    RecyclerView ListRmd;
    private ArrayList<Reminder> arraylist;
    private RecyclerViewAdapter recyclerviewadapter;
    private String classname = "Main";
    private static final String KEY_SHOW = "ToShow";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Bundle Extras = getIntent().getExtras();
        int ToShow=0;
        if (Extras != null)
        {
            ToShow = 1;
        }


        ListRmd = (RecyclerView) findViewById(R.id.ReminderList);

        DBHandler dbHandler = new DBHandler(Main.this);
        arraylist = new ArrayList<>();
        Cursor c = dbHandler.readReminder();
        while (c.moveToNext())
        {
            Reminder reminder = new Reminder(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getInt(5),c.getDouble(6),c.getDouble(7));
            if (reminder.getReminder_seen() == 1 || ToShow == 1)
            {
                arraylist.add(reminder);
            }
        }

        recyclerviewadapter = new RecyclerViewAdapter(Main.this,classname, arraylist, new ClickListener() {
            @Override
            public void onPositionClicked(int position) {
                DBHandler dbHandler = new DBHandler(Main.this);

                Reminder rmd = arraylist.get(position);
                int id = rmd.getId();
                String Id = String.valueOf(id);
                dbHandler.deleteReminder(Id);
                Toast.makeText(Main.this, "Reminder deleted", Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });
        ListRmd.setAdapter(recyclerviewadapter);
        ListRmd.setLayoutManager(new LinearLayoutManager(Main.this));

        Button ShowBtn = (Button) findViewById(R.id.ShowAllBtn);
        ShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, 0);
                Intent intent = getIntent();
                intent.putExtra(KEY_SHOW,"1");
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
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
    public void GoToUpdate(View view) {
        Intent intent = new Intent(this, UpdateReminder.class);
        startActivity(intent);
    }

    }