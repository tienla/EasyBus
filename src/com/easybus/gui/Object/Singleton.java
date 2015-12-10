package com.easybus.gui.Object;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.easybus.Helper.BusHelper;

public class Singleton {

	public static ArrayList<Stop> arrayStop;
	private BusHelper busHelper;
	private static Singleton instance = null;

	public Singleton(Context context) {
		busHelper = BusHelper.getInstance(context);
		Cursor model = busHelper.getAll();
		//if (arrayStop != null) arrayStop.clear();
//		if (model.getCount() == 0){
//			Log.d("check1","2");
//			makeDatabase(context);
//		}
		//makeArrayStop();
	}

	public static Singleton getInstance(Context context) {
		if (instance == null) {
			instance = new Singleton(context);
		}
		
		return instance;
	}
	
//	public void initBusNode(){
//		List<GoThrough> _ogt = busHelper.findAllGoThrough();
//		Cursor c = busHelper.getAllLines();
//
//		for (int i = 1; i <= c.getCount(); i++) {
//		//foreach (BusLine bi in currentHomeVM.Buses.Items)
//		{
//			c.moveToPosition(i-1);
//			Lines bi  = new Lines(c.getString(0),c.getString(1),c.getString(2),c.getString(3),
//					c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8),c.getString(9),c.getString(10),c.getString(11));
//			List<GoThrough> gth = _ogt.Where(s => s.busLineCode == bi.code).ToList();
//			for(GoThrough gt__ : gth)
//			{
//				List<Stop> __bs = MyUtil.textToBusStopList(gt__.busStopCode.ToString());
//				BusNode bn = new BusNode(bi.code, null, __bs.First(), null);
//				__bs.First().arrayNode.Add(bn);
//
//				if (gt__.direction == false)
//					bi.goNode.Add(bn);
//				else
//					bi.returnNode.Add(bn);
//			}
//		}
//		
//
//		foreach (BusLine bi in currentHomeVM.Buses.Items)
//		{
//			for (int i = 0; i < bi.goNode.Count; i++)
//			{
//				bi.goNode[i].busItem = bi;
//				if (i < bi.goNode.Count - 1)
//				{
//					bi.goNode[i].nextNode = bi.goNode[i + 1];
//				}
//			}
//			for (int i = 0; i < bi.returnNode.Count; i++)
//			{
//				bi.returnNode[i].busItem = bi;
//				if (i < bi.returnNode.Count - 1)
//				{
//					bi.returnNode[i].nextNode = bi.returnNode[i + 1];
//				}
//			}
//		}
//	}
	
