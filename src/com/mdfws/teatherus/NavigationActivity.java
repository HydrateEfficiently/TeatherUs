package com.mdfws.teatherus;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.LatLng;
import com.mdfws.teatherus.directions.Direction;
import com.mdfws.teatherus.positioning.DebugSimulatedGps;
import com.mdfws.teatherus.positioning.IGps;
import com.mdfws.teatherus.positioning.SimulatedGps;

import android.app.Activity;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.WindowManager;

public class NavigationActivity extends Activity implements
	ConnectionCallbacks,
	OnConnectionFailedListener {
	
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	
	private LocationClient locationClient;
	private Navigator navigator;
	private IGps gps;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		locationClient = new LocationClient(this, this, this);
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (connectionResult.hasResolution()) {
			try {
				connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
			} catch (SendIntentException ex) {
				ex.printStackTrace();
			}
		} else {
			// TODO: Handler errors with dialog
		}
	}

	@Override
	public void onConnected(Bundle dataBundle) {
		LatLng initialLocation = new LatLng(-43.463292,172.620614); //GoogleUtil.toLatLng(locationClient.getLastLocation());
		
		// gps = new Gps(locationClient);
		gps = new SimulatedGps(initialLocation);
//		Resources res = getResources();
//		int resourceId = res.getIdentifier("navigator_0tilt" , "drawable", getPackageName());
//		Drawable drawable = res.getDrawable(resourceId);
		
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.vehicle);
		VehicleOptions options = new VehicleOptions().anchorY(0.5f).image(bitmap).location(initialLocation);
		navigator = new Navigator(this, gps, options, new NavigatorEvents() {

			@Override
			public void OnVehicleOffRoute() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void OnArrival() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void OnNewDirection(Direction direction) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void OnUpdate(UpdateEventArgs args) {
				// TODO Auto-generated method stub
				
			}
			
		});
		navigator.navigateTo(new LatLng(-43.345998,172.66486));
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
