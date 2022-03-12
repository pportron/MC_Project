package com.mobilecomputing.mc_project;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Main extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    RecyclerView ListRmd;
    private ArrayList<Reminder> arraylist;
    private ArrayList<Reminder> nearrmdlist;
    private RecyclerViewAdapter recyclerviewadapter;
    private String classname = "Main";
    private static final String KEY_SHOW = "ToShow";

    BottomNavigationView bottomNavigationView;

    LocationManager mLocationManager;
    int LOCATION_REFRESH_TIME = 15000; // 15 seconds to update
    int LOCATION_REFRESH_DISTANCE = 1; // 500 meters to update

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static final String KEY_LOCX="locx";
    private static final String KEY_LOCY="locy";
    private static final String TIME_KEY = "TimeKey";
    private static final String ID_KEY = "IdKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityResultLauncher<String[]> locationPermissionRequest =
                    registerForActivityResult(new ActivityResultContracts
                                    .RequestMultiplePermissions(), result -> {
                                //Boolean fineLocationGranted = result.getOrDefault(
                                //Manifest.permission.ACCESS_FINE_LOCATION, false);
                                Boolean fineLocationGranted = MapCompat.getOrDefault(result,Manifest.permission.ACCESS_FINE_LOCATION,false);
                                //Boolean coarseLocationGranted = result.getOrDefault(
                                //Manifest.permission.ACCESS_COARSE_LOCATION,false);
                                Boolean coarseLocationGranted = MapCompat.getOrDefault(result,Manifest.permission.ACCESS_COARSE_LOCATION,false);
                                if (fineLocationGranted != null && fineLocationGranted) {
                                    // Precise location access granted.
                                } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                    // Only approximate location access granted.
                                } else {
                                    // No location access granted.
                                }
                            }
                    );
            locationPermissionRequest.launch(new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);

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

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();
            String Latitude = String.valueOf(latitude);
            String Longitude = String.valueOf(longitude);

            preferences = getApplicationContext().getSharedPreferences("Loc", 0);
            editor = preferences.edit();
            editor.putString(KEY_LOCX, Latitude);
            editor.putString(KEY_LOCY, Longitude);

            DBHandler dbHandler = new DBHandler(Main.this);
            arraylist = new ArrayList<>();
            Cursor c = dbHandler.readReminder();
            int nbrmd = 0;
            while (c.moveToNext())
            {
                Reminder reminder = new Reminder(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getInt(5),c.getDouble(6),c.getDouble(7));
                arraylist.add(reminder);
                nbrmd++;
            }
            nearrmdlist = new ArrayList<>();
            int i = 0;
            do {
                if (arraylist.get(i) != null)
                {
                    Reminder rmd = arraylist.get(i);
                    int id = rmd.getId();
                    Double LocX = rmd.getLocation_x();
                    Double LocY = rmd.getLocation_y();
                    preferences = getApplicationContext().getSharedPreferences("Loc", 0);
                    String LocXCurrent = preferences.getString(KEY_LOCY, "0");
                    String LocYCurrent = preferences.getString(KEY_LOCX, "0");
                    Double locxcurrent = Double.parseDouble(LocXCurrent);
                    Double locycurrent = Double.parseDouble(LocYCurrent);
                    if ((LocX - locxcurrent <= 0.1 && LocX - locxcurrent >= -0.1) && (LocY - locycurrent <= 0.1 && LocY - locycurrent >= -0.1) && rmd.getReminder_seen() == 0)
                    {
                            String rmdtime = rmd.getReminder_time();
                            String Msg = rmd.getMessage();
                            final OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(myWorker.class)
                                    .setInputData(new Data.Builder().putString(TIME_KEY,rmdtime).putInt(ID_KEY,id).build())
                                    .addTag(Msg)
                                    .build();
                            WorkManager.getInstance().enqueue(request);

                            dbHandler.updateReminder(String.valueOf(id),rmd.getMessage(),rmd.getReminder_time(),rmd.getCreation_time(),rmd.getCreator_id(),1,LocX,LocY);
                    }
                    i++;
                }
            }while (i < nbrmd);
        }
        @Override public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override public void onProviderEnabled(String provider) {}
        @Override public void onProviderDisabled(String provider) {}
    };

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