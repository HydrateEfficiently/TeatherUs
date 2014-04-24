package com.mdfws.teatherus.map;

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
	
	public enum ProjectionMode {
		TWO_DIMENSIONAL,
		THREE_DIMENSIONAL
	}
	
	private static final float CONST_THREE_DIMENSIONAL_TILT = 45;
	private static final float INIT_THREE_DIMENSIONAL_ZOOM = 18;
	private static final float CONST_TWO_DIMENSIONAL_TILT = 0;
	private static final float CONST_TWO_DIMENSIONAL_BEARING = 0;
	private static final float INIT_TWO_DIMENSIONAL_ZOOM = 3;
	
	private GoogleMap map;
	private UiSettings mapUiSettings;
	private MapState state;
	private ProjectionMode projectionMode;
	
	public Map(Fragment fragment, final LatLng initialLocation) {
		map = ((MapFragment)fragment).getMap();
		initMapUiSettings();
		state = new MapState() {{
			location = initialLocation;
		}};
		setProjectionMode(ProjectionMode.TWO_DIMENSIONAL);
	}
	
	private void initMapUiSettings() {
		mapUiSettings = map.getUiSettings();
		mapUiSettings.setZoomControlsEnabled(false);
		mapUiSettings.setTiltGesturesEnabled(false);
		mapUiSettings.setCompassEnabled(false);
		mapUiSettings.setRotateGesturesEnabled(false);
	}
	
	public void setLocation(LatLng location) {
		state.location = location;
		map.animateCamera(CameraUpdateFactory.newLatLng(location));
	}
	
	public void setZoom(float zoom) {
		state.zoom = zoom;
		CameraPosition pos = map.getCameraPosition();
		map.animateCamera(CameraUpdateFactory.newCameraPosition(
			new CameraPosition.Builder(pos)
				.zoom(zoom)
				.build()));
	}
	
	public void setBearing(double bearing) {
		state.bearing = (float)bearing;
		if (projectionMode == ProjectionMode.THREE_DIMENSIONAL) {
			CameraPosition pos = map.getCameraPosition();
			map.animateCamera(CameraUpdateFactory.newCameraPosition(
				new CameraPosition.Builder(pos)
					.bearing(state.bearing)
					.build()));
		}
	}
	
	public void setProjectionMode(ProjectionMode projectionMode) {
		if (this.projectionMode != projectionMode) {
			boolean is2d = projectionMode == ProjectionMode.TWO_DIMENSIONAL;
			state.zoom = is2d ? INIT_TWO_DIMENSIONAL_ZOOM : INIT_THREE_DIMENSIONAL_ZOOM;
			CameraPosition cameraPosition = new CameraPosition(
				state.location,
				state.zoom,
				is2d ? CONST_TWO_DIMENSIONAL_TILT : CONST_THREE_DIMENSIONAL_TILT,
				is2d ? CONST_TWO_DIMENSIONAL_BEARING : state.bearing);
			map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		}
		this.projectionMode = projectionMode;
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
