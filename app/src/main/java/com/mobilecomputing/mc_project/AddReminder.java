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
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import java.util.Date;
import java.util.concurrent.TimeUnit;


public class AddReminder extends AppCompatActivity {

    private SharedPreferences preferences;
    private static final String KEY_USERNAME = "username";

    private static final String TIME_KEY = "TimeKey";

    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    private EditText editText;
    private ImageView micButton;

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

                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    Long date = null;
                    try {
                        date = df.parse(remember_time).getTime();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long actualDate = System.currentTimeMillis();
                    long timetodate = date-actualDate;

                    final OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(myWorker.class)
                            .setInitialDelay(timetodate, TimeUnit.MILLISECONDS)
                            .setInputData(new Data.Builder().putString(TIME_KEY,remember_time).build())
                            .addTag(Message)
                            .build();

                    WorkManager.getInstance().enqueue(request);

                }


            }
        });
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

    public void BackToMain(View view) {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }

}