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

    public myWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }

    @NonNull
    @Override
    public Result doWork() {
        String[] array = getTags().toArray(new String[0]);
        displayNotification(array[0],getInputData().getString(TIME_KEY));
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
