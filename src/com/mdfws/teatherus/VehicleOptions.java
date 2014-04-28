package com.mdfws.teatherus;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

public class VehicleOptions {
	private LatLng location;
	private Bitmap image;
	private float anchorY = 0.5f;
	
	public VehicleOptions location(LatLng location) {
		this.location = location;
		return this;
	}
	
	public LatLng location() {
		return location;
	}
	
	public VehicleOptions image(Bitmap image) {
		this.image = image;
		return this;
	}
	
	public Bitmap image() {
		return image;
	}
	
	public VehicleOptions anchorY(float anchorY) {
		this.anchorY = anchorY;
		return this;
	}
	
	public float anchorY() {
		return anchorY;
	}
}