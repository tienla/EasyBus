package com.hieund.gui.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.w3c.dom.Document;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hieund.R;
import com.hieund.Helper.BusHelper;
import com.hieund.Util.BusFunction;
import com.hieund.Util.Constances;
import com.hieund.gui.Activity.MainActivity;
import com.hieund.gui.Object.Nodes;

public final class MapHandle{
	GoogleMap googleMap = MainActivity.googleMap;
	Marker currentmarker, startMarker, endMarker ,midMarker;
	Polyline poly1, poly2, poly3;
	Button goButton, reButton;
	BusHelper busHelper;
	private FragmentActivity fmActivity;
	private static String goGeo;
	private static String reGeo;
	private static String id;
	private static ArrayList<Nodes> goNodeList;
	private static ArrayList<Nodes> reNodeList;
	private static boolean isOnline;
	public MapHandle() {
		MainActivity.showContent(false);
		googleMap.clear();
	}

	public MapHandle(Bundle b, Vector<Nodes> vector) {
		MainActivity.showContent(false);
		googleMap.clear();
        v2GetRouteDirection = new GMapV2GetRouteDirection();
		markers.clear();


		String goGeo = b.getString("goGeo");
		String reGeo = b.getString("reGeo");
		String nodeInfo = b.getString("NodeInfo");
		String id = b.getString("id");
		
		MapHandle.goGeo = goGeo;
		MapHandle.reGeo = reGeo;
		MapHandle.id = id;

		
		
		// hien duong di xe bus
		if (goGeo != null && reGeo != null) {
			showBusDetailPath(goGeo,reGeo);
		}
		
		if (nodeInfo != null){
			showPath(nodeInfo);
		}

		if (vector != null) showBusStop(vector);
		BusFunction.zoomToFitMarkers(markers,MainActivity.googleMap);

	}
	
	public MapHandle(Bundle b, Vector<Nodes> vector, FragmentActivity activity) {
		fmActivity = activity;
		busHelper = BusHelper.getInstance(activity);	
		MainActivity.showContent(false);
		googleMap.clear();
		markers.clear();
        v2GetRouteDirection = new GMapV2GetRouteDirection();


		String goGeo = b.getString("goGeo");
		String reGeo = b.getString("reGeo");
		String id = b.getString("id");
		String nodeInfo = b.getString("NodeInfo");
		
		MapHandle.goGeo = goGeo;
		MapHandle.reGeo = reGeo;
		MapHandle.id = id;

		// hien duong di xe bus
		if (goGeo != null && reGeo != null) {
			showBusDetailPath(goGeo,reGeo);
		}
		isOnline = MainActivity.isNetworkAvailable();
		// hien lo trinh
		if (nodeInfo != null){
			//showPath(routingGeo);
			showFindPath(nodeInfo);
		}

		//if (vector != null) showBusStop(vector);
		if (id != null){
			MapHandle.goNodeList = busHelper.getNodeInfo(MapHandle.id, "go");
			MapHandle.reNodeList = busHelper.getNodeInfo(MapHandle.id, "re");
		}
		BusFunction.zoomToFitMarkers(markers,MainActivity.googleMap);
	}

	private void showBusStop(Vector<Nodes> vector2) {
		for (Nodes node : vector2) {
			googleMap.addMarker(new MarkerOptions().position(node.getGeo())
					.draggable(true));
		}
	}
	
