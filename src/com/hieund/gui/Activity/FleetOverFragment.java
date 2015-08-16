package com.hieund.gui.Activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.hieund.R;
import com.hieund.Helper.BusHelper;
import com.hieund.gui.Object.Nodes;

public class FleetOverFragment extends SherlockFragment {
	static BusHelper busHelper;
	Cursor model;
	BusAdapter busAdapter;
	ListView lv;
	int currentPosition;

	ImageButton btnSearchBus;
	EditText txtBusSearch;
	TextView tvTitle;
	
	public FleetOverFragment() {
		// Empty constructor required for fragment subclasses
	}
	
	List<String> busList = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.list_item, container,
				false);
		getActivity().setTitle("Tuyến buýt đi qua");
		String address = getArguments().getString("address");
		//load content part
		busHelper = BusHelper.getInstance(getActivity());
		Nodes nodes = busHelper.findBusStop(address);
		busList = getFleetOverList(nodes.getsetFleetOver());
		model = busHelper.getNearBus(busList);
		Log.d("db", "items: " + model.getCount());
		busAdapter = new BusAdapter(getActivity(), model);

		lv = (ListView) rootView.findViewById(R.id.lvItem);
		lv.setAdapter(busAdapter);
		lv.setOnItemClickListener(onClicked);
		return rootView;
	}
		
	private List<String> getFleetOverList(String getsetFleetOver) {
		List<String> fleetOverList = new ArrayList<String>();
		String[] string = getsetFleetOver.split(",");
		fleetOverList = Arrays.asList(string);
		return fleetOverList;
	}
	
	private AdapterView.OnItemClickListener onClicked = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			model.moveToPosition(position);
			
			SherlockFragment fragment = new BusDetailFragment();
			
			Bundle args = new Bundle();
			args.putString("code", busHelper.getCode(model)); //so hieu tuyen
			args.putString("name", busHelper.getName(model)); //ten xe
			args.putString("operationTime", busHelper.getOperationTime(model)); //thoi gian hd
			args.putString("frequency", busHelper.getFrequency(model)); //tan suat hd
			args.putString("cost", busHelper.getCost(model)); //chi phi
			args.putString("go", busHelper.getGo(model)); //luot di
			args.putString("re", busHelper.getRe(model)); //luot ve
			
			fragment.setArguments(args);

			getFragmentManager().beginTransaction()
					.add(getId(), fragment).addToBackStack("fragBack").commit();

		}
	};

	//CUSTOM CLASSES
	public class BusAdapter extends CursorAdapter {


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
	        tvName.setText(busHelper.getName(cursor));
	 
	        TextView tvId = (TextView) view.findViewById(R.id.tvId);
	        tvId.setText(busHelper.getCode(cursor));
	        
	        TextView tvCost = (TextView) view.findViewById(R.id.tvCost);
	        tvCost.setText(busHelper.getCost(cursor));
	        
//	        ImageView icon = (ImageView) view.findViewById(R.id.image);
//	        icon.setImageResource(R.drawable.station_icon);
	    }		
	}	
}