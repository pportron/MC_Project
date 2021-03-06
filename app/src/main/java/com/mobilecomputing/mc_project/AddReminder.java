package com.mobilecomputing.mc_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import java.util.Date;
import java.util.concurrent.TimeUnit;


public class AddReminder extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences preferences;
    private static final String KEY_USERNAME = "username";

    private static final String TIME_KEY = "TimeKey";
    private static final String ID_KEY = "IdKey";
    private ArrayList<Reminder> arraylist;

    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    private EditText editText;
    private ImageView micButton;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addremind);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

        editText = findViewById(R.id.NewMessage);
        micButton = findViewById(R.id.imagemicro);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                editText.setText("");
                editText.setHint("Listening...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                editText.setText(data.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        micButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });

        EditText MsgET = (EditText) findViewById(R.id.NewMessage);
        EditText DtET = (EditText) findViewById(R.id.NewDate);
        EditText LxET = (EditText) findViewById(R.id.LocationX);
        EditText LyET = (EditText) findViewById(R.id.LocationY);

        Button BtnADDRMD = (Button) findViewById(R.id.AddNewRButt);
        BtnADDRMD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox NotifCheckBox = (CheckBox) findViewById(R.id.NotifBox);

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

                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Long date = null;
                try {
                    date = df.parse(remember_time).getTime();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long actualDate = System.currentTimeMillis();
                long timetodate = date-actualDate;
                if (timetodate <= 0)
                {
                    reminder_seen = 1;
                }

                if (Message.length() <= 0) {
                    Toast.makeText(AddReminder.this, "Enter message", Toast.LENGTH_SHORT).show();
                } else if (remember_time.length() != 10) {
                    Toast.makeText(AddReminder.this, "Enter due date", Toast.LENGTH_SHORT).show();
                } else if (Strlocation_x.length() <= 0) {
                    Toast.makeText(AddReminder.this, "Enter x location", Toast.LENGTH_SHORT).show();
                } else if (Strlocation_y.length() <= 0) {
                    Toast.makeText(AddReminder.this, "Enter y location", Toast.LENGTH_SHORT).show();
                } else {
                    DBHandler dbHandler = new DBHandler(AddReminder.this);
                    dbHandler.addNewReminder(Message, remember_time, creation_time, creator_id, reminder_seen, location_x, location_y);
                    Toast.makeText(AddReminder.this, "Reminder Added", Toast.LENGTH_SHORT).show();

                    if (NotifCheckBox.isChecked())
                    {
                        arraylist = new ArrayList<>();
                        int i = 0;
                        Cursor c = dbHandler.readReminder();
                        while (c.moveToNext())
                        {
                            Reminder reminder = new Reminder(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getInt(5),c.getDouble(6),c.getDouble(7));
                            arraylist.add(reminder);
                            i++;
                        }
                        Reminder rmd = arraylist.get(i-1);
                        int ID = rmd.getId();

                        final OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(myWorker.class)
                                .setInitialDelay(timetodate, TimeUnit.MILLISECONDS)
                                .setInputData(new Data.Builder().putString(TIME_KEY,remember_time).putInt(ID_KEY,ID).build())
                                .addTag(Message)
                                .build();

                        WorkManager.getInstance().enqueue(request);
                    }


                }


            }
        });
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.page_add);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.page_rmd:
                Intent intent1 = new Intent(this, Main.class);
                startActivity(intent1);
                return true;

            case R.id.page_add:
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
                Intent intent4 = new Intent(this, MapsActivity.class);
                startActivity(intent4);
                return true;
        }
        return false;
    }

}