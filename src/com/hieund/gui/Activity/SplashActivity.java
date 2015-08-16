package com.hieund.gui.Activity;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.SQLException;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.hieund.R;
import com.hieund.Helper.BusHelper;
import com.hieund.Util.Constances;
import com.hieund.gui.Object.Singleton;

public class SplashActivity extends Activity {
	BusHelper busHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		SharedPreferences pref = getSharedPreferences("ActivityChooseCityPREF", Context.MODE_PRIVATE);
        if(pref.getBoolean("activity_executed", true)){
            Editor ed = pref.edit();
            ed.putBoolean("activity_executed", false);
            ed.commit();
        }
        statusCheck();
		
//		SOME BUG HERE
//		if (savedInstanceState == null){
//			(new MyAsyncTask()).execute();
//		}
	}
	
	public void statusCheck()
    {
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
        	Log.d("A","A");
            buildAlertMessageNoGps();

        }else{
        	Intent ChooseCityMenu = new Intent(this, ChooseCity.class);
            startActivity(ChooseCityMenu);
         // Remove activity
         	finish();
        }


    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		finish();
		Intent ChooseCityMenu = new Intent(this, ChooseCity.class);
        startActivity(ChooseCityMenu);
	}

     private void buildAlertMessageNoGps() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Bật GPS để sử dụng tốt hơn?")
                   .setCancelable(false)
                   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                       public void onClick(final DialogInterface dialog,  final int id) {
                    	   startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 100);
                       }
                   })
                   .setNegativeButton("No", new DialogInterface.OnClickListener() {
                       public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                            Intent ChooseCityMenu = new Intent(getApplicationContext(), ChooseCity.class);
                            startActivity(ChooseCityMenu);
                         // Remove activity
                         	finish();
                       }
                   });
            final AlertDialog alert = builder.create();
            alert.show();

        }

	// tao database
	class MyAsyncTask extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			Toast.makeText(getApplicationContext(), Constances.DONE_LOADING,
					Toast.LENGTH_SHORT).show();
			Intent i = new Intent(getBaseContext(), MainActivity.class);
			startActivity(i);

			// Remove activity
			finish();
		}

		@Override
		protected void onPreExecute() {
			Toast.makeText(getApplicationContext(), Constances.LOADING,
					Toast.LENGTH_SHORT).show();
		}

		@Override
		protected Void doInBackground(String... params) {
//			busHelper = BusHelper.getInstance(getApplicationContext());
//			Cursor model = busHelper.getAll();
//			if (model.getCount() == 0){
//	        try {
//	        	busHelper.createDataBase();
//		 	} catch (IOException ioe) {
//		 
//		 		throw new Error("Unable to create database");
//		 	}
//	 	try {
//	 		Log.d("check1","3");
//	 		busHelper.openDataBase();
//	 
//	 	}catch(SQLException sqle){
//	 
//	 		throw sqle;
//	 
//	 	}
//			}
				
			return null;
		}
	}
}
