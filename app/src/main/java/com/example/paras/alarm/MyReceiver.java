package com.example.paras.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.concurrent.ExecutionException;

import static android.content.Context.ALARM_SERVICE;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.d("debug", "Phone Restarted");
            FileReadWrite ffile = new FileReadWrite(context);

            if(ffile.readSavedData()!=""){
                Log.d("debug", "Setting alarm from file");
                SetAlarm s = new SetAlarm(context);
                s.set();
            }
            else {

                SharedPreferences settings = context.getSharedPreferences("File", 0);
                String url = settings.getString("url", "URL");

                if (url == "URL") {
                    Log.d("paras", "Not a valid url");
                } else {
                    try {
                        String json = new LoadFromUrl(context).execute(url).get();
                        //Log.d("paras", json);
                        // Toast.makeText(context, "Loaded File", Toast.LENGTH_LONG);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }

            SharedPreferences settings = context.getSharedPreferences("File", 0);
            int interval = settings.getInt("interval", 180);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

            Intent myIntent = new Intent(context, IntervalReceiver.class);
            long time = System.currentTimeMillis()+interval*60*1000;
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, time, interval*60*1000, pendingIntent);
        }
    }
}
