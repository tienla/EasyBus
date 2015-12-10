package com.easybus.gui.Error;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;
import com.easybus.R;
import com.easybus.gui.Activity.BusDetailFragment;
import com.easybus.gui.Activity.SendEmailFragment;

public class Baoloi extends SherlockFragment{
	Button btn1,btn2,btn3;
	SherlockFragment fragment;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.baoloi, container,
				false);
		getActivity().setTitle("Bao loi");
		btn1 = (Button) rootView.findViewById(R.id.button1);
		btn2 = (Button) rootView.findViewById(R.id.button2);
		btn3 = (Button) rootView.findViewById(R.id.button3);
		btn1.setOnClickListener(onClick);
		btn2.setOnClickListener(onClick);
		btn3.setOnClickListener(onClick);
		btn3.setVisibility(View.INVISIBLE);
        return rootView;
    }

	private OnClickListener onClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			switch(v.getId()){
        	case R.id.button1:
    			fragment = new Camon();
    			fragment.setArguments(getArguments());
    			getFragmentManager().beginTransaction().add(getId(), fragment).addToBackStack("fragBack").commit();

        	break;
        	
        	case R.id.button2:
    			fragment = new SendEmailFragment();
    			fragment.setArguments(getArguments());
    			getFragmentManager().beginTransaction().add(getId(), fragment).addToBackStack("fragBack").commit();
        	break;
        	
        	case R.id.button3:
    			fragment = new Chinhsuabendo();
    			fragment.setArguments(getArguments());
    			getFragmentManager().beginTransaction().add(getId(), fragment).addToBackStack("fragBack").commit();

        	break;
        	
        	default:
    			fragment = new BusDetailFragment();
    			fragment.setArguments(getArguments());
    			getFragmentManager().beginTransaction().add(getId(), fragment).addToBackStack("fragBack").commit();

        		break;
        }

		}
	};
	
}
