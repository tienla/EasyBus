package com.hieund.gui.Handler;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hieund.R;
import com.hieund.Helper.BusHelper;
import com.hieund.Util.BusFunction;
import com.hieund.Util.Constances;
import com.hieund.gui.Activity.MainActivity;
import com.hieund.gui.Object.Nodes;
import com.hieund.gui.Object.ResultSearchObject;

public class RouteResultAdapter extends BaseAdapter {
	public Activity a;
	public ArrayList<ResultSearchObject> myResult = new ArrayList<ResultSearchObject>();
	private BusHelper busHelper;
	public RouteResultAdapter (Activity a){
		this.a = a;
	}
	
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row;
		//MainActivity.showNavBox(true);
		busHelper = BusHelper.getInstance(a);
		row = a.getLayoutInflater().inflate(
				R.layout.row_findbus, parent, false);
		ViewHolder holder;
		if (row.getTag() == null) {
			holder = new ViewHolder();
			holder.animation = (LinearLayout) row
					.findViewById(R.id.relayout_animation);
			holder.startEndWay = (TextView) row
					.findViewById(R.id.start_end_way);
			holder.total_time = (TextView) row
					.findViewById(R.id.total_time);
			holder.chi_phi =  (TextView) row
					.findViewById(R.id.chi_phi);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}
//		holder.imageFindBus.setBackgroundResource(R.drawable.flag);
		
		ResultSearchObject object = (ResultSearchObject) getItem(position);
		for (String element : object.getBusIds()) {
			if(element.equalsIgnoreCase(Constances.MOVETO)){
				ImageView walk = new ImageView(this.a);
				walk.setBackgroundResource(R.drawable.walk_icon);
				holder.animation.addView(walk);
			}else{
				TextView busId = new TextView(this.a);
				busId.setText(element+" ");
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                llp.setMargins(25, 0, 0, 0);
                busId.setLayoutParams(llp);
				busId.setTextAppearance(this.a, R.style.MyBusIdTextStyle);
				holder.animation.addView(busId);
			}
		}
		double distance = calculateRoute(object);
		Log.d("distance",""+distance);
		distance = (double)Math.round(distance * 10) / 10;

		int totalCost = 0;
		for  (String s : object.getBusIds()){
			totalCost += busHelper.getBusCost(s);
		}
		holder.chi_phi.setText("Chi phí: "+totalCost+"đ");
		holder.startEndWay.setText(object.getStartEndDeclare());
		//Tong TG:
		holder.total_time.setText("~"+distance+"km");
		return row;
	}

	private double calculateRoute(ResultSearchObject object) {
		String nodeInfo = object.getNodeInfo();
		String[] path = nodeInfo.split("\\|");
		double distance = 0;
		for(int i = 0; i < (path.length); i++){
			String busId = path[i].split(";")[0];
			String start = path[i].split(";")[1];
			String end = path[i].split(";")[2];
			
			if (busId.equals("W")){
				Nodes s = null;
				Nodes e = null;
				if (start.contains("(") && start.contains(":")){
					LatLng l = BusFunction.stringToLatLng(start);
					s = new Nodes(null, "Điểm đi bộ");
					s.setGeo(l);
					s.setFleetOver("");
				}else
					s = busHelper.findBusStop(start);
				if (end.contains("(") && end.contains(":")){
					LatLng l = BusFunction.stringToLatLng(end);
					e = new Nodes(null, "Điểm đi bộ");
					e.setGeo(l);
					e.setFleetOver("");
				}else
					e = busHelper.findBusStop(end);
				distance += BusFunction.countDistance(s.getGeo().latitude,s.getGeo().longitude,
						e.getGeo().latitude,e.getGeo().longitude);
			}else{
				ArrayList<Nodes> goNodeTemp = busHelper.getNodeInfo(busId, "go");
				ArrayList<Nodes> reNodeTemp = busHelper.getNodeInfo(busId, "re");
				ArrayList<Nodes> listNodes = null;
				boolean isGo = false;
				for (int j = 0; j < goNodeTemp.size(); j++) {
					if (goNodeTemp.get(j).getStopName().equals(start))
						isGo = true;
				}
				int check = 0;
				if(isGo)
					listNodes = goNodeTemp;
				else
					listNodes = reNodeTemp;
				for (int j = 0; j < listNodes.size(); j++) {
						if (listNodes.get(j).getStopName().equals(start)){
							check=1;
						}else if (listNodes.get(j).getStopName().equals(end)){
							distance += BusFunction.countDistance(listNodes.get(j).getGeo().latitude, listNodes.get(j).getGeo().longitude
									, listNodes.get(j-1).getGeo().latitude, listNodes.get(j-1).getGeo().longitude);
							break;
						}else if (check == 1){
							distance += BusFunction.countDistance(listNodes.get(j).getGeo().latitude, listNodes.get(j).getGeo().longitude
									, listNodes.get(j-1).getGeo().latitude, listNodes.get(j-1).getGeo().longitude);
						}
			
					}
				}
			}
		return distance;
	}
	

	private LatLng stringToLatLng(String start) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCount() {
		return myResult.size();
	}

	@Override
	public Object getItem(int position) {
		return myResult.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
}

class ViewHolder {
	LinearLayout animation;
	TextView startEndWay;
	TextView total_time;
	TextView chi_phi;
}
