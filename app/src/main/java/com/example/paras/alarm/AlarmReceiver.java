package com.example.paras.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("paras", intent.getExtras().getString("desc"));
        Intent i = new Intent(context, Main2Activity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        i.putExtra("desc", intent.getExtras().getString("desc"));
        context.startActivity(i);

        //context.startActivity(new Intent(context, Main2Activity.class));
    }
}
