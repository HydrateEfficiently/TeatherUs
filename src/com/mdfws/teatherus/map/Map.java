package com.mdfws.teatherus.map;

import android.R;
import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map {
	
	public enum ProjectionMode {
		TWO_DIMENSIONAL,
		THREE_DIMENSIONAL
	}
	
	private static final int MAP_PADDING = 100;
	private static final float TWO_DIMENSIONAL_TILT = 0;
	private static final float TWO_DIMENSIONAL_BEARING = 0;
	private static final float THREE_DIMENSIONAL_TILT = 45;
	private static final float INIT_TWO_DIMENSIONAL_ZOOM = 3;
	private static final float INIT_THREE_DIMENSIONAL_ZOOM = 18;
	private static final int PROJECTION_CHANGE_ANIMATION_TIME = 1000;
	
	private GoogleMap map;
	private UiSettings mapUiSettings;
	private ProjectionMode projectionMode;
	private GroundOverlay marker;
	private LatLng location;
	private float bearing;
	private float zoom;
	
	public Map(Fragment fragment, LatLng initialLocation, Bitmap navigator) {
		map = ((MapFragment)fragment).getMap();
		map.setPadding(MAP_PADDING, MAP_PADDING, MAP_PADDING, MAP_PADDING);
		initMapUiSettings();
		location = initialLocation;
		initMarker(initialLocation, navigator);
		setProjectionMode(ProjectionMode.TWO_DIMENSIONAL);
	}
	
	private void initMarker(LatLng initialLocation, Bitmap navigator) {
		marker = map.addGroundOverlay(new GroundOverlayOptions()
			.position(initialLocation, (float)navigator.getWidth(), (float)navigator.getHeight())
			.image(BitmapDescriptorFactory.fromBitmap(navigator)));
		map.setOnCameraChangeListener(new OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition position) {
				marker.setPosition(position.target);
				// marker.setBearing				
			}
		});
	}
	
	private void initMapUiSettings() {
		mapUiSettings = map.getUiSettings();
		mapUiSettings.setZoomControlsEnabled(false);
		mapUiSettings.setTiltGesturesEnabled(false);
		mapUiSettings.setCompassEnabled(false);
		mapUiSettings.setRotateGesturesEnabled(false);
	}
	
	public void invalidate(int animationTime) {
		boolean is2d = projectionMode == ProjectionMode.TWO_DIMENSIONAL;
		
		CameraPosition position = new CameraPosition(
			location,
			zoom,
			is2d ? TWO_DIMENSIONAL_TILT : THREE_DIMENSIONAL_TILT,
			is2d ? TWO_DIMENSIONAL_BEARING : bearing);
		
		map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
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
			
			boolean is2d = projectionMode == ProjectionMode.TWO_DIMENSIONAL;
			
			CameraPosition position = new CameraPosition(
				location,
				zoom,
				is2d ? TWO_DIMENSIONAL_TILT : THREE_DIMENSIONAL_TILT,
				is2d ? TWO_DIMENSIONAL_BEARING : bearing);
			
			CancelableCallback callback = new CancelableCallback() {
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
				}
			};
			
			map.animateCamera(CameraUpdateFactory.newCameraPosition(position), PROJECTION_CHANGE_ANIMATION_TIME, callback);
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
