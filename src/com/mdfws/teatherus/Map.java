package com.mdfws.teatherus;

import android.app.Fragment;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class Map {
	
	public enum Mode {
		NAVIGATING,
		INTERACTING
	}
	
	private static final float NAVIGATING_TILT = 45;
	private static final float NAVIGATING_ZOOM = 18;
	private static final float INTERACTING_TILT = 0;
	private static final float INTERACTING_BEARING = 0;
	private static final float INTERACTING_ZOOM = 3;
	
	private GoogleMap map;
	private UiSettings mapUiSettings;
	private Mode mode;
	private LatLng currentLocation;
	private float currentBearing;
	
	public Map(Fragment fragment, LatLng initialLocation) {
		map = ((MapFragment)fragment).getMap();
		mapUiSettings = map.getUiSettings();
		mapUiSettings.setZoomControlsEnabled(false);
		currentLocation = initialLocation;
		currentBearing = 0;
		setMode(Mode.INTERACTING);
	}
	
	public void setMode(Mode mode) {
		if (mode == Mode.NAVIGATING) {
			map.setMyLocationEnabled(false);
		} else if (mode == Mode.INTERACTING) {
			map.setMyLocationEnabled(true);
		}
		
		CameraPosition cameraPosition = getInitialCameraPosition(mode);
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		this.mode = mode;
	}
	
	private CameraPosition getInitialCameraPosition(Mode mode) {
		if (mode == Mode.NAVIGATING) {
			return new CameraPosition(
				currentLocation,
				NAVIGATING_ZOOM,
				NAVIGATING_TILT,
				currentBearing);
		} else if (mode == Mode.INTERACTING) {
			return new CameraPosition(
				currentLocation,
				INTERACTING_ZOOM,
				INTERACTING_TILT,
				INTERACTING_BEARING);
		}
		return null;
	}
	
	public void setLocation(LatLng location) {
		currentLocation = location;
		map.animateCamera(CameraUpdateFactory.newLatLng(currentLocation));
	}
	
	public void setZoom(float zoom) {
		CameraPosition pos = map.getCameraPosition();
		map.animateCamera(CameraUpdateFactory.newCameraPosition(
			new CameraPosition.Builder(pos)
				.zoom(zoom)
				.build()));
	}
	
	public void setBearing(double bearing) {
		currentBearing = (float)bearing;
		CameraPosition pos = map.getCameraPosition();
		map.animateCamera(CameraUpdateFactory.newCameraPosition(
			new CameraPosition.Builder(pos)
				.bearing(currentBearing)
				.build()));
	}
}
