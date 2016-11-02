package com.example.paras.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by paras on 11/1/16.
 */

public class SetAlarm {
    Context context;
    SetAlarm(Context ctx){
        context = ctx;
    }
    public void set(){
        FileReadWrite ffile = new FileReadWrite(context);
        try {
            JSONArray jArray = new JSONArray(ffile.readSavedData());
            int idx=1;

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            switch (day) {
                case Calendar.SUNDAY:
                    day=1;
                    break;
                case Calendar.MONDAY:
                    day=2;
                    break;
                case Calendar.TUESDAY:
                    day=3;
                    break;
                case Calendar.WEDNESDAY:
                    day=4;
                    break;
                case Calendar.THURSDAY:
                    day=5;
                    break;
                case Calendar.FRIDAY:
                    day=6;
                    break;
                case Calendar.SATURDAY:
                    day=7;
                    break;
            }

            for (int i = 0; i < jArray.length(); i++) {

                JSONObject jObject = jArray.getJSONObject(i);

                String time = jObject.getString("time");
                String desc = jObject.getString("description");
                Log.d("paras", desc);
                int hour = Integer.parseInt(time.substring(0, time.length() - 2));
                int minute = Integer.parseInt(time.substring(time.length() - 2));
                JSONArray arr = jObject.getJSONArray("repeat");

                for(int j=0;j<arr.length();j++){
                    int d = arr.getInt(j);

                    if(d<1 || d>7){
                        Log.d("paras", "Wrong day value");
                        d = d%7 + 1;
                    }

                    Calendar OffSet = Calendar.getInstance();
                    OffSet.set(Calendar.HOUR_OF_DAY, hour);
                    OffSet.set(Calendar.MINUTE, minute);
                    OffSet.set(Calendar.SECOND, 00);
                    OffSet.set(Calendar.MILLISECOND, 00);

                    long Time = OffSet.getTimeInMillis();

                    if(d>day){
                        Time += (d-day)*AlarmManager.INTERVAL_DAY;
                    }
                    else if(d==day){
                        if(Calendar.getInstance().getTimeInMillis()>=Time){
                            Time += 7*AlarmManager.INTERVAL_DAY;
                        }
                    }
                    else{
                        Time += (7-day+d)*AlarmManager.INTERVAL_DAY;
                    }

                    Intent myIntent = new Intent(context, AlarmReceiver.class);
                    myIntent.putExtra("desc", desc);
                    myIntent.putExtra("id", idx);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, idx, myIntent, 0);
                    idx++;
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, Time, pendingIntent);
                    Log.d("paras", "Setting Alarm "+Integer.toString(idx-1)+" "+Long.toString(Time)+" "+Integer.toString(d)+" "+Integer.toString(day));
                }

                Log.d("paras", Integer.toString(hour) + ":" + Integer.toString(minute) + " " + desc);

            } // End Loop
        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
        } // catch (JSONException e)
    }

    public void deleteall(){
        Log.d("debug", "Delete called");
        FileReadWrite ffile = new FileReadWrite(context);

        try {
            JSONArray jArray = new JSONArray(ffile.readSavedData());
            int idx=1;

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

            for (int i = 0; i < jArray.length(); i++) {

                JSONObject jObject = jArray.getJSONObject(i);

                String time = jObject.getString("time");
                Log.d("debug", time);
                while(time.length()<4){
                    time += "0"+time;
                }
                String desc = jObject.getString("description");
                int hour = Integer.parseInt(time.substring(0, time.length() - 2));
                int minute = Integer.parseInt(time.substring(time.length() - 2));
                JSONArray arr = jObject.getJSONArray("repeat");

                for(int j=0;j<arr.length();j++){
                    int d = arr.getInt(j);

                    Intent myIntent = new Intent(context, AlarmReceiver.class);
                    myIntent.putExtra("desc", desc);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, idx, myIntent, 0);
                    idx++;
                    alarmManager.cancel(pendingIntent);
                    Log.d("debug", "Deleting Alarm "+Integer.toString(idx-1));
                }

               // Log.d("paras", Integer.toString(hour) + ":" + Integer.toString(minute) + " " + desc);

            } // End Loop
        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
        } // catch (JSONException e)
    }
}
