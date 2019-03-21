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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import com.android.config.Config;
import com.android.controller.*;
import com.android.model.*;
import com.android.faces.*;
import com.android.model.MapRepo;
import android.app.ProgressDialog;

public class MapService{

	private MapService mapservice = null;
	private MapRepo maprepo = null;
	private Db_sqlite sqlite;
	private Context con;
	private DialogService dialog;

	public MapService(Context context){
		maprepo = new MapRepo(context);
		con = context;
		dialog = new DialogService(context);
		sqlite = new Db_sqlite(context);
	}

	private String setBufferedUrl(String data, String link){
	try{
		String line = null;     
		StringBuilder sb = new StringBuilder();
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

	public AsyncResponse getDeliveryService(){

		return new AsyncResponse(){

		    public void preprocess(){
				dialog.ShowProgress("Loading Delivery");
		    }

		    public String asyncBackground(String args[], String link){
		    	try{
		    		String data = URLEncoder.encode("lat", "UTF-8") + 
			    	"=" + URLEncoder.encode(args[0], "UTF-8");

			    	return setBufferedUrl(data, link);
		    	}
		    	catch(Exception e){
		    		return new String("Exception: " + e.getMessage());
		    	}

		    }
		    public void processFinish(String data){
        		try{
        			
        			dialog.showDialog("Success", data);

        			boolean adapter = maprepo.deliveryList(data);
	        		
        			AsyncResponse asyncdata = getDeliveryLocation();
					AsyncData location = new AsyncData(con, "androidgetlocation", asyncdata);
					location.execute("");
					
        		}catch(Exception e){
					dialog.showDialog("Error", e.getMessage());
        		}
    		}
        };

	}

	public AsyncResponse getDeliveryLocation(){
		return new AsyncResponse(){

			public void preprocess(){
				dialog.ShowProgress("Loading location");

			}

			public String asyncBackground(String args[], String link){
		    	try{
		    		String data = URLEncoder.encode("id", "UTF-8") + 
			    	"=" + URLEncoder.encode(args[0], "UTF-8");

			    	return setBufferedUrl(data, link);
		    	}
		    	catch(Exception e){
		    		return new String("Exception: " + e.getMessage());
		    	}

		    }
			public void processFinish(String data){
			try{
				
				boolean is_map = maprepo.getDeliveryLocation(data);

				if (is_map) {
				    dialog.HideProgress();
				}

				}catch(Exception e){
					dialog.showDialog("Error", e.getMessage());
				}
			}

		};
	}

	public AsyncResponse getTrackedSave(){
		return new AsyncResponse(){

			public void preprocess(){
				dialog.ShowProgress("");

			}

			public String asyncBackground(String args[], String link){
		    	try{
		    		String data = URLEncoder.encode("id", "UTF-8") + 
			    	"=" + URLEncoder.encode(args[0], "UTF-8");

			    	data += "&" + URLEncoder.encode("lat", "UTF-8") + 
			    	"=" + URLEncoder.encode(args[1], "UTF-8");

			    	data += "&" + URLEncoder.encode("lng", "UTF-8") + 
			    	"=" + URLEncoder.encode(args[2], "UTF-8");
			    	
			    	return setBufferedUrl(data, link);
		    	}
		    	catch(Exception e){
		    		return new String("Exception: " + e.getMessage());
		    	}

		    }

			public void processFinish(String data){
				try{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(con);
					AlertDialog alertDialog;  
					alertDialogBuilder.setTitle("Success");
					alertDialogBuilder.setMessage(data);
					alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				}catch(Exception e){
					dialog.showDialog("Process Error", e.getMessage());
				}
			}


		};

	}

	public AsyncResponse getUpdateToServer(final int delivery_id){

		return new AsyncResponse(){

			public void preprocess(){
				dialog.ShowProgress("Uploading data");

			}

			public String asyncBackground(String args[], String link){
		    	try{
		    		String data = URLEncoder.encode("geotracked", "UTF-8") + 
			    	"=" + URLEncoder.encode(args[0], "UTF-8");
			    	
			    	return setBufferedUrl(data, link);
		    	}
		    	catch(Exception e){
		    		return new String("Exception: " + e.getMessage());
		    	}

		    }

		    public void processFinish(String data){
				try{
					sqlite.updateStatusUpload(delivery_id);
					dialog.HideProgress();
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(con);
					AlertDialog alertDialog;  
					alertDialogBuilder.setTitle("Success");
					alertDialogBuilder.setMessage(data);
					alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				}catch(Exception e){
					dialog.showDialog("Process Error", e.getMessage());
				}
			}

		};

	}

}