	public void showGoPath(){
		googleMap.clear();

		ArrayList<LatLng> temp1 = decodeToNode(MapHandle.goGeo);
		PolylineOptions rectLine1 = new PolylineOptions().width(
				Constances.POLYLINE_WIDTH).color(Color.RED); //@duy import com.hieund.R
		for (int j = 0; j < temp1.size(); j++) {
			rectLine1.add(temp1.get(j));
		}
		
		for (int j = 0; j < goNodeList.size(); j++) {
			if (j == 0){
				startMarker = googleMap.addMarker(new MarkerOptions().position(
						goNodeList.get(j).getGeo()).draggable(false).title(""+goNodeList.get(j).getStopName()).snippet("Bến đỗ: "+goNodeList.get(j).getsetFleetOver()));
				startMarker.showInfoWindow();
				startMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_start));
				markers.add(startMarker);
				
			}else if (j == goNodeList.size() - 1){
				endMarker = googleMap.addMarker(new MarkerOptions().position(
						goNodeList.get(j).getGeo()).draggable(false).title(""+goNodeList.get(j).getStopName()).snippet("Bến đỗ: "+goNodeList.get(j).getsetFleetOver()));
				endMarker.showInfoWindow();
				endMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_end));
				markers.add(endMarker);

			}else{
				midMarker = googleMap.addMarker(new MarkerOptions().position(
						goNodeList.get(j).getGeo()).draggable(false).title(""+goNodeList.get(j).getStopName()).snippet("Bến đỗ: "+goNodeList.get(j).getsetFleetOver()));
								midMarker.showInfoWindow();
				midMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.bus_pin));
				markers.add(midMarker);
			}
		}
		googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				if(marker.isInfoWindowShown())
					marker.hideInfoWindow();
				else
					marker.showInfoWindow();
				return false;
			}
		});
		MainActivity.zoomMapTo(startMarker.getPosition());
		poly1 = googleMap.addPolyline(rectLine1);
		BusFunction.zoomToFitMarkers(markers,MainActivity.googleMap);

	}
	
	public void showRePath(){
		googleMap.clear();

		ArrayList<LatLng> temp1 = decodeToNode(MapHandle.reGeo);
		PolylineOptions rectLine1 = new PolylineOptions().width(
				Constances.POLYLINE_WIDTH).color(Color.BLUE);
		for (int j = 0; j < temp1.size(); j++) {
			rectLine1.add(temp1.get(j));
		}
		
		for (int j = 0; j < reNodeList.size(); j++) {
			if (j == 0){
				startMarker = googleMap.addMarker(new MarkerOptions().position(
						reNodeList.get(j).getGeo()).draggable(false).title(""+reNodeList.get(j).getStopName()).snippet("Bến đỗ: "+reNodeList.get(j).getsetFleetOver()));
				startMarker.showInfoWindow();
				startMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_start));
				markers.add(startMarker);

				
			}else if (j == reNodeList.size() - 1){
				endMarker = googleMap.addMarker(new MarkerOptions().position(
						reNodeList.get(j).getGeo()).draggable(false).title(""+reNodeList.get(j).getStopName()).snippet("Bến đỗ: "+reNodeList.get(j).getsetFleetOver()));
				endMarker.showInfoWindow();
				endMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_end));
				markers.add(endMarker);

			}else{
				midMarker = googleMap.addMarker(new MarkerOptions().position(
						reNodeList.get(j).getGeo()).draggable(false).title(""+reNodeList.get(j).getStopName()).snippet("Bến đỗ: "+reNodeList.get(j).getsetFleetOver()));
				midMarker.showInfoWindow();
				midMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.bus_pin));
				markers.add(midMarker);

			}
		}
		
		googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				if(marker.isInfoWindowShown())
					marker.hideInfoWindow();
				else
					marker.showInfoWindow();
				return false;
			}
		});
		MainActivity.zoomMapTo(startMarker.getPosition());
		poly2 = googleMap.addPolyline(rectLine1);
		BusFunction.zoomToFitMarkers(markers,MainActivity.googleMap);

	}


	private void showBusDetailPath(String goGeo, String reGeo) {
		ArrayList<LatLng> temp = decodeToNode(goGeo);
		
		MainActivity.zoomMapTo(temp.get(0));
		PolylineOptions rectLine = new PolylineOptions().width(
				Constances.POLYLINE_WIDTH).color(Color.RED);
		for (int j = 0; j < temp.size(); j++) {
			rectLine.add(temp.get(j));
		}
		
		ArrayList<Nodes> nodeList = busHelper.getNodeInfo(MapHandle.id, "go");
		
		for (int j = 0; j < nodeList.size(); j++) {
			if (j == 0){
				startMarker = googleMap.addMarker(new MarkerOptions().position(
						nodeList.get(j).getGeo()).draggable(false).title(""+nodeList.get(j).getStopName()).snippet("Bến đỗ: "+nodeList.get(j).getsetFleetOver()));
				startMarker.showInfoWindow();
				startMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_start));
				markers.add(startMarker);
				
			}else if (j == nodeList.size() - 1){
				endMarker = googleMap.addMarker(new MarkerOptions().position(
						nodeList.get(j).getGeo()).draggable(false).title(""+nodeList.get(j).getStopName()).snippet("Bến đỗ: "+nodeList.get(j).getsetFleetOver()));
				endMarker.showInfoWindow();
				endMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_end));
				markers.add(endMarker);
			}else{
				midMarker = googleMap.addMarker(new MarkerOptions().position(
						nodeList.get(j).getGeo()).draggable(false).title(""+nodeList.get(j).getStopName()).snippet("Bến đỗ: "+nodeList.get(j).getsetFleetOver()));
				midMarker.showInfoWindow();
				midMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.bus_pin));
				markers.add(midMarker);
			}
		}
		
		googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				if(marker.isInfoWindowShown())
					marker.hideInfoWindow();
				else
					marker.showInfoWindow();
				return false;
			}
		});
		MainActivity.zoomMapTo(startMarker.getPosition());
		poly1 = googleMap.addPolyline(rectLine);
	}


	
	public ArrayList<LatLng> decodeToNode(String str) {
		if (str == null) str="";
		ArrayList<LatLng> result = new ArrayList<LatLng>();
		String[] geoArray = str.split(" ");
		for(int i=0; i<geoArray.length; i++){
			Double latitude;
			Double longitude;
				latitude = Double.parseDouble(geoArray[i].split("\\|")[1]);
				longitude = Double.parseDouble(geoArray[i].split("\\|")[0]);
			result.add(new LatLng(latitude, longitude));
		}
		return result;
	}

	private void showPath(String routingGeo) {
		ArrayList<LatLng> temp = decodeRoutingPoly(routingGeo);

		MainActivity.zoomMapTo(temp.get(0));

		PolylineOptions rectLine = new PolylineOptions().width(
				Constances.POLYLINE_WIDTH).color(Color.BLUE);
		for (int j = 0; j < temp.size(); j++) {
			rectLine.add(temp.get(j));
			if (j == 0)
				startMarker = googleMap.addMarker(new MarkerOptions().position(
						temp.get(j)).draggable(true));
			else if (j == temp.size() - 1)
				endMarker = googleMap.addMarker(new MarkerOptions().position(
						temp.get(j)).draggable(true));
		}
		poly3 = googleMap.addPolyline(rectLine);
	}
	private void showFindPath(String nodeInfo){
		Log.d("nodeInfo",nodeInfo);
		String[] path = nodeInfo.split("\\|");
		
		for(int i = 0; i < (path.length); i++){
			Log.d("path1",path[i]);
			String busId = path[i].split(";")[0];
			String start = path[i].split(";")[1];
			String end = path[i].split(";")[2];
			int color;
			if(i%2==0) color = Color.RED;
			else color = Color.RED;
			int order = i == (path.length-1) ? 99 : i;
			if (path.length == 1)
				order = -1;
			if (busId.equals("W")){
				drawWalk(start,end,order);
			}else
				drawPath(busId,start,end,color,order);
		}
		MainActivity.zoomMapTo(startMarker.getPosition());
	}
	ArrayList<Marker> markers = new ArrayList<Marker>();
	private void drawWalk(String start, String end, int order) {
		PolylineOptions rectLine1 = new PolylineOptions().width(
				Constances.POLYLINE_WIDTH).color(Color.GRAY); 
		Nodes c = null;
		if (start.contains("(") && start.contains(":")){
			LatLng l = stringToLatLng(start);
			c = new Nodes(null, "Điểm đi bộ");
			c.setGeo(l);
			c.setFleetOver("");
		}else
			c = busHelper.findBusStop(start);
		startMarker = googleMap.addMarker(new MarkerOptions().position(
				c.getGeo()).draggable(false).title(""+c.getStopName()).snippet("Bến đỗ: "+c.getsetFleetOver()));
		startMarker.showInfoWindow();
		if (order==0)
			startMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_start));
		else
			startMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_walk));
		rectLine1.add(c.getGeo());
		if (end.contains("(") && end.contains(":")){
			LatLng l = stringToLatLng(end);
			c = new Nodes(null, "Điểm đi bộ");
			c.setGeo(l);
			c.setFleetOver("");
		}else
			c = busHelper.findBusStop(end);
		
		endMarker = googleMap.addMarker(new MarkerOptions().position(
				c.getGeo()).draggable(false).title(""+c.getStopName()).snippet("Bến đỗ: "+c.getsetFleetOver()));
		if (order==99 || order ==-1){
			endMarker.showInfoWindow();
			endMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_end));
		}

		
		rectLine1.add(c.getGeo());
		googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				if(marker.isInfoWindowShown())
					marker.hideInfoWindow();
				else
					marker.showInfoWindow();
				return false;
			}
		});

		if (!isOnline)
			poly3 = googleMap.addPolyline(rectLine1);
		else{
	        fromPosition = startMarker.getPosition();
	        toPosition = endMarker.getPosition();
			GetRouteTask getRoute = new GetRouteTask();
	        getRoute.execute();
		}
	}

	private LatLng stringToLatLng(String string) {
		string = string.replace("(","");
    	string = string.replace(")","");
		LatLng t = new LatLng(Double.parseDouble(string.split("\\:")[0]),
								Double.parseDouble(string.split("\\:")[1]));
		return t;
	}

	private void drawPath(String busId, String start, String end, int color,int order){
		Cursor c = busHelper.getGeo(busId);
		c.moveToNext();
		String id = c.getString(2);
		
		ArrayList<Nodes> goNodeTemp = busHelper.getNodeInfo(id, "go");
		ArrayList<Nodes> reNodeTemp = busHelper.getNodeInfo(id, "re");
		int check = 0;
		PolylineOptions rectLine1 = new PolylineOptions().width(
				Constances.POLYLINE_WIDTH).color(color); 
		boolean isGo = false;
		for (int j = 0; j < goNodeTemp.size(); j++) {
			if (goNodeTemp.get(j).getStopName().equals(start))
				isGo = true;
		}
		
		if(isGo){
			for (int j = 0; j < goNodeTemp.size(); j++) {
				if (goNodeTemp.get(j).getStopName().equals(start)){
					check=1;
					startMarker = googleMap.addMarker(new MarkerOptions().position(
							goNodeTemp.get(j).getGeo()).draggable(false).title(""+goNodeTemp.get(j).getStopName()).snippet("Bến đỗ: "+goNodeTemp.get(j).getsetFleetOver()));
					startMarker.showInfoWindow();
					startMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_bus_green));
					rectLine1.add(goNodeTemp.get(j).getGeo());
				}else if (goNodeTemp.get(j).getStopName().equals(end)){
					endMarker = googleMap.addMarker(new MarkerOptions().position(
							goNodeTemp.get(j).getGeo()).draggable(false).title(""+goNodeTemp.get(j).getStopName()).snippet("Bến đỗ: "+goNodeTemp.get(j).getsetFleetOver()));
					endMarker.showInfoWindow();
					endMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_bus_red));
					rectLine1.add(goNodeTemp.get(j).getGeo());
					check=2;
					break;
				}else if (check == 1){
					midMarker = googleMap.addMarker(new MarkerOptions().position(
							goNodeTemp.get(j).getGeo()).draggable(false).title(""+goNodeTemp.get(j).getStopName()).snippet("Bến đỗ: "+goNodeTemp.get(j).getsetFleetOver()));
					midMarker.showInfoWindow();
					midMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.bus_pin));
					rectLine1.add(goNodeTemp.get(j).getGeo());
					markers.add(midMarker);

				}
	
			}
			
			if (check != 2){
				for (int j = 0; j < reNodeTemp.size(); j++) {
					if (reNodeTemp.get(j).getStopName().equals(end)){
						endMarker = googleMap.addMarker(new MarkerOptions().position(
								reNodeTemp.get(j).getGeo()).draggable(false).title(""+reNodeTemp.get(j).getStopName()).snippet("Bến đỗ: "+reNodeTemp.get(j).getsetFleetOver()));
						endMarker.showInfoWindow();
						endMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_bus_red));
						rectLine1.add(reNodeTemp.get(j).getGeo());
						check=2;
						break;
					}else if (check == 1){
						midMarker = googleMap.addMarker(new MarkerOptions().position(
								reNodeTemp.get(j).getGeo()).draggable(false).title(""+reNodeTemp.get(j).getStopName()).snippet("Bến đỗ: "+reNodeTemp.get(j).getsetFleetOver()));
						midMarker.showInfoWindow();
						midMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.bus_pin));
						rectLine1.add(reNodeTemp.get(j).getGeo());
						markers.add(midMarker);

					}
