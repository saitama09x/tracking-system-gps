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
import android.content.Context;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.*;
import android.graphics.Color;
import com.android.model.*;
import com.android.faces.*;
import com.android.service.*;
import com.android.objects.*;
import android.app.ProgressDialog;

public class DeliveryLocation extends AppCompatActivity 
implements OnMapReadyCallback {

private GoogleMap map;
public static int delivery_id;
private Context con = this;
private GPSTracker tracker;
private MapOfflineService offlineloc;

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.delivery_location);

SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
mapFragment.getMapAsync(this);

setTitle("GPS Delivery Tracking");
offlineloc = new MapOfflineService(this);
Toolbar toolbar = findViewById(R.id.toolbar);
setSupportActionBar(toolbar);

}

public void onMapReady(final GoogleMap googleMap) {
offlineloc.getLocation(DeliveryLocation.delivery_id, googleMap);
offlineloc.getTracked(DeliveryLocation.delivery_id, googleMap);

LocationCallback loc_ch = new LocationCallback(){

	public void getLatLong(double lng, double lat){
		GeoLocation geo = new GeoLocation(
			DeliveryLocation.delivery_id,
			lat,
			lng
		);

		offlineloc.setTracked(geo, googleMap);
	}

};

tracker = new GPSTracker(this, loc_ch);

}

public void goHome(View view){
Intent goToNextActivity = new Intent(getApplicationContext(), Delivery.class);
finish();
startActivity(goToNextActivity);	
}

public void statusDone(View view){
boolean status = offlineloc.setStatusDone(DeliveryLocation.delivery_id);
if(status){
	Intent goToNextActivity = new Intent(getApplicationContext(), Delivery.class);
	finish();
	startActivity(goToNextActivity);	
}

}


}