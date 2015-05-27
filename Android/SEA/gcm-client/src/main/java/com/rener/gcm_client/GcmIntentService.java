package com.rener.gcm_client;

import android.app.Activity;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {

	public static final String HANDLE_GCM_MESSAGE_ACTION = ".HandleGcmMessage";

	public GcmIntentService() {
		super("GcmMessageHandler");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
			if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				String message = extras.getString("message");
				Log.i(this.toString(), "handling GCM message");
				handleGcmMessage(message);
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void handleGcmMessage(String message) {
		String action = getPackageName()+HANDLE_GCM_MESSAGE_ACTION;
		Intent intent = new Intent(action);
		intent.putExtra("message", message);
		final BroadcastReceiver finalReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				abortBroadcast();
				Log.i(this.toString(), "GCM message dumped");
			}
		};
		sendOrderedBroadcast(intent, null, finalReceiver, null, Activity.RESULT_OK, null, null);
	}
}
