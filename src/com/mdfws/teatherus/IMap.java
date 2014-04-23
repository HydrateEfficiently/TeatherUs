package com.mdfws.teatherus;

import android.location.Location;

public interface IMap {
	void setLocation(Location location);
	void zoomTo(float zoom);
}
