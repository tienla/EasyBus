package com.hieund.gui.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.actionbarsherlock.app.SherlockFragment;
import com.hieund.R;

public class AboutFragment extends SherlockFragment {
	
	private OnClickListener onClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Uri uri;
			switch(v.getId()){
				case R.id.btn:
					Log.d("a", "0");
					uri = Uri.parse("https://play.google.com/store/apps/details?id=hust.se.vtio.icompanion");
					break;
				case R.id.btn1:
					Log.d("a", "1");
					uri = Uri.parse("https://play.google.com/store/apps/details?id=com.sigandroid.atm");
					break;
				case R.id.btn2:
					Log.d("a", "2");
					uri = Uri.parse("https://play.google.com/store/apps/details?id=hust.hgbk.vtio.vinafood");
					break;			case R.id.btn3:
					Log.d("a", "3");
					uri = Uri.parse("https://play.google.com/store/apps/details?id=nvt.com.activity");
					break;	
				default:
					uri = Uri.parse("https://play.google.com/store/apps/developer?id=Semantic+Innovation+Group");
					break;
			}
			
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}
	};

	public AboutFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_about, container,
				false);
		ImageButton btn = (ImageButton)rootView.findViewById(R.id.btn);
		btn.setOnClickListener(onClick );
		ImageButton btn1 = (ImageButton)rootView.findViewById(R.id.btn1);
		btn1.setOnClickListener(onClick );
		ImageButton btn2 = (ImageButton)rootView.findViewById(R.id.btn2);
		btn2.setOnClickListener(onClick );
		ImageButton btn3 = (ImageButton)rootView.findViewById(R.id.btn3);
		btn3.setOnClickListener(onClick );
		return rootView;
	}
}