//					if (reNodeTemp.get(j).getStopName().equals(end)) 
//					{
//						check=2;
//						break;
//					}
	
				}
			}
		}else{
			for (int j = 0; j < reNodeTemp.size(); j++) {
				if (reNodeTemp.get(j).getStopName().equals(start)){
					check=1;
					startMarker = googleMap.addMarker(new MarkerOptions().position(
							reNodeTemp.get(j).getGeo()).draggable(false).title(""+reNodeTemp.get(j).getStopName()).snippet("Bến đỗ: "+reNodeTemp.get(j).getsetFleetOver()));
					startMarker.showInfoWindow();
					startMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_bus_green));
					rectLine1.add(reNodeTemp.get(j).getGeo());
				}else if (reNodeTemp.get(j).getStopName().equals(end)){
					endMarker = googleMap.addMarker(new MarkerOptions().position(
							reNodeTemp.get(j).getGeo()).draggable(false).title(""+reNodeTemp.get(j).getStopName()).snippet("Bến đỗ: "+reNodeTemp.get(j).getsetFleetOver()));
					endMarker.showInfoWindow();
					endMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_bus_red));
					rectLine1.add(reNodeTemp.get(j).getGeo());
				}else if (check == 1){
					midMarker = googleMap.addMarker(new MarkerOptions().position(
							reNodeTemp.get(j).getGeo()).draggable(false).title(""+reNodeTemp.get(j).getStopName()).snippet("Bến đỗ: "+reNodeTemp.get(j).getsetFleetOver()));
									midMarker.showInfoWindow();
					midMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.bus_pin));
					rectLine1.add(reNodeTemp.get(j).getGeo());
					markers.add(midMarker);

				}
				if (reNodeTemp.get(j).getStopName().equals(end)) 
				{
					check=2;
					break;
				}
	
			}
			
			if (check != 2){
				for (int j = 0; j < goNodeTemp.size(); j++) {
						if (goNodeTemp.get(j).getStopName().equals(end)){
						endMarker = googleMap.addMarker(new MarkerOptions().position(
								goNodeTemp.get(j).getGeo()).draggable(false).title(""+goNodeTemp.get(j).getStopName()).snippet("Tuyến đi qua: "+goNodeTemp.get(j).getsetFleetOver()));
						endMarker.showInfoWindow();
						endMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_bus_red));
						rectLine1.add(goNodeTemp.get(j).getGeo());
					}else if (check == 1){
						midMarker = googleMap.addMarker(new MarkerOptions().position(
								goNodeTemp.get(j).getGeo()).draggable(false).title(""+goNodeTemp.get(j).getStopName()).snippet("Tuyến đi qua: "+goNodeTemp.get(j).getsetFleetOver()));
										midMarker.showInfoWindow();
						midMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.bus_pin));
						rectLine1.add(goNodeTemp.get(j).getGeo());
						markers.add(midMarker);
					}
					if (goNodeTemp.get(j).getStopName().equals(end)) 
					{
						check=2;
						break;
					}
	
				}
			}
		}
		if (order == 0){
			startMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_start));
			endMarker.remove();
		}else if (order == 99){
			endMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_end));
		}else if (order == -1){
			startMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_start));
			endMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_end));
		}else{
			endMarker.remove();
		}if (order == -1)
		googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				if(marker.isInfoWindowShown())
					marker.hideInfoWindow();
				else
					marker.showInfoWindow();
				return false;
			}
		});
		//poly3 = googleMap.addPolyline(rectLine1);
		markers.add(startMarker);
		markers.add(endMarker);
		
		if (!isOnline)
			poly3 = googleMap.addPolyline(rectLine1);
		else{
			//poly3 = googleMap.addPolyline(rectLine1);
			for(int i=0; i < rectLine1.getPoints().size();i++)
				googlePoints.add(rectLine1.getPoints().get(i));
//				if (i < rectLine1.getPoints().size()-1){
//					fromPosition = rectLine1.getPoints().get(i);
//			        toPosition = rectLine1.getPoints().get(i+1);
//					GetRouteTask getRoute = new GetRouteTask();
//			        getRoute.execute();
//				}
			GetRouteTask getRoute = new GetRouteTask();
	        getRoute.execute();
		}
			
		
	}
	List<LatLng> googlePoints = new ArrayList<LatLng>();
	public ArrayList<LatLng> decodeRoutingPoly(String a) {
		String[] temp = a.split(" ");
		ArrayList<LatLng> result = new ArrayList<LatLng>();
		for (int i = 0; i < temp.length; i++) {
			String[] latlng = temp[i].split(",");
			double latitude = Double.parseDouble(latlng[0]);
			double longitude = Double.parseDouble(latlng[1]);
			result.add(new LatLng(latitude, longitude));
		}
		return result;
	}
	
	///////////////////////////////
    GMapV2GetRouteDirection v2GetRouteDirection;
    LatLng fromPosition;
    LatLng toPosition;
    Document document;

	 private class GetRouteTask extends AsyncTask<String, Void, String> {
         
         String response = "";
         ArrayList<LatLng> directionPoint = new ArrayList<LatLng>();
         PolylineOptions rectLine = new PolylineOptions().width(10).color(
                 Color.RED);
         ProgressDialog Dialog;
         @Override
         protected void onPreExecute() {
               Dialog = new ProgressDialog(fmActivity);
               Dialog.setMessage("Tải đường đi...");
               Dialog.show();
        	 
         }

         @Override
         protected String doInBackground(String... urls) {
               //Get All Route values
        	 for(int i=0; i<googlePoints.size();i++){
        		 if (i < googlePoints.size()-1){
        			 fromPosition = googlePoints.get(i);
        			 toPosition = googlePoints.get(i+1);
        			 document = v2GetRouteDirection.getDocument(fromPosition, toPosition, GMapV2GetRouteDirection.MODE_DRIVING);
                    // response = "Success";
                    // if(response.equalsIgnoreCase("Success")){
                         directionPoint.addAll(v2GetRouteDirection.getDirection(document));
                        

                         
                         // Adding route on the map

                    //     }
        		 }
        	 }

             return response;

         }

         @Override
         protected void onPostExecute(String result) {
               //googleMap.clear();
        	 for (int i1 = 0; i1 < directionPoint.size(); i1++) {
                 rectLine.add(directionPoint.get(i1));
           }
             googleMap.addPolyline(rectLine);
             Dialog.dismiss();
         }
   }

}