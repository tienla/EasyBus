package com.hieund.gui.NavBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hieund.R;
import com.hieund.Helper.BusHelper;
import com.hieund.Util.AStarAlgo;
import com.hieund.Util.BusFunction;
import com.hieund.Util.Constances;
import com.hieund.Util.ConvertUnsigned;
import com.hieund.gui.Activity.MainActivity;
import com.hieund.gui.Handler.FragmentCommunicator;
import com.hieund.gui.Object.Lines;
import com.hieund.gui.Object.Nodes;
import com.hieund.gui.Object.ResultObject;
import com.hieund.gui.Object.ResultSearchObject;
import com.hieund.gui.Object.Singleton;
import com.hieund.gui.Object.Stop;

public class SearchBox extends Fragment {
	private ConvertUnsigned converUnsigned = new ConvertUnsigned();
	private ArrayList<Stop> arrStop = new ArrayList<Stop>();
	private ArrayList<Stop> arrStartStop = new ArrayList<Stop>();
	private ArrayList<Stop> arrEndStop = new ArrayList<Stop>();
	private ArrayList<ArrayList<ResultObject>> totalResult = new ArrayList<ArrayList<ResultObject>>();
	private ArrayList<ResultObject> searchResultElement = new ArrayList<ResultObject>();
	private ArrayList<ResultSearchObject> myResult = new ArrayList<ResultSearchObject>();
	private ArrayList<String> resultGeo = new ArrayList<String>();
	private ArrayList<Vector<Nodes>> arrayNodeAns = new ArrayList<Vector<Nodes>>();
	private String walkStart = "";
	private String walkEnd = "";
	private boolean alterStart = true;
	AutoCompleteTextView startAuto,endAuto;
	ImageButton btnSearch;
	ImageButton btnSwap;
	ImageButton btnTrace1;
	ImageButton btnTrace2;

	ProgressDialog progressDialog;
	FragmentCommunicator comm;
	
	BusHelper helper;

	public SearchBox() {
		// Empty constructor required for fragment subclasses
		helper = BusHelper.getInstance(null);
	}
	
	public SearchBox(Context context) {
		// Empty constructor required for fragment subclasses
		helper = BusHelper.getInstance(context);
	}

	List<String> busStopName = new ArrayList<String>();
	BusHelper busHelper;
	ListView lvBusStop;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.box_search, container, false);
		busHelper = BusHelper.getInstance(getActivity());
		btnSearch = (ImageButton) rootView.findViewById(R.id.btnSearch);
		btnSwap = (ImageButton) rootView.findViewById(R.id.btnSwap);
		btnTrace1 = (ImageButton) rootView.findViewById(R.id.btnTrace1);
		btnTrace2 = (ImageButton) rootView.findViewById(R.id.btnTrace2);
		lvBusStop = (ListView) rootView.findViewById(R.id.lvItem);
		Cursor c = busHelper.getBusStopName();
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
			busStopName.add(c.getString(0));
		//lvBusStop.setAdapter(new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_list_item_1, busStopName));
		
		
		startAuto = (AutoCompleteTextView) rootView.findViewById(R.id.txtStart);
		startAuto.setThreshold(1);//will start working from first character  
		startAuto.setAdapter(new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_list_item_1, busStopName));//setting the adapter data into the AutoCompleteTextView  
		endAuto = (AutoCompleteTextView) rootView.findViewById(R.id.txtEnd);
		endAuto.setThreshold(1);//will start working from first character  
		endAuto.setAdapter(new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_list_item_1, busStopName));//setting the adapter data into the AutoCompleteTextView  
