package com.mdfws.teatherus.map;

import com.google.android.gms.maps.model.LatLng;
import com.mdfws.teatherus.Defaults;
import com.mdfws.teatherus.util.PointD;

public class MapOptions {
	
	private LatLng location = Defaults.LOCATION;
	private PointD anchor = new PointD(0.5, 0.5);
	
	public LatLng location() {
		return location;
	}
	
	public MapOptions location(LatLng location) {
		this.location = location;
		return this;
	}
	
	public PointD anchor() {
		return this.anchor;
	}
	
	public MapOptions anchor(PointD anchor) {
		this.anchor = anchor;
		return this;
	}
}
