package com.mdfws.teatherus;

import com.google.android.gms.maps.model.LatLng;
import com.mdfws.teatherus.directions.Direction;

public interface NavigatorEvents {
	void OnVehicleOffRoute();
	void OnArrival();
	void OnNewDirection(Direction direction);
	void OnUpdate(UpdateEventArgs args);
	
	public class UpdateEventArgs {
		
		public UpdateEventArgs(LatLng locationOnRoute, double bearingOnRoute, int timeToArrival, int distanceToArrival, Direction direction) {
			this.locationOnRoute = locationOnRoute;
			this.bearingOnRoute = bearingOnRoute;
			this.timeToArrival = timeToArrival;
			this.distanceToArrival = distanceToArrival;
			this.direction = direction;
		}
		
		public LatLng locationOnRoute;
		public double bearingOnRoute;
		public int timeToArrival;
		public int distanceToArrival;
		public Direction direction;
	}
}
