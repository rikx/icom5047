package com.rener.sea;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

public class MainActivity extends FragmentActivity {

	private String username = null;
	private DBEmulator database;            //emulates dummy database
	private MenuListFragment menuListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

	    setActivityUser();
	    getData();

	    FragmentManager manager = getFragmentManager();
	    menuListFragment = (MenuListFragment) manager.findFragmentByTag("MENU");

	    if(menuListFragment == null) {
		    FragmentTransaction transaction = manager.beginTransaction();
		    menuListFragment = new MenuListFragment();
		    transaction.add(R.id.menu_list_container, menuListFragment, "MENU");
		    transaction.commit();
	    }
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
	    transaction.replace(R.id.menu_list_container, menuListFragment, "MENU");
	    transaction.replace(R.id.menu_selected_container, new Fragment());
	    transaction.commit();
    }

	private void getData() {
		database = new DBEmulator();
	}

	public DBEmulator getDB() {
		return database;
	}
}
