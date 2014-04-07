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

public class RemC implements RemFactory {

	protected static final String TAG = null;

	String message, type;	
	public RemC( String message, String type ) {
		this.message = message;
		this.type = type;
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
			      nameValuePairs.add(new BasicNameValuePair( UserLogin.MARK_LATI, UserLogin.INVALIDATE));
			      nameValuePairs.add(new BasicNameValuePair( UserLogin.MARK_LONG, UserLogin.INVALIDATE));
			      nameValuePairs.add(new BasicNameValuePair( UserLogin.REMI_TYPE, type ));
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
	/*
	@Override
	public void postReminderData( final double lat, final double lon, final String type, final String rem ) {
		Thread t = new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost( UserLogin.LOCA_URL);
			    try {
			      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			      nameValuePairs.add(new BasicNameValuePair( UserLogin.USER_ID, UserLogin.accountName ));
			      //nameValuePairs.add(new BasicNameValuePair( UserLogin.MARK_LATI, null));
			      //nameValuePairs.add(new BasicNameValuePair( UserLogin.MARK_LONG, null));
			      nameValuePairs.add(new BasicNameValuePair( UserLogin.REMI_TYPE, type ));
			      nameValuePairs.add(new BasicNameValuePair( UserLogin.REMI_REMI, rem ));
			      nameValuePairs.add(new BasicNameValuePair("action", "put"));
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
	public void deleteReminderData( double lat, double lon, String type, String rem ) {
		// TODO Auto-generated method stub
		
	}*/



}
