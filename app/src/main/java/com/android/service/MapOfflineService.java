package com.android.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
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
import com.android.objects.*;
import com.android.adapter.*;
import com.android.model.MapRepo;
import android.app.ProgressDialog;
import android.widget.Toast;

public class MapOfflineService{

	private Db_sqlite sqlite;
	public Context con;
	public MapOfflineService(Context context){
		sqlite = new Db_sqlite(context);
		con = context;
	}

	public DeliveryListAdapter RefreshDelivery(){

		List<DeliveryObj> res = sqlite.getDelivery();
		DeliveryListAdapter adapter = null;
		if(res.size() > 0){
			String[] delivery_no = new String[res.size()];
			String[] source = new String[res.size()];
			String[] destination = new String[res.size()];
			String[] status = new String[res.size()];
			Integer[] ids = new Integer[res.size()];

			for(int i = 0; i < res.size(); i++){
				DeliveryObj obj = res.get(i);
				ids[i] = obj.id;
				delivery_no[i] = obj.delivery_no;
				source[i] = obj.source;
				destination[i] = obj.destination;
				status[i] = obj.status;
			}

			adapter = new DeliveryListAdapter(con, ids, delivery_no, 
				source, destination, status);

		}
		
		return adapter;
	}

	public void getLocation(int id, final GoogleMap map){
		List<GeoLocation> loc = sqlite.getLocation(id);
		List<LatLng> loc_lists = new ArrayList<LatLng>();
		for(int i = 0; i < loc.size(); i++){
			GeoLocation _loc = 	loc.get(i);
			double _lat = _loc.latitude;
			double _long = _loc.longitude;
			LatLng geo = new LatLng(_lat, _long);
			loc_lists.add(geo);
			map.addMarker(new MarkerOptions().position(geo)
				.title("testing")
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
			map.moveCamera(CameraUpdateFactory.newLatLng(geo));
		}

		map.addPolyline(new PolylineOptions()
			.addAll((Iterable<LatLng>) loc_lists)
			.width(5)
			.color(Color.BLUE));
	}

	public void setTracked(GeoLocation geo, final GoogleMap map){
		int delivery_id = geo.delivery_id;
		String lat = Double.toString(geo.latitude);
		String lng = Double.toString(geo.longitude);
		sqlite.setTracked(delivery_id, lat, lng);
		Toast toast = Toast.makeText(con, Double.toString(geo.longitude), Toast.LENGTH_SHORT);
		toast.show();
		LatLng cur_loc = new LatLng(geo.latitude, geo.longitude);
		map.addMarker(new MarkerOptions().position(cur_loc)
				.title("testing")
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
			map.moveCamera(CameraUpdateFactory.newLatLng(cur_loc));
	}

	public void getTracked(int id, final GoogleMap map){
		List<GeoLocation> loc = sqlite.getTracked(id);
		List<LatLng> loc_lists = new ArrayList<LatLng>();
		for(int i = 0; i < loc.size(); i++){
			GeoLocation _loc = 	loc.get(i);
			double _lat = _loc.latitude;
			double _long = _loc.longitude;
			LatLng geo = new LatLng(_lat, _long);
			loc_lists.add(geo);
			map.addMarker(new MarkerOptions().position(geo)
				.title("testing")
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
			map.moveCamera(CameraUpdateFactory.newLatLng(geo));
		}

		map.addPolyline(new PolylineOptions()
			.addAll((Iterable<LatLng>) loc_lists)
			.width(5)
			.color(Color.GREEN));
	}

	public boolean setStatusDone(int delivery_id){

		boolean status = sqlite.updateStatusDone(delivery_id);
		return status;

	}

}