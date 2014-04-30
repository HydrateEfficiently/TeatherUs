package com.mdfws.teatherus;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.mdfws.teatherus.directions.Direction;
import com.mdfws.teatherus.directions.Directions;
import com.mdfws.teatherus.directions.Route.DirectionsRetrieved;
import com.mdfws.teatherus.directions.Route;
import com.mdfws.teatherus.map.Map;
import com.mdfws.teatherus.positioning.DebugSimulatedGps;
import com.mdfws.teatherus.positioning.IGps;
import com.mdfws.teatherus.positioning.IGps.OnTickHandler;
import com.mdfws.teatherus.positioning.Position;
import android.app.Activity;

public class Navigator {
	
	private MapFragment mapFragment;
	private Map map;
	private Vehicle vehicle;
	private VehicleOptions vehicleOptions;
	private IGps gps;
	private NavigationState navigationState;
	private NavigatorEvents events;
	private Position lastPosition;
	private long lastTickTime;
	
	public Navigator(Activity activity, final IGps gps, VehicleOptions vehicleOptions, NavigatorEvents events) {
		mapFragment = (MapFragment)activity.getFragmentManager().findFragmentById(R.id.main_map_view);
		this.vehicleOptions = vehicleOptions;
		this.events = events;
		this.gps = gps;
		gps.onTick(new OnTickHandler() {
			@Override
			public void invoke(Position position) {
				try {
					onGpsTick(position);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		gps.enableTracking();
		gps.forceTick();
	}
	
	private void onGpsTick(Position position) throws Exception {
		lastPosition = position;
		
		if (map == null) {
			initMap(position);
		}
		
		if (isNavigating()) {
			navigationState.update(position.location, position.bearing);
		} else {
			updateVehicleMarker(position.location, position.bearing);
		}
	}
	
	private void initMap(Position position) {
		map = new Map(mapFragment, position.location);
		vehicleOptions.location(position.location);
		vehicle = new Vehicle(map, vehicleOptions);
	}
	
	private void updateVehicleMarker(LatLng location, double bearing) {
		vehicle.setLocation(location);
		vehicle.setHeading((float)bearing);
		map.invalidate();
	}
	
	public void navigateTo(LatLng latLng) {
		Route request = new Route(lastPosition.location, latLng);
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
			prepareMapForNavigation(directions);
			
			if (gps instanceof DebugSimulatedGps) {
				((DebugSimulatedGps)gps).followPath(directions.getLatLngPath());
			}
		}
	}
	
	private void createNavigationState(Directions directions) {
		navigationState = new NavigationState(directions, new NavigatorEvents() {
			@Override
			public void OnVehicleOffRoute() {
				events.OnVehicleOffRoute();
			}
			
			@Override
			public void OnNewDirection(Direction direction) {
				events.OnNewDirection(direction);				
			}
			
			@Override
			public void OnArrival() {
				events.OnArrival();
			}

			@Override
			public void OnUpdate(UpdateEventArgs args) {
				updateVehicleMarker(args.locationOnRoute, args.bearingOnRoute);
				events.OnUpdate(args);
			}
		});
	}
	
	private void prepareMapForNavigation(Directions directions) {
		map.setProjectionMode(Map.ProjectionMode.THREE_DIMENSIONAL);
		map.lockUi();
		map.addPolyline(directions.getLatLngPath());
	}
}
