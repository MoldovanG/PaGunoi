package com.example.pagunoi.utils;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.widget.Toast;

import java.lang.reflect.Method;

public class EmailSender {

    public static void sendEmail(Context context,
                                 Uri imageUri,
                                 String name,
                                 String adress,
                                 String locatieSesizare,
                                 String mentiuni) {
        if(Build.VERSION.SDK_INT>=24){
            try{
                //For API's > 24, runtime exception occurs when a URI is exposed BEYOND this particular app that you are writing (AKA when user attempts to open in device/emulator
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try
        {
            String email = "garda_mediu@yahoo.com";
            String subject = "Sesizare depozitare ilegala gunoi";
            String message = MessageCreator.createIllegalGarbageComplaint(name,
                    adress,
                    locatieSesizare,
                    mentiuni);
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { email });
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,subject);
            if (imageUri != null) {
                emailIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            }
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
            context.startActivity(Intent.createChooser(emailIntent,"Sending email..."));
        }
        catch (Throwable t)
        {
            Toast.makeText(context, "Request failed try again: " + t.toString(),Toast.LENGTH_LONG).show();
        }
    }

}
