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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import com.android.config.Config;
import com.android.controller.*;
import com.android.faces.*;
import com.android.model.*;
import android.app.ProgressDialog;
import android.app.Dialog;

public class DialogService{


	private ProgressDialog progress;
	private AlertDialog.Builder alertDialogBuilder;
	private AlertDialog alertDialog; 
	private Context con;

	public DialogService(Context context){
		con = context;
		progress = new ProgressDialog(context);
		alertDialogBuilder = new AlertDialog.Builder(context);
	}

	public void showDialog(String title, String msg){
		alertDialogBuilder.setTitle(title);
		alertDialogBuilder.setMessage(msg);
		alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void ShowProgress(String msg){
		progress.setIndeterminate(false);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setMessage(msg);
		progress.setCancelable(true);
		progress.show();
	}

	public void HideProgress(){
		if (progress.isShowing()) {
			progress.dismiss();
		}
	}

	public Dialog ConfigDialog(){
		final Dialog dialog = new Dialog(con);  
		dialog.setContentView(R.layout.dialog_config);
		dialog.show();
		return dialog;
	}
}