package com.mdfws.teatherus;

import android.app.Activity;
import android.os.Bundle;

public class NavigationActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		GoogleMapWrapper map = new GoogleMapWrapper(getFragmentManager().findFragmentById(R.id.main_map_view));
		Navigator navigator = new Navigator(this, map);
	}
}