//		Log.d("Searchbox","");

		btnSearch.setOnClickListener(searchClick);
		btnSwap.setOnClickListener(swapClick);

		btnTrace1.setOnClickListener(traceClick1);
		btnTrace2.setOnClickListener(traceClick2);

		// startAuto.requestFocus();
		endAuto.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					btnSearch.performClick();
				}
				return false;
			}
		});
        positionMarker = MainActivity.googleMap.addMarker(new MarkerOptions().position(MainActivity.googleMap.getCameraPosition().target));     
        positionMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.position_marker));
        positionMarker.setVisible(false);
		MainActivity.googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng arg0) {
                if (isTracing1 || isTracing2) {
                    Toast.makeText(getActivity().getApplicationContext(), arg0.latitude + " - " + arg0.longitude, Toast.LENGTH_SHORT).show();
                    
                    btnTrace1.setImageResource(R.drawable.trace_icon);
                    btnTrace2.setImageResource(R.drawable.trace_icon);
                    positionMarker.setPosition(arg0);
                    if (isTracing1)
                    	startAuto.setText("("+arg0.latitude + ":" + arg0.longitude+")");
                    else
                    	endAuto.setText("("+arg0.latitude + ":" + arg0.longitude+")");
                    isTracing1 = false;
                    isTracing2 = false;
                }
            }
        });

		comm = (FragmentCommunicator) getActivity();
		new MyAsyncTask().execute();
		return rootView;
	}
	Marker positionMarker;
	private OnClickListener traceClick1 = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ImageButton ghost = (ImageButton) v;
			if (!isTracing2)
	            if(!isTracing1) {
	                isTracing1 = true;                	
	                ghost.setImageResource(R.drawable.trace_clicked_icon);
	                positionMarker.setVisible(true);
	            } else {
	                isTracing1 = false;
	                ghost.setImageResource(R.drawable.trace_icon);
	                positionMarker.setVisible(false);
	            }
		}
	};
	private OnClickListener traceClick2 = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ImageButton ghost = (ImageButton) v;
			if (!isTracing1)
	            if(!isTracing2) {
	                isTracing2 = true;                	
	                ghost.setImageResource(R.drawable.trace_clicked_icon);
	                positionMarker.setVisible(true);
	            } else {
	                isTracing2 = false;
	                ghost.setImageResource(R.drawable.trace_icon);
	                positionMarker.setVisible(false);
	            }
		}
	};

	private boolean isTracing1 = false;
	private boolean isTracing2 = false;
	private ProgressDialog pd;
	class MyAsyncTask extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
