package com.app.ali.locationfinder;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import Utils.AsyncResponse;
import Utils.RequestExecutor;
import Utils.RgPreference;


/**
 * Created by Ali ( 03154342359 ) on 7/26/2016.
 */
public class LocationService extends Service implements AsyncResponse {

    LocationManager mlocmag;
    LocationListener mlocList;
    Location loc;
    String Bid;
    String lat;
    String lng;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Bundle extras = intent.getExtras();
        Bid = extras.getString("BID");


        Toast.makeText(this, "Service Start...." + Bid, Toast.LENGTH_SHORT).show();

        UpdateWithNewLocation(loc); // This method is used to get updated location.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mlocmag.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocList);


        // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Bid = sharedPreferences.getString(RgPreference.Bus_Id,null);




    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mlocmag = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocList = new MyLocationList();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        loc = mlocmag.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
      //  UpdateWithNewLocation(loc);







        /*
        mlocmag = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocList = new MyLocationList();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
         Location loc = mlocmag.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        UpdateWithNewLocation(loc); // This method is used to get updated location.
        mlocmag.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocList);



       // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
       // Bid = sharedPreferences.getString(RgPreference.Bus_Id,null);

        Toast.makeText(this, "Service Start...." + Bid, Toast.LENGTH_SHORT).show();





*/


    }







    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mlocmag.removeUpdates(mlocList);

        Toast.makeText(this, "Service Stop....",Toast.LENGTH_SHORT).show();

    }

    private void UpdateWithNewLocation(final Location loc) {
        // TODO Auto-generated method stub

        if(loc!= null)
        {
            lat = ""+loc.getLatitude(); // Updated lat
            lng = ""+loc.getLongitude(); // Updated long


            RequestExecutor re = new RequestExecutor(this);
            re.delegate = this;
            re.execute("2",lat,lng,Bid);


            Toast.makeText(this, "Your location is "+ "" + lat  +" "+ lng + " ::::: " + Bid,Toast.LENGTH_SHORT).show();


        }

        else
        {
            String latLongStr = "No lat and longitude found";
            Toast.makeText(this, "Your location is "+latLongStr ,Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onProcessCompelete(Object result) {

    }


    public class MyLocationList implements LocationListener {

        public void onLocationChanged(Location arg0) {
            // TODO Auto-generated method stub
            UpdateWithNewLocation(arg0);
        }

        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
            Toast.makeText(getApplicationContext(), "GPS Disable ", Toast.LENGTH_SHORT).show();
        }

        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
            Toast.makeText(getApplicationContext(), "GPS enabled", Toast.LENGTH_SHORT).show();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }


    }



}
