package com.mobilecomputing.mc_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Login extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static final String ADMINUSERNAME="adminusername";
    private static final String ADMINPASSWORD="adminpassword";

    private static final String KEY_USERNAME="username";
    private static final String KEY_PASSWORD="password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        preferences = getApplicationContext().getSharedPreferences("Log", 0);
        String test = preferences.getString(ADMINUSERNAME, "");
        if (test.equals(""))
        {
            editor = preferences.edit();
            editor.putString(ADMINUSERNAME,"admin");
            editor.putString(ADMINPASSWORD,"admin");
            editor.apply();
        }

        EditText UsernameET = (EditText) findViewById(R.id.UsernameEditText);
        EditText PasswordET = (EditText) findViewById(R.id.PasswordEditText);
        Button BtnLogIn = (Button) findViewById(R.id.LoginButt);

        BtnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String UsernameEntered = UsernameET.getText().toString();
                String PasswordEntered = PasswordET.getText().toString();

                String UsernameSaved = preferences.getString(KEY_USERNAME, "");
                String PasswordSaved = preferences.getString(KEY_PASSWORD,"");

                String UsernameAdmin = preferences.getString(ADMINUSERNAME, "");
                String PasswordAdmin = preferences.getString(ADMINPASSWORD,"");

                if(UsernameEntered.length() <= 0){
                    Toast.makeText(Login.this, "Enter username", Toast.LENGTH_SHORT).show();
                }
                else if( PasswordEntered.length() <= 0){
                    Toast.makeText(Login.this, "Enter password", Toast.LENGTH_SHORT).show();
                }
                else {

                    if (UsernameEntered.equals(UsernameSaved) && PasswordEntered.equals(PasswordSaved) || UsernameEntered.equals(UsernameAdmin) && PasswordEntered.equals(PasswordAdmin)) {
                        Intent intent = new Intent(getApplicationContext(), Main.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Username/Password is incorrect",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    public void GoToRegister(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

}



