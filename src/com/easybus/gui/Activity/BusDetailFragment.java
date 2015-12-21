package com.easybus.gui.Activity;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.easybus.R;
import com.easybus.Helper.BusHelper;
import com.easybus.Util.Constances;
import com.easybus.gui.Error.Baoloi;
import com.easybus.gui.Handler.MapHandle;

//Class for bus's detail information view
public class BusDetailFragment extends SherlockFragment {

	public static MHandler mHandler;
	
	MapHandle mapHandle;
	
	public BusDetailFragment() {
		// Empty constructor required for fragment subclasses
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_busdetail,
				container, false);
		//Title of the bus with the bus code
		getActivity().setTitle(Constances.TITLE_BUSDETAIL+ " "+ getArguments().getString("code"));
		
		mHandler = new MHandler();
		
		Button btnMapView = (Button)rootView.findViewById(R.id.btnViewinMap);
		//Open map view for bus's route
		btnMapView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				Bundle args = new Bundle();
				BusHelper busHelper = BusHelper.getInstance(getActivity());
				Cursor c = busHelper.getGeo(getArguments().getString("code"));
				c.moveToNext();
				args.putString("goGeo", c.getString(0));
				args.putString("reGeo", c.getString(1));
				args.putString("id", c.getString(2));
				mapHandle = new MapHandle(args, null, getActivity());
			}
		});
		setContent(rootView);
		return rootView;
	}

	
	private void setContent(View pView) {
		// Load arguments
		LinearLayout llContent = (LinearLayout) pView
				.findViewById(R.id.llBusContent);
		String arg;
		TextView label = (TextView) pView.findViewById(R.id.tvBusContent);
		
		// Bus's code and name
		arg = getArguments().getString("code") + " - "
				+ getArguments().getString("name");
		label.setText(arg);

		// Bus schedule, frequency and cost
		label = new TextView(getActivity());
		label.setTextColor(Color.BLACK);
		label.setTextSize(16);
		arg = "- Thời gian hoạt động: "
				+ getArguments().getString("operationTime") + "\n"
			+ "- Tần suất hoạt động: " 
				+ getArguments().getString("frequency") + "\n"
			+ "- Chi phí: " 
				+ getArguments().getString("cost");
		label.setText(arg);
		label.setTypeface(null, Typeface.ITALIC);
		label.setPadding(0, 0, 0, 20);
		llContent.addView(label);

		// Heading
		TextView partName =  new TextView(getActivity());
		partName.setTextAppearance(getActivity(), R.style.MyBoldTextStyle);
		partName.setTextSize(16);
		partName.setText("LƯỢT ĐI");
		partName.setGravity(Gravity.CENTER);
		llContent.addView(partName);
		
		// Go route
		label = new TextView(getActivity());
		label.setTextSize(16);
		arg = getArguments().getString("go");//.replace("- ", "\n• ").replace("— ", "\n• ");
		label.setText(arg);
		llContent.addView(label);

		partName =  new TextView(getActivity());
		partName.setTextAppearance(getActivity(), R.style.MyBoldTextStyle);
		partName.setTextSize(16);
		partName.setText("\nLƯỢT VỀ");
		partName.setGravity(Gravity.CENTER);
		llContent.addView(partName);
		
		// Return Route
		label = new TextView(getActivity());
//		label.setTextColor(Color.RED);
		label.setTextSize(16);
		arg = getArguments().getString("re");//.replace("- ", "\n- ").replace("— ", "\n- ");
		label.setText(arg);
//		label.setLayoutParams(params);
		llContent.addView(label);

	}
	
	//Handle to get the bus infor when user click on Bus Error Report option on navigation bar
	public class MHandler extends Handler implements Parcelable{

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==1){
				Bundle args = getArguments();
				//Class Baoloi: Bus Error Report
				SherlockFragment fragment = new Baoloi();
				fragment.setArguments(getArguments());
//				Intent intent = new Intent(getActivity().getApplicationContext(), Baoloi.class);
//				intent.putExtra("code", args.getString("code"));
//				intent.putExtra("name", args.getString("name"));
//				intent.putExtra("operationTime", args.getString("operationTime"));
//				intent.putExtra("frequency", args.getString("frequency"));
//				intent.putExtra("cost", args.getString("cost"));
//				intent.putExtra("go", args.getString("go").replace("- ", "\n- "));
//				intent.putExtra("re", args.getString("re").replace("- ", "\n- "));
//				getActivity().startActivity(intent);
				
				getFragmentManager().beginTransaction()
				.add(getId(), fragment).addToBackStack("fragBack").commit();

			}
		}

		@Override
		public void writeToParcel(Parcel arg0, int arg1) {
		}
	}
}