/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.easybus.gui.Activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.easybus.R;

public class SuperAwesomeCardFragment extends SherlockFragment{

	private static final String ARG_POSITION = "position";

	private int position;

	public static SuperAwesomeCardFragment newInstance(int position) {
		SuperAwesomeCardFragment f = new SuperAwesomeCardFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		position = getArguments().getInt(ARG_POSITION);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		FrameLayout fl = new FrameLayout(getActivity());
		fl.setLayoutParams(params);

		//@duy_del margin for content of card of tab
//		final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
//				.getDisplayMetrics());

		TextView v = new TextView(getActivity());
//		params.setMargins(margin, margin, margin, margin);
		v.setLayoutParams(params);
//		v.setLayoutParams(params);
		v.setGravity(Gravity.CENTER);
		v.setBackgroundResource(R.drawable.background_card);
		v.setText("CARD " + (position + 1));

		
		LinearLayout lnLayout = new LinearLayout(getActivity());
		lnLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		lnLayout.setBackgroundResource(R.drawable.background_card);
		lnLayout.setOrientation(LinearLayout.VERTICAL);
		lnLayout.setId(1234567890);
	//	Toast.makeText(getActivity(),"pos+ "+ position, Toast.LENGTH_SHORT).show();
		
		switch (position) {
		case 0:
			//TODO load favorite bus
			ListItemFragment fragment = new ListItemFragment();		
			getFragmentManager().beginTransaction().add(lnLayout.getId(), fragment , "fragment").commit();		
			break;
		case 1:
			//TODO load fav location
			NearBusFragment fragment1 = new NearBusFragment();		
			getFragmentManager().beginTransaction().add(lnLayout.getId(), fragment1 , "fragment").commit();	
			break;

		default:
			lnLayout.addView(v);
			break;
		}
		
		fl.addView(lnLayout);
		return fl;
	}

}