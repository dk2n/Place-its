package com.example.place_its;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class RemLActivity extends Activity {
	//public static final String TAG = "AddItemActivity";
	private Spinner spinner;
	private EditText remDescription;
	ProgressDialog dialog;
	double lng1; 
	double lat1; 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		lng1 = extras.getDouble("long1");
		lat1 = extras.getDouble("lat1");

		setContentView(R.layout.additem);
		Button registerItem = (Button) findViewById(R.id.register_item);
	
		remDescription = (EditText) findViewById(R.id.ed_reminder);
		spinner = (Spinner) findViewById(R.id.item_spinner);
		
		registerItem.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//CustomMarkerListener aMarker = new CustomMarkerListener( lat1, lng1 );
				//aMarker.postMarkerData();		// Store that marker to the database.
				RemL aRem= new RemL( remDescription.getText().toString(), lat1, lng1 );
				aRem.postReminderData();				// Store the reminder to the database.
				Intent myIntent = new Intent(RemLActivity.this, NewRemindersActivity.class);
				myIntent.putExtra("long", lng1);
				myIntent.putExtra("lat", lat1);
				startActivity(myIntent);
			}
		});
	   
		dialog = ProgressDialog.show(this, "Loading data...", "Please wait :) ", false);
		dialog.dismiss();
		//new UpdateSpinnerTask().execute(UserLogin.USER_URL);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	 public class UpdateSpinnerTask extends AsyncTask<String, Void, List<String>> {
		 @Override
	     protected List<String> doInBackground(String... url) {
	    	 HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(UserLogin.LOCA_URL);
				List<String> list = new ArrayList<String>();
				try {
					HttpResponse response = client.execute(request);
					HttpEntity entity = response.getEntity();
					String data = EntityUtils.toString(entity);
					JSONObject myjson;
					try {
						myjson = new JSONObject(data);
						JSONArray array = myjson.getJSONArray("data");
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.getJSONObject(i);
							list.add(obj.get( UserLogin.USER_ID ).toString());
						}
					} catch (JSONException e) {
				    	Log.d("yo", "Error in parsing JSON");
					}
				} catch (ClientProtocolException e) {
			    	Log.d("yo", "ClientProtocolException while trying to connect to GAE");
				} catch (IOException e) {
					Log.d("yo", "IOException while trying to connect to GAE");
				}
	         return list;
	     }
	     protected void onPostExecute(List<String> list) {
				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
						android.R.layout.simple_spinner_item, list);
				dataAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(dataAdapter);
				dialog.dismiss();	
	     }
	 } 
}
