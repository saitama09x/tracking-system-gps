package com.android.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import com.android.adapter.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.*;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import com.android.config.Config;
import com.android.controller.*;
import com.android.model.*;
import com.android.faces.*;
import com.android.model.MapRepo;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectService{

	private ConnectivityManager cm;
	private boolean isConnected = false;
	private MapOfflineService offline;
	private MapService mapservice = null;
	private GPSTracker tracker;
	private Context con;
	public ConnectService(Context context){
		con = context;
		cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if(activeNetwork != null){
			isConnected = activeNetwork.isConnectedOrConnecting();
		}
		offline = new MapOfflineService(context);
		mapservice = new MapService(context);
	}


}