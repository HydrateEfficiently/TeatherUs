package com.mdfws.teatherus;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.os.Bundle;

public class NavigationActivity extends Activity implements
	GooglePlayServicesClient.ConnectionCallbacks,
	GooglePlayServicesClient.OnConnectionFailedListener {
	
	private LocationClient locationClient;
	private Navigator navigator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		
		locationClient = new LocationClient(this, this, this);
		Map map = new Map(getFragmentManager().findFragmentById(R.id.main_map_view));
		navigator = new Navigator(this, map, locationClient);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onConnected(Bundle dataBundle) {
		navigator.locationClientReady();
		navigator.navigateTo(new LatLng(-43.530707 ,172.641946));
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
	}
	
    @Override
    protected void onStart() {
        super.onStart();
        locationClient.connect();
    }

    @Override
    protected void onStop() {
    	locationClient.disconnect();
        super.onStop();
    }
}
