package com.rener.sea;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

/**
 * Represents an activity in which the data is managed and displayed
 */
public class MainActivity extends FragmentActivity
		implements MenuListFragment.OnMenuItemSelectedListener {

	private DBService dbService;
	private Fragment leftFragment;
	private Fragment rightFragment;
	private boolean mBound = false;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    Log.i(this.toString(), "created");
        setContentView(R.layout.activity_main);

	    FragmentManager manager = getFragmentManager();
	    leftFragment = manager.findFragmentByTag("MAIN");

	    if(savedInstanceState == null) {
		    FragmentTransaction transaction = manager.beginTransaction();
		    leftFragment = MenuListFragment.newInstance(MenuListFragment.TYPE_MAIN);
		    transaction.add(R.id.menu_list_container, leftFragment, "MAIN");
		    transaction.commit();
	    }
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
	protected void onDestroy() {
		super.onDestroy();
		Log.i(this.toString(), "destroyed");
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

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.i(this.toString(), "configuration changed");
		super.onConfigurationChanged(newConfig);
	}

    @Override
    public void onBackPressed() {
	    FragmentManager manager = getFragmentManager();
	    int count = manager.getBackStackEntryCount();
	    String left = ((MenuListFragment)leftFragment).getType();
		if(left.equals(MenuListFragment.TYPE_MAIN)) {
		    return;
	    }
	    else {
			FragmentTransaction transaction = manager.beginTransaction();
			leftFragment = MenuListFragment.newInstance(MenuListFragment.TYPE_MAIN);
			rightFragment = MenuListFragment.newInstance(left);
			transaction.replace(R.id.menu_list_container, leftFragment, "MAIN");
			transaction.replace(R.id.menu_selected_container, rightFragment, left);
			transaction.commit();
		}
    }

	public boolean isBound() {
		return mBound;
	}

	public void OnMenuItemSelectedListener(String type, ListView l, View v, int position) {
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		if(type.equals(MenuListFragment.TYPE_MAIN)) {
			String selection = l.getAdapter().getItem(position).toString();
			if(selection.equalsIgnoreCase("PEOPLE")) {
				rightFragment = MenuListFragment.newInstance(MenuListFragment.TYPE_PEOPLE);
				transaction.replace(R.id.menu_selected_container, rightFragment, "PEOPLE");
				transaction.commit();
			}
			else if(selection.equalsIgnoreCase("LOCATIONS")) {
				rightFragment = MenuListFragment.newInstance(MenuListFragment.TYPE_LOCATIONS);
				transaction.replace(R.id.menu_selected_container, rightFragment,
						"LOCATIONS");
				transaction.commit();
			}
			else if (selection.equalsIgnoreCase("REPORTS")) {
				rightFragment = MenuListFragment.newInstance(MenuListFragment.TYPE_REPORTS);
				transaction.replace(R.id.menu_selected_container, rightFragment, "REPORTS");
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
			else if(type.equals(MenuListFragment.TYPE_REPORTS)) {
				Report report = (Report) l.getAdapter().getItem(position);
				showReport(report, position);
			}
		}
	}

	private void showPerson(Person person, int index) {
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		PersonDetailsFragment details = new PersonDetailsFragment();
		details.setPerson(person);
		leftFragment = MenuListFragment.newInstance(MenuListFragment.TYPE_PEOPLE, index);
		rightFragment = details;
		transaction.replace(R.id.menu_list_container, leftFragment, "PEOPLE");
		transaction.replace(R.id.menu_selected_container, rightFragment, "PERSON");
		transaction.commit();
	}

	private void showLocation(Location location, int index) {
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		LocationDetailsFragment details = new LocationDetailsFragment();
		details.setLocation(location);
		leftFragment = MenuListFragment.newInstance(MenuListFragment.TYPE_LOCATIONS, index);
		rightFragment = details;
		transaction.replace(R.id.menu_list_container, leftFragment, "PEOPLE");
		transaction.replace(R.id.menu_selected_container, rightFragment, "PERSON");
		transaction.commit();
	}

	private void showReport(Report report, int index) {
		//TODO: show report details
	}

	public DBService getDBService() {
		return mBound? dbService : null;
	}

}
