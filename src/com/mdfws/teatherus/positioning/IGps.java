package com.mdfws.teatherus.positioning;

public interface IGps {
	
	public interface OnTickHandler {
		void invoke(Position position);
	}
	
	void enableTracking();
	
	void disableTracking();
	
	void forceTick();
	
	void onTick(OnTickHandler handler);
}
