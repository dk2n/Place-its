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
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This class handles seesion-based authentication for the "JSON Server" Drupal module.
 * @author dk
 *
 */
public class UserLogin extends Activity implements OnClickListener {
	private String TAG = "USER_ACCOUNT";
	public static final String USER_URL = "http://task3abcdefg.appspot.com/username";
	public static final String LOCA_URL = "http://task3abcdefg.appspot.com/location";
	//public static final String REML_URI = "http://placeit32.appspot.com/product";
	//public static final String REMC_URI = "http://placeit32.appspot.com/item";
	
	// Table Data fields.
	public static final String USER_NAME  = "name";
	public static final String USER_ID    = "user";
	public static final String USER_PASS  = "password";
	public static final String MARK_LATI  = "Lattitude";
	public static final String MARK_LONG  = "Longtitude";
	public static final String REMI_TYPE  = "Type";
	public static final String REMI_REMI  = "Reminder";
	public static final String ACTION     = "action";
	public static final String INVALIDATE = "0";
	public static final String STATUS     = "0";
	
	
	private Button bt_login, bt_create; 				// User's options control.
	public static String accountName, accountPassword;	// User's info.
	EditText uname, pword;
	
	List<String> listUsername;
	List<String> listPassword;
	
	ArrayList<String> listOfUsername = new ArrayList<String>();
	private ProgressDialog dialog;				// Show actions.

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate( bundle );
		setContentView( R.layout.login_screen );

		uname = (EditText)findViewById( R.id.ed_username );
		pword = (EditText)findViewById( R.id.ed_password );

		//accountName     = uname.getText().toString();
    	bt_login = (Button)findViewById( R.id.bt_login );
    	bt_create = (Button)findViewById( R.id.bt_create );
    	bt_login.setOnClickListener( this );
    	bt_create.setOnClickListener( this );
    	
    	Log.d(TAG, "OnCreate" );
	}

	@Override
    public void onClick(View view) {
		if( view == bt_create ) {
			postLoginData();		// Insert new useraccount info to the database.			
			return;
		}
		if( view == bt_login ){
			//postLoginData();
			accountName     = uname.getText().toString();
			accountPassword = pword.getText().toString();
			UserAccountInfo task = new UserAccountInfo();
			task.execute(USER_URL);
			return;
		}	
		dialog = ProgressDialog.show(this, "Loading product data...", "Please wait...", false);
		dialog.show();
	
    }
	
	
	/** This function uses to create and load user's info to the web-database.
	 */
	private void postLoginData() {
		final ProgressDialog dialog = ProgressDialog.show(this,
				"Posting Data...", "Please wait...", false);
		Thread t = new Thread() {

			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpPost post1 = new HttpPost(USER_URL);

				accountName     = uname.getText().toString();
				accountPassword = pword.getText().toString();

			    try {
			      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			      nameValuePairs.add(new BasicNameValuePair( UserLogin.USER_NAME, accountName));
			      nameValuePairs.add(new BasicNameValuePair( UserLogin.USER_PASS, accountPassword));
			      nameValuePairs.add(new BasicNameValuePair( UserLogin.ACTION, "put"));
			      post1.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			 
			      client.execute(post1);
			    } catch (IOException e) {
			    	Log.d(TAG, "IOException while trying to conect to GAE");
			    }
				dialog.dismiss();
			}
		};

		t.start();
		dialog.show();
	} 

	public class UserAccountInfo extends AsyncTask<String, Void, List<String>> {
		 @Override
	     protected List<String> doInBackground(String... url) {

					HttpClient client = new DefaultHttpClient();
					HttpGet request = new HttpGet(USER_URL);
					listUsername = new ArrayList<String>();
					listPassword = new ArrayList<String>();
					//listofproduct = new ArrayList<String>();
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
								listUsername.add(obj.get("name").toString());
								listPassword.add(obj.get("password").toString());
							}

						} catch (JSONException e) {
					    	Log.d(TAG, "Error in parsing JSON");
						}

					} catch (ClientProtocolException e) {

				    	Log.d(TAG, "ClientProtocolException while trying to connect to GAE");
					} catch (IOException e) {

						Log.d(TAG, "IOException while trying to connect to GAE");
					}
	         return listUsername;
	     }

		 @Override
	     protected void onPostExecute(List<String> list) {
				super.onPostExecute(list);
				for( int i = 0; i < list.size(); i++ ) {
					Log.d("Login", accountName );
				    // Verify UserAccount Authentication.
					if( accountName.equals(listUsername.get(i))) {
				    	if( accountPassword.equals(listPassword.get(i))) {
				    		Intent myIntent = new Intent( UserLogin.this, MainActivity.class);
				    		myIntent.putExtra("accountKey", accountName);
				    		startActivity( myIntent );
				    	}
				    }
				}
	     }

	 }
}