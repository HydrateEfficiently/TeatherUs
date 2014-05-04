package com.mdfws.teatherus;

import java.util.List;

import android.graphics.Point;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mdfws.teatherus.util.PointD;

public class NavigationMap {
	
	public enum MapMode {
		NAVIGATING,
		IDLE
	}
	
	private static final float NAVIGATING_TILT = 60;
	private static final float NAVIGATING_ZOOM = 18;
	private static final float IDLE_TILT = 0;
	private static final float IDLE_BEARING = 0;
	private static final float IDLE_ZOOM = 3;
	
	private MapFragment mapFragment;
	private GoogleMap map;
	private CameraPositionFactory cameraPositionFactory;
	private UiSettings mapUiSettings;
	
	private LatLng location;
	private float bearing;
	private PointD anchor;
	private Marker vehicleMarker;
	private Polyline polylinePath;
	private MapMode mapMode;
	
	public NavigationMap(MapFragment mapFragment, NavigationMapOptions options) {
		this.mapFragment = mapFragment;
		map = mapFragment.getMap();
		cameraPositionFactory = new CameraPositionFactory(map);
		location = options.location();
		anchor = options.anchor();
		initialiseUiSettings();		
		map.setMyLocationEnabled(true);
		map.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {
			@Override
			public boolean onMyLocationButtonClick() {
				return false;
			}
		});
	}
	
	private void initialiseUiSettings() {
		mapUiSettings = map.getUiSettings();
		mapUiSettings.setZoomControlsEnabled(false);
		mapUiSettings.setCompassEnabled(false);
		mapUiSettings.setRotateGesturesEnabled(false);
	}
	
	public Marker addVehicleMarker(Vehicle vehicle) {
		removeVehicleMarker();
		vehicleMarker = map.addMarker(new MarkerOptions()
				.position(vehicle.getLocation())
				.icon(BitmapDescriptorFactory.fromBitmap(vehicle.getImage()))
				.flat(true));
		anchor = vehicle.getAnchor();
		return vehicleMarker;
	}
	
	public void removeVehicleMarker() {
		if (vehicleMarker != null) {
			vehicleMarker.remove();
		}
	}
	
	public Polyline addPathPolyline(List<LatLng> path) {
		removePolylinePath();
		polylinePath = map.addPolyline(new PolylineOptions().addAll(path).color(0xff3073F0));
		return polylinePath;
	}
	
	public void removePolylinePath() {
		if (polylinePath != null) {
			polylinePath.remove();
		}
	}
	
	public void setMapMode(MapMode mode) {
		boolean isNavigating = mode == MapMode.NAVIGATING;
		mapUiSettings.setTiltGesturesEnabled(isNavigating);
		CameraPosition defaultPosition = isNavigating ?
				cameraPositionFactory.newCameraPosition(NAVIGATING_ZOOM, NAVIGATING_TILT, bearing) :
				cameraPositionFactory.newCameraPosition(IDLE_ZOOM, IDLE_TILT, IDLE_BEARING);
		map.moveCamera(CameraUpdateFactory.newCameraPosition(defaultPosition));
	}
	
	public Point getSize() {
		View mapView = mapFragment.getView();
		return new Point(mapView.getMeasuredWidth(), mapView.getMeasuredHeight());
	}
	
	public LatLng getLocation() {
		return location;
	}
	
	public void setLocation(LatLng location) {
		Point size = getSize();
		PointD anchorOffset = new PointD(size.x * (0.5 - anchor.x), size.y * (0.5 - anchor.y));
		PointD screenCenterWorldXY = SphericalMercatorProjection.latLngToWorldXY(location, getZoom());
		PointD newScreenCenterWorldXY = new PointD(screenCenterWorldXY.x + anchorOffset.x, screenCenterWorldXY.y + anchorOffset.y);
		newScreenCenterWorldXY.rotate(screenCenterWorldXY, bearing);
		LatLng offsetLocation = SphericalMercatorProjection.worldXYToLatLng(newScreenCenterWorldXY, getZoom());
		this.location = offsetLocation;
	}
	
	public double getBearing() {
		return bearing;
	}
	
	public void setBearing(float bearing) {
		this.bearing = bearing;
	}
	
	public float getZoom() {
		return map.getCameraPosition().zoom;
	}
	
	public void update() {
		map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPositionFactory.newCameraPosition(location, bearing)));
	}
	
	public void setOnCameraChangeListener(OnCameraChangeListener listener) {
		map.setOnCameraChangeListener(listener);
	}
}
