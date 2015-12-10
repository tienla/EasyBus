package com.easybus.gui.Activity;

import java.util.ArrayList;
import java.util.Vector;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.easybus.R;
import com.easybus.gui.Handler.RouteResultAdapter;
import com.easybus.gui.Object.Nodes;
import com.easybus.gui.Object.ResultSearchObject;

public class SearchFragment extends SherlockFragment {


	ListView lv;
	int currentPosition;
	private RouteResultAdapter adapter;
	private ArrayList<Vector<Nodes>> arrNodes;
	Dialog dialog;

	ArrayList<ResultSearchObject> myResult = new ArrayList<ResultSearchObject>();
	ArrayList<String> resultGeo = new ArrayList<String>();

	public SearchFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search, container,
				false);

		
		adapter = new RouteResultAdapter(getActivity());
		
		lv = (ListView) rootView.findViewById(R.id.lvRouting);		
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(onItemClick);

		return rootView;
	}

	OnItemClickListener onItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			currentPosition = position;

			SherlockFragment fragment = new RouteInstructionFragment(myResult.get(currentPosition).getNodeInfo());
			getFragmentManager().beginTransaction()
			.add(getId(), fragment).addToBackStack("fragBack").commit();

//			dialog = new Dialog(getActivity());
//			dialog.setContentView(R.layout.dialog); 
//			dialog.setTitle("Chi tiết lộ trình");
//			
//			//TextView tv = (TextView) dialog.findViewById(R.id.txt_detail);
//			//tv.setText(detail);
//			Button btn = (Button) dialog.findViewById(R.id.btn_viewPath);
//			String[] nodeInfo = myResult.get(position).getNodeInfo().split("\\|");
//			List<String> nodeInfos = new ArrayList<String>();
//			nodeInfos = Arrays.asList(nodeInfo);
//			travelAdapter = new TravelAdapter(getActivity(), R.layout.row_route, nodeInfo);
//			ListView lv = (ListView) dialog.findViewById(R.id.lvItem);
//			lv.setAdapter(travelAdapter);
//			dialog.show();
//			btn.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					dialog.dismiss();
//
//					Bundle args = new Bundle();
//					args.putString("Geo", resultGeo.get(currentPosition));
//					args.putString("NodeInfo",myResult.get(currentPosition).getNodeInfo());
//					
//					new MapHandle(args, arrNodes.get(currentPosition),getActivity());
//										
//					MainActivity.showNavBox(false);
//				}
//			});
		}
	};
	
	

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
	        holder.start.setText("Từ "+start);
	        holder.end.setText(end);
	        if (busId.equals("W")){
	        	holder.end.setText("Đi bộ tới "+end);
	        	holder.icon.setBackgroundResource(R.drawable.walk);
	        }else{
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
	


	public void updateData(ArrayList<ResultSearchObject> myResult, ArrayList<String> resultGeo, ArrayList<Vector<Nodes>> arrayNodeAns){
		this.myResult = myResult;
		this.resultGeo = resultGeo;
		this.arrNodes = arrayNodeAns;
		adapter.myResult = myResult;
		adapter.notifyDataSetChanged();
	}

}