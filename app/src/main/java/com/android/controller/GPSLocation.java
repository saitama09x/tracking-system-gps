package com.android.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.R;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.view.GravityCompat;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Location;
import android.app.AlertDialog;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import android.content.Context;
import android.os.AsyncTask;

public class GPSLocation extends AppCompatActivity implements OnMapReadyCallback {
  	ImageView letter_btn, shape_btn, start_btn, exit_btn;
  	TextView head_title, text_id, mTitle ;
  	private GPSTracker gps;
    private GoogleMap mMap;
    private DrawerLayout mDrawerLayout;
    protected LocationManager locationManager;
    private Location location;
    private double latitude;
    private double longitude;
    public static String source_label, dest_label;
    public static double source_lat, source_long, dest_lat, dest_long;
    public static int loc_id;
    boolean isNetworkEnabled = false;
    boolean isGPSEnabled = false;
    boolean isPassive = false;
    private GoogleMap map;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    public Context con = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       	setContentView(R.layout.home_start);
       	text_id = (TextView) findViewById(R.id.text_id);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        
        mDrawerLayout = findViewById(R.id.drawer_layout);

        setTitle("GPS Delivery Tracking");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try{
          locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
          isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
          isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
          isPassive = locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);

          if(isPassive){
            locationManager.requestLocationUpdates(
                            LocationManager.PASSIVE_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerGPS);
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                text_id.setText("Passive " + Double.toString(latitude) + " - " + Double.toString(longitude));
                if(location != null) {
                      latitude = location.getLatitude();
                      longitude = location.getLongitude();
                      text_id.setText("Passive " + Double.toString(latitude) + " - " + Double.toString(longitude));
               }
            }
          }

          if (isNetworkEnabled) {
            locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerGPS);
            if (locationManager != null) {
                  location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                  text_id.setText("Network " + Double.toString(latitude) + " - " + Double.toString(longitude));
                  if(location != null) {
                      latitude = location.getLatitude();
                      longitude = location.getLongitude();
                      text_id.setText("Network " + Double.toString(latitude) + " - " + Double.toString(longitude));
               }
            }
          }

          if(isGPSEnabled){
            locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerGPS);

             if (locationManager != null) {
                  location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                  text_id.setText("GPS " + Double.toString(latitude) + " - " + Double.toString(longitude));
                  if(location != null) {
                      latitude = location.getLatitude();
                      longitude = location.getLongitude();
                      text_id.setText("GPS " + Double.toString(latitude) + " - " + Double.toString(longitude));
               }
            }
          }


        }catch(SecurityException ex){

        }
        
	}

  LocationListener locationListenerGPS = new LocationListener() {
      public void onLocationChanged(Location location) {
            latitude =location.getLatitude();
            longitude =location.getLongitude();
            LatLng geo = new LatLng(latitude, longitude);

            map.addMarker(new MarkerOptions().position(geo).title("testing"));
            map.moveCamera(CameraUpdateFactory.newLatLng(geo));
           
            new GpsLogs(con).execute(Double.toString(latitude), Double.toString(longitude));
      }

      public void onStatusChanged(String provider, int status, Bundle extras) {

      }

      public void onProviderEnabled(String provider) {

      }

      public void onProviderDisabled(String provider) {

      }
  };	

  public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng geo = new LatLng(GPSLocation.source_lat, GPSLocation.source_long);
        map.addMarker(new MarkerOptions().position(geo).title(GPSLocation.source_label));
        map.moveCamera(CameraUpdateFactory.newLatLng(geo));

        LatLng _geo = new LatLng(GPSLocation.dest_lat, GPSLocation.dest_long);
        map.addMarker(new MarkerOptions().position(_geo).title(GPSLocation.dest_label));
  }

   public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.app_bar_menu, menu);
      return true;
  }

  public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
            case R.id.open_drawer:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

      return super.onOptionsItemSelected(item);
  }

}


class GpsLogs extends AsyncTask<String,Void, String>{

private Context context;

public GpsLogs(Context context) {
this.context = context;           
}
protected void onPreExecute()  {

}

protected String doInBackground(String... args)  {
String link= "http://192.168.42.37:8000/api/gpslogs";
String line = null;     
StringBuilder sb = new StringBuilder();  
try{
String data  = URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode(args[0], "UTF-8");
data += "&" + URLEncoder.encode("long", "UTF-8") + "=" + URLEncoder.encode(args[1], "UTF-8");
URL url = new URL(link);
URLConnection conn = url.openConnection();     
conn.setDoOutput(true); 

OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 

wr.write( data ); 
wr.flush();

BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
while((line = reader.readLine()) != null)
{
sb.append(line);
}

String ar = sb.toString();

return ar;

}catch(Exception e){
return new String("Exception: " + e.getMessage());
}
}

protected void onPostExecute(String data){
try{
AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.context);
AlertDialog alertDialog;  
alertDialogBuilder.setTitle("Welcome");
alertDialogBuilder.setMessage(data);
alertDialog = alertDialogBuilder.create();
alertDialog.show();
}catch(Exception e){
e.printStackTrace();;
}
}
}