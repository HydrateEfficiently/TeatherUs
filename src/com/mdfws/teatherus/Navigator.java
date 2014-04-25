package com.mdfws.teatherus;


import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.mdfws.teatherus.directions.AsyncDirectionsRequest;
import com.mdfws.teatherus.directions.Directions;
import com.mdfws.teatherus.directions.AsyncDirectionsRequest.DirectionsRetrieved;
import com.mdfws.teatherus.directions.Point;
import com.mdfws.teatherus.map.Map;
import com.mdfws.teatherus.positioning.IGps;
import com.mdfws.teatherus.positioning.IGps.OnTickHandler;
import com.mdfws.teatherus.positioning.Position;
import com.mdfws.teatherus.positioning.SimulatedGps;

import android.app.Activity;

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
			public void invoke(Position position) {
				updateLocation(position);
			}
		});
		gps.enableTracking();
		gps.forceTick();
		
		map = new Map(activity.getFragmentManager().findFragmentById(R.id.main_map_view), currentLocation);
	}
	
	public void updateLocation(Position position) {
		currentLocation = position.location;
		currentBearing = position.bearing;
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
			isNavigating = true;
			prepareMapForNavigation();
			
			if (gps instanceof SimulatedGps) {
				simulateDirections(directions);
			}
		}
	}
	
	private void prepareMapForNavigation() {
		map.setProjectionMode(Map.ProjectionMode.THREE_DIMENSIONAL);
		map.lockUi();
	}
	
	private void simulateDirections(Directions directions) {
		List<Point> points = directions.getPoints();
		List<LatLng> path = new ArrayList<LatLng>();
		for (int i = 0; i < points.size(); i++) {
			path.add(points.get(i).location);
		}
		((SimulatedGps)gps).followPath(path);
	}
}
