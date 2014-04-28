package com.mdfws.teatherus;


import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.mdfws.teatherus.NavigationState.Events;
import com.mdfws.teatherus.NavigationState.Snapshot;
import com.mdfws.teatherus.directions.AsyncDirectionsRequest;
import com.mdfws.teatherus.directions.Direction;
import com.mdfws.teatherus.directions.Directions;
import com.mdfws.teatherus.directions.AsyncDirectionsRequest.DirectionsRetrieved;
import com.mdfws.teatherus.directions.Point;
import com.mdfws.teatherus.map.Map;
import com.mdfws.teatherus.positioning.IGps;
import com.mdfws.teatherus.positioning.IGps.OnTickHandler;
import com.mdfws.teatherus.positioning.Position;
import com.mdfws.teatherus.positioning.SimulatedGps;
import com.mdfws.teatherus.util.LatLngUtil;

import android.app.Activity;
import android.graphics.Bitmap;

public class Navigator {
	
	private MapFragment mapFragment;
	private Map map;
	private Vehicle vehicle;
	private IGps gps;
	private NavigationState navigationState;
	private long lastTickTime;
	
	public Navigator(Activity activity, IGps gps, VehicleOptions vehicleOptions) {
		this.gps = gps;
		gps.onTick(new OnTickHandler() {
			@Override
			public void invoke(Position position) {
				if (vehicle != null) {
					onGpsTick(position);
				}
			}
		});
		gps.enableTracking();
		
		mapFragment = (MapFragment)activity.getFragmentManager().findFragmentById(R.id.main_map_view);
		map = new Map(mapFragment, currentPosition.location);
		vehicle = new Vehicle(map, vehicleOptions);
	}
	
	private void onGpsTick(Position position) throws Exception {
		long time = position.timestamp;
		LatLng location;
		double bearing;
		
		if (isNavigating()) {
			navigationState.update(position.location, position.bearing);
			Snapshot currentState = navigationState.getSnapshot();
			location = currentState.locationOnRoute;
			bearing = currentState.bearingOnRoute;
		} else {
			location = position.location;
			bearing = position.bearing;
		}
		
		updateVehicleMarker(location, bearing, time);
	}
	
	private void updateVehicleMarker(LatLng location, double bearing, long time) {
		int timeSinceLast = lastTickTime == 0 ? 1 : (int)(time - lastTickTime);
		if (vehicle != null) {		
			vehicle.setLocation(location);
			vehicle.setHeading((float)bearing);
			map.invalidate(timeSinceLast);
		}
		lastTickTime = time;
	}
	
	public void navigateTo(LatLng latLng) {
		AsyncDirectionsRequest request = new AsyncDirectionsRequest(currentPosition.location, latLng);
		request.getDirections(new DirectionsRetrieved() {
			@Override
			public void invoke(Directions directions) {
				startNavigation(directions);
			}
		});
	}
	
	private boolean isNavigating() {
		return navigationState != null;
	}
	
	private void startNavigation(Directions directions) {
		if (!isNavigating()) {
			createNavigationState(directions);
			prepareMapForNavigation();
			
			if (gps instanceof SimulatedGps) {
				simulateDirections(directions);
			}
		}
	}
	
	private void createNavigationState(Directions directions) {
		navigationState = new NavigationState(directions, new Events() {
			@Override
			public void OnVehicleOffRoute() {
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
		});
	}
	
	private void prepareMapForNavigation() {
		map.setProjectionMode(Map.ProjectionMode.THREE_DIMENSIONAL);
		map.lockUi();
	}
	
	private void simulateDirections(Directions directions) {
		List<Point> points = directions.getPath();
		List<LatLng> path = new ArrayList<LatLng>();
		for (int i = 0; i < points.size(); i++) {
			path.add(points.get(i).location);
		}
		((SimulatedGps)gps).followPath(path);
	}
}
