package com.example.place_its;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowRemActivity extends Activity implements View.OnClickListener {
	
	public static final String TAG = "ShowItemActivity";

	ProgressDialog dialog;
	ArrayAdapter<String> stringArrayAdapter;
	ListView listview;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView (R.layout.return_item);
		listview = (ListView) findViewById(R.id.mainListView);
		listview.setAdapter(stringArrayAdapter);
		dialog = ProgressDialog.show(this, "Loading data...", "Please wait :) ", false);
	    dialog.show();
		new LoadData().execute( UserLogin.LOCA_URL );
		
	}
	private class LoadData extends AsyncTask<String, Void, List<String>> {
		 @Override
	     protected List<String> doInBackground(String... url) {
					HttpClient client = new DefaultHttpClient();
					HttpGet request = new HttpGet( UserLogin.LOCA_URL);
					List<String> list = new ArrayList<String>();
					try {
						HttpResponse response = client.execute(request);
						HttpEntity entity = response.getEntity();
						String data = EntityUtils.toString(entity);
						Log.d(TAG, data);
						JSONObject myjson;

						try {
							myjson = new JSONObject(data);
							JSONArray array = myjson.getJSONArray("data");
							for (int i = 0; i < array.length(); i++) {
								JSONObject obj = array.getJSONObject(i);
								if( obj.get( UserLogin.USER_ID).toString().equals( UserLogin.accountName )) {
									list.add(obj.get( UserLogin.REMI_REMI ).toString());
								}
							}
						} catch (JSONException e) {
					    	Log.d(TAG, "Error in parsing JSON");
						}
					} catch (ClientProtocolException e) {
				    	Log.d(TAG, "ClientProtocolException while trying to connect to GAE");
					} catch (IOException e) {
						Log.d(TAG, "IOException while trying to connect to GAE");
					}
	         return list;
	     }

	     protected void onPostExecute(List<String> list) {
				ArrayAdapter<String> dataAdapter = new
						ArrayAdapter<String>(getApplicationContext(),
						android.R.layout.simple_expandable_list_item_1, list);
				listview.setAdapter(dataAdapter);
				dialog.dismiss();
				
				
				
				listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			            @Override
			            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
			                Toast.makeText( getApplicationContext(),
			                        ((TextView) view).getText(), Toast.LENGTH_SHORT ).show();
			                
			                // Click on a specific reminder on the list.
			                final String remMsg = (String)adapterView.getItemAtPosition( i );
			                AlertDialog.Builder alert = new AlertDialog.Builder( ShowRemActivity.this );
			            	alert.setTitle( "Update the Reminder" );
			            	final EditText input = new EditText( ShowRemActivity.this );
			            	input.setText( remMsg);
			            	final String msg = input.getText().toString(); 
			            	alert.setView( input );
			            	// Update the reminder
			            	alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
			            		@Override
			            	  public void onClick( DialogInterface dialog, int whichButton ) {
			            			       	   
			            			Toast.makeText( ShowRemActivity.this, "Updated", Toast.LENGTH_SHORT).show();		
			            		}
			            	});
			            	// Click to delete the reminder in the list.
			            	alert.setNegativeButton( "Delete", new DialogInterface.OnClickListener() {
			            		@Override
			                    //onClick method is used in order to delete the current marker from being displayed
			            		public void onClick( DialogInterface dialog, int whichButton ) {
			            			postDeleteData( remMsg);
			            			Toast.makeText( ShowRemActivity.this, "deleted", Toast.LENGTH_SHORT).show();
			               		}
			            	}); 
			                //displaying the alert
			            	alert.show();
			            }
			        });
				 
	     }
	 }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
	/*
	private void postDeleteData( String message ) {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(UserLogin.LOCA_URL);
		//List<String> list = new ArrayList<String>();
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
					String user = obj.get( UserLogin.USER_ID ).toString();
					String msgs = obj.get( UserLogin.REMI_REMI ).toString();
					if( user.equals( UserLogin.accountName ) && msgs.equals( message )) {
						obj.remove(user);
					}
				}
			} catch (JSONException e) {
		    	Log.d("yo", "Error in parsing JSON");
			}
		} catch (ClientProtocolException e) {
		   	Log.d("yo", "ClientProtocolException while trying to connect to GAE");
		} catch (IOException e) {
			Log.d("yo", "IOException while trying to connect to GAE");
		} 
	 } */
	
	
	private void postDeleteData( final String message ) {
		Thread t = new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost( UserLogin.LOCA_URL);
			    try {
			      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			      nameValuePairs.add(new BasicNameValuePair( "id", message ));
			      nameValuePairs.add(new BasicNameValuePair( "action", "delete" ));
			      
			      post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			  
			      client.execute(post);
			      } catch (IOException e) {	Log.d(TAG, "IOException while trying to conect to GAE"); }
			}
		};
		t.start();
		
	 }
}