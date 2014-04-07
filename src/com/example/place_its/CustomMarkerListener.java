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
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.model.Marker;


/** 
 * This class is meanless. Intend to delete later.
 *
 */
public class CustomMarkerListener {
	/*protected static final String TAG = null;
	//Marker aMarker;
	double lat, lon;
	//static List<Pair> myList;// = new ArrayList<Pair>();
	List<Double>latList;
	List<Double>lonList;
	CustomMarkerListener( double lat, double lon ) {
		this.lat = lat;
		this.lon = lon;
	}

	public void postMarkerData() {
		Thread t = new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost( UserLogin.LOCA_URL);
			    try {
			      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			      nameValuePairs.add(new BasicNameValuePair( UserLogin.USER_NAME, String.valueOf(lat)));
			      nameValuePairs.add(new BasicNameValuePair("description", ""));
			      nameValuePairs.add(new BasicNameValuePair("price", String.valueOf(lon)));
			      nameValuePairs.add(new BasicNameValuePair("product", UserLogin.accountName ));
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
	
	public void deleteMarkerData() throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpDelete delete = new HttpDelete( UserLogin.LOCA_URL );
		client.execute(delete);
	}*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*public void getMarkerData() {
	MarkerDataInfo ms = new MarkerDataInfo();
	ms.execute( UserLogin.ITEM_URI );
}

public void setList(List<Pair> myList ) {
	this.myList = myList;
}

public List<Pair> getList() {
	Log.d( "Size", myList.size() + "" );
	return this.myList;
}

public List<Double> getLatList() {
	return this.latList;
}
public List<Double> getLonList() {
	return this.lonList;
}*/
	
	
	
	/*
	private class MarkerDataInfo extends AsyncTask<String, Void, Void> {
		 
	     List<Pair> list;
		 private CustomMarkerListener mLis;
		 public MarkerDataInfo() {}
		 public MarkerDataInfo( CustomMarkerListener mLis ) {
			 this.mLis = mLis;
		 }
		 @Override
		 protected Void doInBackground(String... url) {
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet( UserLogin.ITEM_URI);
				//CustomMarkerListener.myList = new ArrayList<Pair>();
				latList = new ArrayList<Double>();
				lonList = new ArrayList<Double>();
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
							String markerOfUser = obj.get("product").toString();
							if( markerOfUser.equals( UserLogin.accountName )) {
								String latname = obj.get("name").toString();
								String lonname = obj.get("price").toString();
								double latData = Double.parseDouble(latname);
								double lonData =  Double.parseDouble(lonname);
								//CustomMarkerListener.myList.add( new Pair( latData, lonData ));
								latList.add( latData );
								lonList.add( lonData );
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
			//return CustomMarkerListener.myList;
				return null;
	    }
	   	protected void onPostExecute(List<Pair> list) {
	   		super.onPostExecute(list);
	   		mLis.setList( list );
	   		Log.d("CUstome", "" + list.size());
	   	} 	
	 }*/
}
