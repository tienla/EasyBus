package com.easybus.gui.Activity;

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
import com.easybus.R;
import com.easybus.Helper.BusHelper;
import com.easybus.gui.Object.Nodes;

//Class for the bus list which fleet over a bus stop
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
	
	//Fleet over bus list
	List<String> busList = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.list_item, container,
				false);
		getActivity().setTitle(getArguments().getString("address"));
		String address = getArguments().getString("address");
		//load content part
		busHelper = BusHelper.getInstance(getActivity());
		//Find bus stop corresponding to the address
		Nodes nodes = busHelper.findBusStop(address);
		
		//Convert fleet over string in db to array
		busList = getFleetOverList(nodes.getsetFleetOver());
		//Convert busList to cursor
		model = busHelper.getNearBus(busList);
		Log.d("db", "items: " + model.getCount());
		busAdapter = new BusAdapter(getActivity(), model);

		lv = (ListView) rootView.findViewById(R.id.lvItem);
		lv.setAdapter(busAdapter);
		lv.setOnItemClickListener(onClicked);
		return rootView;
	}
		
	
	//Convert fleet over string in db to array
	private List<String> getFleetOverList(String getsetFleetOver) {
		List<String> fleetOverList = new ArrayList<String>();
		String[] string = getsetFleetOver.split(",");
		fleetOverList = Arrays.asList(string);
		return fleetOverList;
	}
	
	//Open bus details view when clicked 
	private AdapterView.OnItemClickListener onClicked = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			model.moveToPosition(position);
			
			SherlockFragment fragment = new BusDetailFragment();
			
			Bundle args = new Bundle();
			args.putString("code", busHelper.getCode(model)); //Bus code
			args.putString("name", busHelper.getName(model)); //Bus name
			args.putString("operationTime", busHelper.getOperationTime(model)); //Bus schedule
			args.putString("frequency", busHelper.getFrequency(model)); //Bus frequency
			args.putString("cost", busHelper.getCost(model)); //Bus cost
			args.putString("go", busHelper.getGo(model)); //Bus go route
			args.putString("re", busHelper.getRe(model)); //Bus return route
			
			fragment.setArguments(args);

			getFragmentManager().beginTransaction()
					.add(getId(), fragment).addToBackStack("fragBack").commit();

		}
	};

	//Bus adapter for each bus row
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