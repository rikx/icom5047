package com.rener.gcm_client;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.logging.Handler;

public class GcmIntentService extends IntentService {

	public GcmIntentService() {
		super("GcmMessageHandler");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String type = gcm.getMessageType(intent);
		String title = extras.getString("title");
		String message = extras.getString("message");
		Log.i(this.toString(), "\t"+type+"\t"+title+"\t"+message);
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}
}
