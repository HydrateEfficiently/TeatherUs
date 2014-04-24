package com.mdfws.teatherus.util;

import com.google.android.gms.maps.model.LatLng;

public class Geom {

	public static double calculateDistance(LatLng to, LatLng from) {
		double d2r = Math.PI / 180;
		double dLong = (to.longitude - from.longitude) * d2r;
		double dLat = (to.latitude - from.latitude) * d2r;
		double a = Math.pow(Math.sin(dLat / 2.0), 2) + Math.cos(from.latitude * d2r)
					* Math.cos(to.latitude * d2r) * Math.pow(Math.sin(dLong / 2.0), 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return 6367000 * c;
	}
}
