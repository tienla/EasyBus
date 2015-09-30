package com.hieund.gui.Error;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.hieund.R;
import com.hieund.gui.Activity.SendEmailFragment;

public class Chinhsuabendo extends SherlockFragment{
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.chinhsuabendo, container,
				false);
		getActivity().setTitle("Chỉnh sửa bến đỗ");
		Button btn = (Button) rootView.findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

			}
		});
		
		Button btn2 = (Button) rootView.findViewById(R.id.button2);
		btn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

			}
		});
        
        return rootView;
    }
}
