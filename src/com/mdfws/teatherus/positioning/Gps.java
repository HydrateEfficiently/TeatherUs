package com.mdfws.teatherus.positioning;

import android.location.Location;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.mdfws.teatherus.util.GoogleUtil;

public class Gps extends AbstractGps implements LocationListener {
	
	private static final int UPDATE_INTERVAL_MS = 1000;
	
	private LocationClient locationClient;
	private boolean isTrackingEnabled = false;
	
	public Gps(LocationClient locationClient) {
		this.locationClient = locationClient;
	}

	@Override
	public void enableTracking() {
		if (!isTrackingEnabled) {
			LocationRequest request = LocationRequest.create();
			request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
			request.setInterval(UPDATE_INTERVAL_MS);
			this.locationClient.requestLocationUpdates(request, this);
			isTrackingEnabled = true;
		}
	}

	@Override
	public void disableTracking() {
		if (isTrackingEnabled) {
			this.locationClient.removeLocationUpdates(this);
			isTrackingEnabled = false;
		}
	}
	
	@Override
	public void forceTick() {
		onLocationChanged(locationClient.getLastLocation());		
	}
	
	@Override
	public void onLocationChanged(final Location loc) {
		onTickHandler.invoke(new Position(GoogleUtil.toLatLng(loc), loc.hasBearing() ? loc.getBearing() : 0, System.currentTimeMillis()));
	}
}
