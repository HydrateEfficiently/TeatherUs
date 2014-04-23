package com.mdfws.teatherus;

import android.app.Activity;

public class Navigator {
	
	private LocationTracker tracker;

	public Navigator(Activity activity, IMap map) {
		this.tracker = new LocationTracker(activity, map);
		this.tracker.enable();
		map.zoomTo(15);
	}
}
