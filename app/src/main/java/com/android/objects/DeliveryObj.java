package com.android.objects;

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


public class DeliveryObj{

	public int id;
	public int account_id;
	public String delivery_no;
	public String unit_no;
	public String routes_details;
	public String source;
	public String destination;
	public String status;


	public DeliveryObj(int id, int account_id, String delivery_no, 
		String unit_no, String routes_details, String source, String destination,
		String status){
		this.id = id;
		this.delivery_no = delivery_no;
		this.unit_no = unit_no;
		this.routes_details = routes_details;
		this.source = source;
		this.destination = destination;
		this.status = status;
	}

}