package com.mdfws.teatherus;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.mdfws.teatherus.NavigatorEvents.UpdateEventArgs;
import com.mdfws.teatherus.directions.Direction;
import com.mdfws.teatherus.directions.Directions;
import com.mdfws.teatherus.directions.Point;
import com.mdfws.teatherus.util.LatLngUtil;

public class NavigationState {
	
	private final int MAX_TIME_OFF_ROUTE_MS = 5000;
	private final int OFF_ROUTE_TOLERANCE_METERS = 10;
	private final int OFF_ROUTE_TOLERANCE_BEARING = 45;
	private final int LOOK_AHEAD_POINTS = 5;
	private final int LOOK_BEHIND_POINTS = 2;
	private final int MIN_ARRIVAL_DIST_METERS = 10;
	
	private NavigatorEvents events;
	private List<Point> path;
	private Point destination;
	private LatLng realLocation;
	private double realBearing;
	private LatLng snappedLocation;
	private double snappedBearing;
	private double distanceOffRoute;
	private double bearingOffRoute;
	private long offRouteStartTime;
	private Point prevPoint;
	private int timeToArrival;
	private int distanceToArrival;
	private int pathIndex = 0;
	private boolean hasArrived = false;
	private boolean isOnRoute = true;
	
	public NavigationState(Directions directions, NavigatorEvents events) {
		this.events = events;
		path = directions.getPath();
		destination = path.get(path.size() - 1);
	}
	
	public void update(LatLng location, double bearing) throws Exception {
		realLocation = location;
		realBearing = bearing;
		if (!hasArrived) {
			prevPoint = path.get(pathIndex);
			checkArrival();
			snapLocationToRoute();
			snapBearingToRoute();
			calculateDistanceToArrival();
			calculateTimeToArrival();
			checkDirectionChanged();
			fireUpdate();
			checkOffRoute();
		} else {
			throw new Exception("NavigationState expired, vehicle has arrived at destination.");
		}
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
			unsnapPosition();
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
	
	private void unsnapPosition() {
		snappedLocation = realLocation;
		snappedBearing = realBearing;
	}
	
	private void calculateDistanceToArrival() {
		
	}
	
	private void calculateTimeToArrival() {
		
	}
	
	private void checkDirectionChanged() {
		Direction currentDirection = path.get(pathIndex).direction;
		Direction prevDirection = prevPoint.direction;
		if (currentDirection != prevDirection){
			events.OnNewDirection(currentDirection);
		}
	}
	
	private void fireUpdate() {
		events.OnUpdate(new UpdateEventArgs(snappedLocation, snappedBearing, timeToArrival, distanceToArrival, path.get(pathIndex).direction));
	}
}
