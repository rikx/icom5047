package com.rener.gcm_client;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;


public class MainActivity extends Activity implements View.OnClickListener {

	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	String regid;

	Button btnRegId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnRegId = (Button) findViewById(R.id.btnGetRegId);
		btnRegId.setOnClickListener(this);

		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(this);
		} else {
			Log.i(this.toString(), "No valid Google Play Services APK found.");
			btnRegId.setEnabled(false);
		}
	}

	@Override
	public void onClick(View v) {
		new GcmRegistrationAsyncTask(this).execute();
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private int getAppVersion() {
		try {
			PackageInfo packageInfo = getPackageManager()
					.getPackageInfo(getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				String error = "This device is not supported.";
				Log.i(this.toString(), error);
				Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
			}
			return false;
		}
		return true;
	}

	private String getRegistrationId() {
		final SharedPreferences prefs = getSharedPreferences(this);
		String registrationId = prefs.getString(PROPERTY_REG_ID, null);
		if (registrationId == null) {
			Log.i(this.toString(), "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion();
		if (registeredVersion != currentVersion) {
			Log.i(this.toString(), "App version changed.");
			return "";
		}
		return registrationId;
	}

	private static SharedPreferences getSharedPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences, but
		// how you store the regID in your app is up to you.
		return context.getSharedPreferences(MainActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	private void storeRegistrationId(String regId) {
		final SharedPreferences prefs = getSharedPreferences(this);
		int appVersion = getAppVersion();
		Log.i(this.toString(), "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.apply();
	}
}
