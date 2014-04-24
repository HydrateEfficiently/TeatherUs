package com.mdfws.teatherus.directions;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.mdfws.teatherus.util.Google;

public class Direction {
	
	private String text;
	private LatLng start;
	private LatLng end;
	private List<LatLng> points;

	public Direction(JSONObject step) throws JSONException {
		text = step.getString("html_instructions");
		start = getLatLng(step.getJSONObject("start_location"));
		end = getLatLng(step.getJSONObject("start_location"));
		points = Google.decodePolyline(step.getJSONObject("polyline").getString("points"));
		points.add(0, start);
		points.add(points.size(), end);
	}
	
	private LatLng getLatLng(JSONObject serializedLatLng) throws JSONException {
		double lat = serializedLatLng.getDouble("lat");
		double lng = serializedLatLng.getDouble("lng");
		return new LatLng(lat, lng);
	}
	
	public String getText() {
		return text;
	}
	
	public LatLng getStart() {
		return start;
	}
	
	public LatLng getEnd() {
		return end;
	}
	
	public List<LatLng> getPoints() {
		return points;
	}
}
