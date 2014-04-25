package com.mdfws.teatherus.map;

import android.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class Map {
	
	public enum ProjectionMode {
		TWO_DIMENSIONAL,
		THREE_DIMENSIONAL
	}
	
	private static final float TWO_DIMENSIONAL_TILT = 0;
	private static final float TWO_DIMENSIONAL_BEARING = 0;
	private static final float THREE_DIMENSIONAL_TILT = 45;
	private static final float INIT_TWO_DIMENSIONAL_ZOOM = 3;
	private static final float INIT_THREE_DIMENSIONAL_ZOOM = 18;
	
	private GoogleMap map;
	private UiSettings mapUiSettings;
	private ProjectionMode projectionMode;
	
	private LatLng location;
	private float zoom;
	private float bearing;
	
	public Map(Fragment fragment, LatLng initialLocation) {
		map = ((MapFragment)fragment).getMap();
		initMapUiSettings();
		location = initialLocation;
		setProjectionMode(ProjectionMode.TWO_DIMENSIONAL);
	}
	
	private void initMapUiSettings() {
		mapUiSettings = map.getUiSettings();
		mapUiSettings.setZoomControlsEnabled(false);
		mapUiSettings.setTiltGesturesEnabled(false);
		mapUiSettings.setCompassEnabled(false);
		mapUiSettings.setRotateGesturesEnabled(false);
	}
	
	public void invalidate() {
		boolean is2d = projectionMode == ProjectionMode.TWO_DIMENSIONAL;
		map.animateCamera(CameraUpdateFactory.newCameraPosition(
			new CameraPosition(
				location,
				zoom,
				is2d ? TWO_DIMENSIONAL_TILT : THREE_DIMENSIONAL_TILT,
				is2d ? TWO_DIMENSIONAL_BEARING : bearing)));
	}
	
	public void setLocation(LatLng location) {
		this.location = location;
	}
	
	public void setZoom(float zoom) {
		this.zoom = zoom;
	}
	
	public void setBearing(float bearing) {
		this.bearing = bearing;
	}
	
	public void setProjectionMode(ProjectionMode projectionMode) {
		if (this.projectionMode != projectionMode) {
			this.projectionMode = projectionMode;
			this.zoom = projectionMode == ProjectionMode.TWO_DIMENSIONAL ? INIT_TWO_DIMENSIONAL_ZOOM : INIT_THREE_DIMENSIONAL_ZOOM;
			invalidate();
		}
	}
	
	public void lockUi() {
		mapUiSettings.setScrollGesturesEnabled(false);
	}
	
	public void unlockUi() {
		mapUiSettings.setScrollGesturesEnabled(true);
	}
	
	public void toggleProjectionMode() {
		setProjectionMode(projectionMode == ProjectionMode.TWO_DIMENSIONAL ? ProjectionMode.THREE_DIMENSIONAL : ProjectionMode.TWO_DIMENSIONAL);
	}
}
