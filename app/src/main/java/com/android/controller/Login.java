package com.android.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.app.AlertDialog;

import com.android.R;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import android.os.Handler;
import android.os.AsyncTask;
import android.graphics.Typeface;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import android.content.DialogInterface;

import com.android.model.Db_sqlite;
import android.database.Cursor;

public class Login extends AppCompatActivity {

	private Button login;
	private EditText userfield, passfield;
	private TextView passlabel;
    public Context con = this;
    public Db_sqlite sqlite;
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
       	setContentView(R.layout.loginlayout);

        sqlite = new Db_sqlite(this);

       	setTitle("GPS Delivery Tracking");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        login = (Button) findViewById(R.id.login);
        userfield = (EditText) findViewById(R.id.userfield);
        passfield = (EditText) findViewById(R.id.passfield);
        passlabel = (TextView) findViewById(R.id.passlabel);

    }

    public BufferedReader urlLink(String link, String data) throws Exception{
	    URL url = new URL(link);
	    URLConnection conn = url.openConnection();     
	    conn.setDoOutput(true); 

	    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 

	    wr.write( data ); 
	    wr.flush(); 

	    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    return reader;
	}

    public void login(View view){
        String user = userfield.getText().toString();
        String pass = passfield.getText().toString();
        new MysqlLogin(con).execute(user, pass);
        
    }

    class MysqlLogin  extends AsyncTask<String,Void, String>{

        private Context context;
        private EditText result;
        private String fname, lname;       
        
        public MysqlLogin(Context context) {
              this.context = context;           
        }
        
        protected void onPreExecute()  {

        }

        protected String doInBackground(String... args)  {
            String link= "http://192.168.137.37:8000/api/androidLogin";
            String line = null;     
            StringBuilder sb = new StringBuilder();    
            try{
                String data  = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(args[0], "UTF-8");
                 data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(args[1], "UTF-8");

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
            }catch(Exception ex){

                return new String("Exception: " + ex.getMessage());
            }                
         }
           
        protected void onPostExecute(String data){
            try{
                JSONObject jsonObj = new JSONObject(data);
                JSONObject res = jsonObj.getJSONObject("data");
                JSONObject user = res.getJSONObject("user");
                String token = res.getString("token");
                String username = user.getString("username");
                String fname = user.getString("firstname");
                String lname = user.getString("lastname");

                sqlite.insertUser(fname, lname, username, token);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.context);
                AlertDialog alertDialog;  
                alertDialogBuilder.setTitle("Welcome");
                
                alertDialogBuilder.setMessage(fname + " " + lname)
                .setPositiveButton("Proceed",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Intent goToNextActivity = new Intent(getApplicationContext(), DeliveryLocation.class);
                        finish();
                        startActivity(goToNextActivity);
                    }
                });

                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}

