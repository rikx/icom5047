package com.rener.sea;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private String username = null;
	private DBEmulator database;            //emulates dummy database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		setActivityUser();
	    populateData();

		//Manage fragment transaction
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.menu_list_container, new MenuListFragment(), "MAIN");
        transaction.commit();

    }

	public String getCurrentUser() {
		return username;
	}

	private void setActivityUser() {
		SharedPreferences sharedPref = this.getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		String key = getString(R.string.key_saved_username);
		String saved = sharedPref.getString(key, "DEFAULT");
		this.username = saved;
	}

    @Override
    public void onBackPressed() {

		// Check if the back stack should be popped
	    FragmentManager manager = getFragmentManager();
	    FragmentTransaction transaction = manager.beginTransaction();
	    transaction.replace(R.id.menu_list_container, new MenuListFragment(), "MAIN");
	    transaction.replace(R.id.menu_selected_container, new Fragment());
	    transaction.commit();

	    //Toast for feedback
	    String string = "BACK pressed";
	    Toast.makeText(this.getApplicationContext(), string, Toast.LENGTH_SHORT).show();
    }

	private void populateData() {
		database = new DBEmulator();
	}

	public DBEmulator getDB() {
		return database;
	}
}
