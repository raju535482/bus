package com.example.sony.citybusmanagement;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by SONY on 17-04-2016.
 */
public class MainActivty30 extends AppCompatActivity implements LocationListener{
    ArrayList<LatLng> mMarkerPoints;
    double mLatitude=0;
    double mLongitude=0;
    public static final String TAG = "Error";
    private static final String LOG_TAG = "CheckNetworkStatus";
    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;

        GoogleMap mGoogleMap;
        LatLng latLng;
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_map);
      //      RunTask();
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            receiver = new NetworkChangeReceiver();
            registerReceiver(receiver, filter);

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#004D40"));

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00695C")));
       mMarkerPoints = new ArrayList<LatLng>();

            // Getting reference to SupportMapFragment
            SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);

            // Creating GoogleMap from SupportMapFragment
            mGoogleMap= ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.0300, 72.5800), 14.0f));

            // Enabling MyLocation button for the Google Map
            mGoogleMap.setMyLocationEnabled(true);

            // Setting OnClickEvent listener for the GoogleMap
//            mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                @Override
//                public void onMapClick(LatLng latlng) {
//                    addMarker(latlng);
//                    sendToServer(latlng);
//                }
//            });

            // Starting locations retrieve task
//            new RetrieveTask().execute();

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Getting Current Location From GPS
            Location location = locationManager.getLastKnownLocation(provider);

            if(location!=null){
                onLocationChanged(location);
            }

            locationManager.requestLocationUpdates(provider, 20000, 0, this);

            // Setting onclick event listener for the map
            mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng point) {

                    // Already map contain destination location
//                    if(mMarkerPoints.size()>1){
//
//                        FragmentManager fm = getSupportFragmentManager();
//                        mMarkerPoints.clear();
//                        mGoogleMap.clear();
//                        LatLng startPoint = new LatLng(mLatitude, mLongitude);
//
//                        // draw the marker at the current position
//                        drawMarker(startPoint);
//                    }

                    // draws the marker at the currently touched location
                   // drawMarker(point);

                    // Checks, whether start and end locations are captured
                    if(mMarkerPoints.size() >= 2){
                        LatLng origin = mMarkerPoints.get(0);
                        LatLng dest = mMarkerPoints.get(1);

                        // Getting URL to the Google Directions API
                        String url = getDirectionsUrl(origin, dest);

                        DownloadTask downloadTask = new DownloadTask();

                        // Start downloading json data from Google Directions API
                        downloadTask.execute(url);
                    }

                }
            });


        }

    private void RunTask() {
        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {

            //Toast.makeText(getApplicationContext(),"Connected",8000).show();

        } else {

//            Toast.makeText(getApplicationContext(), "You are not online!!!!", 8000).show();
//            Log.v("Home", "############################You are not online!!!!");
            try {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivty30.this).create();

                alertDialog.setTitle("Problem occurred!!!");
                alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                alertDialog.setIcon(R.drawable.can1);
                alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                });
                alertDialog.setButton(Dialog.BUTTON_POSITIVE,"Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        RunTask();

                    }
                });
                alertDialog.show();
            } catch (Exception e) {
                String TAG="error";
                Log.d(TAG, "Show Dialog: "+e.getMessage());
            }
        }

    }

    private void drawMarker(LatLng point){
        mMarkerPoints.add(point);

        // Creating MarkerOptions
        MarkerOptions options = new MarkerOptions();

        // Setting the position of the marker
        options.position(point);

        /**
         * For the start location, the color of marker is GREEN and
         * for the end location, the color of marker is RED.
         */
//        if(mMarkerPoints.size()==1){
//            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//        }else if(mMarkerPoints.size()==2){
//            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//        }

        // Add new marker to the Google Map Android API V2
      //  mGoogleMap.addMarker(options);
    }


    //Adding marker on the GoogleMaps
        private void addMarker(LatLng latlng,String s) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latlng);
            markerOptions.title(s);
            markerOptions.snippet(+ latlng.latitude + "," + latlng.longitude);
            mGoogleMap.addMarker(markerOptions);
        }

    @Override
    public void onLocationChanged(Location location) {
        // Draw the marker, if destination location is not set
        if(mMarkerPoints.size() < 2){

            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            LatLng point = new LatLng(mLatitude, mLongitude);

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));

             drawMarker(point);
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    // Invoking background thread to store the touched location in Remove MySQL server
//        private void sendToServer(LatLng latlng) {
//            new SaveTask().execute(latlng);
//        }
        // Background thread to save the location in remove MySQL server
