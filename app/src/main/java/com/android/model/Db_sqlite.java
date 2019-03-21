package com.android.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import com.android.R;
import com.android.faces.*;
import com.android.objects.*;
import android.widget.Toast;

public class Db_sqlite extends SQLiteOpenHelper {

private Context con;

public Db_sqlite(Context context)
{
super(context, "tracking_five.db" , null, 1);
con = context;
}


public void onCreate(SQLiteDatabase db) {

db.execSQL(
"create table if not exists users " +
"(id integer primary key, fname varchar, lname varchar, uname varchar, token text)"
);

db.execSQL(
"create table if not exists delivery " +
"(id integer primary key, account_id varchar, delivery_no varchar, unit_no varchar, "+
"routes_details text, source varchar, destination varchar, status varchar)"
);

db.execSQL(
"create table if not exists geolocation " +
"(id integer primary key, delivery_id int, latitude text, longitude text)"
);

db.execSQL(
"create table if not exists geotracked " +
"(id integer primary key, delivery_id int, latitude text, longitude text)"
);

}

@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


}

public void EmptyTable(){
SQLiteDatabase db = this.getWritableDatabase();
db.execSQL("delete from delivery");	
db.execSQL("delete from geolocation");	
db.execSQL("delete from geotracked");	
}

public boolean insertUser(String fname, String lname, String uname, String token){
SQLiteDatabase db = this.getWritableDatabase();
ContentValues contentValues = new ContentValues();

contentValues.put("fname", fname);
contentValues.put("lname", lname);
contentValues.put("uname", uname);
contentValues.put("token", token);	
db.insert("users", null, contentValues);
return true;
}

public Cursor getData(String uname){
SQLiteDatabase db = this.getReadableDatabase();
Cursor res =  db.rawQuery("select a.id, a.fname, a.lname, a.token from users a", null);
return res;
}

public boolean insertDelivery(DeliveryObj deliv){

SQLiteDatabase db = this.getWritableDatabase();
ContentValues contentValues = new ContentValues();
contentValues.put("id", deliv.id);
contentValues.put("account_id", deliv.account_id);
contentValues.put("delivery_no", deliv.delivery_no);
contentValues.put("routes_details", deliv.routes_details);
contentValues.put("source", deliv.source);
contentValues.put("destination", deliv.destination);
contentValues.put("status", deliv.status);
db.insert("delivery", null, contentValues);
return true;

}

public List<DeliveryObj> getDelivery(){
SQLiteDatabase db = this.getReadableDatabase();
Cursor res =  db.rawQuery("select * from delivery", null);
int count_row = res.getCount();
List<DeliveryObj> delivery = new ArrayList<DeliveryObj>();
if(count_row > 0){
res.moveToFirst();
while(res.isAfterLast() == false){
delivery.add(new DeliveryObj(
res.getInt(res.getColumnIndex("id")),
res.getInt(res.getColumnIndex("account_id")),
res.getString(res.getColumnIndex("delivery_no")),
res.getString(res.getColumnIndex("unit_no")),
res.getString(res.getColumnIndex("routes_details")),
res.getString(res.getColumnIndex("source")),
res.getString(res.getColumnIndex("destination")),
res.getString(res.getColumnIndex("status"))
));
res.moveToNext();
}
}
return delivery;
}

public boolean setLocation(int id, String latitude, String longitude){

SQLiteDatabase db = this.getWritableDatabase();
ContentValues contentValues = new ContentValues();
contentValues.put("delivery_id", id);
contentValues.put("latitude", latitude);
contentValues.put("longitude", longitude);
db.insert("geolocation", null, contentValues);
return true;
}


public List<GeoLocation> getLocation(int id){
SQLiteDatabase db = this.getReadableDatabase();
Cursor res =  db.rawQuery("select * from geolocation where delivery_id = " + id, null);
int count_row = res.getCount();
List<GeoLocation> location = new ArrayList<GeoLocation>();
if(count_row > 0){
res.moveToFirst();
while(res.isAfterLast() == false){
location.add(new GeoLocation(
res.getInt(res.getColumnIndex("delivery_id")),
res.getDouble(res.getColumnIndex("latitude")),
res.getDouble(res.getColumnIndex("longitude"))
));
res.moveToNext();
}
}
return location;
}

public boolean setTracked(int id, String latitude, String longitude){
SQLiteDatabase db = this.getWritableDatabase();
ContentValues contentValues = new ContentValues();
contentValues.put("delivery_id", id);
contentValues.put("latitude", latitude);
contentValues.put("longitude", longitude);
db.insert("geotracked", null, contentValues);
return true;
}

public List<GeoLocation> getTracked(int id){
SQLiteDatabase db = this.getReadableDatabase();
Cursor res =  db.rawQuery("select * from geotracked where delivery_id = " + id, null);
int count_row = res.getCount();
List<GeoLocation> location = new ArrayList<GeoLocation>();
if(count_row > 0){
res.moveToFirst();
while(res.isAfterLast() == false){
location.add(new GeoLocation(
res.getInt(res.getColumnIndex("delivery_id")),
res.getDouble(res.getColumnIndex("latitude")),
res.getDouble(res.getColumnIndex("longitude"))
));
res.moveToNext();
}
}
return location;
}

public boolean updateStatusDone(int delivery_id){
   SQLiteDatabase db = this.getWritableDatabase();
   ContentValues contentValues = new ContentValues();
   contentValues.put("status", "Done");
   db.update("delivery", contentValues, "id=" + delivery_id, null);
   return true;
}

public boolean updateStatusUpload(final int delivery_id){
   SQLiteDatabase db = this.getWritableDatabase();
   ContentValues contentValues = new ContentValues();
   contentValues.put("status", "Uploaded");
   db.update("delivery", contentValues, "id=" + delivery_id, null);
   return true;
}

}