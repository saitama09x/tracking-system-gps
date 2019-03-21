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
import com.android.faces.*;

public class AsyncData extends AsyncTask<String,Void, String>{

private Context context;
private String address;
public AsyncResponse response = null;

public AsyncData(Context context, String address, AsyncResponse response){

	this.context = context;
	this.address = Config.url + address;
	this.response = response;
}

protected void onPreExecute()  {
	this.response.preprocess();
}

protected String doInBackground(String... args)  {
	return this.response.asyncBackground(args, this.address);
}

protected void onPostExecute(String data){
	this.response.processFinish(data);
}

}
