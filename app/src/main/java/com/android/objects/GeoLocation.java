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

public class GeoLocation{

	public int delivery_id;
	public double latitude;
	public double longitude;

	public GeoLocation(int delivery_id, double latitude, double longitude){
		this.delivery_id = delivery_id;
		this.latitude = latitude;
		this.longitude = longitude;
	}

}