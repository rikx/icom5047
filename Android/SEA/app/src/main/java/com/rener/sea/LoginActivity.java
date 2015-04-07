package com.rener.sea;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class LoginActivity extends Activity {

	private DBService dbService;
	private boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
	    Log.i(this.toString(), "created");
    }

	@Override
	protected void onStart() {
		super.onStart();
		//Bind to DB Service
		Intent intent = new Intent(this, DBService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		Log.i(this.toString(), "binding to service...");
	}

	@Override
	protected void onStop() {
		super.onStop();
		//Unbind from DB Service
		if(mBound) {
			unbindService(mConnection);
			mBound = false;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(this.toString(), "destroyed");
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
			//Bound to DB Service successful, get the service instance
			DBService.DBBinder binder = (DBService.DBBinder) iBinder;
			dbService = binder.getService();
			mBound = true;
			Log.i(this.toString(), "bound to "+dbService.toString());
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			mBound = false;
		}
	};

	public void startLogin(View view) {

        //Get username from text field
        EditText editUsername = (EditText) findViewById(R.id.field_username);
        String username = editUsername.getText().toString();


        //Get password from text field
        EditText editPassword = (EditText) findViewById(R.id.field_password);
        String password = editPassword.getText().toString();

		//Check login credentials
		if(dbService.checkLogin(username, password)) {
			//Successful login
			saveLogin(username, password);
			startActivity(new Intent(this, MainActivity.class));
			Log.i(this.toString(), "login successful");
			finish();
		}
		else {
			//TODO failed login
			Log.i(this.toString(), "login failed");
		}
    }


    public void saveLogin(String username, String password) {
        SharedPreferences sharedPref = this.getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.key_saved_username), username);
        editor.putString(getString(R.string.key_saved_password), password);
        editor.apply();
    }
}
