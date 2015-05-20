package com.rener.sea;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Represents the activity that performs all functions related to authenticating the user
 */
public class LoginActivity extends Activity implements View.OnClickListener, TextView.OnEditorActionListener {

    private String username = null;
    private String password = null;
    private DBHelper dbHelper;
    private NetworkHelper networkHelper;
    private ProgressDialog progressDialog;

    public static void deleteLogin(Context context) {
        //Delete the saved login credentials
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("Log");
//        editor.remove(context.getString(R.string.key_saved_username));
//        editor.remove(context.getString(R.string.key_saved_password));
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hide the legacy action bar
        //getActionBar().hide();

        setContentView(R.layout.activity_login);
        Log.i(this.toString(), "created");
        // the db is created when accessed for first time
        // when get writable or readable db
        dbHelper = new DBHelper(getApplicationContext());
        networkHelper = new NetworkHelper(this);
        networkHelper.isInternetAvailable();

        //Perform the login procedure
        loadLogin();
        Button login = (Button) findViewById(R.id.login_button);
        login.setOnClickListener(this);
        EditText passView = (EditText) findViewById(R.id.field_password);
        passView.setOnEditorActionListener(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                login();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        int vid = textView.getId();
        if (vid == R.id.field_password) {
            login();
            return true;
        }
        return false;
    }

    /**
     * Start the login process by authenticating the user credentials
     */
    public void login() {
        //Get username from text field
        EditText editUsername = (EditText) findViewById(R.id.field_username);
        EditText editPassword = (EditText) findViewById(R.id.field_password);
        username = editUsername.getText().toString();
        password = editPassword.getText().toString();
        if (password.length() >= 72) {
            String message = getString(R.string.long_password_error);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else if (password.length() == 0) {

            String message = getString(R.string.empty_password_error);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            attemptLogin();
        }
    }

    /**
     * Saves the login credentials to the preference file
     */
    private void saveLogin() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.key_saved_username), username);
        editor.putString(getString(R.string.key_saved_password), password);
        editor.putBoolean("Log", true);
        editor.apply();
        Log.i(this.toString(), "saveLogin: " + username + ", " + password);
    }

    private void loadLogin() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String sUsername = sharedPref.getString(getString(R.string.key_saved_username), null);
        String sPassword = sharedPref.getString(getString(R.string.key_saved_password), null);
        boolean log = sharedPref.getBoolean("Log", false);
        Log.i(this.toString(), "loadLogin: " + sUsername + ", " + sPassword);
        if (log) {
            if (sUsername != null && sPassword != null) {
                username = sUsername;
                password = sPassword;
                attemptLogin();
            }
        }
    }

    private void attemptLogin() {
        showLoginProgressDialog();
        final BroadcastReceiver loginReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                unregisterReceiver(this);
                handleLoginResult(intent);
            }
        };
        IntentFilter filter = new IntentFilter("AUTH");
        registerReceiver(loginReceiver, filter);
        dbHelper.authLogin(username, password);
    }

    private void handleLoginResult(Intent intent) {
        int result = intent.getIntExtra("AUTH_RESULT", -1);
        Log.i(this.toString(), " Login Handler result = " + result);
        switch (result) {
            case 1: //successful login with previous user
                successfulLogin();
                break;
            case -200: //login failed, wrong credentials");
                failedLogin();
                break;
            case 2: //successful login with new user
                performNewUserLogin();
                break;
	        default:
		        failedLogin();
		        break;
        }
    }

    private void performNewUserLogin() {
        final BroadcastReceiver initialSyncReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                unregisterReceiver(this);
                handleInitialSyncResult(intent);
            }
        };
        IntentFilter filter = new IntentFilter("SYNC");
        registerReceiver(initialSyncReceiver, filter);
        dbHelper.syncDB();
    }

    private void handleInitialSyncResult(Intent intent) {
        int result = intent.getIntExtra("SYNC_RESULT", -1);
        switch (result) {
            case 200:
                performFullSync();
                break;
        }
    }

    private void performFullSync() {
        final BroadcastReceiver fullSyncReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                unregisterReceiver(this);
                successfulLogin();
            }
        };
        IntentFilter filter = new IntentFilter("SYNC_FULL");
        registerReceiver(fullSyncReceiver, filter);
        dbHelper.setSyncDone();
        dbHelper.syncDBFull();
    }

    private void successfulLogin() {
        saveLogin();
	    Intent intent = new Intent(this, MainActivity.class);
	    intent.putExtra("LOGIN", true);
        startActivity(intent);
        Log.i(this.toString(), "login successful");
        progressDialog.dismiss();
        finish();
    }

    private void failedLogin() {
        deleteLogin(this);
        SharedPreferences sharedPref = this.getSharedPreferences(
                this.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("Log");
        editor.apply();
        EditText editPassword = (EditText) findViewById(R.id.field_password);
        editPassword.setText("");
        String s = getResources().getString(R.string.login_nak);
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        Log.i(this.toString(), "login failed");
        progressDialog.dismiss();
    }

    private void showLoginProgressDialog() {
        if(progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        progressDialog.show();
    }
}
