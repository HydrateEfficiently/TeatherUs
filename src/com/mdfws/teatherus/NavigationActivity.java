package com.mdfws.teatherus;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class NavigationActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.main_map_view);
		GoogleMap map = mapFragment.getMap();
	}
}
