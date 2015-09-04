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

package com.hieund.gui.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hieund.R;
import com.hieund.Util.Constances;
import com.hieund.gui.Handler.FragmentCommunicator;
import com.hieund.gui.Handler.MapHandle;
import com.hieund.gui.Handler.PageSlidingTabStripFragment;
import com.hieund.gui.NavBox.SearchBox;
import com.hieund.gui.Object.Nodes;
import com.hieund.gui.Object.ResultSearchObject;

import java.util.ArrayList;
import java.util.Vector;

public class MainActivity extends SherlockFragmentActivity implements
		LocationListener, FragmentCommunicator {
	enum MenuEnum {
		THONGTINBUS, TIMDUONG, GANBAN, CAIDAT, TACGIA, THOAT
	};

	DrawerLayout mDrawerLayout;
	ListView mDrawerList;
	ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPlanetTitles;

	MenuEnum curItem = null;
	private static RelativeLayout navBox;
	private static RelativeLayout contentView;
	private static boolean isMapOn;
	private static boolean navMode;
	public static GoogleMap googleMap;
	private LocationManager locationManager;

	private Button goButton, reButton;

	LatLng currCoor;
	Marker currentmarker;

	public static com.actionbarsherlock.view.MenuItem item;

	public static String PACKAGE_NAME;

	boolean itemVisibility = false;
	MHandler handler;

	private OnInfoWindowClickListener onInfoWindowClickListener = new OnInfoWindowClickListener() {

		@Override
		public void onInfoWindowClick(Marker arg0) {
			// TODO Auto-generated method stub
			Bundle args = new Bundle();
			String address = arg0.getTitle();
			args.putString("address", address);

			SherlockFragment fragment = new FleetOverFragment();
			fragment.setArguments(args);

			getSupportFragmentManager().beginTransaction()
					.add(R.id.content, fragment).addToBackStack("fragBack")
					.commit();

			showContent(true);

		}

	};
	static ConnectivityManager connectivityManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		handler = new MHandler();
		PACKAGE_NAME = getApplicationContext().getPackageName();
		mTitle = mDrawerTitle = getTitle();
		mPlanetTitles = getResources().getStringArray(R.array.slidemenu_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		// map = new MapHandle();
		// get dimension
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int width = displaymetrics.widthPixels;

		LayoutParams lParams = mDrawerList.getLayoutParams();
		lParams.width = width * 2 / 3;
		mDrawerList.setLayoutParams(lParams);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mPlanetTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		isMapOn = false;
		navMode = false;

		navBox = (RelativeLayout) findViewById(R.id.navbox);
		// int height = displaymetrics.heightPixels;
		//
		// lParams = navBox.getLayoutParams();
		// lParams.height = height /4;
		// navBox.setLayoutParams(lParams);
		contentView = (RelativeLayout) findViewById(R.id.content);
		// khoi tao map
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getApplicationContext());
		if (status != ConnectionResult.SUCCESS) {
			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
					requestCode);
			dialog.show();
		}

		else {
			SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);
			goButton = (Button) findViewById(R.id.goButton);
			goButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					MapHandle map = new MapHandle();
					map.showGoPath();
				}
			});

			reButton = (Button)findViewById(R.id.reButton);
			reButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					MapHandle map = new MapHandle();
					map.showRePath();
				}
			});
			
			View mapView = fm.getView();
			// Get the button view
			View locationButton = ((View) mapView.findViewById(1).getParent())
					.findViewById(2);
			RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton
					.getLayoutParams();
			// position on left bottom
			rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
			rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
			rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
			rlp.addRule(RelativeLayout.ALIGN_LEFT, RelativeLayout.TRUE);
			rlp.setMargins(30, 0, 0, 30);

			googleMap = fm.getMap();

			googleMap.setMyLocationEnabled(true);
			googleMap.setOnInfoWindowClickListener(onInfoWindowClickListener );
			locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			String provider = locationManager.getBestProvider(criteria, true);
			Location location = locationManager.getLastKnownLocation(provider);

			if (location != null) {
				onLocationChanged(location);
			}

			if (!isNetworkAvailable()) {
				Toast.makeText(getApplicationContext(), Constances.NOINTERNET,
						Toast.LENGTH_SHORT).show();
			}

			locationManager.requestLocationUpdates(provider, 20000, 0, this);
		}

//		googleMap.setOnMapLongClickListener(mapLongListener);
//		googleMap.setOnMarkerClickListener(markerClickListener);
		if (savedInstanceState == null) {
			selectItem(MenuEnum.THONGTINBUS.ordinal());
		}

