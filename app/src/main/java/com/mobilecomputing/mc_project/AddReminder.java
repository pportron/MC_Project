package com.mobilecomputing.mc_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class AddReminder extends AppCompatActivity {

    private SharedPreferences preferences;
    private static final String KEY_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addremind);

        EditText MsgET = (EditText) findViewById(R.id.NewMessage);
        EditText DtET = (EditText) findViewById(R.id.NewDate);
        EditText LxET = (EditText) findViewById(R.id.LocationX);
        EditText LyET = (EditText) findViewById(R.id.LocationY);

        Button BtnADDRMD = (Button) findViewById(R.id.AddNewRButt);
        BtnADDRMD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Message = MsgET.getText().toString();
                String remember_time = DtET.getText().toString();
                String creation_time = java.text.DateFormat.getDateTimeInstance().format(new Date());
                preferences = getApplicationContext().getSharedPreferences("Log", 0);
                String creator_id = preferences.getString(KEY_USERNAME, "");
                int reminder_seen = 0;
                String Strlocation_x = LxET.getText().toString();
                double location_x = Double.parseDouble(Strlocation_x);
                String Strlocation_y = LyET.getText().toString();
                double location_y = Double.parseDouble(Strlocation_y);

                if (Message.length() <= 0) {
                    Toast.makeText(AddReminder.this, "Enter message", Toast.LENGTH_SHORT).show();
                } else if (remember_time.length() <= 0) {
                    Toast.makeText(AddReminder.this, "Enter due date", Toast.LENGTH_SHORT).show();
                } else if (Strlocation_x.length() <= 0) {
                    Toast.makeText(AddReminder.this, "Enter x location", Toast.LENGTH_SHORT).show();
                } else if (Strlocation_y.length() <= 0) {
                    Toast.makeText(AddReminder.this, "Enter y location", Toast.LENGTH_SHORT).show();
                } else {
                    DBHandler dbHandler = new DBHandler(AddReminder.this);
                    dbHandler.addNewReminder(Message, remember_time, creation_time, creator_id, reminder_seen, location_x, location_y);
                    Toast.makeText(AddReminder.this, "Reminder Added", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


    public void BackToMain(View view) {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }


}