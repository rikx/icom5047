package com.rener.sea;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            dbHelper.syncDB();

        //Perform the login procedure
        loadLogin();
        Button login = (Button) findViewById(R.id.login_button);
        login.setOnClickListener(this);
        EditText passView = (EditText) findViewById(R.id.field_password);
        passView.setOnEditorActionListener(this);

        //Set the action bar title
        String app = getResources().getString(R.string.app_name);
        String label = getResources().getString(R.string.login);
        String title = app+" > "+label;
        getActionBar().setTitle(title);
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
        if (!attemptLogin()) {
            //editUsername.setText("");
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

    private void deleteLogin() {
        //Delete the saved login credentials
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(getString(R.string.key_saved_username));
        editor.remove(getString(R.string.key_saved_password));
        editor.apply();
    }

    private boolean attemptLogin() {
        //Check login credentials
        Context context = getApplicationContext();
        dbHelper.syncDB();
        if (dbHelper.authLogin(username, password)) {
            //Successful login
            saveLogin();
            startActivity(new Intent(this, MainActivity.class));
            Log.i(this.toString(), "login successful");
            finish();
            return true;
        } else {
            //Failed login
            deleteLogin();
            String s = getResources().getString(R.string.login_nak);
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            Log.i(this.toString(), "login failed");
            return false;
        }
    }
}
