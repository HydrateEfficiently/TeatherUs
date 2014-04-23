package com.mdfws.teatherus.Directions;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Directions {
	
	private ArrayList<Leg> legs;
	
	public Directions(String jsonString) throws JSONException {
		JSONObject jsonDirections = new JSONObject(jsonString);
		JSONArray jsonRoutes = jsonDirections.getJSONArray("routes");
		JSONArray jsonLegs = jsonRoutes.getJSONObject(0).getJSONArray("legs");
		legs = new ArrayList<Leg>();
		for (int i = 0; i < jsonLegs.length(); i++) {
			legs.add(new Leg(jsonLegs.getJSONObject(i)));
		}
	}
}
