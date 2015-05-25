package com.rener.gcm_client;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;


public class MainActivity extends Activity implements View.OnClickListener {

	Button btnRegId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnRegId = (Button) findViewById(R.id.btnGetRegId);

		btnRegId.setOnClickListener(this);
	}
	public void getRegId(){
		new GcmRegistrationAsyncTask(this).execute();
	}
	@Override
	public void onClick(View v) {
		getRegId();
	}
}
