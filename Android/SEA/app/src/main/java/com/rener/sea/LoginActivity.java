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
import android.widget.Toast;

/**
 * Represents the activity that performs all functions related to authenticating the user
 */
public class LoginActivity extends Activity {

    private String username = null;
    private String password = null;
	private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(this.toString(), "created");
        // the db is created when accessed for first time
        // when get writable or readable db
        dbHelper = new DBHelper(getApplicationContext());
    }

    /**
     * Start the login process by authenticating the user credentials
     */
    public void login(View view) {
        //Get username from text field
        EditText editUsername = (EditText) findViewById(R.id.field_username);
        EditText editPassword = (EditText) findViewById(R.id.field_password);
        username = editUsername.getText().toString();
        password = editPassword.getText().toString();
        if (!attemptLogin()) {
            editUsername.setText("");
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

    private boolean attemptLogin() {
        //Check login credentials
        Context context = getApplicationContext();
        if (dbHelper.authLogin(username, password)) {
            //Successful login
            saveLogin();
            startActivity(new Intent(this, MainActivity.class));
            Log.i(this.toString(), "login successful");
            finish();
            return true;
        } else {
            //Failed login
            String s = getResources().getString(R.string.login_nak);
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            Log.i(this.toString(), "login failed");
            return false;
        }
    }
}
