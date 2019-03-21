package com.android.model;

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

public class MapRepo{
	
	private Context con;
	public String[] delivery_no;
	public String[] source;
	public String[] destination;
	public String[] status;
	public Integer[] ids;

	private Db_sqlite sqlite;

	public MapRepo(Context context){
		con = context;
		sqlite = new Db_sqlite(context);
	}

	public boolean deliveryList(String data) throws Exception{
		
		DeliveryListAdapter adapter = null;

		JSONObject jsonObj = new JSONObject(data);
		JSONArray res = jsonObj.getJSONArray("data");
		delivery_no = new String[res.length()];
		source = new String[res.length()];
		destination = new String[res.length()];
		status = new String[res.length()];
		ids = new Integer[res.length()];

		if(res.length() > 0){
			for(int i = 0; i < res.length(); i++){
				JSONObject c = res.getJSONObject(i);
				ids[i] = c.getInt("id");
				delivery_no[i] = c.getString("delivery_no");
				source[i] = c.getString("source");
				destination[i] = c.getString("destination");
				status[i] = c.getString("status");
				sqlite.insertDelivery(
					new DeliveryObj(
						c.getInt("id"),
						c.getInt("id"),
						c.getString("delivery_no"),
						c.getString("delivery_no"),
						c.getString("delivery_no"),
						c.getString("source"),
						c.getString("destination"),
						c.getString("status")
					)
				);
			}
		}

		return true;
	}

	public boolean getDeliveryLocation(String data) throws Exception{
		JSONObject jsonObj = new JSONObject(data);
		JSONObject res = jsonObj.getJSONObject("data");
		JSONArray arr_a = res.getJSONArray("loc");
		JSONArray arr_b = res.getJSONArray("track");
		ArrayList<LatLng> loc_lists;
		if(arr_a.length() > 0){
			loc_lists = new ArrayList<LatLng>();
			for(int i = 0; i < arr_a.length(); i++){
				JSONObject c = arr_a.getJSONObject(i);
				int delivery_id = c.getInt("delivery_id");
				double _lat = c.getDouble("latitude");
				double _long = c.getDouble("longitude");
				sqlite.setLocation(delivery_id, 
					Double.toString(_lat),
					Double.toString(_long));
			}
		}
		if(arr_b.length() > 0){
			loc_lists = new ArrayList<LatLng>();
			for(int i = 0; i < arr_b.length(); i++){
				JSONObject c = arr_b.getJSONObject(i);
				int delivery_id = c.getInt("delivery_id");
				double _lat = c.getDouble("latitude");
				double _long = c.getDouble("longitude");
				sqlite.setTracked(delivery_id, 
					Double.toString(_lat),
					Double.toString(_long));
			}
		}

		return true;
	}
}