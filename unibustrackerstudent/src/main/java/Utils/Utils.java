package Utils;

import java.util.Calendar;
import java.util.TimeZone;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
	private static ConnectivityManager connectivityManager = null;
	private static HttpClient httpClient = null;

	private static Configuration conf = null;



	public static boolean isNetworkAvailable(Context con) {
		if (connectivityManager == null)
			connectivityManager = (ConnectivityManager) con
					.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public static HttpClient getClient() {
		if (httpClient == null) {
			httpClient = new DefaultHttpClient();
		}
		return httpClient;
	}

	public static String getCurrentUTCTime() {
		return Calendar.getInstance(TimeZone.getTimeZone("UTC")) + "";
	}

	public static void popNoInternetAlert(String message,
			final Activity currentActivity) {
		AlertDialog.Builder builder = null;
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					dialog.dismiss();
					currentActivity
							.startActivity(new Intent(
									android.provider.Settings.ACTION_WIRELESS_SETTINGS));

					break;

				case DialogInterface.BUTTON_NEGATIVE:
					// No button clicked
					dialog.dismiss();
					currentActivity.finish();
					break;
				}
			}
		};
		builder = new AlertDialog.Builder(currentActivity);
		builder.setMessage(message)
				.setPositiveButton("YES",
						dialogClickListener)
				.setNegativeButton("NO",
						dialogClickListener).show().setCancelable(false);
	}

	public static void popNoServerAlert(String message,
			final Activity currentActivity) {
		AlertDialog.Builder builder = null;
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					dialog.dismiss();
					currentActivity.finish();
					break;
				}
			}
		};
		builder = new AlertDialog.Builder(currentActivity);
		builder.setMessage(message)
				.setPositiveButton("OK",
						dialogClickListener).show().setCancelable(false);
	}
	public static void NotificationAlert(String message,
			final Activity currentActivity) {
		AlertDialog.Builder builder = null;
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					dialog.dismiss();
					break;
				}
			}
		};
		builder = new AlertDialog.Builder(currentActivity);
		builder.setMessage(message)
				.setPositiveButton("OK",
						dialogClickListener).show().setCancelable(false);
	}




}
