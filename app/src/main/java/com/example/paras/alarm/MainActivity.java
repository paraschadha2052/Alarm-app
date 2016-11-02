package com.example.paras.alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar ab=getSupportActionBar();
        ab.setTitle("Settings");

        final EditText URL = (EditText) findViewById(R.id.editText);
        final SharedPreferences settings = getSharedPreferences("File", 0);

        URL.setText(settings.getString("url", "Enter URL"));

        final int interval = settings.getInt("interval", 180);

        Button load= (Button) findViewById(R.id.button);
        final Context context = this;
        load.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ComponentName receiver = new ComponentName(context, MyReceiver.class);
                PackageManager pm = context.getPackageManager();

                pm.setComponentEnabledSetting(receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);

                Log.d("debug", "Restart receiver set");

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                Intent myInten = new Intent(MainActivity.this, IntervalReceiver.class);
                PendingIntent pendingInten = PendingIntent.getBroadcast(MainActivity.this, 0, myInten, 0);
                alarmManager.cancel(pendingInten);

                Log.d("debug", "Interval Pending alert cancelled");

                Intent myIntent = new Intent(MainActivity.this, IntervalReceiver.class);
                long time = System.currentTimeMillis()+interval*60*1000;
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, 0);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, time, interval*60*1000, pendingIntent);

                Log.d("debug", "Interval Pending alert set");

                String url = String.valueOf(URL.getText());
                if(url=="URL" || url==""){
                    Toast.makeText(context, "Enter a Valid URL", Toast.LENGTH_SHORT).show();
                }
                else{
                    SharedPreferences settings = getSharedPreferences("File", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("url", url);
                    editor.commit();

                    Log.d("debug", "URL set: "+url);

                    try {
                        Log.d("debug", "Call to json loader");
                        String json = new LoadFromUrl(MainActivity.this).execute(url).get();
                        Log.d("debug", "JSON loaded successfully");
                       // Toast.makeText(context, "Loaded File", Toast.LENGTH_LONG);


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Button Delete = (Button) findViewById(R.id.button2);
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FileReadWrite ffile = new FileReadWrite(context);

                Log.d("debug", "Deleting alarms");

                SetAlarm s = new SetAlarm(context);
                if(ffile.readSavedData()!=""){
                    s.deleteall();
                }

                Log.d("debug", "Stopping Restart Service");
                ComponentName receiver = new ComponentName(context, MyReceiver.class);
                PackageManager pm = context.getPackageManager();

                pm.setComponentEnabledSetting(receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);

                Log.d("debug", "Cancelling Intent Receiver");

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                Intent myInten = new Intent(MainActivity.this, IntervalReceiver.class);
                PendingIntent pendingInten = PendingIntent.getBroadcast(MainActivity.this, 0, myInten, 0);
                alarmManager.cancel(pendingInten);

            }
        });

        EditText ed = (EditText) findViewById(R.id.editText2);
        ed.setText(Integer.toString(interval));

        Button save = (Button) findViewById(R.id.button3);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("debug", "Setting Interval receiver");

                EditText ed = (EditText) findViewById(R.id.editText2);
                int interval = Integer.parseInt(String.valueOf(ed.getText()));

                SharedPreferences settings = getSharedPreferences("File", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("interval", interval);
                editor.commit();

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                Intent myInten = new Intent(MainActivity.this, IntervalReceiver.class);
                PendingIntent pendingInten = PendingIntent.getBroadcast(MainActivity.this, 0, myInten, 0);
                alarmManager.cancel(pendingInten);

                Intent myIntent = new Intent(MainActivity.this, IntervalReceiver.class);
                long time = System.currentTimeMillis()+interval*60*1000;
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, 0);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, time, interval*60*1000, pendingIntent);

                Log.d("debug", "Set Interval receiver");

            }
        });

        /*
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        long time = System.currentTimeMillis()+10*1000;
        myIntent.putExtra("desc", "First");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, (int)time, myIntent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        /*
        time = System.currentTimeMillis()+10*1000;
        Intent myInten = new Intent(MainActivity.this, AlarmReceiver.class);
        myInten.putExtra("desc", "Second");
        PendingIntent pendingInten = PendingIntent.getBroadcast(MainActivity.this, (int)time, myInten, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingInten);
        */
    }
}
