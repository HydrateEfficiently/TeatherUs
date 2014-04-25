package com.mdfws.teatherus.directions;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.android.gms.maps.model.LatLng;
import com.mdfws.teatherus.util.GisUtil;

public class Directions {
	
	private ArrayList<Direction> directions;
	private ArrayList<Point> points;
	private LatLng endLocation;
	
	public Directions(String jsonString) throws JSONException {
		JSONObject route = new JSONObject(jsonString).getJSONArray("routes").getJSONObject(0); // Only one route supported
		JSONObject leg = route.getJSONArray("legs").getJSONObject(0); // Only one leg supported
		createDirections(leg.getJSONArray("steps"));
		createPoints();
	}
	
	private void createDirections(JSONArray steps) throws JSONException {
		directions = new ArrayList<Direction>();
		int length = steps.length();
		for (int i = 0; i < length; i++) {
			directions.add(new Direction(steps.getJSONObject(i)));
		}
		endLocation = directions.get(length - 1).getEnd();
	}
	
	private void createPoints() {
		Direction currentDirection;
		Point currentPoint;
		Point prevPoint = createLastPoint();
		
		points = new ArrayList<Point>();
		points.add(prevPoint);
		
		for (int i = directions.size() - 1; i >= 0; i--) {
			currentDirection = directions.get(i);
			List<LatLng> currentDirectionPoints = currentDirection.getPoints();
			for (int j = currentDirectionPoints.size() - 2; j >= 0; j--) {
				currentPoint = createPoint(currentDirectionPoints.get(j), prevPoint, currentDirection);
				points.add(0, currentPoint);
				prevPoint = currentPoint;
			}
		}
	}
	
	private Point createLastPoint() {
		return new Point() {{
			location = endLocation;
			distanceToCurrentDirectionMeters = 0;
			timeToCurrentDirectionMinutes = 0;
			distanceToNextDirectionMeters = 0;
			timeToNextDirectionMinutes = 0;
			distanceToArrivalMeters = 0;
			timeToArrivalMinutes = 0;
			direction = directions.get(directions.size() - 1);
			nextDirection = null;
			nextPoint = null;
		}};
	}
	
	private Point createPoint(final LatLng loc, final Point next, final Direction dir) {
		final double distanceToNext = GisUtil.distanceInMeters(loc, next.location);
		final boolean isNewDirection = next.direction != dir;
		return new Point() {{
			location = loc;
			distanceToCurrentDirectionMeters = isNewDirection ? 0 : next.distanceToCurrentDirectionMeters + distanceToNext;
			distanceToNextDirectionMeters = isNewDirection ? next.distanceToCurrentDirectionMeters + distanceToNext : next.distanceToNextDirectionMeters + distanceToNext;
			distanceToArrivalMeters = next.distanceToArrivalMeters + distanceToNext;
			direction = dir;
			nextDirection = isNewDirection ? next.direction : next.nextDirection;
			nextPoint = next;
		}};
	}
	
	public List<Direction> getDirectionsList() {
		return directions;
	}
	
	public List<Point> getPoints() {
		return points;
	}
}
