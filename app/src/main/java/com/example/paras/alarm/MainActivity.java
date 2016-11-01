package com.example.paras.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar ab=getSupportActionBar();
        ab.setTitle("Settings");

        Button load= (Button) findViewById(R.id.button);
        final Context context = this;
        load.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText URL = (EditText) findViewById(R.id.editText);
                String url = String.valueOf(URL.getText());
                if(url=="URL" || url==""){
                    Toast.makeText(context, "Enter a Valid URL", Toast.LENGTH_SHORT).show();
                }
                else{
                    SharedPreferences settings = getSharedPreferences("File", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("url", url);
                    editor.commit();

                    try {
                        String json = new LoadFromUrl(MainActivity.this).execute(url).get();
                        //Log.d("paras", json);
                        Toast.makeText(context, "Loaded File", Toast.LENGTH_LONG);

                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);
                        long time = System.currentTimeMillis()+5*1000;
                        myIntent.putExtra("desc", json);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, (int)time, myIntent, 0);
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        ComponentName receiver = new ComponentName(context, MyReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Button Delete = (Button) findViewById(R.id.button2);
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ToDo: Logic for deleting the alarms

                ComponentName receiver = new ComponentName(context, MyReceiver.class);
                PackageManager pm = context.getPackageManager();

                pm.setComponentEnabledSetting(receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
            }
        });

        /*
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        long time = System.currentTimeMillis()+5*1000;
        myIntent.putExtra("desc", "First");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, (int)time, myIntent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        time = System.currentTimeMillis()+10*1000;
        Intent myInten = new Intent(MainActivity.this, AlarmReceiver.class);
        myInten.putExtra("desc", "Second");
        PendingIntent pendingInten = PendingIntent.getBroadcast(MainActivity.this, (int)time, myInten, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingInten);
        */
    }
}
