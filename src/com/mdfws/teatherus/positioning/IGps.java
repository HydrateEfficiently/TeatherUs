package com.mdfws.teatherus.positioning;

import android.location.Location;

public interface IGps {
	
	public interface OnTickHandler {
		void invoke(Location location);
	}
	
	void enableTracking();
	
	void disableTracking();
	
	void forceTick();
	
	void onTick(OnTickHandler handler);
}
