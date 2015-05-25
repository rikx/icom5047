package com.rener.gcm_client;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String pack = context.getPackageName();
		String name = GcmIntentService.class.getName();
		ComponentName component = new ComponentName(pack, name);

		startWakefulService(context, intent.setComponent(component));
		setResultCode(Activity.RESULT_OK);
	}

}
