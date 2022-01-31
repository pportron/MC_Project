package com.mobilecomputing.mc_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void GoToLogin(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        //bite
    }

    public void GoToProfile(View view) {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }
}