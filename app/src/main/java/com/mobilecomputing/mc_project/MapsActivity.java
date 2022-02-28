package com.mobilecomputing.mc_project;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobilecomputing.mc_project.databinding.ActivityMapsBinding;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, BottomNavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static final String KEY_LOCX="locx";
    private static final String KEY_LOCY="locy";

    private static final String TIME_KEY = "TimeKey";
    private static final String ID_KEY = "IdKey";

    private ArrayList<Reminder> arraylist;

    RecyclerView ListRmd;
    private ArrayList<Reminder> arraynearrmd;
    private RecyclerViewAdapter recyclerviewadapter;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        LatLng Helsinki= new LatLng(60,25);
        mMap.addMarker(new MarkerOptions().position(Helsinki).title("You are here"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(Helsinki));

        preferences = getApplicationContext().getSharedPreferences("Loc", 0);
        editor = preferences.edit();
        String loc_x = Double.toString(Helsinki.longitude);
        String loc_y = Double.toString(Helsinki.latitude);
        editor.putString(KEY_LOCX, loc_x);
        editor.putString(KEY_LOCY,loc_y);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                mMap.clear();
                //Log.d("Marker at : ", latLng.longitude + "-" + latLng.latitude);
                mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));
                String loc_x = Double.toString(latLng.longitude);
                String loc_y = Double.toString(latLng.latitude);
                editor.putString(KEY_LOCX, loc_x);
                editor.putString(KEY_LOCY,loc_y);
                editor.commit();

                DBHandler dbHandler = new DBHandler(MapsActivity.this);
                arraylist = new ArrayList<>();
                Cursor c = dbHandler.readReminder();
                int nbrmd = 0;
                while (c.moveToNext())
                {
                    Reminder reminder = new Reminder(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getInt(5),c.getDouble(6),c.getDouble(7));
                    arraylist.add(reminder);
                    nbrmd++;
                }
                ListRmd = (RecyclerView) findViewById(R.id.NearRmdList);
                arraynearrmd = new ArrayList<>();
                int i = 0;
                do {
                    if (arraylist.get(i) != null)
                    {
                        Reminder rmd = arraylist.get(i);
                        int id = rmd.getId();
                        Double LocX = rmd.getLocation_x();
                        Double LocY = rmd.getLocation_y();
                        preferences = getApplicationContext().getSharedPreferences("Loc", 0);
                        String LocXonMap = preferences.getString(KEY_LOCX, "0");
                        String LocYonMap = preferences.getString(KEY_LOCY, "0");
                        Double locxonmap = Double.parseDouble(LocXonMap);
                        Double locyonmap = Double.parseDouble(LocYonMap);
                        if ((LocX - locxonmap <= 5 && LocX - locxonmap >= -5) && (LocY - locyonmap <= 5 && LocY - locyonmap >= -5))
                        {
                            arraynearrmd.add(rmd);
                            /*String rmdtime = rmd.getReminder_time();
                            String Msg = rmd.getMessage();
                            final OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(myWorker.class)
                                    .setInputData(new Data.Builder().putString(TIME_KEY,rmdtime).putInt(ID_KEY,id).build())
                                    .addTag(Msg)
                                    .build();

                            WorkManager.getInstance().enqueue(request);
                            Intent intent = new Intent(MapsActivity.this, Main.class);
                            startActivity(intent);*/
                        }
                        i++;
                    }
                }while (i < nbrmd);
                recyclerviewadapter = new RecyclerViewAdapter(MapsActivity.this,"MapsActivity", arraynearrmd, new ClickListener() {
                    @Override
                    public void onPositionClicked(int position) {
                    }
                });
                ListRmd.setAdapter(recyclerviewadapter);
                ListRmd.setLayoutManager(new LinearLayoutManager(MapsActivity.this));
                ListRmd.setVisibility(View.VISIBLE);
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.page_map);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.page_rmd:
                Intent intent4 = new Intent(this, Main.class);
                startActivity(intent4);
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
                return true;
        }
        return false;
    }
}