package com.mdfws.teatherus.map;

import android.R;
import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
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
	
	public interface OnProjectionModeChangeListener {
		public void invoke(ProjectionMode projectionMode);
	}
	
	public enum ProjectionMode {
		TWO_DIMENSIONAL,
		THREE_DIMENSIONAL
	}
	
	private static final int MAP_PADDING = 100;
	private static final float TWO_DIMENSIONAL_TILT = 0;
	private static final float TWO_DIMENSIONAL_BEARING = 0;
	private static final float THREE_DIMENSIONAL_TILT = 60;
	private static final float INIT_TWO_DIMENSIONAL_ZOOM = 3;
	private static final float INIT_THREE_DIMENSIONAL_ZOOM = 18;
	private static final int PROJECTION_CHANGE_ANIMATION_TIME = 1000;
	
	private GoogleMap map;
	private MapFragment mapFragment;
	private UiSettings mapUiSettings;
	private ProjectionMode projectionMode;
	private OnProjectionModeChangeListener projectionModeListener;
	private GroundOverlay marker;
	private LatLng location;
	private float bearing;
	private float zoom;
	
	public Map(MapFragment mapFragment, LatLng initialLocation) {
		this.mapFragment = mapFragment;
		map = mapFragment.getMap();
		// map.setPadding(MAP_PADDING, MAP_PADDING, MAP_PADDING, MAP_PADDING);
		initMapUiSettings();
		location = initialLocation;
		setProjectionMode(ProjectionMode.THREE_DIMENSIONAL);
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
	
	public Point getSize() {
		View mapView = mapFragment.getView();
		return new Point(mapView.getMeasuredWidth(), mapView.getMeasuredHeight());
	}
	
	public Marker addMarker(MarkerOptions options) {
		return map.addMarker(options);
	}
	
	public Projection getProjection() {
		return map.getProjection();
	}
	
	public void setOnCameraChangeListener(OnCameraChangeListener listener) {
		map.setOnCameraChangeListener(listener);
	}
	
	public void setOnProjectionModeChangeListener(OnProjectionModeChangeListener listener) {
		projectionModeListener = listener;
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
