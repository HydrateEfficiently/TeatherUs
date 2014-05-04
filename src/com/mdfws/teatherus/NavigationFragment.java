package com.mdfws.teatherus;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.mdfws.teatherus.directions.Direction;
import com.mdfws.teatherus.positioning.Gps;
import com.mdfws.teatherus.positioning.IGps;
import com.mdfws.teatherus.positioning.SimulatedGps;

import android.app.Activity;
import android.app.Fragment;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class NavigationFragment extends Fragment implements
	ConnectionCallbacks,
	OnConnectionFailedListener  {
	
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;	

	private NavigationEvents events;
	private NavigationOptions options;
	private Activity parent;
	private LocationClient locationClient;
	private NavigationMap map;
	private IGps gps;
	private Navigator navigator;
	private NavigatorEvents internalEvents;
	
	public NavigationFragment(NavigationEvents events, NavigationOptions options) {
		this.events = events;
		this.options = options;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.navigation_fragment, container, false);
	}
	
	@Override
	public void onStart() {
		parent = getActivity();
		parent.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map_fragment);
		MapEventsListener mapEventsListener = (MapEventsListener)getView().getRootView().findViewById(R.id.map_events_listener_view);
		map = new NavigationMap(mapFragment, mapEventsListener, options.mapOptions());
		locationClient = new LocationClient(parent, this, this);
		locationClient.connect();
		super.onStart();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (connectionResult.hasResolution()) {
			try {
				connectionResult.startResolutionForResult(parent, CONNECTION_FAILURE_RESOLUTION_REQUEST);
			} catch (SendIntentException ex) {
				ex.printStackTrace();
			}
		} else {
			// TODO: Handler errors with dialog
		}
	}

	@Override
	public void onConnected(Bundle dataBundle) {
		gps = createGps();
		internalEvents = createNavigatorEvents();
		navigator = new Navigator(gps, map, options.vehicleOptions(), internalEvents);
		events.OnNavigatorReady(navigator);				
	}
	
	// TODO: Extract to SimulatedNavigationFragment
	private IGps createGps() {
		final boolean USE_SIMULATED_GPS = true;
		if (USE_SIMULATED_GPS) {
			return new SimulatedGps(new LatLng(-43.463292,172.620614));
		} else {
			return new Gps(locationClient);
		}
	}
	
	private NavigatorEvents createNavigatorEvents() {
		return new NavigatorEvents() {
			@Override
			public void OnVehicleOffRoute() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void OnUpdate(UpdateEventArgs args) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void OnNewDirection(Direction direction) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void OnArrival() {
				// TODO Auto-generated method stub
				
			}
		};
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
	}
	
	public Navigator getNavigator() {
		return navigator;
	}
}