	public void makeArrayStop() {
		this.arrayStop = new ArrayList<Stop>();
		Cursor c = busHelper.getAll();

		for (int i = 1; i <= c.getCount(); i++) {
			// lay node tu csdl
			c.moveToPosition(i-1);

			// tach thong tin luot di
			Lines busLine1 = new Lines(busHelper.getCode(c),
					busHelper.getName(c), 0, busHelper.getCost(c),
					busHelper.getFrequency(c), busHelper.getFrequency(c));

//			Cursor cNode = busHelper.getNodes(i);
//			cNode.moveToFirst();
//			if(busHelper.getGoNode(cNode).contains("{")){
			ArrayList<Nodes> arrayNodes = new ArrayList<Nodes>();
			arrayNodes = busHelper.getNodeInfo(c.getString(2),"go");
			for (Nodes s : arrayNodes)
				s.setLine(busLine1);
			/////////////////////////////////////////////////////////
//			JSONArray array;
//			try {
//				array = new JSONArray(busHelper.getGoNode(cNode));
//	    	for(int i1=0; i1<array.length();i1++){
//	    		JSONObject obj = array.getJSONObject(i1);
//	    		//Log.d("idNode",""+array.length());
//				String stopName = (String)obj.get("Name");
//				String fleetOver = (String)obj.get("FleetOver");
//				Double latitude = obj.getJSONObject("Geo").getDouble("Lat");
//				Double longitude = obj.getJSONObject("Geo").getDouble("Lng");
//				Nodes node = new Nodes(busLine1, stopName);
//				node.setFleetOver(fleetOver);
//				node.setGeo(new LatLng(latitude, longitude));
//				if (i1 > 0) {
//					arrayNodes.get(arrayNodes.size() - 1).setNextNode(node);
//				}
//				if (i1 == array.length() - 1) {
//					node.setNextNode(null);
//				}
//				//Log.d("node", node.getLine().getBus_id() + "|" + node.getsetFleetOver());
//				arrayNodes.add(node);
//    		}
	    	
			/////////////////////////////////////////////////////////
//			String[] seperatedStation1 = busHelper.getGoNode(cNode).split("\"ObjectID\"");
//			// System.out.println(seperatedStation1[0]);
//			for (int j = 1; j < seperatedStation1.length; j++) {
//				// Lay thong tin toa do va ten cua node
//				
//				String[] subSeparatedStation1 = seperatedStation1[j]
//						.split(",\"Geo\":");
//
//				String nameOfStation = (((subSeparatedStation1[0].split("Name"))[1]
//						.split("\",\""))[0].split("\":\""))[1];
//
//				Nodes newNode = new Nodes(busLine1, nameOfStation);
//				if (j > 1) {
//					arrayNodes.get(arrayNodes.size() - 1).setNextNode(newNode);
//				}
//				if (j == seperatedStation1.length - 1) {
//					newNode.setNextNode(null);
//				}
//				arrayNodes.add(newNode);
//			}

			for (int j = 0; j < arrayNodes.size(); j++) {
				Stop index = searchArrayStop(arrayNodes.get(j).getStopName());
				if (index == null) {
					Stop newStop = new Stop(arrayNodes.get(j).getStopName(),
							arrayNodes.get(j).getGeo(), arrayNodes.get(j).getsetFleetOver());
					arrayNodes.get(j).setGeo(arrayNodes.get(j).getGeo());
					newStop.getArrayNode().add(arrayNodes.get(j));
					this.arrayStop.add(newStop);
				} else {
					for (int i1 = 0; i1 < arrayStop.size(); i1++) {
						if (arrayStop.get(i1).getName().equals(index.getName())) {
							arrayStop.get(i1).getArrayNode().add(arrayNodes.get(j));
						}
					}
//					arrayNodes.get(j).setGeo(index.getGeo());
//					index.getArrayNode().add(arrayNodes.get(j));
//					arrayStop.add(index);
				}
			}
			
			// tach thong tin luot ve
			arrayNodes.clear();
			Lines busLine2 = new Lines(busHelper.getCode(c),
					busHelper.getName(c), 1, busHelper.getCost(c),
					busHelper.getFrequency(c), busHelper.getFrequency(c));
			arrayNodes = busHelper.getNodeInfo(c.getString(0), "re");
			for (Nodes s : arrayNodes)
				s.setLine(busLine1);
//			array = new JSONArray(busHelper.getReNode(cNode));
//	    	for(int i1=0; i1<array.length();i1++){
//	    		JSONObject obj = array.getJSONObject(i1);
////	    		Log.d("idNode",""+array.length());
//				String stopName = (String)obj.get("Name");
//				String fleetOver = (String)obj.get("FleetOver");
//				Double latitude = obj.getJSONObject("Geo").getDouble("Lat");
//				Double longitude = obj.getJSONObject("Geo").getDouble("Lng");
//				Nodes node = new Nodes(busLine2, stopName);
//				node.setFleetOver(fleetOver);
//				node.setGeo(new LatLng(latitude, longitude));
//				if (i1 > 0) {
//					arrayNodes.get(arrayNodes.size() - 1).setNextNode(node);
//				}
//				if (i1 == array.length() - 1) {
//					node.setNextNode(null);
//				}
//				arrayNodes.add(node);
//    		}
//			String[] seperatedStation2 = busHelper.getReNode(cNode).split("\"ObjectID\"");
//			
//			for (int j = 1; j < seperatedStation2.length; j++) {
//				String[] subSeparatedStation2 = seperatedStation2[j]
//						.split(",\"Geo\":");
//
//				String nameOfStation = (((subSeparatedStation2[0].split("Name"))[1]
//						.split("\",\""))[0].split("\":\""))[1];
//				Nodes newNode = new Nodes(busLine2, nameOfStation);
//				if (j > 1) {
//					arrayNodes.get(arrayNodes.size() - 1).setNextNode(newNode);
//				}
//				if (j == seperatedStation2.length - 1) {
//					newNode.setNextNode(null);
//				}
//				arrayNodes.add(newNode);
//
//			}
			for (int j = 0; j < arrayNodes.size(); j++) {
				Stop index = searchArrayStop(arrayNodes.get(j).getStopName());
				if (index == null) {
					Stop newStop = new Stop(arrayNodes.get(j).getStopName(),
							arrayNodes.get(j).getGeo(), arrayNodes.get(j).getsetFleetOver());
					newStop.setGeo(arrayNodes.get(j).getGeo());
					newStop.getArrayNode().add(arrayNodes.get(j));
					arrayStop.add(newStop);
				} else {
					//System.out.println(index.getName());
					//arrayNodes.get(j).setGeo(index.getGeo());
					for (int i1 = 0; i1 < arrayStop.size(); i1++) {
						if (arrayStop.get(i1).getName().equals(index.getName())) {
							arrayStop.get(i1).getArrayNode().add(arrayNodes.get(j));
						}
					}
					//.getArrayNode().add(arrayNodes.get(j));
					//arrayStop.add(index);
				}
			}
		}
		Log.d("ArrayStop",""+arrayStop.size());
	//}
	}

	public Stop searchArrayStop(String name) {
		for (int i = 0; i < arrayStop.size(); i++) {
			if (this.arrayStop.get(i).getName().equals(name)) {
				return this.arrayStop.get(i);
			}
		}
		return null;
	}

	public ArrayList<Stop> searchApproStop(String name) {
		ArrayList<Stop> result = new ArrayList<Stop>();
		for (int i = 0; i < arrayStop.size(); i++) {
			Stop a = arrayStop.get(i);
			if (a.getName().contains(name)) {
				result.add(a);
			}
		}
		return result;
	}

	public String removeBlockCursor(String a) {
		String result = a.split("\"")[1];
		return result;
	}

}
