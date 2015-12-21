package com.easybus.gui.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.easybus.R;
import com.easybus.gui.Handler.MapHandle;

//Class for route instruction after finding shortest path
public class RouteInstructionFragment extends SherlockFragment{
	
	String node;
	private TravelAdapter travelAdapter;
	
	public RouteInstructionFragment(String nodeInfo) {
		this.node = nodeInfo;
	}
	
	public RouteInstructionFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_route_instruction, container,
				false);
		MainActivity.showNavBox(false);
		getActivity().setTitle("Lộ trình cụ thể");
		Button btn = (Button) rootView.findViewById(R.id.btn_viewPath);
		if (node != null){
			String[] nodeInfo = this.node.split("\\|");
			travelAdapter = new TravelAdapter(getActivity(), R.layout.row_route, nodeInfo);
			ListView lv = (ListView) rootView.findViewById(R.id.lvItem);
			lv.setAdapter(travelAdapter);
			//open map route for the path
			btn.setOnClickListener(new OnClickListener() {
	
				@Override
				public void onClick(View v) {
					Bundle args = new Bundle();
					args.putString("NodeInfo",node);
					
					new MapHandle(args, null,getActivity());
										
					MainActivity.showNavBox(false);
				}
			});
		}
		return rootView;
	}
	
	//Each instruction adapter
	public class TravelAdapter extends ArrayAdapter<String> {

		Context context; 
	    int layoutResourceId;    
	    String data[] = null;
	    
	    public TravelAdapter(Context context, int layoutResourceId, String[] data) {
	        super(context, layoutResourceId, data);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.data = data;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	View row = convertView;
	        NodeInfoHolder holder = null;
	        
	        if(row == null)
	        {
	            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
	            row = inflater.inflate(layoutResourceId, parent, false);
	            
	            holder = new NodeInfoHolder();
	            holder.start = (TextView)row.findViewById(R.id.start);
	            holder.end = (TextView)row.findViewById(R.id.end);
	            holder.icon = (TextView)row.findViewById(R.id.icon);
	            row.setTag(holder);
	        }else
	        {
	            holder = (NodeInfoHolder)row.getTag();
	        }
	        
	        String nodeInfo = data[position];
	        String busId = nodeInfo.split(";")[0];
			String start = nodeInfo.split(";")[1];
			String end = nodeInfo.split(";")[2];
	        holder.start.setText("Từ "+start); //From start
	        holder.end.setText(end);
	        if (busId.equals("W")){ //If busid = 'W' => ask user to walk to destination
	        	holder.end.setText("Đi bộ tới "+end);
	        	holder.icon.setBackgroundResource(R.drawable.walk);
	        }else{ //Else => Ask user to catch the bus wwith busID to destination
	        	holder.end.setText("Bắt xe buýt tới "+end); 
	        	holder.icon.setText(busId);
	        }
	        return row;
	    }
	    
	    class NodeInfoHolder
	    {
	    	TextView start;
	    	TextView end;
	    	TextView icon;
	    }
		
	}	
	
}
