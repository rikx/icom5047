package com.rener.sea;

import android.app.Activity;
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
import android.widget.Toolbar;

/**
 * Represents the activity that performs all functions related to authenticating the user
 */
public class LoginActivity extends Activity implements View.OnClickListener, TextView.OnEditorActionListener {

    private String username = null;
    private String password = null;
    private DBHelper dbHelper;
    private NetworkHelper networkHelper;

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
        // TODO: fill db with dummy data eliminar despues
//        dbHelper.deleteDB();
        if (dbHelper.getDummy())
            dbHelper.syncDBFull();
        else dbHelper.syncDB();

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
        if(password.length() >= 72) {
            String message = getString(R.string.long_password_error);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
        else if(password.length() == 0) {

            String message = getString(R.string.empty_password_error);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
        else if(!attemptLogin()) {
            editPassword.setText("");
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
        editor.apply();
        Log.i(this.toString(), "saveLogin: " + username + ", " + password);
    }

    private void loadLogin() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String sUsername = sharedPref.getString(getString(R.string.key_saved_username), null);
        String sPassword = sharedPref.getString(getString(R.string.key_saved_password), null);
        Log.i(this.toString(), "loadLogin: " + sUsername + ", " + sPassword);
        if (sUsername != null && sPassword != null) {
            username = sUsername;
            password = sPassword;
            attemptLogin();
        }
    }

    public static void deleteLogin(Context context) {
        //Delete the saved login credentials
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(context.getString(R.string.key_saved_username));
        editor.remove(context.getString(R.string.key_saved_password));
        editor.apply();
    }

    private boolean attemptLogin() {
        //Check login credentials
        dbHelper.syncDB();
        String salt = "$2a$06$DkLy6BIWUalW66HzwYF48e";
        String hash = BCrypt.hashpw(password, salt);
        Boolean match = BCrypt.checkpw(password, salt);
        if (dbHelper.authLogin(username, password)) {
            successfulLogin();
            return true;
        } else {
            failedLogin();
            return false;
        }
    }

    private void setLoginReceiver() {
        final BroadcastReceiver loginReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                unregisterReceiver(this);
                handleLoginResult(intent);
            }
        };
        IntentFilter filter = new IntentFilter("AUTH");
        registerReceiver(loginReceiver, filter);
    }

    private void handleLoginResult(Intent intent) {
        String key = "";
        String success = "";
        String result = intent.getStringExtra(""); //TODO: set key
        if(key.equals(success)) successfulLogin();
        else failedLogin();
    }

    private void successfulLogin() {
        saveLogin();
        startActivity(new Intent(this, MainActivity.class));
        Log.i(this.toString(), "login successful");
        finish();
    }

    private void failedLogin() {
        deleteLogin(this);
        String s = getResources().getString(R.string.login_nak);
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        Log.i(this.toString(), "login failed");
    }
}
