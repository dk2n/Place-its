package com.example.place_its;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class ProxAlert extends Service {
	
	String proxi;
	int n = 0;
	private BroadcastReceiver myBroadcast;
	private LocationManager   locationManager;
	MyLocationListener locationListenerp;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onCreate() {
		myBroadcast = new ProximityIntentReceiver();
		locationManager = (LocationManager)getSystemService( Context.LOCATION_SERVICE);
	}
	
	
	double lat, lng;
	float radius = 50f;
	long expiration = -1;
	
	
	public class ProximityIntentReceiver extends BroadcastReceiver {
		private static final int NOTIFICATION_ID = 1000;
		@Override
		public void onReceive(Context context, Intent intent) {
			String key = LocationManager.KEY_PROXIMITY_ENTERING;
			
			Boolean entering = intent.getBooleanExtra(key, false);
			String here = intent.getExtras().getString("alert");
			String happy = intent.getExtras().getString("type");
			
			NotificationManager notificationManager = (NotificationManager) getSystemService( context.NOTIFICATION_SERVICE );
			PendingIntent pendingIntent = PendingIntent.getActivity( context, 0, intent, 0);
			Notification notification = createNotification();
			notification.setLatestEventInfo( context,  "Entering Proximity!", "You are approaching a " + here + " marker.", pendingIntent);
			notificationManager.notify(NOTIFICATION_ID, notification);
		}
		
		private Notification createNotification() {
            Notification notification = new Notification();

            notification.icon = R.drawable.common_signin_btn_icon_dark;
            notification.when = System.currentTimeMillis();

            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.flags |= Notification.FLAG_SHOW_LIGHTS;

            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.defaults |= Notification.DEFAULT_LIGHTS;

            notification.ledARGB = Color.WHITE;
            notification.ledOnMS = 1500;
            notification.ledOffMS = 1500;
            return notification;
		}	
	}

	public class MyLocationListener implements LocationListener {
         public void onLocationChanged(Location location) {
             Toast.makeText(getApplicationContext(), "I was here", Toast.LENGTH_LONG).show();
         }

         public void onProviderDisabled(String s) {
         }
         public void onProviderEnabled(String s) {            
         }
         @Override
         public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
             // TODO Auto-generated method stub
         }
     }
}