//        private class SaveTask extends AsyncTask<LatLng, Void, Void> {
//            @Override
//            protected Void doInBackground(LatLng... params) {
//                String lat = Double.toString(params[0].latitude);
//                String lng = Double.toString(params[0].longitude);
//                String strUrl = "http://192.168.1.3/location_marker_mysql/save.php";
//                URL url = null;
//                try {
//                    url = new URL(strUrl);
//
//                    HttpURLConnection connection = (HttpURLConnection) url
//                            .openConnection();
//                    connection.setRequestMethod("POST");
//                    connection.setDoOutput(true);
//                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
//                            connection.getOutputStream());
//
//                    outputStreamWriter.write("lat=" + lat + "&lng="+lng);
//                    outputStreamWriter.flush();
//                    outputStreamWriter.close();
//
//                    InputStream iStream = connection.getInputStream();
//                    BufferedReader reader = new BufferedReader(new
//                            InputStreamReader(iStream));
//
//                    StringBuffer sb = new StringBuffer();
//
//                    String line = "";
//
//                    while( (line = reader.readLine()) != null){
//                        sb.append(line);
//                    }
//
//                    reader.close();
//                    iStream.close();
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                return null;
//            }
//        }

        // Background task to retrieve locations from remote mysql server
        private class RetrieveTask extends AsyncTask<Void, Void, String>{

            @Override
            protected String doInBackground(Void... params) {
                String strUrl = "http://raju1221.dx.am/fetchLatlng.php";
                URL url = null;
                StringBuffer sb = new StringBuffer();
                try {
                    url = new URL(strUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    InputStream iStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(iStream));
                    String line = "";
                    while( (line = reader.readLine()) != null){
                        sb.append(line);
                    }

                    reader.close();
                    iStream.close();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return sb.toString();
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                new ParserTask().execute(result);
                Log.i("tagconvertstr", "[" + result + "]");

            }
        }

        // Background thread to parse the JSON data retrieved from MySQL server
        private class ParserTask extends AsyncTask<String, Void, List<HashMap<String, String>>>{
            @Override
            protected List<HashMap<String,String>> doInBackground(String... params) {
                MarkerJSONParser markerParser = new MarkerJSONParser();
                JSONObject json = null;

                try {
                    json = new JSONObject(params[0]);
//                    JSONArray jsonArray=json.getJSONArray("src");;
//
//                    for (int i=0;i<jsonArray.length();i++)
//                    {
//                        JSONObject c=jsonArray.getJSONObject(0);
//
//                        String latSrc=c.getString("lat");
//                        String lngSrc=c.getString("lng");
//
//
//
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                List<HashMap<String, String>> markersList = markerParser.parse(json);
                return markersList;
            }

            @Override
            protected void onPostExecute(List<HashMap<String, String>> result) {
                for(int i=0; i<result.size();i++){
                    HashMap<String, String> marker = result.get(i);
                    LatLng latlng = new LatLng(Double.parseDouble(marker.get("lat")), Double.parseDouble(marker.get("lng")));
                    String s= new String(marker.get("middlers"));
                    addMarker(latlng,s);

                }
            }
        }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        Log.i("origin:", "[" + str_origin + "]");
        Log.i("destn:", "[" + str_dest + "]");

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception in url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }



    /** A class to download data from Google Directions URL */
    private class DownloadTask extends AsyncTask<String, Void, String>{

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask1 parserTask = new ParserTask1();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Directions in JSON format */
    private class ParserTask1 extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
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
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(4);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            mGoogleMap.addPolyline(lineOptions);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        Log.v(LOG_TAG, "onDestory");
        super.onDestroy();

        unregisterReceiver(receiver);

    }


    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            Log.v(LOG_TAG, "Receieved notification about network status");
            isNetworkAvailable(context);

        }


        private boolean isNetworkAvailable(final Context context) {
            ConnectivityManager connectivity = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            if(!isConnected){
                                Log.v(LOG_TAG, "Now you are connected to Internet!");
                                new RetrieveTask().execute();

                                //  networkStatus.setText("Now you are connected to Internet!");
                                //Toast.makeText(getApplicationContext(),"Now you are connected to Internet!",Toast.LENGTH_LONG).show();
                            }
                            return true;
                        }
                    }
                }
            }
            Log.v(LOG_TAG, "You are not connected to Internet!");
            //    networkStatus.setText("You are not connected to Internet!");
            //Toast.makeText(getApplicationContext(),"You are not connected to Internet!",Toast.LENGTH_LONG).show();
            try {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivty30.this).create();

                alertDialog.setTitle("Problem occurred!!!");
                alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                alertDialog.setIcon(R.drawable.can1);
                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        isNetworkAvailable(context);
                    }
                });
                alertDialog.setButton(Dialog.BUTTON_NEGATIVE,"Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();


                    }
                });

                alertDialog.show();
            } catch (Exception e) {
                Log.d(TAG, "Connectivity Error: " + e.getMessage());
            }

            isConnected = false;
            return false;
        }
    }
}
