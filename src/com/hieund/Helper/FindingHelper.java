package com.hieund.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FindingHelper extends SQLiteOpenHelper {

	public static String DATABASE_NAME = "BusHCM.db";
	public static int SCHEMA_VERSION = 1;
	private static FindingHelper instance;

	public FindingHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.d("TABEL", "onCreate");
		db.execSQL("CREATE TABLE route_favorite (_id INTEGER PRIMARY KEY AUTOINCREMENT, startEndDeclare TEXT, detailInfo TEXT);");
		Log.d("TABEL", "onCreate");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public static FindingHelper getInstance(Context context) {
		synchronized (FindingHelper.class) {
			if (instance == null) {
				instance = new FindingHelper(context);
			}
			return instance;
		}
	}

	public void insert(String startEnd, String detail) {
		ContentValues cv = new ContentValues();
		cv.put("startEndDeclare", startEnd);
		cv.put("detailInfo", detail);
		getWritableDatabase().insert("route_favorite", startEnd, cv);
	}

	public Cursor getItemCondition(String startEnd, String detail) {
		return getReadableDatabase().rawQuery("SELECT * FROM route_favorite WHERE startEndDeclare = '" + startEnd + "' AND detailInfo = '" + detail + "'", null);
	}

	public void delete(String startEnd, String detail) {
		getWritableDatabase().delete("route_favorite", "startEndDeclare = '" + startEnd + "' AND detailInfo ='" + detail + "'", null);
	}

	public Cursor getAll() {
		return getReadableDatabase().rawQuery("SELECT * FROM route_favorite ORDER BY _id", null);
	}

	public String getStartEndDeclare(Cursor c) {
		return c.getString(1);
	}

	public String getDetailInfo(Cursor c) {
		return c.getString(2);
	}

}
