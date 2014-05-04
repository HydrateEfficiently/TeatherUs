package com.mdfws.teatherus;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.BitmapFactory;
import android.os.Bundle;

public class NavigationDemo extends Activity {
	
	private NavigationFragment navigationFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation_demo);
		NavigationFragment navigationFragment = createNavigationFragment();
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.demo_navigation_fragment_container, navigationFragment);
		fragmentTransaction.commit();
	}
	
	private NavigationFragment createNavigationFragment() {
		return new NavigationFragment(
				new NavigationEvents() {
					@Override
					public void OnGpsFound() { navigationFragment_OnGpsFound(); }
					@Override
					public void OnGpsSignalLost() { navigationFragment_OnGpsLost(); }
					@Override
					public void OnNavigatorReady(Navigator navigator) { navigationFragment_OnNavigatorReady(navigator); }
				},
				new NavigationOptions()
					.vehicleOptions(new VehicleOptions()
							.image(BitmapFactory.decodeResource(getResources(), R.drawable.vehicle))));
	}
	
	private void navigationFragment_OnGpsFound() {
		// TODO: Implement me.
	}
	
	private void navigationFragment_OnGpsLost() {
		// TODO: Implement me.
	}
	
	private void navigationFragment_OnNavigatorReady(Navigator navigator) {
		navigator.navigateTo(new LatLng(-43.345998,172.66486));
	}

}
