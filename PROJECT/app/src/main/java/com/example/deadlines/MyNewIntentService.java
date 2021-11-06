package com.example.deadlines;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Objects;

public class MyNewIntentService extends IntentService {
    private static final int NOTIFICATION_ID = 3;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    public MyNewIntentService() {
        super("MyNewIntentService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Calendar cal = Calendar.getInstance();
        final Integer[] amountOfDeadlines = {null};
        //создание уведомления
        FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(auth.getUid())).child("Deadlines").child("Accepted").child(String.valueOf(cal.get(Calendar.YEAR)-1900)).child(String.valueOf(cal.get(Calendar.MONTH))).child(String.valueOf(cal.get(Calendar.DAY_OF_MONTH))).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    amountOfDeadlines[0] = Math.toIntExact(snapshot.getChildrenCount());
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel("SH",
                            "Simple",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setDescription("Notifications");
                    mNotificationManager.createNotificationChannel(channel);
                }
                NotificationCompat.Builder mBuilder;
                if (!amountOfDeadlines[0].equals(0)){
                    mBuilder = new NotificationCompat.Builder(getApplicationContext(), "SH")
                            .setSmallIcon(R.drawable.ic_baseline_circle_green_24) // notification icon
                            .setContentTitle("Дедлайны на сегодня") // title for notification
                            .setContentText("Сегодня у тебя дедлайнов: "+amountOfDeadlines[0]+".")// message for notification
                            .setAutoCancel(true); // clear notification after click
                }
                else {
                    mBuilder = new NotificationCompat.Builder(getApplicationContext(), "SH")
                            .setSmallIcon(R.drawable.ic_baseline_circle_green_24) // notification icon
                            .setContentTitle("Дедлайны на сегодня") // title for notification
                            .setContentText("Сегодня у тебя нет дедлайнов")// message for notification
                            .setAutoCancel(true); // clear notification after click
                }
                Intent intent1 = new Intent(getApplicationContext(), StartActivity.class);
                PendingIntent pi = PendingIntent.getActivity(getBaseContext(), 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(pi);
                mNotificationManager.notify(0, mBuilder.build());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
