package com.mobilecomputing.mc_project;

import androidx.fragment.app.FragmentActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobilecomputing.mc_project.databinding.ActivityMapsBinding;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static final String KEY_LOCX="locx";
    private static final String KEY_LOCY="locy";

    private static final String TIME_KEY = "TimeKey";
    private static final String ID_KEY = "IdKey";

    private ArrayList<Reminder> arraylist;

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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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
                ArrayList<Reminder> arraylocx = new ArrayList<>();
                ArrayList<Reminder> arraylocy = new ArrayList<>();
                Cursor c = dbHandler.readReminder();
                int nbrmd = 0;
                while (c.moveToNext())
                {
                    Reminder reminder = new Reminder(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getInt(5),c.getDouble(6),c.getDouble(7));
                    arraylist.add(reminder);
                    nbrmd++;
                }
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
                        if ((LocX - locxonmap <= 1 && LocX - locxonmap >= -1) && (LocY - locyonmap <= 1 && LocY - locyonmap >= -1))
                        {
                            String rmdtime = rmd.getReminder_time();
                            String Msg = rmd.getMessage();
                            final OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(myWorker.class)
                                    .setInputData(new Data.Builder().putString(TIME_KEY,rmdtime).putInt(ID_KEY,id).build())
                                    .addTag(Msg)
                                    .build();

                            WorkManager.getInstance().enqueue(request);
                            Intent intent = new Intent(MapsActivity.this, Main.class);
                            startActivity(intent);
                        }
                        i++;
                    }
                }while (i < nbrmd);
            }
        });
    }
}