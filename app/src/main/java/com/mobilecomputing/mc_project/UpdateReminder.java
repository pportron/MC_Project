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

public class UpdateReminder extends AppCompatActivity {

    private SharedPreferences preferences;
    private static final String KEY_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_reminder);

        EditText IdMsgET = (EditText) findViewById(R.id.IdMessage);
        EditText MsgET = (EditText) findViewById(R.id.UpdtMessage);
        EditText DtET = (EditText) findViewById(R.id.UpdtDate);
        EditText LxET = (EditText) findViewById(R.id.UpdtLocationX);
        EditText LyET = (EditText) findViewById(R.id.UpdtLocationY);

        Button BtnUpdtRMD = (Button) findViewById(R.id.UpdateRmd);
        BtnUpdtRMD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Id = IdMsgET.getText().toString();
                String Message = MsgET.getText().toString();
                String remember_time = DtET.getText().toString();
                String creation_time = java.text.DateFormat.getDateTimeInstance().format(new Date());
                preferences = getApplicationContext().getSharedPreferences("Log", 0);
                String creator_id = preferences.getString(KEY_USERNAME, "");
                int reminder_seen = 0;
                String Strlocation_x = LxET.getText().toString();
                double location_x = 0;
                if (Strlocation_x.length() > 0) {
                    location_x = Double.parseDouble(Strlocation_x);
                }
                String Strlocation_y = LyET.getText().toString();
                double location_y = 0;
                if (Strlocation_x.length() > 0){
                    location_y = Double.parseDouble(Strlocation_y);
                }


                if (Id.length() > 0 && (remember_time.length() > 0 || Message.length() > 0 || location_x != 0 || location_y != 0)) {
                    DBHandler dbHandler = new DBHandler(UpdateReminder.this);
                    dbHandler.updateReminder(Id,Message, remember_time, creation_time, creator_id, reminder_seen, location_x, location_y);
                    Toast.makeText(UpdateReminder.this, "Reminder Added", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(UpdateReminder.this, "To update you need the old message and at least 1 component changed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void BackToMain(View view) {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }
}