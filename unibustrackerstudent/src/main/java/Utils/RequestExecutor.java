package Utils;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Model.UBus;
import Model.UStop;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;


public class RequestExecutor extends AsyncTask<Object, Object, Object[]> {
	public AsyncResponse delegate = null;
	public Context con;



	ArrayList<UBus> busList = new ArrayList<UBus>();

	public RequestExecutor(Context con) {
		super();
		this.con = con;
	}

	@Override
	protected void onPostExecute(Object[] result) {
		delegate.onProcessCompelete(result);
	};
	@Override
	protected Object[] doInBackground(Object... params) {
		//write logic here


		if (Utils.isNetworkAvailable(con)) {
			switch (params[0].toString()) {
				case "1": {
					try {
						Object[] array = new Object[3];
						array[0]=1;
						array[1]=getBusList();
						return array;

					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
 				case "2": {

					Object[] array = new Object[3];
					array[0]=2;
					array[1]= getCurrentLatLng((String) params[1]);
					return array;



				}
				case "3": {

					Object[] array = new Object[3];
					array[0]=3;
					array[1]= getDistanceOnRoad((LatLng)params[1],(LatLng)params[2]);

					return array;

				}

				case "4": {

					Object[] array = new Object[3];
					array[0]=4;
					array[1]=getDistanceFromNearestStop((ArrayList<LatLng>) params[1] , (LatLng) params[2]);

					return array;

				}



				default: {
					return null;
				}
			}
		}

		else {
			return null ;//"Network error";
		}
		return null ;//"Network error";
	}







	// get bus list

	public Object getBusList() throws IOException
	{

		HttpClient httpclient = Utils.getClient();



		HttpGet httpget = new HttpGet("https://univbustrack.azurewebsites.net/api/UnivBus");
		JSONArray jArray;
		String jsonString = "";
		try {

			HttpResponse response = httpclient.execute(httpget);
			jsonString = EntityUtils.toString(response.getEntity());

			JSONArray jsonArray= new JSONArray(jsonString);

			String Id,Name,CurrentLat,CurrentLong;


			for (int i = 0; i < jsonArray.length(); i++)
			{
				Id = ""+ jsonArray.getJSONObject(i).getInt("Id");
				Name = jsonArray.getJSONObject(i).getString("Name");
				CurrentLat =  jsonArray.getJSONObject(i).getString("CurrentLat");
				CurrentLong = jsonArray.getJSONObject(i).getString("CurrentLong");

				ArrayList<UStop> stoplist = new ArrayList<UStop>();
				JSONArray results= jsonArray.getJSONObject(i).getJSONArray("BusStops");
							for(int j=0; j<results.length();j++)
								{
									stoplist.add(new UStop(
											results.getJSONObject(j).getString("Id"),
											results.getJSONObject(j).getString("Name"),
											results.getJSONObject(j).getString("StopLat"),
											results.getJSONObject(j).getString("StopLong")));
								}
				busList.add(new UBus(Id,Name,CurrentLat,CurrentLong,stoplist));
			}



					} catch (JSONException e1) {
			e1.printStackTrace();
		}
		//		return busList;


		return busList;
	}




	public Object getCurrentLatLng(String Id)
	{
		LatLng coordinates=null;
		HttpClient httpclient = Utils.getClient();

		HttpGet httpget = new HttpGet("https://univbustrack.azurewebsites.net/api/UnivBus/{id}".replace("{id}", Id));

          UBus bus = new UBus();

		String jsonString = "";
		try {

			HttpResponse response = httpclient.execute(httpget);
			jsonString = EntityUtils.toString(response.getEntity());

			JSONObject jsonObject = new JSONObject(jsonString);


			   bus.setCurrentLat("" +jsonObject.getString("CurrentLat"));
			   bus.setCurrentLong("" +jsonObject.getString("CurrentLong"));

			double lat = Double.parseDouble(bus.getCurrentLat());
			double lng = Double.parseDouble(bus.getCurrentLong());

			coordinates = new LatLng(lat, lng);


		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		return coordinates;
	}








	public Object getDistanceOnRoad(LatLng A, LatLng B) {


		Double latitudeA = A.latitude;
		Double longitudeA = A.longitude;
		Double latitudeB = B.latitude;
		Double longitudeB = B.longitude;

		String Distance = null;
		String Time = null;
		String result= null;

		HttpClient httpclient = Utils.getClient();
		HttpGet httpget = new HttpGet("http://maps.googleapis.com/maps/api/distancematrix/json?origins=" + latitudeA + "," + longitudeA + "&destinations=" + latitudeB + "," + longitudeB + "&mode=driving&language=en-EN&sensor=false");
		String jsonString = "Nothing returned";
		try {

			HttpResponse response = httpclient.execute(httpget);
			jsonString = EntityUtils.toString(response.getEntity());
			JSONObject jsonObject = new JSONObject(jsonString);

			JSONArray jsonArray = jsonObject.getJSONArray("rows");

			JSONArray jsonArray2 = jsonArray.getJSONObject(0).getJSONArray("elements");

			JSONObject jsonObject2 =jsonArray2.getJSONObject(0).getJSONObject("distance");
			JSONObject jsonObject3 =jsonArray2.getJSONObject(0).getJSONObject("duration");

			Distance=jsonObject2.getString("text");
			Time=jsonObject3.getString("text");

			result=Distance+ " ..... " +Time;


		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return result;
	}





	public Object getDistanceFromNearestStop(ArrayList<LatLng> A , LatLng B) {


ArrayList<Double>  Distances = new ArrayList<>() ;


		for(int i =0;i<A.size();i++)
		{


			Double latitudeA = A.get(i).latitude;
			Double longitudeA = A.get(i).longitude;
			Double latitudeB = B.latitude;
			Double longitudeB = B.longitude;

			String Distance = null;
			String Time = null;
			String result= null;

			HttpClient httpclient = Utils.getClient();
			HttpGet httpget = new HttpGet("http://maps.googleapis.com/maps/api/distancematrix/json?origins=" + latitudeA + "," + longitudeA + "&destinations=" + latitudeB + "," + longitudeB + "&mode=driving&language=en-EN&sensor=false");
			String jsonString = "Nothing returned";
			try {

				HttpResponse response = httpclient.execute(httpget);
				jsonString = EntityUtils.toString(response.getEntity());
				JSONObject jsonObject = new JSONObject(jsonString);

				JSONArray jsonArray = jsonObject.getJSONArray("rows");

				JSONArray jsonArray2 = jsonArray.getJSONObject(0).getJSONArray("elements");

				JSONObject jsonObject2 =jsonArray2.getJSONObject(0).getJSONObject("distance");
				JSONObject jsonObject3 =jsonArray2.getJSONObject(0).getJSONObject("duration");

				Distance=jsonObject2.getString("text");
				Time=jsonObject3.getString("text");

				result=Distance+ " ..... " +Time;


			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

			double Result = Double.parseDouble(result);

			Distances.add(Result);

		}



		double small = Distances.get(0);
		int indexId = 0;
		for (int i = 0; i < Distances.size(); i++) {
			if (Distances.get(i)< small) {
				small = Distances.get(i);
				indexId = i;
			}

		}


		Object[] data = new Object[2];
		data[0]=small;
		data[1]=indexId;

          return data;


	}













}
