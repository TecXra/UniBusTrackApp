package com.app.ali.unibustrackerstudent;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import Model.UBus;
import Model.UStop;
import Utils.AsyncResponse;
import Utils.DirectionsJSONParser;
import Utils.RequestExecutor;
import Utils.Utils;

public class BusDetail extends FragmentActivity implements AsyncResponse {

    ArrayList<UStop> cordinatelist;
    GoogleMap map;
    ArrayList<LatLng> markerPoints;
    String Id = "";
    LatLng BusLocation;
    LatLng myLocation;
    private GoogleApiClient client;
    LatLng Blatlng;
    LatLng NearestStop;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_detail);
        Id = getIntent().getStringExtra("BusId");
        cordinatelist = (ArrayList<UStop>) getIntent().getSerializableExtra("StopList");
        //   Toast.makeText(getBaseContext(), "select : " + Id, Toast.LENGTH_SHORT).show();
        markerPoints = new ArrayList<LatLng>();
        // Getting reference to SupportMapFragment of the activity_main
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.brmap);
        ViewGroup.LayoutParams params = fm.getView().getLayoutParams();
        //  params.height = 750;
        fm.getView().setLayoutParams(params);
        // Getting Map for the SupportMapFragment
        map = fm.getMap();
        // Enable MyLocation Button in the Map
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
        map.setMyLocationEnabled(true);
        map.setOnMyLocationChangeListener(myLocationChangeListener());




        for (int i = 0; i < cordinatelist.size(); i++) {
            double lat = Double.parseDouble(cordinatelist.get(i).getStopLat());
            double lng = Double.parseDouble(cordinatelist.get(i).getStopLong());
            LatLng stops = new LatLng(lat, lng);
            String Stopname = cordinatelist.get(i).getName();
            setmarker(stops, Stopname);
        }


        if (markerPoints.size() >= 2) {
            int i = cordinatelist.size() - 1;
            LatLng origin = markerPoints.get(0);
            LatLng destination = markerPoints.get(i);
            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, destination);
            DownloadTask downloadTask = new DownloadTask();
            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                BusLocation();
            }
        }, 0, 10000);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener() {
        return new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                map.addMarker(new MarkerOptions().position(myLocation).title("google...").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            }
        };







    }


    public void setmarker(LatLng point, String Stopname) {
        if (markerPoints.size() >= 10) {
            return;
        }
        // Adding new item to the ArrayList
        markerPoints.add(point);
        // Creating MarkerOptions
        MarkerOptions options = new MarkerOptions();
        // Setting the position of the marker
        options.position(point);
        if (markerPoints.size() == 1) {
            options.title(Stopname);
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else if (markerPoints.size() == cordinatelist.size()) {
            options.title(Stopname);
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        } else {
            options.title(Stopname);
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPoints.get(0), 11));
        // Add new marker to the Google Map Android API V2
        map.addMarker(options);
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Waypoints
        String waypoints = "";
        for (int i = 2; i < markerPoints.size(); i++) {
            LatLng point = (LatLng) markerPoints.get(i);
            if (i == 2)
                waypoints = "waypoints=";
            waypoints += point.latitude + "," + point.longitude + "|";
        }
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + waypoints;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
//            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "BusDetail Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.app.ali.unibustrackerstudent/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "BusDetail Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.app.ali.unibustrackerstudent/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }
        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.BLUE);
            }
            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);
        }
    }





    // GET BUS CURRENT LOCATION


    private void BusLocation() {
        RequestExecutor r1 = new RequestExecutor(BusDetail.this);
        r1.delegate = BusDetail.this;
        r1.execute("2",Id);




    }


    @Override
    public void onProcessCompelete(Object ...result) {

        if((int)result[0]==2) {


            BusLocation = (LatLng) result[1];
            map.addMarker(new MarkerOptions().position(BusLocation).title("Bus Location..").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(BusLocation, 11));
            Blatlng = new LatLng(BusLocation.latitude, BusLocation.longitude);

        }




        if((int)result[0]==3) {


            String Distance = (String) result[1];
            TextView Mdist = (TextView) findViewById(R.id.textView5);
            Mdist.setText(Distance);

        }




        RequestExecutor r2 = new RequestExecutor(BusDetail.this);
        r2.delegate = BusDetail.this;
        r2.execute("3",Blatlng,myLocation);





    }





}