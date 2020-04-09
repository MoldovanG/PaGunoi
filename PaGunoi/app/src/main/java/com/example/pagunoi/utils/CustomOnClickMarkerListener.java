package com.example.pagunoi.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;

import com.example.pagunoi.R;
import com.example.pagunoi.activities.ReportCreatorActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CustomOnClickMarkerListener implements GoogleMap.OnMarkerClickListener {
    private Context mContext;
    public CustomOnClickMarkerListener(Context context) {
        mContext = context;
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Doriti sa creati un raport pentru aceasta locatie ?")
                .setCancelable(false)
                .setPositiveButton(R.string.confirm_dialog, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(mContext, ReportCreatorActivity.class);
                        StringBuilder builder = new StringBuilder();
                        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
                        List<Address> addresses = null; //1 num of possible location returned
                        try {
                            addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String address;
                        if (addresses != null) {
                            address = addresses.get(0).getAddressLine(0); //0 to obtain first possible address
                        }
                        else {
                            address = "Geocoder service not working";
                        }
                        builder.append(address);
                        builder.append("\n GPS Coordinates: ");
                        builder.append(marker.getPosition().latitude + ", " + marker.getPosition().longitude);

                        intent.putExtra(ReportCreatorActivity.LOCATION_KEY,builder.toString());
                        mContext.startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.reject_dialog, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        Dialog dialog = builder.create();
        dialog.show();
        return true;
    }
}
