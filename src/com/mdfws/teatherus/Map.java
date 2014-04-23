package com.mdfws.teatherus;

import android.app.Fragment;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class Map {
	
	private GoogleMap map;
	
	public Map(Fragment fragment) {
		map = ((MapFragment)fragment).getMap();
		map.setMyLocationEnabled(true);
	}

	public void setLocation(Location location) {
		map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
	}

	public void zoomTo(float zoom) {
		map.moveCamera(CameraUpdateFactory.zoomTo(zoom));
	}	
}
