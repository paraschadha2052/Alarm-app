package com.example.paras.alarm;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by paras on 11/1/16.
 */

public class LoadFromUrl extends AsyncTask<String, String, String> {

    Context context;

    LoadFromUrl(Context c){
        context = c;
    }

    protected String doInBackground(String... params) {


        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder buffer = new StringBuilder();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
                //Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

            }
            //Log.d("paras", buffer.toString());
            return buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        FileReadWrite ffile = new FileReadWrite(context);
        ffile.writeData(result);

        try {
            JSONArray jArray = new JSONArray(ffile.readSavedData());
            for(int i=0; i < jArray.length(); i++) {

                JSONObject jObject = jArray.getJSONObject(i);

                String time = jObject.getString("time");
                String desc = jObject.getString("description");
                int hour = Integer.parseInt(time.substring(0,time.length()-2));
                int minute = Integer.parseInt(time.substring(time.length()-2));
                JSONArray arr = jObject.getJSONArray("repeat");

                Log.d("paras", Integer.toString(hour)+":"+Integer.toString(minute)+" "+desc);

            } // End Loop
        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
        } // catch (JSONException e)
        Toast.makeText(context, "Loaded File", Toast.LENGTH_LONG).show();

        Log.d("paras", "loading done");
    }

}
