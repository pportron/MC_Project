package com.mobilecomputing.mc_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profile extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static final String KEY_USERNAME="username";
    private static final String KEY_PASSWORD="password";

    private static final String KEY_LOCX="locx";
    private static final String KEY_LOCY="locy";

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        TextView usernameuser = (TextView) findViewById(R.id.logusername);
        TextView passworduser = (TextView) findViewById(R.id.logpassword);
        TextView locxuser = (TextView) findViewById(R.id.loglocx);
        TextView locyuser = (TextView) findViewById(R.id.loglocy);

        preferences = getApplicationContext().getSharedPreferences("Log", 0);
        String UsernameSaved = preferences.getString(KEY_USERNAME, "");
        String PasswordSaved = preferences.getString(KEY_PASSWORD,"");

        usernameuser.setText(UsernameSaved);
        passworduser.setText(PasswordSaved);

        preferences = getApplicationContext().getSharedPreferences("Loc", 0);
        String LocXSaved = preferences.getString(KEY_LOCX, "");
        String LocYSaved = preferences.getString(KEY_LOCY,"");

        locxuser.setText(LocXSaved);
        locyuser.setText(LocYSaved);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.page_profile);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.page_rmd:
                Intent intent3 = new Intent(this, Main.class);
                startActivity(intent3);
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
                return true;

            case R.id.page_map:
                Intent intent4 = new Intent(this, MapsActivity.class);
                startActivity(intent4);
                return true;
        }
        return false;
    }

    public void GoToLogin(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}