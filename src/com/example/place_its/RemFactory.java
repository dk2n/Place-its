package com.example.place_its;

import java.util.List;

import android.os.AsyncTask;

/**
 * Design Pattern requirement.
 */
public interface RemFactory {
	/*String message, dayLeft, placeType;
	
	public RemFactory( String message ) {
		this.message = message;
		//this.dayLeft = dayLeft;
	}
	public RemFactory( String message, String placeType ) {
		this.message = message;
		//this.dayLeft = dayLeft;
		this.placeType = placeType;
	}
	
	
	public String toString() {
		return this.message;// + "\n" + this.dayLeft;
	}*/
	
	//public void postReminderData( final double lat, final double lon, String type, String rem );
	//public void deleteReminderData( double lat, double lon, String type, String rem );
	public void postReminderData();
	public void deleteReminderData();
	/*
	private abstract class ReminderDataInfo extends AsyncTask<String, Void, List<Pair>> {
		protected abstract List<Pair> doInBackground(String... url);
		protected abstract void onPostExecute( List<Pair> list );
	}*/
}
