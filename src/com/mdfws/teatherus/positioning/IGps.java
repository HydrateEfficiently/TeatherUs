package com.mdfws.teatherus.positioning;

import com.google.android.gms.maps.model.LatLng;

public interface IGps {
	
	public interface OnTickHandler {
		void invoke(Position position);
	}
	
	void enableTracking();
	
	void disableTracking();
	
	void forceTick();
	
	void onTick(OnTickHandler handler);
	
	LatLng getLastLocation();
}
