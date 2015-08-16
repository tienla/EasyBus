package com.hieund.gui.NavBox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.hieund.R;
import com.hieund.gui.Activity.MainActivity;
import com.hieund.gui.Activity.SearchFragment;

public class ResultBox extends Fragment{
	private TextView tvMore;
	private Button btnNew;
	
	public ResultBox() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.box_result, container,
				false);
		tvMore = (TextView)rootView.findViewById(R.id.tvMore);
		btnNew = (Button)rootView.findViewById(R.id.btnNew);
		
		tvMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				SherlockFragment fragment = new SearchFragment();
				getFragmentManager().beginTransaction()
				.replace(R.id.content, fragment).commit();
				MainActivity.showContent(true);
			}
		});

		btnNew.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Fragment fbox = new SearchBox();
				getFragmentManager().beginTransaction()
				.replace(R.id.navbox, fbox).addToBackStack("fragBack").commit();
			}
		});
		
		return rootView;
	}
	
}
