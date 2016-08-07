package com.app.ali.unibustrackerstudent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import Adapters.CustomUniBusAdapter;
import Model.UBus;
import Model.UStop;
import Utils.AsyncResponse;
import Utils.RequestExecutor;

public class BusList extends AppCompatActivity implements AsyncResponse {

    private Spinner mySpinner;
    ArrayList<UBus> list;
    ArrayList<UStop> Stops;
    boolean b=true;
    private CustomUniBusAdapter adapter;
    Button btn ;
    String Bid;
    private ProgressDialog progress;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_list);



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





    public void ShowDetail(View view)
 {




     Intent intent = new Intent(BusList.this,BusDetail.class);
     intent.putExtra("BusId",Bid);
     intent.putExtra("StopList",Stops);
     startActivity(intent);




 }


    @Override
    public void onProcessCompelete(Object... result) {
        progress.dismiss();

if((int)result[0]==1) {

    list = (ArrayList<UBus>) result[1];

    adapter = new CustomUniBusAdapter(BusList.this, android.R.layout.simple_spinner_item, list);
    btn = (Button) findViewById(R.id.StartStopBtn);
    mySpinner = (Spinner) findViewById(R.id.spinner);
    mySpinner.setAdapter(adapter); // Set the custom adapter to the spinner
    // You can create an anonymous listener to handle the event when is selected an spinner item
    mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view,
                                   int position, long id) {
            // Here you get the current item (a User object) that is selected by its position
            UBus obj = adapter.getItem(position);
            // Here you can do the action you want to...
            //Toast.makeText(BusDriver.this, "ID: " + obj.getId() + "\nName: " + obj.getName(),
            //      Toast.LENGTH_SHORT).show();

            Bid = obj.getId();
            Stops = obj.StopList;
            Toast.makeText(BusList.this, "ID: " + Bid,
                    Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapter) {
        }
    });

}
    }
}
