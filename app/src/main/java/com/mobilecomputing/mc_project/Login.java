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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class Login extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static final String KEY_USERNAME="username";
    private static final String KEY_PASSWORD="password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //*******************************************************************************************************************************************************
        //Login method : fingerprint
        final Button FingerPBtn = findViewById(R.id.FingerprintBtn);

        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);

        // creating a variable for our Executor
        Executor executor = ContextCompat.getMainExecutor(this);
        // this will give us result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt(Login.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Intent intent = new Intent(getApplicationContext(), Main.class);
                startActivity(intent);
            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        // creating a variable for our promptInfo
        // BIOMETRIC DIALOG
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("GFG")
                .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();
        FingerPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });

        //****************************************************************************************************************************************************
        //Login method : username and password

        preferences = getApplicationContext().getSharedPreferences("Log", 0);
        String test = preferences.getString(KEY_USERNAME, "");
        if (test.equals(""))
        {
            editor = preferences.edit();
            editor.putString(KEY_USERNAME,"test");
            editor.putString(KEY_PASSWORD,"test");
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

                if(UsernameEntered.length() <= 0){
                    Toast.makeText(Login.this, "Enter username", Toast.LENGTH_SHORT).show();
                }
                else if( PasswordEntered.length() <= 0){
                    Toast.makeText(Login.this, "Enter password", Toast.LENGTH_SHORT).show();
                }
                else {

                    if (UsernameEntered.equals(UsernameSaved) && PasswordEntered.equals(PasswordSaved)) {
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