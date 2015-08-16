package com.hieund.gui.Activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.hieund.R;
import com.hieund.Helper.BusHelper;
import com.hieund.Util.Constances;
import com.hieund.gui.Handler.MapHandle;

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
		getActivity().setTitle(Constances.TITLE_BUSDETAIL+ " "+ getArguments().getString("code"));
		
		mHandler = new MHandler();
		
		Button btnMapView = (Button)rootView.findViewById(R.id.btnViewinMap);
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
		// load arguments
		LinearLayout llContent = (LinearLayout) pView
				.findViewById(R.id.llBusContent);
		String arg;
		TextView label = (TextView) pView.findViewById(R.id.tvBusContent);
		
		// so hieu tuyen + ten
		arg = getArguments().getString("code") + " - "
				+ getArguments().getString("name");
		label.setText(arg);

		// thoi gian, tan suat, chi phi hd
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

		// tieu de muc
		TextView partName =  new TextView(getActivity());
		partName.setTextAppearance(getActivity(), R.style.MyBoldTextStyle);
		partName.setTextSize(16);
		partName.setText("LƯỢT ĐI");
		partName.setGravity(Gravity.CENTER);
		llContent.addView(partName);
		
		// luot di
		label = new TextView(getActivity());
		//@duy_bug have — special character in db
//		label.setTextColor(Color.BLUE);
		label.setTextSize(16);
		arg = getArguments().getString("go");//.replace("- ", "\n• ").replace("— ", "\n• ");
		label.setText(arg);
//		label.setLayoutParams(params);
		llContent.addView(label);

		partName =  new TextView(getActivity());
		partName.setTextAppearance(getActivity(), R.style.MyBoldTextStyle);
		partName.setTextSize(16);
		partName.setText("\nLƯỢT VỀ");
		partName.setGravity(Gravity.CENTER);
		llContent.addView(partName);
		
		// luot ve
		label = new TextView(getActivity());
//		label.setTextColor(Color.RED);
		label.setTextSize(16);
		arg = getArguments().getString("re");//.replace("- ", "\n- ").replace("— ", "\n- ");
		label.setText(arg);
//		label.setLayoutParams(params);
		llContent.addView(label);

	}
	
	public class MHandler extends Handler implements Parcelable {

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==1){
				Bundle args = getArguments();
				Intent intent = new Intent(getActivity().getApplicationContext(), SendEmailActivity.class);
				intent.putExtra("code", args.getString("code"));
				intent.putExtra("name", args.getString("name"));
				intent.putExtra("operationTime", args.getString("operationTime"));
				intent.putExtra("frequency", args.getString("frequency"));
				intent.putExtra("cost", args.getString("cost"));
				intent.putExtra("go", args.getString("go").replace("- ", "\n- "));
				intent.putExtra("re", args.getString("re").replace("- ", "\n- "));
				getActivity().startActivity(intent);
			}
		}

		@Override
		public void writeToParcel(Parcel arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
	}
}