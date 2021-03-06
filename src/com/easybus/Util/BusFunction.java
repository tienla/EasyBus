package com.easybus.Util;

import java.util.ArrayList;
import java.util.List;

import android.util.FloatMath;

import com.easybus.gui.Object.Stop;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

//Bus function class
public class BusFunction {
	
	// prevent instantiate instances of this class
	private BusFunction() {}
	
	//Calculate distance
	public static double gps2m(float lat_a, float lng_a, float lat_b, float lng_b) {
	    float pk = (float) (180/3.14169);

	    float a1 = lat_a / pk;
	    float a2 = lng_a / pk;
	    float b1 = lat_b / pk;
	    float b2 = lng_b / pk;

	    float t1 = FloatMath.cos(a1)*FloatMath.cos(a2)*FloatMath.cos(b1)*FloatMath.cos(b2);
	    float t2 = FloatMath.cos(a1)*FloatMath.sin(a2)*FloatMath.cos(b1)*FloatMath.sin(b2);
	    float t3 = FloatMath.sin(a1)*FloatMath.sin(b1);
	    double tt = Math.acos(t1 + t2 + t3);

	    return 6366000*tt;
	}
	
	public static LatLng stringToLatLng(String string) {
		string = string.replace("(","");
    	string = string.replace(")","");
		LatLng t = new LatLng(Double.parseDouble(string.split("\\:")[0]),
								Double.parseDouble(string.split("\\:")[1]));
		return t;
	}

	//zoom the map to fit all the marker
	public static void zoomToFitMarkers(ArrayList<Marker> markers, GoogleMap map) {
		try{
		LatLngBounds.Builder b = new LatLngBounds.Builder();
		for (Marker m : markers) {
		    b.include(m.getPosition());
		}
		LatLngBounds bounds = b.build();
		CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 70);
		map.animateCamera(cu);
		}catch(Exception e){
			
		}
	}
	
	//compute distance
	public static double countDistance(double locLat, double locLon, double desLat,
			double desLon) {
		double distancePerDegree = Constances.earth * 2 * Constances.pi / 360;
		double distanceX = (desLat - locLat) * distancePerDegree;
		double distanceY = (desLon - locLon) * distancePerDegree;
		double distance = Math.sqrt(Math.pow(distanceX, 2)
				+ Math.pow(distanceY, 2));
		return distance;
	}
	//compute distance
	public static double convertToDistance(LatLng geo1, LatLng geo2) {
		double R = 6371;
		double dLat = (geo2.latitude - geo1.latitude) * Math.PI / 180;
		double dLon = (geo2.longitude - geo2.longitude) * Math.PI / 180;
		double lat1 = geo1.latitude * Math.PI / 180;
		double lat2 = geo2.latitude * Math.PI / 180;
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 -a));
		double d = R * c;
		return d;
	}

	public static boolean isEqualGeo(LatLng geo1, LatLng geo2) {
		if (geo1.latitude == geo2.latitude && geo2.longitude == geo1.longitude)
			return true;
		return false;
	}

//	public static List<Stop> textToBusStopList(String string) {
//		String delimiterChars = " ";
//		String[] words = string.split(delimiterChars);
//		List<Stop> bsList = new ArrayList<Stop>();
//		
//		for (String word : words)
//		{
//			var matches = currentListInstance.Buses.Stops.Where((bus) = bus.code.Equals(word));
//			if (matches.Count() >= 1) 
//				bsList.Add(matches.First());
//		}
//		if (bsList.Count == 0) return null;
//		return bsList;
//	}

}
