package com.mdfws.teatherus;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.mdfws.teatherus.directions.Direction;
import com.mdfws.teatherus.directions.Directions;
import com.mdfws.teatherus.directions.Point;
import com.mdfws.teatherus.util.LatLngUtil;

public class NavigationState {
	
	public interface Events {
		void OnVehicleOffRoute();
		void OnArrival();
		void OnNewDirection(Direction direction);
	}
	
	public class Snapshot {
		
		public Snapshot(LatLng location, double bearing) {
			locationOnRoute = location;
			bearingOnRoute = bearing;
		}
		
		public int timeToArrival;
		public int distanceToArrival;
		public LatLng locationOnRoute;
		public double bearingOnRoute;
		public Direction direction;
	}
	
	private final int MAX_TIME_OFF_ROUTE_MS = 5000;
	private final int OFF_ROUTE_TOLERANCE_METERS = 10;
	private final int OFF_ROUTE_TOLERANCE_BEARING = 45;
	private final int LOOK_AHEAD_POINTS = 5;
	private final int LOOK_BEHIND_POINTS = 2;
	private final int MIN_ARRIVAL_DIST_METERS = 10;
	
	private Directions directions;
	private Events events;
	private List<Point> path;
	private Point destination;
	private LatLng realLocation;
	private double realBearing;
	private LatLng snappedLocation;
	private double snappedBearing;
	private double distanceOffRoute;
	private double bearingOffRoute;
	private long offRouteStartTime;
	private Snapshot snapshot;
	private int pathIndex = 0;
	private boolean hasArrived = false;
	private boolean isOnRoute = true;
	
	public NavigationState(Directions directions, Events events) {
		this.directions = directions;
		this.events = events;
		path = directions.getPath();
		destination = path.get(path.size() - 1);
	}
	
	public void update(LatLng location, double bearing) throws Exception {
		realLocation = location;
		realBearing = bearing;
		if (!hasArrived) {
			checkArrival();
			snapLocationToRoute();
			snapBearingToRoute();
			checkOffRoute();
			calculateDistanceToArrival();
			calculateTimeToArrival();
			createSnapshot();
		} else {
			throw new Exception("NavigationState expired, vehicle has arrived at destination.");
		}
	}
	
	public Snapshot getSnapshot() {
		return snapshot;
	}
	
	private void checkArrival() {
		if (LatLngUtil.distanceInMeters(realLocation, destination.location) <= MIN_ARRIVAL_DIST_METERS) {
			hasArrived = true;
			events.OnArrival();
		}
	}
	
	private void snapLocationToRoute() {
		double closestDistance = Double.MAX_VALUE;
		int bestIndex = 0;
		LatLng bestLocation = null;
		
		for (int i = Math.max(0, pathIndex - LOOK_BEHIND_POINTS);
				i <= Math.min(path.size() - 1, pathIndex + LOOK_AHEAD_POINTS);
				i++) {
			
			Point currentPoint = path.get(i);
			LatLng currentSnappedLoc = LatLngUtil.closestLocationOnLine(currentPoint.location, currentPoint.nextPoint.location, realLocation);
			double currentDistance = LatLngUtil.distanceInMeters(realLocation, currentSnappedLoc);
			if (currentDistance < closestDistance) {
				closestDistance = currentDistance;
				bestIndex = i;
				bestLocation = currentSnappedLoc;
			}
		}
		
		pathIndex = bestIndex;
		snappedLocation = bestLocation;
		distanceOffRoute = closestDistance;
	}
	
	private void snapBearingToRoute() {
		Point currentPoint = path.get(pathIndex);
		snappedBearing = LatLngUtil.initialBearing(currentPoint.location, currentPoint.nextPoint.location);
		bearingOffRoute = Math.max(snappedBearing, realBearing) - Math.min(snappedBearing, realBearing);
	}
	
	private void checkOffRoute() {
		if (distanceOffRoute > OFF_ROUTE_TOLERANCE_METERS || bearingOffRoute > OFF_ROUTE_TOLERANCE_BEARING) {
			if (isOnRoute) {
				isOnRoute = false;
				offRouteStartTime = System.currentTimeMillis();
			} else if (System.currentTimeMillis() - offRouteStartTime > MAX_TIME_OFF_ROUTE_MS) {
				events.OnVehicleOffRoute();
			}
		} else {
			isOnRoute = true;
		}
	}
	
	private void calculateDistanceToArrival() {
		
	}
	
	private void calculateTimeToArrival() {
		
	}
	
	private void createSnapshot() {
		snapshot = new Snapshot(snappedLocation, snappedBearing);
	}
}
