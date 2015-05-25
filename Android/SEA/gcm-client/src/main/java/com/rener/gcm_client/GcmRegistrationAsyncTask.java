package com.rener.gcm_client;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.rener.sea.backend.registration.Registration;

import java.io.IOException;

public class GcmRegistrationAsyncTask extends AsyncTask<Void, Void, String> {

	public static final String EXTRA_MESSAGE = "message";

	private static Registration regService;
	private GoogleCloudMessaging gcm;
	private Context context;
	String regid;

	private static final String SENDER_ID = "646747748285";

	public GcmRegistrationAsyncTask(Context context) {
		this.context = context;
	}

	@Override
	protected String doInBackground(Void... params) {
		String msg = "";
		try {
			if (gcm == null) {
				gcm = GoogleCloudMessaging.getInstance(context);
			}
			regid = gcm.register(SENDER_ID);
			msg = "Device registered, registration ID=" + regid;

			// You should send the registration ID to your server over HTTP, so it
			// can use GCM/HTTP or CCS to send messages to your app.
			sendRegistrationIdToBackend();

			// For this demo: we don't need to send it because the device will send
			// upstream messages to a server that echo back the message using the
			// 'from' address in the message.

			// Persist the regID - no need to register again.
			//storeRegistrationId(context, regid);
		} catch (IOException ex) {
			msg = "Error :" + ex.getMessage();
			// If there is an error, don't just keep trying to register.
			// Require the user to click a button again, or perform
			// exponential back-off.
		}
		return msg;
	}

	@Override
	protected void onPostExecute(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
		Log.i(this.toString(), msg);
	}

	private void sendRegistrationIdToBackend() {
		// Your implementation here.
	}
}