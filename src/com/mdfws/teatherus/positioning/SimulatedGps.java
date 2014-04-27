package com.mdfws.teatherus.positioning;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.mdfws.teatherus.util.GisUtil;

public class SimulatedGps extends AbstractGps {
	
	private final int SPEED_LIMIT_KPH = 50;
	private final double KPH_TO_MPS = 0.277778;
	private final double SPEED_LIMIT_MPS = SPEED_LIMIT_KPH * KPH_TO_MPS;
	private final double S_TO_MS = 1000;
	private final int MAX_TICK_MS = 5000;
	
	private LatLng currentLocation;
	private double currentBearing;
	private long currentTime;
	
	public SimulatedGps(LatLng location) {
		currentLocation = location;
		currentBearing = 0;
	}
	
	public void followPath(final List<LatLng> path) {
		AsyncTask<Void, Void, Void> tickLoopTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				long travelTime = MAX_TICK_MS;
				currentTime = System.currentTimeMillis();
				
				while (path.size() > 0) {
					try {
						Thread.sleep(travelTime);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
					travelTime = advancePosition(path);
					publishProgress();
				}
				return null;
			}
			
			@Override
			protected void onProgressUpdate(Void... progress) {
				onTickHandler.invoke(new Position() {{
					location = currentLocation;
					bearing = currentBearing;
					timestamp = currentTime;
				}});
			}
		};
		tickLoopTask.execute();
	}
	
	private long advancePosition(List<LatLng> remainingPath) {
		LatLng nextLocation = remainingPath.get(0);
		
		long newTime = System.currentTimeMillis();
		long timePassedMillisconds = newTime - currentTime;
		
		double maxTravelDistance = (timePassedMillisconds / S_TO_MS) * SPEED_LIMIT_MPS;
		double distanceToNextPoint = GisUtil.distanceInMeters(currentLocation, nextLocation);
		final double newBearing = GisUtil.initialBearing(currentLocation, nextLocation);
		
		long travelTime;
		double travelDistance;
		if (maxTravelDistance >= distanceToNextPoint) {
			remainingPath.remove(0);
			travelDistance = maxTravelDistance - distanceToNextPoint;
			travelTime = (long)(travelDistance / SPEED_LIMIT_MPS * S_TO_MS);
		} else {
			travelDistance = maxTravelDistance;
			travelTime = MAX_TICK_MS;
		}
		
		final LatLng newLocation = GisUtil.travel(currentLocation, newBearing, travelDistance);
		
		currentLocation = newLocation;
		currentBearing = newBearing;
		currentTime = newTime;
			
		return travelTime;
	}
	
	public void stopFollowingPath() {
		
	}

	@Override
	public void enableTracking() {
		forceTick();
	}

	@Override
	public void disableTracking() {
		stopFollowingPath();		
	}

	@Override
	public void forceTick() {
		onTickHandler.invoke(new Position() {{
			location = currentLocation;
			bearing = currentBearing;
			timestamp = currentTime;
		}});
	}
}
