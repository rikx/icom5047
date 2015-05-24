package com.rener.gcm;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GCMPost {

	public static final String GCM_URL = "https://android.googleapis.com/gcm/send";

	public static void post(String apiKey, Content content) {

		try {

			//Open the connection
			URL url = new URL(GCM_URL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			//Set up the connection with type and key
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Authorization", "key=" + apiKey);
			connection.setDoOutput(true);

			//Set the output stream
			DataOutputStream stream = new DataOutputStream(connection.getOutputStream());

			//TODO

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
