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
	private final int TICK_MS = 500;
	
	private Position currentPosition;
	
	public SimulatedGps(LatLng location) {
		currentPosition = new Position(location, 0, System.currentTimeMillis());
	}
	
	public void followPath(final List<LatLng> path) {
		AsyncTask<Void, Void, Void> tickLoopTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				currentPosition = new Position(currentPosition.location, 0, System.currentTimeMillis());
				while (path.size() > 0) {
					advancePosition(path);
					publishProgress();
					try {
						Thread.sleep(TICK_MS);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}					
				}
				return null;
			}
			
			@Override
			protected void onProgressUpdate(Void... progress) {
				onTickHandler.invoke(currentPosition);
			}
		};
		tickLoopTask.execute();
	}
	
	private void advancePosition(List<LatLng> remainingPath) {
		long newTime = System.currentTimeMillis();
		long timePassedMillisconds = newTime - currentPosition.timestamp;
		double distanceRemaining = (timePassedMillisconds / S_TO_MS) * SPEED_LIMIT_MPS;
		LatLng currentLocation = currentPosition.location;
		double currentBearing = 0;
		
		while (remainingPath.size() > 0 && distanceRemaining > 0) {
			LatLng nextLocationInPath = remainingPath.get(0);
			double distanceToNextPoint = GisUtil.distanceInMeters(currentLocation, nextLocationInPath);
			double distanceToTravel = Math.min(distanceToNextPoint, distanceRemaining);
			currentBearing = GisUtil.initialBearing(currentLocation, nextLocationInPath);
			currentLocation = GisUtil.travel(currentLocation, currentBearing, distanceToTravel);
			
			distanceRemaining -= distanceToTravel;
			if (distanceRemaining > 0) {
				remainingPath.remove(0);
			}
		}
		
		currentPosition = new Position(currentLocation, currentBearing, newTime);
	}
	
	@Override
	public void enableTracking() {
		forceTick();
	}

	@Override
	public void disableTracking() {		
	}

	@Override
	public void forceTick() {
		onTickHandler.invoke(currentPosition);
	}
}
