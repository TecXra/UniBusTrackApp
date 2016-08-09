package Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.android.gms.maps.model.LatLng;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;


import Models.UniBus;


public class RequestExecutor extends AsyncTask<Object, Object, Object> {
	public AsyncResponse delegate = null;
	public Context con;



	ArrayList<UniBus> busList = new ArrayList<UniBus>();

	public RequestExecutor(Context con) {
		super();
		this.con = con;
	}

	@Override
	protected void onPostExecute(Object result) {
		delegate.onProcessCompelete(result);
	};
	@Override
	protected Object doInBackground(Object... params) {
		//write logic here


		if (Utils.isNetworkAvailable(con)) {
			switch (params[0].toString()) {
				case "1": {
					try {
						return getBusList();


					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}

				case "2": {

					return UpdateDataToWebServices((String)params[1],(String)params[2],(String)params[3]);

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



		HttpGet httpget = new HttpGet(RgPreference.host + RgPreference.busListUrl);
		JSONArray jArray;
		String jsonString = "";
		try {

			HttpResponse response = httpclient.execute(httpget);
			jsonString = EntityUtils.toString(response.getEntity());

			JSONArray jsonArray= new JSONArray(jsonString);

			String Id,Name,CurrentLat,CurrentLong;


			for (int i = 0; i < jsonArray.length(); i++)
			{

				JSONObject jsonObject = jsonArray.getJSONObject(i).getJSONObject("UnivBus") ;


				Id = ""+ jsonObject.getInt("Id");
				Name = jsonObject.getString("Name");
				CurrentLat =  jsonObject.getString("CurrentLat");
				CurrentLong = jsonObject.getString("CurrentLong");


				busList.add(new UniBus(Id,Name,CurrentLat,CurrentLong));
			}



		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		//		return busList;


		return busList;
	}




	public Object UpdateDataToWebServices(String lat,String lng,String Id){



		try{
			HttpClient client = Utils.getClient();
			HttpPut put= new HttpPut(RgPreference.UpdateLatLngUrl.replace("{id}", Id));

			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("Id", Id));
			pairs.add(new BasicNameValuePair("CurrentLat", lat));
			pairs.add(new BasicNameValuePair("CurrentLong", lng));
			put.setEntity(new UrlEncodedFormEntity(pairs));

			HttpResponse response = client.execute(put);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

return "OK";
	}



























}
