package com.mobilecomputing.mc_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Main extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    RecyclerView ListRmd;
    private ArrayList<Reminder> arraylist;
    private RecyclerViewAdapter recyclerviewadapter;
    private String classname = "Main";
    private static final String KEY_SHOW = "ToShow";

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ListRmd = (RecyclerView) findViewById(R.id.ReminderList);

        DBHandler dbHandler = new DBHandler(Main.this);
        arraylist = new ArrayList<>();
        Cursor c = dbHandler.readReminder();
        while (c.moveToNext())
        {
            Reminder reminder = new Reminder(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getInt(5),c.getDouble(6),c.getDouble(7));
            if (reminder.getReminder_seen() == 1 || getIntent().getExtras() != null)
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

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.page_rmd);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.page_rmd:
                return true;

            case R.id.page_add:
                Intent intent1 = new Intent(this, AddReminder.class);
                startActivity(intent1);
                return true;

            case R.id.page_update:
                Intent intent2 = new Intent(this, UpdateReminder.class);
                startActivity(intent2);
                return true;

            case R.id.page_profile:
                Intent intent3 = new Intent(this, Profile.class);
                startActivity(intent3);
                return true;

            case R.id.page_map:
                Intent intent4 = new Intent(this, MapsActivity.class);
                startActivity(intent4);
                return true;
        }
        return false;
    }
    }