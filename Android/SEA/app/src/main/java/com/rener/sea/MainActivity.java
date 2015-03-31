package com.rener.sea;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

public class MainActivity extends FragmentActivity {

	private String username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		setActivityUser();

		//Manage fragment transaction
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.menu_list_container, new MenuListFragment());
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
}
