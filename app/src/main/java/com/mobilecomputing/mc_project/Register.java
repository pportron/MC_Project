package com.mobilecomputing.mc_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;




public class Register extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static final String KEY_USERNAME="username";
    private static final String KEY_PASSWORD="password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        preferences = getApplicationContext().getSharedPreferences("Log", 0);

        EditText NewUsernameET = (EditText) findViewById(R.id.NewUsernameEditText);
        EditText NewPasswordET = (EditText) findViewById(R.id.NewPasswordEditText);
        EditText ConfirmPasswordET = (EditText) findViewById(R.id.ConfirmPasswordEditText);
        Button BtnReg = (Button) findViewById(R.id.ValidateRegisterButton);

        BtnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UsernameEntered = NewUsernameET.getText().toString();
                String PasswordEntered = NewPasswordET.getText().toString();
                String PasswordConfirmed = ConfirmPasswordET.getText().toString();

                if(UsernameEntered.length() <= 0){
                    Toast.makeText(Register.this, "Enter username", Toast.LENGTH_SHORT).show();
                }
                else if( PasswordEntered.length() <= 0){
                    Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
                }
                else if( PasswordConfirmed.length() <= 0){
                    Toast.makeText(Register.this, "Confirm password", Toast.LENGTH_SHORT).show();
                }
                else if( !PasswordEntered.equals(PasswordConfirmed)){
                    Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    editor = preferences.edit();
                    editor.putString(KEY_USERNAME, UsernameEntered);
                    editor.putString(KEY_PASSWORD,PasswordEntered);
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                }



            }
        });

    }

    public void BackToLogin(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}