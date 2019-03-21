package com.android.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.*;
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
import android.support.v4.widget.DrawerLayout;
import android.support.v4.view.GravityCompat;
import android.app.AlertDialog;
import android.content.Context;
import com.android.config.*;
import com.android.adapter.*;
import com.android.faces.*;
import com.android.model.*;
import com.android.service.*;
import android.app.ProgressDialog;
import android.app.Dialog;
import android.widget.Toast;

public class Delivery extends AppCompatActivity {

	ListView mainlist;
	public Context con = this;
	private DialogService dialog;
	private UpdateService updateserv;
    private MapOfflineService offlineloc;

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
       	setContentView(R.layout.delivery_layout);

       	setTitle("GPS Delivery Tracking");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        updateserv = new UpdateService(this);
        offlineloc = new MapOfflineService(this);
        dialog = new DialogService(this);
        mainlist = (ListView) findViewById(R.id.mainlist);
        
    }

    
    public void updateView(View view){
        updateserv.updateDelivery();
    }

    public void refreshView(View view){
        DeliveryListAdapter adapter = offlineloc.RefreshDelivery();
        if(adapter != null){
            mainlist.setAdapter(adapter);
        }
    }

    public void ConfigDialog(View view){
        Dialog _dialog = dialog.ConfigDialog();
        final EditText ip_config = (EditText) _dialog.findViewById(R.id.ip_config);
        ip_config.setText(Config.url);
        Button save_btn = (Button) _dialog.findViewById(R.id.button);
        save_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Config.url = ip_config.getText().toString();
                Toast toast = Toast.makeText(con, "IP Address Updated", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}