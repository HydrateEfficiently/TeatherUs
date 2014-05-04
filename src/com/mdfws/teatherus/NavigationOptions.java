package com.mdfws.teatherus;

import com.mdfws.teatherus.map.MapOptions;

public class NavigationOptions {
	
	private VehicleOptions vehicleOptions = new VehicleOptions();
	private MapOptions mapOptions = new MapOptions();
	
	public NavigationOptions vehicleOptions(VehicleOptions options) {
		vehicleOptions = options;
		return this;
	}
	
	public VehicleOptions vehicleOptions() {
		return vehicleOptions;
	}
	
	public NavigationOptions mapOptions(MapOptions options) {
		mapOptions = options;
		return this;
	}
	
	public MapOptions mapOptions() {
		return mapOptions;
	}
}
