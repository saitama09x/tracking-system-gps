package com.android.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import java.util.*;
import com.android.R;
import com.android.controller.*;
import com.android.service.*;
import com.android.model.*;
import com.android.objects.*;
import com.android.faces.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class DeliveryListAdapter extends ArrayAdapter<String>{

private final Activity context;
private final Context con;
private final MapService maponline;
private final Db_sqlite sqlite;
private final Integer[] ids;
private final String[] delivery_no;
private final String[] status;
private final String[] source;
private final String[] destination;

public DeliveryListAdapter(Context context, 
	Integer[] id,  
	String[] delivery_no, 
	String[] source, 
	String[] destination,
	String[] status){

	super(context, R.layout.delivery_list_adapter, delivery_no);
	this.context = (Activity) context;
	this.con = context;
	this.ids = id;
	this.delivery_no = delivery_no;
	this.source = source;
	this.destination = destination;
	this.status = status;

	this.maponline = new MapService(context);
	this.sqlite = new Db_sqlite(context);

}

public View getView(int position, View view, ViewGroup parent) {
LayoutInflater inflater = context.getLayoutInflater();
View rowView = inflater.inflate(R.layout.delivery_list_adapter, null, true);
TextView _delivery_no = (TextView) rowView.findViewById(R.id.delivery_no);
TextView _source = (TextView) rowView.findViewById(R.id.source);
TextView _destination = (TextView) rowView.findViewById(R.id.destination);
TextView _status = (TextView) rowView.findViewById(R.id.status);
final Button view_btn = (Button) rowView.findViewById(R.id.view);
final Button upload_btn = (Button) rowView.findViewById(R.id.upload);

if(status[position].equals("Pending")){
upload_btn.setVisibility(View.GONE);
}
else if(status[position].equals("Done")){
view_btn.setVisibility(View.GONE);
}
else if(status[position].equals("Uploaded")){
view_btn.setVisibility(View.GONE);
upload_btn.setVisibility(View.GONE);
}

view_btn.setTag(ids[position]);
view_btn.setOnClickListener(new View.OnClickListener() {
public void onClick(View view){
DeliveryLocation.delivery_id = Integer.parseInt(view_btn.getTag().toString());
Intent goToNextActivity = new Intent(context.getApplicationContext(), DeliveryLocation.class);
context.finish();
context.startActivity(goToNextActivity);
}
});

upload_btn.setTag(ids[position]);
upload_btn.setOnClickListener(new View.OnClickListener() {
public void onClick(View view){
int delivery_id = Integer.parseInt(view_btn.getTag().toString());
List<GeoLocation> geoloc = sqlite.getTracked(delivery_id);

try{
JSONArray jsonArray = new JSONArray();
for(int i = 0; i < geoloc.size(); i++){
GeoLocation geoObj = geoloc.get(i);
JSONObject jsonObj = new JSONObject();
jsonObj.put("delivery_id", geoObj.delivery_id);
jsonObj.put("latitude", geoObj.latitude);
jsonObj.put("longitude", geoObj.longitude);
jsonArray.put(jsonObj);
}
if(geoloc.size() > 0){
AsyncResponse asyncdata = maponline.getUpdateToServer(delivery_id);
AsyncData deliv = new AsyncData(con, "androidsettracking", asyncdata);
deliv.execute(jsonArray.toString());
upload_btn.setVisibility(View.GONE);	
}
}catch(Exception e){

}

}
});


_delivery_no.setText(delivery_no[position]);
_source.setText(source[position]);
_destination.setText(destination[position]);
_status.setText(status[position]);
return rowView;
}
}