//		getSupportFragmentManager().addOnBackStackChangedListener(
//				new OnBackStackChangedListener() {
//					public void onBackStackChanged() {
//						int backCount = getSupportFragmentManager()
//								.getBackStackEntryCount();
//						if (backCount == 0) {
//							Toast.makeText(getApplicationContext(),
//									"no more stack", Toast.LENGTH_SHORT).show();
//						} else {
//							Toast.makeText(getApplicationContext(),
//									"have stack", Toast.LENGTH_SHORT).show();
//						}
//					}
//				});
//		Thread thread = new Thread() {
//		    @Override
//		    public void run() {
//		        while(true) {
//				    Singleton.getInstance(getBaseContext()).makeArrayStop();
//				}
//		    }
//		};
//		thread.run();
	}
	
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);
		item = menu.findItem(R.id.action_send_email);
		item.setVisible(itemVisibility);

//		// Associate searchable configuration with the SearchView
//	    SearchManager searchManager =
//	           (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//	    SearchView searchView =
//	            (SearchView) menu.findItem(R.id.action_search).getActionView();
//	    searchView.setSearchableInfo(
//	            searchManager.getSearchableInfo(getComponentName()));

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {

		switch (item.getItemId()) {

		case android.R.id.home: {
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
			break;
		}
		
		case R.id.action_send_email:
			if (curItem.equals(MenuEnum.THONGTINBUS)) {
				Message msg = new Message();
				msg.what = 1;
				BusDetailFragment.mHandler.sendMessage(msg);
			} else
				showNavBox(!navMode);

//		case R.id.action_plus:
//			if (curItem.equals(MenuEnum.THONGTINBUS)) {
//				Toast.makeText(getApplicationContext(), "bus search mode",
//						Toast.LENGTH_SHORT).show();
//			} else
//				showNavBox(!navMode);
	}

		return super.onOptionsItemSelected(item);
	}
	boolean exit = false;
	@Override
	public void onBackPressed() {
		
		if (isMapOn) {
			exit = false;
			if (navMode){
				showNavBox(true);
				showContent(false);
			}else{
				showNavBox(false);
				showContent(true);
			}
			
		} else {
			FragmentManager fm = getSupportFragmentManager();
			if (fm.getBackStackEntryCount() > 2)
				fm.popBackStack();
			else{
				if (!exit){
					this.setGoReButtonEnabled(true);
					exit = true;
					ListItemFragment fragment = new ListItemFragment();
					Bundle bundle = new Bundle();
					bundle.putParcelable("handler", handler);
					fragment.setArguments(bundle);

					getSupportFragmentManager().beginTransaction()
							.replace(R.id.content, fragment).commit();

				}else{
				new AlertDialog.Builder(this)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle("Closing App")
		        .setMessage("Bạn muốn thoát ?")
		        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
				    {
				        @Override
				        public void onClick(DialogInterface dialog, int which) {
				        	Intent intent = new Intent(Intent.ACTION_MAIN);
				        	intent.addCategory(Intent.CATEGORY_HOME);
				        	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				        	startActivity(intent);
				        	finish();
				        	System.exit(0);
				        }
		
				    })
				    .setNegativeButton("No", null)
				    .show();
				}
			}
		}
	}
	
	public void setGoReButtonEnabled(boolean option){
		goButton.setEnabled(option);
		reButton.setEnabled(option);
//		if (option){
//			goButton.setVisibility(View.VISIBLE);
//			reButton.setVisibility(View.VISIBLE);
//		}else{
//			goButton.setVisibility(View.INVISIBLE);
//			reButton.setVisibility(View.INVISIBLE);
//		}
	}
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	// @Override
	// public void onConfigurationChanged(Configuration newConfig) {
	// super.onConfigurationChanged(newConfig);
	// // Pass any configuration change to the drawer toggles
	// mDrawerToggle.onConfigurationChanged(newConfig);
	// }

	// The click listener for ListView in the navigation drawer
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	public static boolean isNetworkAvailable() {
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	private void selectItem(int position) {
		MenuEnum mnPos = MenuEnum.values()[position];

//		if (curItem != null && curItem.equals(mnPos))
//			return;

		curItem = mnPos;
		SherlockFragment fragment;

		String[] menuItems = getResources().getStringArray(
				R.array.slidemenu_array);
		setTitle(menuItems[position]);

		if (!curItem.equals(MenuEnum.TIMDUONG)) {
			showContent(true);
			showNavBox(false);
		} else
			showNavBox(true);

		switch (curItem) {
		case THONGTINBUS: // thong tin tuyen bus
			this.setGoReButtonEnabled(true);
			fragment = new ListItemFragment();
			Bundle bundle = new Bundle();
			bundle.putParcelable("handler", handler);
			fragment.setArguments(bundle);

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content, fragment).commit();
			break;

		case TIMDUONG: // che do ban do
			new MapHandle();
			this.setGoReButtonEnabled(false);
			Fragment fbox = new SearchBox(getBaseContext());
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.navbox, fbox).commit();
			//onSearchRequested();
			showNavBox(true);
			break;

		case GANBAN: // tab fragment
//			getSupportFragmentManager()
//					.beginTransaction()
//					.replace(R.id.content,
//							PageSlidingTabStripFragment.newInstance(),
//							PageSlidingTabStripFragment.TAG).commit();
			

			this.setGoReButtonEnabled(true);
		    Handler mHandler = new Handler();
		    mHandler.postDelayed(new Runnable() {
	            public void run() {
	            	SherlockFragment fragment1 = new NearBusFragment();
					getSupportFragmentManager().beginTransaction()
							.replace(R.id.content, fragment1).commit();	            }
	        }, 300);
				
			
			break;
		case CAIDAT:
			//@duy_note activity_choose_city called again
			SharedPreferences pref = getSharedPreferences("ActivityChooseCityPREF", Context.MODE_PRIVATE);
	        if(pref.getBoolean("activity_executed", true)){
	            Editor ed = pref.edit();
	            ed.putBoolean("activity_executed", false);
	            ed.commit();
	        }
            Intent ChooseCityMenu = new Intent(this, ChooseCity.class);
            startActivity(ChooseCityMenu);

			break;
			
		case TACGIA:
			fragment = new AboutFragment();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content, fragment).commit();
			break;
			
		case THOAT:
			new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Closing App")
	        .setMessage("Bạn muốn thoát ?")
	        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
			    {
			        @Override
			        public void onClick(DialogInterface dialog, int which) {
			        	Intent intent = new Intent(Intent.ACTION_MAIN);
			        	intent.addCategory(Intent.CATEGORY_HOME);
			        	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			        	startActivity(intent);
			        	finish();
			        	System.exit(0);
			        }
	
			    })
			    .setNegativeButton("No", null)
			    .show();
			break;
		default:
			
			fragment = new AboutFragment();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content, fragment).commit();
			break;
		}

		setTitle(mPlanetTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * show/hide navigation box at the top
	 * @param mode <br> true - show<br> false - hide
	 */
	public static void showNavBox(boolean mode) {
		if (mode == false) {
			navBox.setVisibility(View.GONE);
			navBox.invalidate();
		} else {
			navBox.setVisibility(View.VISIBLE);
			navBox.invalidate();
		}
		navMode = mode;
	}

	/**
	 * show/hide content view
	 * @param mode <br> true - show<br> false - hide
	 */
	public static void showContent(boolean mode) {
		if (mode == false) {
			contentView.setVisibility(View.INVISIBLE);
			contentView.invalidate();
		} else {
			contentView.setVisibility(View.VISIBLE);
			contentView.invalidate();
		}
		isMapOn = !mode; //map on means that no content displayed
	}

	@Override
	public void onLocationChanged(Location location) {
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		LatLng latLng = new LatLng(latitude, longitude);

		zoomMapTo(latLng);
		locationManager.removeUpdates(this);
	}

	public static void zoomMapTo(LatLng latLng) {
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(12), 1000, null);
	}

	@Override
	public void onProviderDisabled(String arg0) {

	}

	@Override
	public void onProviderEnabled(String arg0) {

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

	}

	@Override
	public void passData(ArrayList<ResultSearchObject> resultText,
			ArrayList<String> resultGeo, ArrayList<Vector<Nodes>> arrayNodeAns) {
		SearchFragment fragment = new SearchFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content, fragment).commit();
		getSupportFragmentManager().executePendingTransactions();
		showContent(true);

		FragmentManager mng = getSupportFragmentManager();
		SearchFragment fSearch = (SearchFragment) mng
				.findFragmentById(R.id.content);

		fSearch.updateData(resultText, resultGeo, arrayNodeAns);
	}
	
	public class MHandler extends Handler implements Parcelable {

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {

		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==1) {
				Log.i("aaaa", "fasdfads");
				itemVisibility = true;
				invalidateOptionsMenu();
			}
			if(msg.what==2) {
				itemVisibility = false;
				invalidateOptionsMenu();
			}
		}
	}

	public static void hideSoftKeyboard(FragmentActivity activity) {
	    View view = activity.getCurrentFocus();
		InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
	    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	
}