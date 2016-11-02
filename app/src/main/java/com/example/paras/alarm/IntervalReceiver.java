package com.example.paras.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.concurrent.ExecutionException;

public class IntervalReceiver extends BroadcastReceiver {
    public IntervalReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("debug", "Interval Receiver started");

        SharedPreferences settings = context.getSharedPreferences("File", 0);
        String url = settings.getString("url", "URL");

        if(url=="URL"){
            Log.d("paras", "Not a valid url");
        }
        else {
            try {
                Log.d("debug", "Loading URL in interval receiver");
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
}
