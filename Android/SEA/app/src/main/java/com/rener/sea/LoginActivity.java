package com.rener.sea;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends Activity {

    public final static String EXTRA_USERNAME = "com.rener.sea.USERNAME";
    public final static String EXTRA_PASSWORD = "com.rener.sea.PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void startLogin(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        EditText editText;

        //Get username from text field
        editText = (EditText) findViewById(R.id.field_username);
        String username = editText.getText().toString();
        intent.putExtra(EXTRA_USERNAME, username);

        //Get password from text field
        editText = (EditText) findViewById(R.id.field_password);
        String password = editText.getText().toString();
        intent.putExtra(EXTRA_PASSWORD, password);

        //TODO: other login steps

        //Save login data
        saveLogin(username, password);

        //Go to main activity
        startActivity(intent);
    }


    public void saveLogin(String username, String password) {

        SharedPreferences sharedPref = this.getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.key_saved_username), username);
        editor.putString(getString(R.string.key_saved_password), password);
        editor.apply();

        //Verify saved credentials with a toast
		sharedPref = this.getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String key = getString(R.string.key_saved_username);
        String saved = sharedPref.getString(key, "DEFAULT");
        Context context = this.getApplicationContext();
        Toast.makeText(context, saved+" logged successful", Toast.LENGTH_SHORT).show();

    }
}
