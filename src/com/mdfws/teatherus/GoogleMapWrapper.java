package com.mdfws.teatherus;

import android.app.Fragment;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class GoogleMapWrapper implements IMap {
	
	private GoogleMap map;
	
	public GoogleMapWrapper(Fragment fragment) {
		map = ((MapFragment)fragment).getMap();
	}

	@Override
	public void setLocation(Location location) {
		map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
	}

	@Override
	public void zoomTo(float zoom) {
		map.moveCamera(CameraUpdateFactory.zoomTo(zoom));
	}
}
