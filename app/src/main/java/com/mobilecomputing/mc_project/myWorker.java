package com.mobilecomputing.mc_project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class myWorker extends Worker {

    private static final String TIME_KEY = "TimeKey";
    private static final String ID_KEY = "IdKey";
    private Context context;

    public myWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;

    }

    @NonNull
    @Override
    public Result doWork() {
        String[] array = getTags().toArray(new String[0]);
        displayNotification(array[0],getInputData().getString(TIME_KEY));

        DBHandler dbHandler = new DBHandler(context);
        int id = getInputData().getInt(ID_KEY,0);
        String Id=String.valueOf(id);
        String Message = dbHandler.GetAMessage(Id);
        String remember_time = dbHandler.GetATime(Id);
        String creation_time = dbHandler.GetACreatTime(Id);
        String creator_id = dbHandler.GetACreator(Id);
        Double location_x = dbHandler.GetALocX(Id);
        Double location_y = dbHandler.GetALocY(Id);
        int reminder_seen = 1;
        dbHandler.updateReminder(Id,Message, remember_time, creation_time, creator_id, reminder_seen, location_x, location_y);

        return Result.success();
    }

    private void displayNotification(String task, String desc)
    {
        NotificationManager manager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("a","b",NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        Intent intent = new Intent(getApplicationContext(), Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"a")
                .setContentTitle(task)
                .setContentText(desc)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher);

        manager.notify(1, builder.build());
    }
}
