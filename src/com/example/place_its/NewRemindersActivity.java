package com.example.place_its;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * This class is used to control the reminder activities.
 * A user can select to add location or category reminder as they wish.
 * Show reminders will display all of the reminders that the user have.
 */
public class NewRemindersActivity extends Activity {
	public static final String TAG = "AddItemActivity";
	double lat, lng;
	//Intent myIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.reminder_main);
		Button bt_addRemLoc  = (Button) findViewById(R.id.bt_addRemLoc);
		Button bt_addRemCat  = (Button) findViewById(R.id.bt_addRemCat);
		Button bt_showRem    = (Button) findViewById(R.id.bt_showReminders);
		Button bt_finish     = (Button) findViewById(R.id.bt_finish);	
		
		// Get the Marker's position from the map.
		Bundle extras = getIntent().getExtras();
		lng = extras.getDouble("long");
		lat = extras.getDouble("lat");
		
		
		bt_addRemLoc.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(NewRemindersActivity.this, RemLActivity.class);
				myIntent.putExtra("long1", lng);
				myIntent.putExtra("lat1", lat);
				startActivity(myIntent);
			}
		});
		
		bt_addRemCat.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent( NewRemindersActivity.this, RemCActivity.class );
				myIntent.putExtra("long1", lng);
				myIntent.putExtra("lat1", lat);
				startActivity( myIntent );
			}
		});
		
		bt_showRem.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(NewRemindersActivity.this, ShowRemActivity.class);
				startActivity(myIntent);
			}
		});
		
		bt_finish.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(NewRemindersActivity.this, MainActivity.class);
				startActivity(myIntent);
			}
		});
	}
	
	protected void onResume() {
		super.onResume();
		Log.d("New Reminder", "this is line is on resume");
	}
}
