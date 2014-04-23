package com.mdfws.teatherus.Directions;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Leg {
	
	private ArrayList<Step> steps;
	
	public Leg(JSONObject jsonLeg) throws JSONException {
		JSONArray jsonSteps = jsonLeg.getJSONArray("steps");
		steps = new ArrayList<Step>();
		for (int i = 0; i < jsonSteps.length(); i++) {
			steps.add(new Step(jsonSteps.getJSONObject(i)));
		}
	}
}