//			Toast.makeText(getActivity(), Constances.DONE_LOADING,
//					Toast.LENGTH_SHORT).show();
			pd.dismiss();
		}

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(getActivity(), null, "Đang khởi tạo dữ liệu, bạn vui lòng chờ nhé!",true);
//			Toast.makeText(getActivity(), Constances.LOADING,
//					Toast.LENGTH_SHORT).show();
		}

		@Override
		protected Void doInBackground(String... params) {
			if (Singleton.getInstance(getActivity()).arrayStop == null){
				Singleton.getInstance(getActivity()).makeArrayStop();
				arrStop = Singleton.getInstance(getActivity()).arrayStop;
			}else{
				arrStop = Singleton.getInstance(getActivity()).arrayStop;
			}
			return null;
		}
	}
	
	
	// CUSTOM FUNCTIONS
	private ArrayList<ResultObject> convertToResultObject(Vector<Nodes> arrNodes) {
		ArrayList<ResultObject> result = new ArrayList<ResultObject>();
		Nodes startNode;
		startNode = arrNodes.get(0);
		int foundSize = arrNodes.size();
		StringBuilder temp = new StringBuilder();
		for (int k = 0; k < foundSize; k++) {
			Nodes current = arrNodes.get(k);
			temp.append(String.valueOf(current.getGeo().latitude) + ","
					+ String.valueOf(current.getGeo().longitude) + " ");
			if (Lines.lineEqual(current.getLine(), startNode.getLine())
					&& k < foundSize - 1) {
				continue;
			} else if (Lines.lineEqual(current.getLine(), startNode.getLine())
					&& k == foundSize - 1) {
				ResultObject ro = new ResultObject(startNode.getStopName(),
						arrNodes.get(k).getStopName(), startNode.getLine());
				result.add(ro);
			}

			else {
				ResultObject ro = new ResultObject(startNode.getStopName(),
						arrNodes.get(k).getStopName(), startNode.getLine());
				result.add(ro);
				startNode = current;
			}
		}
		resultGeo.add(temp.toString());
		return result;
	}

	@SuppressLint("DefaultLocale")
	private ArrayList<Stop> searchApproStop(String name) {
		ArrayList<Stop> result = new ArrayList<Stop>();

		String lower = converUnsigned.ConvertString(name.toLowerCase());
		for (int i = 0; i < arrStop.size(); i++) {
			Stop a = arrStop.get(i);
			String currName = converUnsigned.ConvertString(a.getName()
					.toLowerCase());
			if (currName.contains(lower)) {
				result.add(a);
			}
		}
		return result;
	}

	public boolean isEqualArrayLine(ArrayList<ResultObject> a,
			ArrayList<ResultObject> b) {
		ArrayList<Lines> aLine = getArrayLine(a);
		ArrayList<Lines> bLine = getArrayLine(b);
		boolean result = true;
		Log.d("ab", "asize: " + aLine.size() + ", " + bLine.size());
		if (aLine.size() != b.size())
			result = false;
		else {
			for (int count = 0; count < aLine.size(); count++) {
				if (!(aLine.get(count).getCode().equals(bLine.get(count).getCode()))) {
					result = false;
					break;
				}

			}
		}
		return result;
	}

	public ArrayList<Lines> getArrayLine(ArrayList<ResultObject> a) {
		ArrayList<Lines> result = new ArrayList<Lines>();
		for (int count = 0; count < a.size(); count++) {
			result.add(a.get(count).getLine());
		}
		return result;
	}

	private OnClickListener swapClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String temp = startAuto.getText().toString();
			startAuto.setText(endAuto.getText().toString());
			endAuto.setText(temp);
		}
	};
	private OnClickListener searchClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			MainActivity.hideSoftKeyboard(getActivity());
			myResult.clear();
			resultGeo.clear();
			totalResult.clear();
			String a = findBusStop(startAuto.getText().toString());
			if (!a.equals(startAuto.getText().toString()))
				walkStart = startAuto.getText().toString();
			else
				walkStart = "";
			String b = findBusStop(endAuto.getText().toString());
			if (!b.equals(endAuto.getText().toString()))
				walkEnd = endAuto.getText().toString();
			else
				walkEnd = "";
			String startPoint = a, endPoint = b;
			if (a.contains(", Hà Nội")) {
				startPoint = (a.split(", Hà Nội"))[0];
			}
			if (a.contains(", Hà Noi")) {
				startPoint = (a.split(", Hà Noi"))[0];
			}
			if (b.contains(", Hà Nội")) {
				endPoint = (b.split(", Hà Nội"))[0];
			}
			if (b.contains(", Hà Noi")) {
				endPoint = (b.split(", Hà Noi"))[0];
			}
			Log.d("startPoint", startPoint);
			Log.d("endPoint", endPoint);
			if (startPoint.equals(endPoint)) {
				Toast.makeText(getActivity(), Constances.START_EQUAL_END,
						Toast.LENGTH_SHORT).show();
			} else if (startPoint.equals("") || endPoint.equals("")) {
				Toast.makeText(getActivity(), Constances.EMPTY_FIELD,
						Toast.LENGTH_SHORT).show();
			} else {
				myResult.clear();
				progressDialog = ProgressDialog.show(getActivity(), "",
						Constances.LOADING, true);
				Log.d("progress", String.valueOf(progressDialog.isShowing()));
				arrStartStop = searchApproStop(startPoint);
				arrEndStop = searchApproStop(endPoint);

				if (arrStartStop.size() == 0) {
					AssetManager assetManager = getActivity().getAssets();
					InputStream fis;
					BufferedReader reader;
					try {
						fis = assetManager.open("nearby.txt");
						reader = new BufferedReader(new InputStreamReader(fis));
						String line = null;
						while ((line = reader.readLine()) != null) {
							if (line.contains(startPoint)) {
								String newPoint = (line.split(":"))[0];
								arrStartStop = searchApproStop(newPoint);
								break;
							}

						}
					} catch (IOException e) {
						e.printStackTrace();

					}

				} else if (arrEndStop.size() == 0) {
					AssetManager assetManager = getActivity().getAssets();
					InputStream fis;
					BufferedReader reader;
					try {
						fis = assetManager.open("nearby.txt");
						reader = new BufferedReader(new InputStreamReader(fis));
						String line = null;
						while ((line = reader.readLine()) != null) {
							if (line.contains(endPoint)) {
								String newPoint = (line.split(":"))[0];
								arrEndStop = searchApproStop(newPoint);
								break;
							}

						}
					} catch (IOException e) {
						e.printStackTrace();
					}

				}

				if (arrStartStop.size() > 0 && arrEndStop.size() > 0) {
					(new FindingNameTask()).execute();
				} else if (arrEndStop.size() == 0) {
					progressDialog.dismiss();
					Toast.makeText(getActivity(),
							Constances.NODATA + " " + Constances.STARTPOINT,
							Toast.LENGTH_SHORT).show();
				} else {
					progressDialog.dismiss();
					Toast.makeText(getActivity(),
							Constances.NODATA + " " + Constances.STARTPOINT,
							Toast.LENGTH_SHORT).show();
				}

			}
		}

		private String findBusStop(String string) {
			Cursor c = busHelper.getBusStop();
			String nearestBusStop = "";
			double nearestDistance = 99999;
			if (string.contains("(") && string.contains(":")){
				if(c.getCount()>0)
				{
				    for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
				    {
				    	string = string.replace("(","");
				    	string = string.replace(")","");
						double distance = BusFunction.countDistance(Double.parseDouble(c.getString(4).split("\\|")[1]),
								Double.parseDouble(c.getString(4).split("\\|")[0]),Double.parseDouble(string.split("\\:")[0]),
								Double.parseDouble(string.split("\\:")[1]));

				    	if (Double.compare(nearestDistance, distance) >= 0){
				    		nearestBusStop = c.getString(2);
				    		nearestDistance = distance;
				    	}
				    }
				}
				return nearestBusStop;
			}else
				return string;
			
		}
	};

