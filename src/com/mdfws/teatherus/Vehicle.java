package com.mdfws.teatherus;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.mdfws.teatherus.map.Map;

public class Vehicle {
	
	private LatLng location;
	private Bitmap image;
	private float anchorY;
	private Marker marker;
	private float heading;
	private Map map;
		
	public Vehicle(Map map, VehicleOptions options) {
		this.map = map;
		location = options.location();
		image = options.image();
		anchorY = options.anchorY();
		heading = 0f;
		
		marker = map.addMarker(new MarkerOptions()
				.position(location)
				.icon(BitmapDescriptorFactory.fromBitmap(image))
				.flat(true));
		
		map.setOnCameraChangeListener(new OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition cameraPosition) {
				marker.setPosition(location);
				marker.setRotation(heading);
			}
		});
	}
	
	public void setLocation(LatLng location) {
		this.location = location;
		map.setLocation(location);
	}
	
	public LatLng getLocation(LatLng location) {
		return location;
	}
	
	public void setHeading(float heading) {
		this.heading = heading;
		map.setBearing(heading);
	}
	
	public float getHeading() {
		return heading;
	}
}
