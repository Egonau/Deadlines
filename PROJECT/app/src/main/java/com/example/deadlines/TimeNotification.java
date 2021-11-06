package com.example.deadlines;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class TimeNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, MyNewIntentService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startService(intent1);
        }
    }
}
