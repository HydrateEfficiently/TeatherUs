package com.mdfws.teatherus;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationTracker {
	
	private final int UPDATE_INTERVAL_MS = 500;
	private final int UPDATE_MIN_DIST_METERS = 5;
	
	private LocationManager manager;
	private LocationListener listener;
	
	public LocationTracker(Activity activity, final IMap map) {
		this.manager = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
		this.listener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				map.setLocation(location);
			}

			@Override
			public void onProviderDisabled(String provider) {
			}

			@Override
			public void onProviderEnabled(String provider) {
			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
			}
		};
	}
	
	public void enable() {
		this.manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_INTERVAL_MS, UPDATE_MIN_DIST_METERS, this.listener);
		this.listener.onLocationChanged(this.manager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
	}
	
	public void disable() {
		this.manager.removeUpdates(this.listener);
	}
}
