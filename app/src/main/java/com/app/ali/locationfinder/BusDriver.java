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

    boolean b=true;
    private CustomUniBusAdapter adapter;
    Button btn ;
    String ID;
    private ProgressDialog progress;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_driver);




        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait !!!");
        progress.setMessage(" Loading ...");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();


        RequestExecutor re = new RequestExecutor(this);
        re.delegate = this;
        re.execute("1");








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
                //Toast.makeText(BusDriver.this, "ID: " + obj.getId() + "\nName: " + obj.getName(),
                  //      Toast.LENGTH_SHORT).show();


                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BusDriver.this);
                sharedPreferences.edit().putString(RgPreference.Bus_Id,"" + obj.getId()).commit();
                ID = sharedPreferences.getString(RgPreference.Bus_Id,null);

                Toast.makeText(BusDriver.this, "ID: " + ID,
                        Toast.LENGTH_SHORT).show();




            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });


    }




public void StartStopService(View view)
{
 changeOption(mySpinner);
}


    private void changeOption(Spinner spinner)
    {


        if (b) {
            spinner.setEnabled(false);
            Intent i = new Intent(BusDriver.this,LocationService.class);
            i.putExtra("BID",ID );
            startService(i);

//            sharedPreferences.edit().putString("ServiceState", "Start").apply();

            btn.setText("Stop Service");
            btn.setBackgroundColor(Color.parseColor("green"));
            b=false;
        } else {
            spinner.setEnabled(true);
            stopService(new Intent(BusDriver.this,LocationService.class));
//            sharedPreferences.edit().putString("ServiceState", "Stop").apply();

            btn.setText("Start Service");
            btn.setBackgroundColor(Color.parseColor("red"));
            b=true;

        }

    }





}
