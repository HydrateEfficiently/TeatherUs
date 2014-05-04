package com.mdfws.teatherus;

public interface NavigationEvents {
	
	void OnNavigatorReady(Navigator navigator);
	
	void OnGpsFound();
	
	void OnGpsSignalLost();
	
}
