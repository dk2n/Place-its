package com.example.place_its;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class RemL implements RemFactory {

	protected static final String TAG = null;

	String message;
	double lat, lon;
	public RemL( String message, double lat, double lon ) {
		this.message = message;
		this.lat = lat;
		this.lon = lon;
	}
	
	public String toString() {
		return this.message;
	}

	@Override
	public void postReminderData() {
		Thread t = new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost( UserLogin.LOCA_URL);
			    try {
			      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			      nameValuePairs.add(new BasicNameValuePair( UserLogin.USER_ID, UserLogin.accountName ));
			      nameValuePairs.add(new BasicNameValuePair( UserLogin.MARK_LATI, String.valueOf(lat)));
			      nameValuePairs.add(new BasicNameValuePair( UserLogin.MARK_LONG, String.valueOf(lon)));
			      nameValuePairs.add(new BasicNameValuePair( UserLogin.REMI_TYPE, UserLogin.INVALIDATE));
			      nameValuePairs.add(new BasicNameValuePair( UserLogin.REMI_REMI, message ));
			      nameValuePairs.add(new BasicNameValuePair( UserLogin.ACTION, "put"));
			      post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			  
			      HttpResponse response = client.execute(post);
			      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			      String line = "";
			      while ((line = rd.readLine()) != null) {    Log.d(TAG, line);       }
			    } catch (IOException e) {	Log.d(TAG, "IOException while trying to conect to GAE"); }
			}
		};
		t.start();
		
	}
	@Override
	public void deleteReminderData() {
		
	}

}
