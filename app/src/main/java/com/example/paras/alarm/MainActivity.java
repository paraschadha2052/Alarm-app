package com.example.paras.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar ab=getSupportActionBar();
        ab.setTitle("Settings");

        Button b= (Button) findViewById(R.id.button);
        final Context context = this;
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Main2Activity.class);
                startActivity(i);
            }
        });

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
    }
}
