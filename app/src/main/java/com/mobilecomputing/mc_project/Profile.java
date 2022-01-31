package com.mobilecomputing.mc_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static final String KEY_USERNAME="username";
    private static final String KEY_PASSWORD="password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        TextView usernameuser = (TextView) findViewById(R.id.logusername);
        TextView passworduser = (TextView) findViewById(R.id.logpassword);

        preferences = getApplicationContext().getSharedPreferences("Log", 0);
        String UsernameSaved = preferences.getString(KEY_USERNAME, "");
        String PasswordSaved = preferences.getString(KEY_PASSWORD,"");

        usernameuser.setText(UsernameSaved);
        passworduser.setText(PasswordSaved);

    }

    public void GoToLogin(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void BackToMain(View view) {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }
}