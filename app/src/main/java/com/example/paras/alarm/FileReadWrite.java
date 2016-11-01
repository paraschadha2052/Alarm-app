package com.example.paras.alarm;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by paras on 11/1/16.
 */

public class FileReadWrite {
    Context c;
    FileReadWrite(Context ctx){
        c = ctx;
    }
    public void writeData ( String data ) {
        try {
            FileOutputStream fOut = c.openFileOutput ( "JSON.json" , c.MODE_PRIVATE ) ;
            OutputStreamWriter osw = new OutputStreamWriter ( fOut ) ;
            osw.write ( data ) ;
            osw.flush ( ) ;
            osw.close ( ) ;
        } catch ( Exception e ) {
            e.printStackTrace ( ) ;
        }
    }

    public String readSavedData ( ) {
        StringBuffer datax = new StringBuffer("");
        try {
            FileInputStream fIn = c.openFileInput ( "JSON.json" ) ;
            InputStreamReader isr = new InputStreamReader ( fIn ) ;
            BufferedReader buffreader = new BufferedReader ( isr ) ;

            String readString = buffreader.readLine ( ) ;
            while ( readString != null ) {
                datax.append(readString);
                readString = buffreader.readLine ( ) ;
            }

            isr.close ( ) ;
        } catch (IOException ioe ) {
            return "";
            //ioe.printStackTrace ( ) ;
        }
        return datax.toString() ;
    }
}
