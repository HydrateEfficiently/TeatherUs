package com.mdfws.teatherus.directions;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Directions {
	
	private ArrayList<Direction> directions;
	private ArrayList<Point> points;
	
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
	}
	
	private void createPoints() {
		points = new ArrayList<Point>();
		points.add(new Point() {{
			nextDirection = directions.get(0);
		}});
	}
	
	public List<Direction> getDirectionsList() {
		return directions;
	}
	
	public List<Point> getPoints() {
		return points;
	}
}
