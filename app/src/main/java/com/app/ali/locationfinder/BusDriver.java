package com.app.ali.locationfinder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Adapters.CustomUniBusAdapter;
import Models.UniBus;
import Utils.AsyncResponse;
import Utils.RequestExecutor;
import Utils.RgPreference;

public class BusDriver extends AppCompatActivity implements AsyncResponse {

    private Spinner mySpinner;
    ArrayList<UniBus> list;

    boolean flag;
    private CustomUniBusAdapter adapter;
    Button btn ;
    String service;
    String ID;
    private ProgressDialog progress;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_driver);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BusDriver.this);

        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait !!!");
        progress.setMessage(" Loading ...");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

        RequestExecutor re = new RequestExecutor(this);
        re.delegate = this;
        re.execute("1");


        Toast.makeText(BusDriver.this, "OnCreate",Toast.LENGTH_SHORT).show();



    }


















    @Override
    public void onProcessCompelete(Object result) {


        progress.dismiss();



        list = (ArrayList<UniBus>)result;

        adapter = new CustomUniBusAdapter(BusDriver.this, android.R.layout.simple_spinner_item,list);
        btn = (Button)findViewById(R.id.StartStopBtn) ;
        mySpinner = (Spinner) findViewById(R.id.spinner);
        mySpinner.setAdapter(adapter); // Set the custom adapter to the spinner
        // You can create an anonymous listener to handle the event when is selected an spinner item
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                UniBus obj = adapter.getItem(position);
                // Here you can do the action you want to...


                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BusDriver.this);
                sharedPreferences.edit().putString(RgPreference.Bus_Id,"" + obj.getId()).commit();
                ID = obj.getId();

                //  Toast.makeText(BusDriver.this, "ID: " + ID,
                //          Toast.LENGTH_SHORT).show();




            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });


        check();


    }



    @Override
    public void onStart() {
        super.onStart();

    }

    public void check() {



        // Toast.makeText(BusDriver.this, "OnStart",Toast.LENGTH_SHORT).show();

        flag =  sharedPreferences.getBoolean("flag",true);
        //    Toast.makeText(BusDriver.this,""+sharedPreferences.getBoolean("flag",false),
        //           Toast.LENGTH_SHORT).show();
        if (!flag) {
            mySpinner.setEnabled(false);

            btn.setText("Stop Service");
            btn.setBackgroundColor(Color.parseColor("#000066"));

        } else {
            mySpinner.setEnabled(true);
            btn.setText("Start Service");
            btn.setBackgroundColor(Color.parseColor("#DD741D"));


        }




    }




/*



    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(BusDriver.this, "OnResume",Toast.LENGTH_SHORT).show();

    }



    @Override
    public void onRestart() {
        super.onRestart();
        Toast.makeText(BusDriver.this, "OnRestart",Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onStop() {
        super.onStop();
        Toast.makeText(BusDriver.this, "OnStop",Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(BusDriver.this, "OnDestroy",Toast.LENGTH_SHORT).show();
    }




*/




    public void StartStopService(View view)
    {
        changeOption(mySpinner);
    }


    private void changeOption(Spinner spinner)
    {


        flag =  sharedPreferences.getBoolean("flag",true);

        if (flag) {
            spinner.setEnabled(false);
            Intent i = new Intent(BusDriver.this,LocationService.class);
            i.putExtra("BID",ID );
            startService(i);
            sharedPreferences.edit().putBoolean("flag",false).commit();
            sharedPreferences.edit().putString("ServiceState", "Start").apply();

            //   Toast.makeText(BusDriver.this,""+sharedPreferences.getBoolean("flag",false),
            //             Toast.LENGTH_SHORT).show();

            btn.setText("Stop Service");
            btn.setBackgroundColor(Color.parseColor("#000066"));

        } else {
            spinner.setEnabled(true);
            stopService(new Intent(BusDriver.this,LocationService.class));
            sharedPreferences.edit().putString("ServiceState", "Stop").apply();
            sharedPreferences.edit().putBoolean("flag",true).commit();
            btn.setText("Start Service");
            btn.setBackgroundColor(Color.parseColor("#DD741D"));
            //    Toast.makeText(BusDriver.this,""+sharedPreferences.getBoolean("flag",false),
            //             Toast.LENGTH_SHORT).show();

        }

    }

}
