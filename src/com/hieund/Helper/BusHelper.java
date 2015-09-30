package com.hieund.Helper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.hieund.Util.*;
import com.hieund.gui.Activity.MainActivity;
import com.hieund.gui.Object.GoThrough;
import com.hieund.gui.Object.Nodes;

public class BusHelper extends SQLiteOpenHelper {

	public static String DATABASE_NAME = "HanoiBus.sqlite";
	public static int SCHEMA_VERSION = 1;
	public static BusHelper instance;
	public ConvertUnsigned convert = new ConvertUnsigned();
	//The Android's default system path of your application database.
    @SuppressLint("SdCardPath") private static String DB_PATH = "/data/data/com.hieund/databases/";
 
//    private static String DB_HCM = "BusHCM.db";
//    private static String DB_HN = "BusHN.db";
    
    private SQLiteDatabase myDataBase; 
 
    private final Context myContext;

	public BusHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
		this.myContext = context;
	}

	public static BusHelper getInstance(Context context) {
		synchronized (BusHelper.class) {
			if (instance == null) {
				instance = new BusHelper(context);
			}else{
				instance = new BusHelper(context);
			}
			return instance;
		}
	}
	
	public static BusHelper newInstance(Context context) {
		synchronized (BusHelper.class) {
			instance = new BusHelper(context);
			return instance;
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
//		db.execSQL("CREATE TABLE busNodeInfo (_id INTEGER PRIMARY KEY AUTOINCREMENT, code VARCHAR(5) , "
//				+ "goNodes TEXT, reNodes TEXT);");
//		db.execSQL("CREATE TABLE route_favorite (_id INTEGER PRIMARY KEY AUTOINCREMENT, startEndDeclare TEXT, detailInfo TEXT);");
//		db.execSQL("CREATE TABLE busInfo (_id INTEGER PRIMARY KEY AUTOINCREMENT, code VARCHAR(5) , name TEXT, "
//				+ "operationTime TEXT, frequency VARCHAR(15), cost VARCHAR(15), go TEXT, re TEXT, isFavorite TEXT, "
//				+ "goGeo TEXT, reGeo TEXT);");
//		db.execSQL("CREATE TABLE IF NOT EXITS BusLine (_id	integer NOT NULL PRIMARY KEY AUTOINCREMENT,"
//				+ "name	varchar,"
//				+ "code	varchar,"
//				+ "activityType	varchar,"
//				+ "distance	varchar,"
//				+ "runningTime	varchar,"
//				+ "spacingTime	varchar,"
//				+ "cost	varchar,"
//				+ "busType	varchar,"
//				+ "operationsTime	varchar,"
//				+ "tripsPerDay	varchar,"
//				+ "goRoutePath	varchar,"
//				+ "returnRoutePath	varchar,"
//				+ "goRoutePathGeo	varchar,"
//				+ "returnRoutePathGeo	varchar,"
//				+ "goRouteThroughStops	varchar,"
//				+ "returnRouteThroughStops	varchar"
//				+ ");");
//		db.execSQL("CREATE TABLE IF NOT EXITS BusStop ("
//				+"_id	integer NOT NULL PRIMARY KEY AUTOINCREMENT,"
//				+"busPassBy	varchar,"
//				+"address_name	varchar,"
//				+"code	varchar,"
//				+"location	varchar"
//				+");");
//		db.execSQL("CREATE TABLE IF NOT EXITS GoThrough ("
//	+"_id	integer NOT NULL PRIMARY KEY AUTOINCREMENT,"
//	+"busLineCode	varchar,"
//	+"busStopCode	varchar,"
//	+"direction	integer,"
//	+"gtOrder	integer,"
//	+"pathToNextBusStop	varchar,"
//	+"nextBusStop	integer,"
//	+"distance	float"
//	+");");
//		Log.d("Thong tin", "database is created (2)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public String removeBlockCursor(String a) {
		String result = a.split("\"")[1];
		return result;
	}

	public void insert(String code, String name, String operationTime,
			String frequency, String cost, String go, String re, String goGeo,
			String reGeo) {
		ContentValues newcv = new ContentValues();
		newcv.put("code", code);
		newcv.put("name", name);
		newcv.put("operationTime", operationTime);
		newcv.put("frequency", frequency);
		newcv.put("cost", cost);
		newcv.put("go", go);
		newcv.put("re", re);
		newcv.put("goGeo", goGeo);
		newcv.put("reGeo", reGeo);
		getWritableDatabase().insert("busInfo", "code", newcv);
	}

	public void insertNode(String code, String goNodes, String reNodes){
		ContentValues newcv = new ContentValues();
		newcv.put("code", code);
		newcv.put("goNodes", goNodes);
		newcv.put("reNodes", reNodes);
		getWritableDatabase().insert("busNodeInfo", "code", newcv);
	}
	
	public void insertToFavorite(String code) {
		getWritableDatabase().execSQL(
				"UPDATE busInfo Set isFavorite ='yes' WHERE code = '" + code
						+ "';");
	}

	public void removeFromFavorite(String code) {
		getWritableDatabase().execSQL(
				"UPDATE busInfo SET isFavorite ='' WHERE code = '" + code
						+ "';");
	}

	public Cursor getAllFavorite() {
		return getReadableDatabase().rawQuery(
				"SELECT * FROM busInfo WHERE isFavorite ='yes' ORDER BY CODE",
				null);
	}

	public Cursor getAll() {
		return getReadableDatabase().rawQuery(
				"SELECT * FROM BusLine ORDER BY _id", null);

	}
	
	public Cursor getAllLines() {
		return getReadableDatabase().rawQuery(
				"SELECT _id,code,name,operationsTime,spacingTime,cost,goRoutePath,goRoutePathGeo,goRouteThroughStops,returnRoutePath,returnRoutePathGeo, returnRouteThroughStops FROM BusLine ORDER BY _id", null);

	}
	
	public Cursor getBusStopName(){
		return getReadableDatabase().rawQuery(
				"SELECT address_name FROM BusStop ORDER BY _id", null);
	}
	
	public Cursor getBusStop(){
		return getReadableDatabase().rawQuery(
				"SELECT * FROM BusStop ORDER BY _id", null);
	}

	public Cursor getBusSearch(String txt) {
		String s = "0";
		Cursor c = getReadableDatabase().rawQuery(
				"SELECT * FROM BusLine ", null);
		if(c.getCount()>0)
		{
		    for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
		    {
		    	try {
		    	    Integer.parseInt(txt);
			    	if (convert.ConvertString(this.getCode(c)).contains(convert.ConvertString(txt)))
			    		s += " OR _id = " + c.getString(0);
		    	} catch (NumberFormatException e) {
		    		
			    	if (convert.ConvertString(this.getCode(c)).contains(convert.ConvertString(txt)))
			    		s += " OR _id = " + c.getString(0);
			    	else if (convert.ConvertString(this.getName(c)).contains(convert.ConvertString(txt)))
		        		s += " OR _id = " + c.getString(0);
//			    	else if (convert.ConvertString(this.getGo(c)).contains(convert.ConvertString(txt)))
//		        		s += " OR _id = " + c.getString(0);
//			    	else if (convert.ConvertString(this.getRe(c)).contains(convert.ConvertString(txt)))
//		        		s += " OR _id = " + c.getString(0);
		    	}
		    }
		    if (s.equals("0")){
		    	for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
			    {
		    		if (convert.ConvertString(this.getGo(c)).contains(convert.ConvertString(txt)))
		        		s += " OR _id = " + c.getString(0);
			    	else if (convert.ConvertString(this.getRe(c)).contains(convert.ConvertString(txt)))
		        		s += " OR _id = " + c.getString(0);
			    }
		    }
		}
		
		return getReadableDatabase().rawQuery(
				"SELECT * FROM BusLine WHERE "+s, null);

	}
	

	public int getBusCost(String code) {
		// TODO Auto-generated method stub
		ConvertUnsigned converUnsigned = new ConvertUnsigned();
		Cursor c = getReadableDatabase().rawQuery(
				"SELECT cost FROM BusLine WHERE code = '"+code+"'", null);
		if (c.getCount()>0){
			c.moveToNext();
			String cost = c.getString(0);
			cost = cost.split("\\/")[0];
			cost = converUnsigned.ConvertString(cost.toLowerCase());
			if (cost.contains("vnd"))
				cost = cost.replace("vnd", "");
			if (cost.contains("d"))
				cost = cost.replace("d", "");
			int price = Integer.parseInt(cost);
			return price;
		}
		return 0;
	}

	public Nodes findBusStop(String string) {
		// TODO Auto-generated method stub
		Cursor busStop = getReadableDatabase().rawQuery(
				"SELECT * FROM busStop WHERE address_name = '" + string
				+ "'", null);
		if(busStop.getCount()>0)
		{
    		busStop.moveToNext();
			String stopName = busStop.getString(2);
			String fleetOver = busStop.getString(1);
    		//Log.d("latitude",busStop.getString(4).split("\\|")[1]);

			Double latitude = Double.parseDouble(busStop.getString(4).split("\\|")[1]);
			Double longitude = Double.parseDouble(busStop.getString(4).split("\\|")[0]);
			Nodes node = new Nodes(null, stopName);
			node.setFleetOver(fleetOver);
			node.setGeo(new LatLng(latitude, longitude));
			return node;
		}
		return null;
	}


	public Cursor getItemCondition(String code) {
		return getReadableDatabase().rawQuery(
				"SELECT * FROM busInfo WHERE code = '" + code
						+ "' AND isFavorite = 'yes';", null);
	}
	public ArrayList<Nodes> getNodeInfo(String idBus, String direction) {
		Cursor c = null;
		//Log.d("ID",idBus);
		if (direction=="go")
			c = getReadableDatabase().rawQuery(
				"SELECT goRouteThroughStops FROM BusLine WHERE code = '"+idBus+"'", null);
		else 
			c = getReadableDatabase().rawQuery(
					"SELECT returnRouteThroughStops FROM BusLine WHERE code = '"+idBus+"'", null);
		ArrayList<Nodes> nodeList = new ArrayList<Nodes>();
		if(c.getCount()>0){
		
			c.moveToFirst();
		    String str = c.getString(0);
		    String[] busStopID = str.split(" ");
		    String query = "SELECT * FROM BusStop WHERE ";
			for(int i=0; i< busStopID.length;i++){
			   query += "code ='"+busStopID[i]+"' OR ";
			   nodeList.add(new Nodes(null,""));
			}
			List<String> busStopIDs = new ArrayList<String>();
			busStopIDs = Arrays.asList(busStopID);
			query +="0";
			Cursor busStop = getReadableDatabase().rawQuery(query, null);
			if(busStop.getCount()>0)
			{
				for(busStop.moveToFirst();!busStop.isAfterLast();busStop.moveToNext())
			    {
					String stopName = busStop.getString(2);
					String fleetOver = busStop.getString(1);
					Double latitude = Double.parseDouble(busStop.getString(4).split("\\|")[1]);
					Double longitude = Double.parseDouble(busStop.getString(4).split("\\|")[0]);
					Nodes node = new Nodes(null, stopName);
					node.setFleetOver(fleetOver);
					node.setGeo(new LatLng(latitude, longitude));
//					if (busStop.getPosition()>0) {
//						nodeList.get(nodeList.size() - 1).setNextNode(node);
//					}
//					if (busStop.isLast()) {
//						node.setNextNode(null);
//					}
					nodeList.get(busStopIDs.indexOf(busStop.getString(3))).setStopName(stopName);
					nodeList.get(busStopIDs.indexOf(busStop.getString(3))).setFleetOver(fleetOver);
					nodeList.get(busStopIDs.indexOf(busStop.getString(3))).setGeo(new LatLng(latitude, longitude));
					//nodeList.add(busStopIDs.indexOf(busStop.getString(3)),node);
			    }
			}
			for (int i = 0 ; i < nodeList.size();i++){
				if (i > 0) {
					nodeList.get(i - 1).setNextNode(nodeList.get(i));
				}
				if (i ==  nodeList.size()-1) {
					nodeList.get(i).setNextNode(null);
				}
			}
			busStop.close();
//			if (nodeList.size() > 0)
//					nodeList.get(0).setNextNode(nodeList.get(1));

		}
		c.close();
		return nodeList;
	}
	/*
	public ArrayList<Nodes> getNodeInfo(String idBus, String direction) {
		Cursor c = null;
		//Log.d("ID",idBus);
		if (direction=="go")
			c = getReadableDatabase().rawQuery(
				"SELECT goRouteThroughStops FROM BusLine WHERE _id = "+idBus, null);
		else 
			c = getReadableDatabase().rawQuery(
					"SELECT returnRouteThroughStops FROM BusLine WHERE _id = "+idBus, null);
		ArrayList<Nodes> nodeList = new ArrayList<Nodes>();
		if(c.getCount()>0)
		{
//		    for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
//		    {
			c.moveToFirst();
		    		String str = c.getString(0);
		    		String[] busStopID = str.split(" ");
			    	for(int i=0; i< busStopID.length;i++){
			    		Cursor busStop = getReadableDatabase().rawQuery(
								"SELECT * FROM BusStop WHERE code = '"+ busStopID[i]+"'", null);
			    		//Log.d("busStop",busStopID[i]);
			    		if(busStop.getCount()>0)
			    		{
				    		busStop.moveToNext();
							String stopName = busStop.getString(2);
							String fleetOver = busStop.getString(1);
				    		//Log.d("latitude",busStop.getString(4).split("\\|")[1]);
	
							Double latitude = Double.parseDouble(busStop.getString(4).split("\\|")[1]);
							Double longitude = Double.parseDouble(busStop.getString(4).split("\\|")[0]);
							Nodes node = new Nodes(null, stopName);
							node.setFleetOver(fleetOver);
							node.setGeo(new LatLng(latitude, longitude));
							if (i > 0) {
								nodeList.get(nodeList.size() - 1).setNextNode(node);
							}
							if (i == busStopID.length - 1) {
								node.setNextNode(null);
							}
							nodeList.add(node);
							busStop.close();
			    		}
		    		}
			    	if (nodeList.size() > 0)
			    		nodeList.get(0).setNextNode(nodeList.get(1));

		    	
//		    }
		}
		c.close();
		return nodeList;
	}
	*/
	public Cursor getGeo(String code) {
		return getReadableDatabase().rawQuery(
				"SELECT goRoutePathGeo, returnRoutePathGeo,code FROM busLine WHERE code = '" + code + "'",
				null);
	}
	
	public Cursor getNearBus(List<String> nearBusList) {
		// TODO Auto-generated method stub
		String query = "SELECT * FROM BusLine WHERE ";
		for (String s : nearBusList)
			query += "code = '"+s+"' OR ";
		query += "0";
		
		return getReadableDatabase().rawQuery(query,null);
	}
	


	public Cursor getNearStop(List<String> nearBusList) {
		String query = "SELECT * FROM BusStop WHERE ";
		for (String s : nearBusList)
			query += "code = '"+s+"' OR ";
		query += "0";
		
		return getReadableDatabase().rawQuery(query,null);
	}

	
	public List<GoThrough> findAllGoThrough() {
		// TODO Auto-generated method stub
		List<GoThrough> ls = new ArrayList<GoThrough>();
		Cursor c = getReadableDatabase().rawQuery("SELECT * FROM GoThrough",null);
		if(c.getCount()>0)
		{
		    for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
		    {
		    	ls.add(new GoThrough(c.getString(1), c.getString(2), c.getInt(3), c.getInt(4), c.getInt(5)));
		    }
		}
		return null;
	}

 
	
	public String getCode(Cursor c) {
		return c.getString(2);
	}

	public String getName(Cursor c) {
		return c.getString(1);
	}

	public String getOperationTime(Cursor c) {
		return c.getString(9);
	}

	public String getFrequency(Cursor c) {
		return c.getString(6);
	}

	public String getCost(Cursor c) {
		return c.getString(7);
	}

	public String getGo(Cursor c) {
		return c.getString(11);
	}

	public String getRe(Cursor c) {
		return c.getString(12);
	}

	public String getGoGeo(Cursor c) {
		return c.getString(13);
	}

	public String getReGeo(Cursor c) {
		return c.getString(14);
	}
	
	public String getGoNode(Cursor c){
		return c.getString(15);
	}
	
	public String getReNode(Cursor c){
		return c.getString(16);
	}
		

	/**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
	public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
    	dbExist = false;
    	if(dbExist){
    		//do nothing - database already exist
    	}else{
 
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        	try {
 
    			copyDataBase();
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	}
 
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try{
//    		String myPath = DB_PATH + DB_HCM;
//    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    		String myPath = DB_PATH + DATABASE_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    		Log.d("checkDB",checkDB.toString());
    	}catch(SQLiteException e){
 
    		//database does't exist yet.
 
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DATABASE_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DB_PATH + DATABASE_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY| SQLiteDatabase.OPEN_READONLY);
 
    }
 
    @Override
	public synchronized void close() {
 
    	    if(myDataBase != null)
    		    myDataBase.close();
 
    	    super.close();
 
	}

	
	
}
	