//	private OnClickListener traceClick = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			ImageButton ghost = (ImageButton) v;
//			ghost.setImageResource(R.drawable.trace_clicked_icon);
//		}
//	};

	// CUSTOM CLASSES

	class FindingNameTask extends AsyncTask<Void, Void, Void> {
		int result_count = 0;
		final int MAX_PER_RESULT = 3;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			arrayNodeAns.clear();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			int n1 = arrStartStop.size();
			int n2 = arrEndStop.size();
			Log.d("arrStartStrop",arrStartStop.get(0).getName());
			Log.d("arrEndStop",arrEndStop.get(0).getName());
			Stop startStop = arrStartStop.get(0);
			processAlg(startStop,0);
//			for (int i = 0; i < n1 && i <= MAX_PER_RESULT; i++) {
//				Stop startStop = arrStartStop.get(i);
//				for (int j = 0; j < n2; j++) {
//					if (processAlg(startStop, j) == false)
//					{	
//						break;
//					}
//				}
//			}
			return null;
		}

		private boolean processAlg(Stop startStop, int j) {
			AStarAlgo AS1 = new AStarAlgo(arrStop, startStop, arrEndStop.get(j));
			AS1.algorithm();
			Vector<Nodes> foundAnswer = AS1.answer;
			for (Nodes s : foundAnswer){
				Log.d("formation",s.getLine().getCode()+"|"+s.getStopName());
			}
			if (foundAnswer != null && foundAnswer.size() > 0) {
				result_count++;
				arrayNodeAns.add(foundAnswer);
				searchResultElement = convertToResultObject(foundAnswer);
				
				if (totalResult.size() != 0) {
					Log.d("count", "total: " + totalResult.size()
							+ ", search: " + searchResultElement.size());
					if (!isEqualArrayLine(
							totalResult.get(totalResult.size() - 1),
							searchResultElement)) {
						ArrayList<ResultObject> newObject = new ArrayList<ResultObject>();

						ResultSearchObject rso = null;
						if (makeResultObject(newObject)!=null)
							rso = makeResultObject(newObject);

						if (newObject.size() < totalResult.get(0).size()) {
							if (rso != null){
								totalResult.add(0, newObject);
								myResult.add(0, rso);
							}
						} else {
							if (rso != null){
								totalResult.add(newObject);
								myResult.add(rso);
							}
						}
						searchResultElement.clear();
					}
				} else {
					ArrayList<ResultObject> newObject = new ArrayList<ResultObject>();
					ResultSearchObject rso = null;
					if (makeResultObject(newObject)!=null)
						rso = makeResultObject(newObject);

					if (rso!= null)
						myResult.add(rso);
					
					searchResultElement.clear();
				}
				if (result_count >= MAX_PER_RESULT) {
					result_count = 0;
					return false;
				}
			}
			return true;
		}

		private ResultSearchObject makeResultObject(
				ArrayList<ResultObject> newObject) {
			for (int hk = 0; hk < searchResultElement.size(); hk++) {
				newObject.add(searchResultElement.get(hk));
			}
			StringBuffer detail = new StringBuffer();
//			String str_busIds = "Tuyến bus: ";
			ArrayList<String> str_busIds = new ArrayList<String>();
			String nodeInfo = "";
			if (!walkStart.equals(""))
				nodeInfo += "W" +";"+walkStart+";"+searchResultElement.get(0).getOrigin()+"|";
			Log.d("searchResult", String.valueOf(searchResultElement.size()));
			boolean check = true;
			String alterBus = null;
			for (int index = 0; index < searchResultElement.size(); index++) {
				ResultObject ro = searchResultElement.get(index);
				

				if (index == 0){
					str_busIds.add(Constances.MOVETO);
					detail.append(Constances.MOVETO);
				}
				if (!ro.getLine().getCode().equals(str_busIds.get(str_busIds.size()-1)))
				{	
					detail.append("\n" + Constances.FROM);
					str_busIds.add( ""+ro.getLine().getCode() );
					alterBus = checkRoute(""+ro.getLine().getCode(),ro.getOrigin(),ro.getDestination());
					
					Log.d("Bus",ro.getLine().getCode()+"|"+ro.getOrigin()+"|"+ro.getDestination()+"|"+check);
//					if (check==null)
//						break;
					
					if (alterBus != null){
						if (alterBus.contains("+")){
							String start = alterBus.split("\\+")[0];
							String end = alterBus.split("\\+")[1];
							nodeInfo += "W" +";"+ro.getOrigin()+";"+start+"|";
							nodeInfo += ro.getLine().getCode() +";"+start+";"+end+"|";
							nodeInfo += "W" +";"+end+";"+ro.getDestination()+"|";
							detail.append(ro.getOrigin() + 
									Constances.MOVENEXT + start + Constances.TAKEBUS
									+ ro.getLine().getCode() + "("
									+ ro.getLine().getBus_name() + Constances.TO
									+ end + Constances.MOVENEXT 
									+ ro.getDestination() + "\n");
							break;
						}
						if (alterStart){
							nodeInfo += "W" +";"+ro.getOrigin()+";"+alterBus+"|";
							nodeInfo += ro.getLine().getCode() +";"+alterBus+";"+ro.getDestination()+"|";
							detail.append(ro.getOrigin() + 
									Constances.MOVENEXT + alterBus + Constances.TAKEBUS
									+ ro.getLine().getCode() + "("
									+ ro.getLine().getBus_name() + Constances.TO
									+ ro.getDestination() + "\n");
						}else{
							nodeInfo += ro.getLine().getCode() +";"+ro.getOrigin()+";"+alterBus+"|";
							nodeInfo += "W" +";"+alterBus+";"+ro.getDestination()+"|";
							detail.append(ro.getOrigin() + Constances.TAKEBUS
									+ ro.getLine().getCode() + "("
									+ ro.getLine().getBus_name() + Constances.TO
									+ alterBus + Constances.MOVENEXT 
									+ ro.getDestination() + "\n");
						}
					}else{
						nodeInfo += ro.getLine().getCode() +";"+ro.getOrigin()+";"+ro.getDestination()+"|";
						detail.append(ro.getOrigin() + Constances.TAKEBUS
								+ ro.getLine().getCode() + "("
								+ ro.getLine().getBus_name() + Constances.TO
								+ ro.getDestination() + "\n");
					}
				}else{
					check=false;
					break;
				}

			}
			if (check){
				String startEnd = "Từ: " + searchResultElement.get(0).getOrigin()
						+ "\nĐến: "
						+ searchResultElement.get(searchResultElement.size() - 1)
								.getDestination();
				if (!walkEnd.equals(""))
					nodeInfo += "W" +";"+walkEnd+";"+searchResultElement.get(searchResultElement.size() - 1)
							.getDestination()+"|";
				ResultSearchObject rso = new ResultSearchObject(startEnd, detail.toString());
				rso.setBusIds(str_busIds);
				rso.setNodeInfo(nodeInfo);
				Log.d("Bus","1");
				return rso;
			}else{
				Log.d("Bus","0");
				return null;
			}
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (myResult.size() == 0) {
				progressDialog.dismiss();
				Toast.makeText(getActivity(), Constances.NOANSWER,
						Toast.LENGTH_SHORT).show();
			}

			else {
				progressDialog.dismiss();
				Log.d("Thong tin", "myResult size: " + myResult.size());

				comm.passData(myResult, resultGeo, arrayNodeAns);
			}
		}
		private String checkRoute(String busId, String start, String end){
			alterStart = true;
			Cursor c = helper.getGeo(busId);
			c.moveToNext();
			String id = c.getString(2);
			ArrayList<Nodes> goNodeTemp = helper.getNodeInfo(id, "go");
			ArrayList<Nodes> reNodeTemp = helper.getNodeInfo(id, "re");
			boolean isGo = false;
			int startID = 0;

			for (int j = 0; j < goNodeTemp.size(); j++) {
				if (goNodeTemp.get(j).getStopName().equals(start)){
					isGo = true;
					startID = j;
					break;
				}

			}
			int check = 0;
			if(isGo){
				for (int j = 0; j < goNodeTemp.size(); j++) {
					if (goNodeTemp.get(j).getStopName().equals(start))
						check=1;
					if (check == 1){
						if (goNodeTemp.get(j).getStopName().equals(end))
							return null;
					}
				}
			}else{
				for (int j = 0; j < reNodeTemp.size(); j++) {
					if (reNodeTemp.get(j).getStopName().equals(start))
						check=1;
					if (check == 1){
						if (reNodeTemp.get(j).getStopName().equals(end))
							return null;
					}
				}
			}
			
			//Find alter start
			String alterBusStop = "";
			double alterBusDistance = 9999;
			double distanceWalk = 0;
			int endID = 0;
			int alterEndID = 0;
			int alterStartID = 0;
			if (isGo){
				for (int j = 0; j < reNodeTemp.size(); j++) {
					double distance = BusFunction.countDistance(goNodeTemp.get(startID).getGeo().latitude,
							goNodeTemp.get(startID).getGeo().longitude,
									reNodeTemp.get(j).getGeo().latitude,
									reNodeTemp.get(j).getGeo().longitude);
			    	if (Double.compare(alterBusDistance, distance) >= 0){
			    		alterBusStop = reNodeTemp.get(j).getStopName();
			    		alterStartID = j;
			    		alterBusDistance = distance;
			    	}
				}
				distanceWalk = BusFunction.countDistance(goNodeTemp.get(startID).getGeo().latitude,
						goNodeTemp.get(startID).getGeo().longitude,
						reNodeTemp.get(alterStartID).getGeo().latitude,
						reNodeTemp.get(alterStartID).getGeo().longitude);
			}else{
				for (int j = 0; j < reNodeTemp.size(); j++) {
					if (reNodeTemp.get(j).getStopName().equals(start)){
						startID = j;
						break;
					}

				}
				for (int j = 0; j < goNodeTemp.size(); j++) {
					double distance = BusFunction.countDistance(reNodeTemp.get(startID).getGeo().latitude,
							reNodeTemp.get(startID).getGeo().longitude,
							goNodeTemp.get(j).getGeo().latitude,
							goNodeTemp.get(j).getGeo().longitude);
			    	if (Double.compare(alterBusDistance, distance) >= 0){
			    		alterBusStop = goNodeTemp.get(j).getStopName();
			    		alterStartID = j;
			    		alterBusDistance = distance;
			    	}
				}
				distanceWalk = BusFunction.countDistance(reNodeTemp.get(startID).getGeo().latitude,
						reNodeTemp.get(startID).getGeo().longitude,
						goNodeTemp.get(alterStartID).getGeo().latitude,
						goNodeTemp.get(alterStartID).getGeo().longitude);
			}
			Log.d("alterStart",alterBusStop);
			//Check alter start
			check = 0;
			if(!isGo){
				for (int j = 0; j < goNodeTemp.size(); j++) {
					if (goNodeTemp.get(j).getStopName().equals(alterBusStop))
						check+=1;
					if (check == 1){
						if (goNodeTemp.get(j).getStopName().equals(end))
							return alterBusStop;
					}
					if (goNodeTemp.get(j).getStopName().equals(end))
						check+=1;
				}
			}else{
				for (int j = 0; j < reNodeTemp.size(); j++) {
					if (reNodeTemp.get(j).getStopName().equals(alterBusStop))
						check+=1;
					Log.d("stop",reNodeTemp.get(j).getStopName() );
					if (check == 1){
						if (reNodeTemp.get(j).getStopName().equals(end))
							return alterBusStop;
					}
					if (reNodeTemp.get(j).getStopName().equals(end))
						check+=1;
				}
			}
			// Check alter end
			Double distanceWalkEnd;
			alterBusDistance = 9999;
			endID = 0;
			String alterBusStopEnd = "";
			isGo = false;
			for (int i = 0; i < goNodeTemp.size(); i++) {
				if (goNodeTemp.get(i).getStopName().equals(end)){
					isGo = true;
					endID = i;
					break;
				}
			}
			if (isGo){
				for (int j = 0; j < reNodeTemp.size(); j++) {
					double distance = BusFunction.countDistance(goNodeTemp.get(endID).getGeo().latitude,
							goNodeTemp.get(endID).getGeo().longitude,
									reNodeTemp.get(j).getGeo().latitude,
									reNodeTemp.get(j).getGeo().longitude);
			    	if (Double.compare(alterBusDistance, distance) >= 0){
			    		alterBusStopEnd = reNodeTemp.get(j).getStopName();
			    		alterEndID = j;
			    		alterBusDistance = distance;
			    	}
				}
				distanceWalkEnd = BusFunction.countDistance(goNodeTemp.get(endID).getGeo().latitude,
						goNodeTemp.get(endID).getGeo().longitude,
						reNodeTemp.get(alterEndID).getGeo().latitude,
						reNodeTemp.get(alterEndID).getGeo().longitude);
			}else{
				for (int i = 0; i < reNodeTemp.size(); i++) {
					if (reNodeTemp.get(i).getStopName().equals(end)){
						endID = i;
						break;
					}
				}
				for (int j = 0; j < goNodeTemp.size(); j++) {
					double distance = BusFunction.countDistance(reNodeTemp.get(endID).getGeo().latitude,
							reNodeTemp.get(endID).getGeo().longitude,
							goNodeTemp.get(j).getGeo().latitude,
							goNodeTemp.get(j).getGeo().longitude);
			    	if (Double.compare(alterBusDistance, distance) >= 0){
			    		alterBusStopEnd = goNodeTemp.get(j).getStopName();
			    		alterEndID = j;
			    		alterBusDistance = distance;
			    	}
				}
				distanceWalkEnd = BusFunction.countDistance(reNodeTemp.get(endID).getGeo().latitude,
						reNodeTemp.get(endID).getGeo().longitude,
						goNodeTemp.get(alterEndID).getGeo().latitude,
						goNodeTemp.get(alterEndID).getGeo().longitude);
			}
			
//			if (Double.compare(distanceWalk, distanceWalkEnd)<0){
//				return alterBusStop;
//			}else{
			if (check==1)
				return alterBusStop+"+"+alterBusStopEnd;
			else{
				Log.d ("AlterEnd", alterBusStopEnd);
				alterStart = false;
				Log.d("distance", ""+distanceWalkEnd);
//				if (Double.compare(distanceWalkEnd, 0.2)>0)
//					return "no";
//				else
					return alterBusStopEnd;
			}
			//}
		}

	}
}
