package com.mdfws.teatherus;

import com.google.android.gms.maps.model.LatLng;
import com.mdfws.teatherus.util.PointD;

public class NavigationMapOptions {
	
	private LatLng location = Defaults.LOCATION;
	private PointD anchor = new PointD(0.5, 0.5);
	
	public LatLng location() {
		return location;
	}
	
	public NavigationMapOptions location(LatLng location) {
		this.location = location;
		return this;
	}
	
	public PointD anchor() {
		return this.anchor;
	}
	
	public NavigationMapOptions anchor(PointD anchor) {
		this.anchor = anchor;
		return this;
	}
}
