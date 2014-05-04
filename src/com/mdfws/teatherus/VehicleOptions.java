package com.mdfws.teatherus;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.mdfws.teatherus.util.PointD;

public class VehicleOptions {
	private LatLng location;
	private Bitmap image;
	private PointD anchor = new PointD(0.5d, 0.9d);
	
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
	
	public VehicleOptions anchor(PointD anchor) {
		this.anchor = anchor;
		return this;
	}
	
	public PointD anchor() {
		return anchor;
	}
}