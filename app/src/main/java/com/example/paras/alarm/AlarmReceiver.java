package com.example.paras.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        long Time = Calendar.getInstance().getTimeInMillis();
        String desc = intent.getExtras().getString("desc");
        int id = intent.getExtras().getInt("id");

        Intent myIntent = new Intent(context, AlarmReceiver.class);
        myIntent.putExtra("desc", desc);
        myIntent.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, Time+7*AlarmManager.INTERVAL_DAY, pendingIntent);

        Log.d("paras", intent.getExtras().getString("desc"));
        Intent i = new Intent(context, Main2Activity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        i.putExtra("desc", desc);
        context.startActivity(i);

        //context.startActivity(new Intent(context, Main2Activity.class));
    }
}
