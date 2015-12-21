package com.easybus.gui.Activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.easybus.R;
import com.easybus.Helper.BusHelper;


//Class for show near bus stop list
public class NearBusFragment extends SherlockFragment {
	static BusHelper busHelper;
	Cursor model;
	BusAdapter busAdapter;
	ListView lv;
	int currentPosition;
	private ProgressDialog pd;
	ImageButton btnSearchBus;
	EditText txtBusSearch;
	TextView tvTitle;
	
	public NearBusFragment() {
		// Empty constructor required for fragment subclasses
	}
	
	List<String> nearBusList = new ArrayList<String>();
	TextView text;
	private Handler mHandler = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.list_item, container,
				false);
		text= (TextView)rootView.findViewById(R.id.text);
		text.setTextSize(16);
		text.setText("Đang tìm kiếm....");
		//load content part
		busHelper = BusHelper.getInstance(getActivity());
		//new MyAsyncTask().execute();
		lv = (ListView) rootView.findViewById(R.id.lvItem);
		
		//Thread for searching the near bus stop list
		new Thread(new Runnable() {
			public void run() {
				mHandler.post(new Runnable() {
					public void run() {
						nearBusList = findNearBus();
						model = busHelper.getNearStop(nearBusList);
						Log.d("db", "items: " + model.getCount());
						busAdapter = new BusAdapter(getActivity(), model);
						lv.setAdapter(busAdapter);
						lv.setOnItemClickListener(onClicked);
						if (model.getCount()<=0) 
							text.setText("Hệ thống không tìm thấy bến đỗ trong vòng 500m, bạn vui lòng thử lại lần sau hoặc kiểm tra tên thành phố bạn chọn");
						else
							text.setText("");
					}
				});
			}
		}).start();
		return rootView;
	}
	
	class MyAsyncTask extends AsyncTask<String, Void, Void> {

		
		
		@Override
		protected void onPostExecute(Void result) {
			//			Toast.makeText(getActivity(), Constances.DONE_LOADING,
//					Toast.LENGTH_SHORT).show();

			pd.dismiss();
		}
		
	    protected void onProgressUpdate(Void... progress) {
	        Log.d("a","a");
			nearBusList = findNearBus();
			model = busHelper.getNearStop(nearBusList);
			Log.d("db", "items: " + model.getCount());
			busAdapter = new BusAdapter(getActivity(), model);
			lv.setAdapter(busAdapter);
			lv.setOnItemClickListener(onClicked);
			if (model.getCount()<=0) 
				text.setText("Hệ thống không tìm thấy bến đỗ trong vòng 500m, bạn vui lòng kiểm tra tên thành phố bạn chọn hoặc thử lại lần sau");

		}
		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(getActivity(), null, "Đang tìm, bạn vui lòng chờ nhé!",true);
		}

		@Override
		protected Void doInBackground(String... params) {
			publishProgress();

			return null;

		}
	}
	
	//Find near bus function
	private ArrayList<String> findNearBus() {
		Log.d("Near","1");
    	MainActivity.googleMap.setMyLocationEnabled(true);

		Cursor c = busHelper.getBusStop();
		Log.d("Near","2");
		
		//Set the searching radius is 500 m
		double nearestDistance = 500;
		ArrayList<String> array = new ArrayList<String>();
		Location myLocation = null;
		int time=0;
		
		//Enable my location
		while (time<500 && myLocation==null){
			MainActivity.googleMap.setMyLocationEnabled(true);
			myLocation = MainActivity.googleMap.getMyLocation();
			time++;
		}
		if(c.getCount()>0)
		{
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
			    {
				
					//Get location of each bus stop
			    	Location temp1 = new Location("A");
			    	temp1.setLatitude(Double.parseDouble(c.getString(4).split("\\|")[1]));
			    	temp1.setLongitude(Double.parseDouble(c.getString(4).split("\\|")[0]));
			    	if (MainActivity.googleMap.isMyLocationEnabled()){
				    	try{
				    		//Compute distance of mylocation to the bus stop location
				    		double distance  = myLocation.distanceTo(temp1);
					    	if (nearestDistance >= distance){
					    		String[] list = null;
					    		list = c.getString(1).split(",");
					    		for (int i=0; i < list.length; i++){
					    			if (!array.contains(list[i]))
					    				array.add(c.getString(3));
					    		}
					    	}
				    	}catch(Exception e){
				    		//Exception if GPS is off
				    		e.printStackTrace();
				    		Toast.makeText(getActivity(), "GPS chưa được bật", Toast.LENGTH_SHORT);
				    	}
			    	}else
			    		Log.d("Check","chưa bật");
			    }
		}		
		return array;
	}

	protected void searchBus(String txt) {
		model = busHelper.getBusSearch(txt);
		busAdapter.swapCursor(model);
		busAdapter.notifyDataSetChanged();
	}
	

	//Open fleet over bus list when click on bus stop row
	private AdapterView.OnItemClickListener onClicked = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			model.moveToPosition(position);
			
			SherlockFragment fragment = new FleetOverFragment();
			
			Bundle args = new Bundle();
			args.putString("address", model.getString(2));
			
			fragment.setArguments(args);

			getFragmentManager().beginTransaction()
					.add(getId(), fragment).addToBackStack("fragBack").commit();

		}
	};

	
	//Bus stop row adapter
	public class BusAdapter extends CursorAdapter {

		Cursor c;
		@SuppressWarnings("deprecation")
		public BusAdapter(Context context, Cursor c) {
			super(context, c);
		}
		
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			// when the view will be created for first time,
	        // we need to tell the adapters, how each item will look
	        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
	        View retView = inflater.inflate(R.layout.row_busitem, parent, false);
	 
	        return retView;
		}

		@Override
	    public void bindView(View view, Context context, Cursor cursor) {
	        // here we are setting our data
	        // that means, take the data from the cursor and put it in views
	 
	        TextView tvName = (TextView) view.findViewById(R.id.tvName);
	        tvName.setText(cursor.getString(2));
	 
	        TextView tvId = (TextView) view.findViewById(R.id.tvId);
	        tvId.setText("N");
	        
	        TextView tvCost = (TextView) view.findViewById(R.id.tvCost);
	        tvCost.setText("Tuyến đi qua:"+cursor.getString(1));
	        
//	        ImageView icon = (ImageView) view.findViewById(R.id.image);
//	        icon.setImageResource(R.drawable.station_icon);
	    }		
	}	
	
}