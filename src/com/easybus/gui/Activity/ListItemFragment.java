package com.easybus.gui.Activity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.CursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
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


//Class for bus list view
public class ListItemFragment extends SherlockFragment {
	static BusHelper busHelper;
	Cursor model;
	BusAdapter busAdapter;
	ListView lv;
	int currentPosition;

	ImageButton btnSearchBus;
	EditText txtBusSearch;
	TextView tvTitle;
	
	public ListItemFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_listitem, container,
				false);
		//load content part
		busHelper = BusHelper.getInstance(getActivity());
		model = busHelper.getAll();
		Log.d("db", "items: " + model.getCount());
		busAdapter = new BusAdapter(getActivity(), model);
		lv = (ListView) rootView.findViewById(R.id.lvItem);
		lv.setAdapter(busAdapter);
		lv.setOnItemClickListener(onClicked);
		lv.requestFocus();
		getActivity().setTitle("Thông tin tuyến buýt");
//		registerForContextMenu(lv);
		
//		btnSearchBus = (ImageButton)rootView.findViewById(R.id.btnSearchBus);
//		btnSearchBus.setOnClickListener(searchBusListener);
		
		txtBusSearch = (EditText)rootView.findViewById(R.id.txtBusSearch);
//		tvTitle = (TextView)rootView.findViewById(R.id.tvTitle);\
		
		//Catch the text change event on search box to instant searching the bus
		txtBusSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				searchBus(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		return rootView;
	}
	
	//Search bus with text input for search box
	protected void searchBus(String txt) {
		model = busHelper.getBusSearch(txt);
		busAdapter.swapCursor(model);
		busAdapter.notifyDataSetChanged();
	}
	
	//Open bus details information when clicking on bus row
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
			try{
				args.putParcelable("handler", getArguments().getParcelable("handler"));
			}catch (NullPointerException e){
				e.printStackTrace();
			}
			MainActivity.hideSoftKeyboard(getActivity());
			fragment.setArguments(args);

			getFragmentManager().beginTransaction()
					.replace(getId(), fragment).commit();

			//Set to appear bus error report button on bus details view
			Message msg = new Message();
			msg.what=1;
			try{
				((MainActivity.MHandler)getArguments().getParcelable("handler")).sendMessage(msg);
			}catch (NullPointerException e){
				e.printStackTrace();
			}
		}
	};

	//Bus row adapter
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
//			int lastPosition = -1;
//			Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), (cursor.getPosition() > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//			view.startAnimation(animation);
//			lastPosition = cursor.getPosition();
			
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