package com.example.place_its;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity; 
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle; 
import android.util.Log;
import android.view.View; 
import android.widget.AdapterView; 
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener; 
import android.widget.ArrayAdapter; 
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner; 
import android.widget.TextView;
import android.widget.Toast;


public class RemCActivity extends Activity {
	
	List<String> strList = new ArrayList<String>();
	//ArrayAdapter<String>adapter;
	//ArrayAdapter<String> listViewAdapter;
	String googlePlaces[];
	double lng1, lat1;
	@Override
	protected void onCreate( Bundle aBundle ) {
		super.onCreate(aBundle);
		setContentView( R.layout.category_activity );
		Bundle extras = getIntent().getExtras();
		lng1 = extras.getDouble("long1");
		lat1 = extras.getDouble("lat1");
		
		Spinner spinner  = (Spinner)  findViewById( R.id.catOpt);
		final EditText message = (EditText) findViewById( R.id.ed_des );
		ListView cateRem = (ListView) findViewById( R.id.lv_cat );
		Button   btReg   = (Button)   findViewById( R.id.register_item );
		

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( 
		this, R.array.googlePlaces, android.R.layout.simple_spinner_item);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				int index = arg0.getSelectedItemPosition();
				// storing string resources into Array 
				googlePlaces = getResources().getStringArray(R.array.googlePlaces);
				Toast.makeText(getBaseContext(), "You have selected : " + googlePlaces[index], 
						Toast.LENGTH_SHORT).show();
				if( index != 0 )
					strList.add( googlePlaces[index] );
			}
			public void onNothingSelected(AdapterView<?> arg0) { 
				// Nothing Selected.
			}
		});
		
		
		
		ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, strList);
		cateRem.setAdapter( listViewAdapter );
		cateRem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText( getApplicationContext(),
                        ((TextView) view).getText(), Toast.LENGTH_SHORT ).show();
                
                final String item = (String)adapterView.getItemAtPosition( i );
                AlertDialog.Builder alert = new AlertDialog.Builder( RemCActivity.this );
            	alert.setTitle( "Update the Reminder" );
            	final EditText input = new EditText( RemCActivity.this );
            	
            	alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            		@Override
            		public void onClick( DialogInterface dialog, int whichButton ) {
            			return;
            		}
            	});
            	// Click to delete the reminder in the list.
            	alert.setNegativeButton( "Delete", new DialogInterface.OnClickListener() {
            		@Override
                    //onClick method is used in order to delete the current marker from being displayed
            		public void onClick( DialogInterface dialog, int whichButton ) {
            			strList.remove(item);
            		}
            	}); 
                //displaying the alert
            	alert.show();
            }
        });
		
		
		// Register the data to the database.
		btReg.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String mesInput = message.getText().toString();
				if( strList.size() != 0 && !mesInput.equals(null)) {
					
					for( int i = 0; i < strList.size(); i++ ) {
						RemC aRem = new RemC( mesInput, strList.get(i));
						aRem.postReminderData();
					}
				}		// Store the reminder to the database.
				Intent myIntent = new Intent( RemCActivity.this, NewRemindersActivity.class);
				myIntent.putExtra("long", lng1);
				myIntent.putExtra("lat", lat1);
				startActivity(myIntent);
			}
		});
	}	
}
