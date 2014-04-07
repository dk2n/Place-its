package com.example.place_its;

/*
 * TEAM 32
 * Isabella Do, Anh Dang, Duy Le, Chao Li
 * Derek Nguyen ,Yevgeniy Galipchak
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.place_its.MainActivity;
import com.example.place_its.NewRemindersActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/*
 * This is the main class. This class is used to display the user's current location. 
 * Then fetch the database, and display the reminders on the map base on the coordinates.
 * The categorical reminders will not displayed on the map but still be recorded its
 * location responsible by the google API. 
 * If there is any reminders come within the user's radius, a notification will pop-up,
 * and user can delete or re-post it as will.
 */
public class MainActivity extends Activity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener,
		OnMapClickListener, CancelableCallback {

	
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	// Milliseconds per second
	private static final int MILLISECONDS_PER_SECOND = 1000;
	// Update frequency in seconds
	public static final int UPDATE_INTERVAL_IN_SECONDS = 1;
	// Update frequency in milliseconds
	private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND
			* UPDATE_INTERVAL_IN_SECONDS;
	// The fastest update frequency, in seconds
	private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
	// A fast frequency ceiling in milliseconds
	private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND
			* FASTEST_INTERVAL_IN_SECONDS;

	// Define an object that holds accuracy and frequency parameters
	LocationRequest mLocationRequest;
	boolean mUpdatesRequested;
	private SharedPreferences.Editor mEditor;
	private SharedPreferences mPrefs;
	private LocationClient mLocationClient;
	private GoogleMap mMap;
	//private TextView mLatLong;
	private LatLng pos;
	private List<Marker> mMarkers = new ArrayList<Marker>();
	private Iterator<Marker> marker;
	Intent intentService;
	private Marker aMarker;
	//private static final String LOGCAT = "MainActivity";
	private double userLat, userLog;
	private final double MIN_RADIUS_CIRCLE = 500;
	private final double MAX_RADIUS_CIRCLE = 5000;
	CustomMarkerListener markerListener;
	ProgressDialog dialog;
	private static int counter = 0;
	
	public static List<String> googleTypePlaces;
	ArrayList<Marker> mMarkerList;
	/*
	 * onCreate method holds the initial clicks which will be toggled depending
	 * on the users clicks. Holds decisions that will lead to creation of reminder.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// loading the markers
		dialog = ProgressDialog.show(this, "Loading markers...", "Please wait :)", false);
	    dialog.show();
		
        //initializaiton of nMap
		setUpMapIfNeeded();
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		mMap.setMyLocationEnabled(true);
		mMap.setOnMapClickListener(this);
		Button myButton = (Button) findViewById( R.id.refresh );
		Button myButton2 = (Button) findViewById( R.id.showrem );
		myButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	//counter++;
		    	Log.i("COUNTER:  ", "The counter is: " + counter );
		        startActivity( new Intent( MainActivity.this, MainActivity.class ));
		    }
		});

		myButton2.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Intent inte =  new Intent( MainActivity.this, NewRemindersActivity.class );
		    	inte.putExtra("long", 0);
		    	inte.putExtra("lat", 0);
		    	startActivity( inte );
		    }
		});
		
		
		Button btLogout = (Button) findViewById( R.id.logout );
		btLogout.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	startActivity( new Intent( MainActivity.this, UserLogin.class ));
		    }
		});
		
		
		MarkerDataInfo markerInfo = new MarkerDataInfo();
		markerInfo.execute( UserLogin.USER_URL );	
		
		//OnMarkerCLickListener
		mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(final Marker marker) {	
				// TODO: delete or re-post feature.
				AlertDialog.Builder alert3 = new AlertDialog.Builder(MainActivity.this);
				alert3.setTitle("Do you want to delete?");
				alert3.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						//removal of aMarker from the map
						aMarker = marker;
						aMarker.remove();
				    	mMarkers.remove(aMarker);
				    	//Verification of deletion.
					    Toast.makeText(MainActivity.this, "Marker deleted!", Toast.LENGTH_SHORT).show();
					}
				});
			
				alert3.show();
			  return false;			
			}  
		});
		
		
		
		//mLatLong = (TextView) findViewById(R.id.latLong);
		// Create the LocationRequest object
		mLocationRequest = LocationRequest.create();
		// Use high accuracy
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		// Set the update interval to 5 seconds
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		// Set the fastest update interval to 1 second
		mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
		// Open the shared preferences
		mPrefs = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
		// Get a SharedPreferences editor
		mEditor = mPrefs.edit();
		// Start with updates turned on
		mUpdatesRequested = true;
		mLocationClient = new LocationClient(this, this, this);		
	}
	
	public void googlePlaceServices( final List<String> googlePlace ) {
		mMarkerList = new ArrayList<Marker>();
		
		mMap.setOnCameraChangeListener( new GoogleMap.OnCameraChangeListener() {
			@Override
			public void onCameraChange( CameraPosition cameraPosition ) {
				double centerLat = cameraPosition.target.latitude;
				double centerLng = cameraPosition.target.longitude;
				
				//String placeType = "cafe";								// Category type (Hard Coding)
				String radius    = "800";
				for( int i = 0; i < mMarkerList.size(); i++ ) {
					Marker marker = mMarkerList.get(i);
					marker.remove();
				}
				mMarkerList.clear();
				
				for( int i = 0; i < googlePlace.size(); i++ ) {
					String placeType = googlePlace.get(i);
					AQuery aQuery = new AQuery(getApplicationContext());
					String urlString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
						+ "location=" + centerLat + "," + centerLng + "&radius="
						+ radius + "&types=" + placeType + "&sensor=true&"
						+ "key=AIzaSyDiEzxx3aJ3aP2wSuGAlxfcQnduwqB8cto";
					aQuery.ajax( urlString, JSONObject.class, new AjaxCallback<JSONObject>(){
						@Override
						// Automatically update the place location on the map.
						public void callback( String url, JSONObject jsonObject, AjaxStatus ajaxStatus) {
							if( jsonObject != null ) {
								Log.v("myapp", "jsonObject = " + jsonObject );
								try{
									JSONArray jsonArray = jsonObject.getJSONArray("results");
									//for( int i = 0; i < mMarkerList.size(); i++ ) {
									//	Marker marker = mMarkerList.get(i);
										//marker.remove();
									//}
									//mMarkerList.clear();
									for( int i = 0; i < jsonArray.length(); i++ ) {
										JSONObject latLngObj = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
										double lat = latLngObj.getDouble("lat");
										double lng = latLngObj.getDouble("lng");
									
										MarkerOptions markerOptions = new MarkerOptions();
										markerOptions.position(new LatLng(lat, lng));
										Marker tmpMarker = mMap.addMarker(markerOptions);
										//if( counter % 2 == 0 )
											tmpMarker.setVisible( true );
										//else if( counter % 2 == 1 )
											//tmpMarker.setVisible(false);
										mMarkerList.add(tmpMarker);  // Need it here for verify the code.
									}
								} catch( JSONException e ) {
									e.printStackTrace();
								}
							
							}
						
						}
					});		
				}
			}
		});
					
	}
	
	/**
	 * This clas is used to explore the database very time user come this screen.
	 * If there is any reminder on the database, it will display on the map.
	 *
	 */
	private class MarkerDataInfo extends AsyncTask<String, Void, List<String>> {
		List<Pair> coorList; 
		List<String> msgList;
		@Override
	     protected List<String> doInBackground(String... url) {
			 		Log.d ("EXECUTE 2 time", "");
					HttpClient client = new DefaultHttpClient();
					HttpGet request = new HttpGet( UserLogin.LOCA_URL);
					List<String> list = new ArrayList<String>();
					msgList = new ArrayList<String>();
					coorList = new ArrayList<Pair>();
					try {
						HttpResponse response = client.execute(request);
						HttpEntity entity = response.getEntity();
						String data = EntityUtils.toString(entity);
						JSONObject myjson;
						try {
							//Bundle extras = getIntent().getExtras();
							myjson = new JSONObject(data);
							JSONArray array = myjson.getJSONArray("data");
							for (int i = 0; i < array.length(); i++) {																							
								JSONObject obj = array.getJSONObject(i);				
								String userKey = obj.get( UserLogin.USER_ID ).toString();				
								if ( userKey.equals( UserLogin.accountName )) {								
									String k1 = obj.get( UserLogin.MARK_LATI ).toString();
									String k2 = obj.get( UserLogin.MARK_LONG ).toString();
									String typeKey = obj.get( UserLogin.REMI_TYPE ).toString();
									if( !k1.equals( UserLogin.INVALIDATE ) && !k2.equals( UserLogin.INVALIDATE )) {
										double x = Double.parseDouble(k1);
										double y = Double.parseDouble(k2);
										coorList.add( new Pair( x, y ));
										msgList.add( obj.get(UserLogin.REMI_REMI).toString());
									}
									if( !typeKey.equals("")) {
										list.add( typeKey );
									}
								}
							}
						} catch (JSONException e) {
					    	Log.d("print", "Error in parsing JSON");
						}
					} catch (ClientProtocolException e) {
				    	Log.d("print", "ClientProtocolException while trying to connect to GAE");
					} catch (IOException e) {
						Log.d("print", "IOException while trying to connect to GAE");
					}
	         return list;
	     }
	     protected void onPostExecute(List<String> list) {	    	 
				for (int k = 0; k < coorList.size(); k++)  {	
					aMarker = mMap.addMarker(new MarkerOptions()
					.position(new LatLng(coorList.get(k).x, coorList.get(k).y))
					.title( msgList.get(k) ));
					mMarkers.add(aMarker);
					Log.d("Main Activity", "" + list.get(k));
				}			
				googlePlaceServices(list);
				dialog.dismiss();
	     }
	 }
    
	
	/**
	 * onMapClick method used to display the option of adding a new reminder.
	 */
	@Override
	public void onMapClick( final LatLng position) {
		pos = position;
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Add a new Reminder");
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			    Intent intentService = new Intent( MainActivity.this, NewRemindersActivity.class );
			    intentService.putExtra("long", position.longitude);
				intentService.putExtra("lat", position.latitude);
				startActivity( intentService );
			}
		});
		//cancelling option
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Toast.makeText(MainActivity.this, "Nothing added!",
						Toast.LENGTH_SHORT).show();
					}
		});
		alert.show();
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
    //onCreateOptionMenu which inflates the menu of the database
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//Inflate the menu; this adds items to the action bar if it is present
		getMenuInflater().inflate( R.menu.main, menu);
		return true;
	}

    //onPause which holds all of the actions which are bound by being on pause
	@Override
	protected void onPause() {
		super.onPause();
		// Save the current setting for updates
		mEditor.putBoolean("KEY_UPDATES_ON", mUpdatesRequested);
		mEditor.commit();
	}

    //onStart which holds all of the actions which are bound by being on start
	@Override
	protected void onStart() {
		super.onStart();
		mLocationClient.connect();
	}

    //onResume which holds all of the actions which are bound by being on resume,
    //and this method also generates the new marker on the map.
	@Override
	protected void onResume() {
		//Log.d("print" , "" + pos.latitude);
		super.onResume();
		/*
		if (AddItemActivity.checkMe() == true)
		{
			dialog = ProgressDialog.show(this, "Loading markers...", "Please wait :)", false);
		    dialog.show();
			new data().execute(MainActivity.ITEM_URI);
		}
		*/
	    
	}

    //setUpMapIfNeeded does a null check to confirm if we already have the map up
	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
		    // The Map is verified. It is now safe to manipulate the map.
			}
		}
	}

    //notifyUser is the actual notification that is displayed on the android drop
    //down list letting the user know that they are near their reminder.
	public void notifyUser() {
		//Creation of the mBuilder
		NotificationCompat.Builder mBuilder =
		new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle("I am a reminder")
		.setContentText("...within a reminder, within a reminder...");

		// Sets an ID for the notification
		int mNotificationId = 001;
		// Gets an instance of the NotificationManager service
		NotificationManager mNotifyMgr = 
		(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// Builds the notification and issues it.
		mNotifyMgr.notify(mNotificationId, mBuilder.build());
		}

	// Define the callback method that receives location updates
	@Override
	public void onLocationChanged(Location location) {
		// Report to the UI that the location was updated
		userLat = location.getLatitude();
		userLog = location.getLongitude();
		//mLatLong.setText("Lat: " + userLat + " Long: " + userLog);	
		marker = mMarkers.iterator();
		
		//A continuous check of the markers to calculate their magnitude in relation
		//to the target users location.
		while (marker.hasNext()) {
			Marker current = marker.next();
			//Check to see if the user is within the bounds of notification.
			if(calDistance( userLat, userLog, current ) < MIN_RADIUS_CIRCLE ) {
				counter++;
				//Prevention of spamming the reminder an infinite amount of times
				if( counter == 1 ) {
					//Intent i = new Intent( MainActivity.this, StatusBar.class );
					notifyUser();
			    //resetting the count number
				} else 
					counter = 2;
			} else if( calDistance( userLat, userLog, current ) > MAX_RADIUS_CIRCLE ) {
				counter = 0;
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
			Toast.makeText(this, "FAILURE!", Toast.LENGTH_LONG).show();
		}
	}

	/*
	 * Called by Location Services when the request to connect the client
	 * finishes successfully. At this point, you can request the current
	 * location or start periodic updates
	 */
	@Override
	public void onConnected(Bundle dataBundle) {
		// Display the connection status
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
		// If already requested, start periodic updates
		if (mUpdatesRequested) {
			mLocationClient.requestLocationUpdates(mLocationRequest, this);
		}
	}

	/*
	 * Called by Location Services if the connection to the location client
	 * drops because of an error.
	 */
	@Override
	public void onDisconnected() {
		// Display the connection status
		Toast.makeText(this, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();
	}

    //onFinish method which holds the functions needed during the onFinish process
    //of the program.
	@Override
	public void onFinish() {
		//run while there is a marker in existance
		if (marker.hasNext()) {
			Marker current = marker.next();
			mMap.animateCamera(
					CameraUpdateFactory.newLatLng(current.getPosition()), 2000,
					this);
			current.showInfoWindow();
		}
	}

    //Empty onCancel method
	@Override
	public void onCancel() {
	}
	
	
	//calDistance is uder in order to calculate the magnitude of the users location
	//in relation to the reminder that is set.
    public  double calDistance(double Lat_cu, double Long_cu, Marker whatMarker) {
    	//initialized variables used for the magnitude calculation
		LatLng position = whatMarker.getPosition();
		double Lat_marker = position.latitude;
		double Long_marker = position.longitude;
    	double dLat = Math.toRadians(Lat_marker-Lat_cu);
    	double dLon = Math.toRadians(Long_marker-Long_cu);
    	double lat1 = Math.toRadians(Lat_cu);
    	double lat2 = Math.toRadians(Lat_marker);
        //Magnitude calculation
    	double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
    	        Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
    	double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
    	double d = 6371.0 * c;
    	//returning the result
    	return d * 1000 ;
    }


}