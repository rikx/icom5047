package com.rener.sea;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkHelper {

	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager)
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected());
	}

	public static boolean isServerAvailable(Context context) {
		boolean connected = isNetworkConnected(context);
		//TODO: establish connection to SEA
		return false;
	}

}
