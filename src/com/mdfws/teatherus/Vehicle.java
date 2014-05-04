package com.mdfws.teatherus;

import android.graphics.Bitmap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.mdfws.teatherus.positioning.Position;
import com.mdfws.teatherus.util.PointD;

public class Vehicle {
	
	private Position position;
	private Bitmap image;
	private PointD anchor;
	private Marker marker;
	private NavigationMap map;
		
	public Vehicle(NavigationMap map, VehicleOptions options) {
		this.map = map;
		position = options.position();
		image = options.image();
		anchor = options.anchor();
		marker = map.addVehicleMarker(this);
	}
	
	public void setPosition(Position position) {
		this.position = position;
		map.setVehiclePosition(position);
		marker.setPosition(position.location);
		marker.setRotation((float)position.bearing);
	}
	
	public Position getPosition() {
		return position;
	}
	
	public Bitmap getImage() {
		return image;
	}
	
	public PointD getAnchor() {
		return anchor;
	}
}
