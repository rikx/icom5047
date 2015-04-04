package com.rener.sea;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity
		implements MenuListFragment.OnMenuItemSelectedListener {

	private String username = null;
	private DBEmulator database;            //emulates dummy database
	private MenuListFragment menuListFragment;
	private MenuListFragment selectedListFragment;
	private Fragment detailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

	    setActivityUser();
	    initDB();

	    FragmentManager manager = getFragmentManager();
	    menuListFragment = (MenuListFragment) manager.findFragmentByTag("MAIN");

	    if(savedInstanceState == null) {
		    FragmentTransaction transaction = manager.beginTransaction();
		    menuListFragment = MenuListFragment.newInstance("MAIN");
		    transaction.add(R.id.menu_list_container, menuListFragment, "MAIN");
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
	    FragmentManager manager = getFragmentManager();
	    int count = manager.getBackStackEntryCount();
		manager.popBackStack();
    }

	public void OnMenuItemSelectedListener(String type, ListView l, View v, int position) {
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		if(type.equals(MenuListFragment.TYPE_MAIN)) {
			String selection = l.getAdapter().getItem(position).toString();
			if(selection.equalsIgnoreCase("PEOPLE")) {
				selectedListFragment = MenuListFragment.newInstance(MenuListFragment.TYPE_PEOPLE);
				transaction.replace(R.id.menu_selected_container, selectedListFragment, "PEOPLE");
				transaction.commit();
			}
			else if(selection.equalsIgnoreCase("LOCATIONS")) {
				selectedListFragment = MenuListFragment.newInstance(MenuListFragment.TYPE_LOCATIONS);
				transaction.replace(R.id.menu_selected_container, selectedListFragment,
						"LOCATIONS");
				transaction.commit();
			}
			else if (selection.equalsIgnoreCase("REPORTS")) {
				selectedListFragment = MenuListFragment.newInstance(MenuListFragment.TYPE_REPORTS);
				transaction.replace(R.id.menu_selected_container, selectedListFragment, "REPORTS");
				transaction.commit();
			}
		}
		else {
			if(type.equals(MenuListFragment.TYPE_PEOPLE)) {
				Person person = (Person) l.getAdapter().getItem(position);
				showPerson(person, position);
			}
			else if(type.equals(MenuListFragment.TYPE_LOCATIONS)) {
				Location location = (Location) l.getAdapter().getItem(position);
				showLocation(location, position);
			}
		}
	}

	private void showPerson(Person person, int index) {
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		PersonDetailsFragment details = new PersonDetailsFragment();
		details.setPerson(person);
		MenuListFragment list =
				MenuListFragment.newInstance(MenuListFragment.TYPE_PEOPLE, index);
		transaction.replace(R.id.menu_list_container, list, "PEOPLE");
		transaction.replace(R.id.menu_selected_container, details, "PERSON");
		transaction.addToBackStack(null);
		transaction.commit();
	}

	private void showLocation(Location location, int index) {
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		LocationDetailsFragment details = new LocationDetailsFragment();
		details.setLocation(location);
		MenuListFragment list =
				MenuListFragment.newInstance(MenuListFragment.TYPE_LOCATIONS, index);
		transaction.replace(R.id.menu_list_container, list, "LOCATIONS");
		transaction.replace(R.id.menu_selected_container, details, "LOCATION");
		transaction.addToBackStack(null);
		transaction.commit();
	}

	private void initDB() {
		database = new DBEmulator();
	}

	public DBEmulator getData() {
		return database;
	}

}
