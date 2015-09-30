package com.hieund.gui.Activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.hieund.R;
import com.hieund.Helper.BusHelper;
import com.hieund.Util.Constances;
import com.hieund.gui.Object.Singleton;

public class ChooseCity extends Activity {
	
	//TODO change to dynamic data to load in to array
	// Array of strings storing city names
    String[] city_names = new String[] { "Hà Nội", "Hồ Chí Minh"};

	// Array of integers points to images stored in /res/drawable-ldpi/
	int[] city_imgs = new int[] { R.drawable.city_hanoi, R.drawable.city_hcm};

	// Array of strings to store currencies
	String[] bus_num = new String[] { "75 ", "120"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_city);

		// @duy make a list contains each row in the list stores city
		// name,numbers of bus and photo
		List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < city_names.length; i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("city_name", city_names[i]);
			hm.put("num_of_bus", "Số tuyến : " + bus_num[i]);
			hm.put("city_img", Integer.toString(city_imgs[i]));
			aList.add(hm);
		}

		// Keys used in Hashmap
		String[] from = { "city_img", "city_name", "num_of_bus" };

		// Ids of views in layout.city_list_item
		int[] to = { R.id.imageView_city, R.id.textView_city_name,
				R.id.textView_bus_num };

		// Instantiating an adapter to store each items
		// R.layout.city_list_item defines the layout of each item
		SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList,
				R.layout.city_list_item, from, to){
			
			@Override
			public boolean isEnabled(int position) {
			    if(position ==2 ){
			        return false;
			    }
			    return true;
			}
		};
		
		// Getting a reference to listview of activity_main.xml layout file
		ListView list_cities = (ListView) findViewById(R.id.listView_cities);
		
		// Setting the adapter to the list_cities
		list_cities.setAdapter(adapter);
		// @duy_note make this activity appear in first only once time, can
		// called again
		SharedPreferences pref = getSharedPreferences("ActivityChooseCityPREF",
				Context.MODE_PRIVATE);
		if (pref.getBoolean("activity_executed", false)) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		} else {
			Editor ed = pref.edit();
			ed.putBoolean("activity_executed", true);
			ed.commit();
		}
		list_cities.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (id == 0) {
					BusHelper.DATABASE_NAME = "HanoiBus.sqlite";
				} else if (id == 1) {
					BusHelper.DATABASE_NAME = "HCMBus.sqlite";
				}
				// TODO load CHOSEN city data to jump to main activity
				new MyAsyncTask().execute();
			}
		});
	}
	public void onBackPressed() {
		new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Closing App")
        .setMessage("Bạn muốn thoát ?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
		    {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	Intent intent = new Intent(Intent.ACTION_MAIN);
		        	intent.addCategory(Intent.CATEGORY_HOME);
		        	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        	startActivity(intent);
		        	finish();
		        	System.exit(0);
		        }

		    })
		    .setNegativeButton("No", null)
		    .show();	
	}
	// tao database
	private ProgressDialog pd;

	class MyAsyncTask extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			pd.dismiss();
			Intent i = new Intent(getBaseContext(), MainActivity.class);
			startActivity(i);

			// Remove activity
			finish();
		}

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(ChooseCity.this, null, "Đang khởi tạo dữ liệu",true);
		}

		@Override
		protected Void doInBackground(String... params) {
			//Intent MainAct = new Intent(ChooseCity.this, MainActivity.class);
			BusHelper busHelper;
			busHelper = BusHelper.newInstance(getApplicationContext());
//			Cursor model = busHelper.getAll();
//			if (model.getCount() == 0) {
				try {
					busHelper.createDataBase();
				} catch (IOException ioe) {

					throw new Error("Unable to create database");

				}
				//
				try {
					busHelper.openDataBase();
					
				} catch (SQLException sqle) {

					throw sqle;

				}
			//}
			Singleton.getInstance(getBaseContext());

//			ChooseCity.this.startActivity(MainAct);
//			ChooseCity.this.finish();

			return null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.choose_city, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
