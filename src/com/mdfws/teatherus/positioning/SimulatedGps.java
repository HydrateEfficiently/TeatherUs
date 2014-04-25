package com.mdfws.teatherus.positioning;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class SimulatedGps extends AbstractGps {
	
	private final int SPEED_LIMIT_KPH = 50;
	private final double KPH_TO_MPS = 0.277778;
	private final double SPEED_LIMIT_MPS = SPEED_LIMIT_KPH * KPH_TO_MPS;
	
	private LatLng currentLocation;
	
	public SimulatedGps(LatLng location) {
		currentLocation = location;
	}
	
	public void followPath(List<LatLng> path) {
		
	}

	@Override
	public void enableTracking() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disableTracking() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void forceTick() {
		// TODO Auto-generated method stub
		
	}
}
