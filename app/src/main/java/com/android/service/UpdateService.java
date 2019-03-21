package com.android.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.android.R;
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
import com.android.adapter.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import com.android.config.Config;
import com.android.controller.*;
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
import com.android.objects.*;
import com.android.adapter.*;
import com.android.model.*;
import com.android.faces.*;

public class UpdateService{

	private Db_sqlite sqlite;
	private MapService mapserv;
	private Context con;

	public UpdateService(Context context){
		sqlite = new Db_sqlite(context);
		mapserv = new MapService(context);
		con = context;
	}


	public void updateDelivery(){
		sqlite.EmptyTable();
		
		AsyncResponse asyncdata = mapserv.getDeliveryService();
        AsyncData deliv = new AsyncData(con, "androidgetdelivery", asyncdata);
        deliv.execute("");

	}

}