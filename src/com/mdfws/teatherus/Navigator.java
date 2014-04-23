package com.mdfws.teatherus;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.mdfws.teatherus.Directions.AsyncDirectionsRequest;
import com.mdfws.teatherus.Directions.Directions;
import com.mdfws.teatherus.Directions.AsyncDirectionsRequest.DirectionsRetrieved;

import android.app.Activity;
import android.location.Location;

public class Navigator implements LocationListener {
	
	public static final int UPDATE_INTERVAL_MS = 1000;
	
	private Map map;
	private LocationClient locationClient;
	private LatLng currentLocation;

	public Navigator(Activity activity, Map map, LocationClient locationClient) {
		this.map = map;
		this.locationClient = locationClient;
		map.zoomTo(15);
	}
	
	public boolean isReady() {
		return this.locationClient != null && this.locationClient.isConnected();
	}
	
	public void locationClientReady() {
		listenForLocationUpdates();
		Location lastLocation = this.locationClient.getLastLocation();
		this.currentLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
	}
	
	private void listenForLocationUpdates() {
		LocationRequest request = LocationRequest.create();
		request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		request.setInterval(UPDATE_INTERVAL_MS);
		this.locationClient.requestLocationUpdates(request, this);
	}
	
	@Override
	public void onLocationChanged(Location location) {
		this.currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
		this.map.setLocation(location);
	}
	
	public void navigateTo(LatLng latLng) {
		AsyncDirectionsRequest request = new AsyncDirectionsRequest(this.currentLocation, latLng);
		request.getDirections(new DirectionsRetrieved() {
			@Override
			public void invoke(Directions directions) {
				startNavigation(directions);
			}
		});
	}
	
	private void startNavigation(Directions directions) {
		
	}
}
