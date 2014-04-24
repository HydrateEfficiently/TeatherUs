package com.mdfws.teatherus;


import com.google.android.gms.maps.model.LatLng;
import com.mdfws.teatherus.directions.AsyncDirectionsRequest;
import com.mdfws.teatherus.directions.Directions;
import com.mdfws.teatherus.directions.AsyncDirectionsRequest.DirectionsRetrieved;
import com.mdfws.teatherus.map.Map;
import com.mdfws.teatherus.positioning.IGps;
import com.mdfws.teatherus.positioning.IGps.OnTickHandler;

import android.app.Activity;
import android.location.Location;

public class Navigator {
	
	private Map map;
	private IGps gps;
	private LatLng currentLocation;
	private double currentBearing;
	private boolean isNavigating = false;
	
	public Navigator(Activity activity, IGps gps) {
		this.gps = gps;
		gps.onTick(new OnTickHandler() {
			@Override
			public void invoke(Location location) {
				updateLocation(location);
			}
		});
		gps.enableTracking();
		gps.forceTick();
		
		map = new Map(activity.getFragmentManager().findFragmentById(R.id.main_map_view), currentLocation);
	}
	
	public void updateLocation(Location location) {
		currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
		currentBearing = location.hasBearing() ? location.getBearing() : 0;
		if (map != null) {
			map.setLocation(this.currentLocation);
			map.setBearing(this.currentBearing);
		}
	}
	
	public void navigateTo(LatLng latLng) {
		AsyncDirectionsRequest request = new AsyncDirectionsRequest(this.currentLocation, latLng);
		request.getDirections(new DirectionsRetrieved() {
			@Override
			public void invoke(Directions directions) {
				startNavigation(directions);
			}
		});
	}
	
	private void startNavigation(Directions directions) {
		if (!isNavigating) {
			map.setProjectionMode(Map.ProjectionMode.THREE_DIMENSIONAL);
			map.lockUi();
			isNavigating = true;
		}
	}
}
