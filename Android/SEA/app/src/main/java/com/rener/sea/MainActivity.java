package com.rener.sea;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

/**
 * Represents an activity in which the primary navigation for the application is performed
 */
public class MainActivity extends FragmentActivity {

	private DBService dbService;
	private Fragment leftFragment;
	private Fragment rightFragment;
	private boolean mBound = false;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    Log.i(this.toString(), "created");
        setContentView(R.layout.activity_main);
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
			showReportsList();
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
	    super.onBackPressed();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//Determine which Action Bar option was selected
		switch (item.getItemId()) {
			case R.id.reports :
				showReportsList();
				break;
			case R.id.new_report_action :
				newReport();
				break;
			case R.id.people :
				showPeopleList();
				break;
			case R.id.locations :
				showLocationsList();
				break;
			case R.id.action_logout :
				logout();
				break;
			case R.id.action_settings :
				showSettings();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showReportsList() {
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		leftFragment = manager.findFragmentByTag("REPORTS");
		if(leftFragment == null) {
			leftFragment = MenuListFragment.newInstance(MenuListFragment.TYPE_REPORTS);
			transaction.replace(R.id.main_list_container, leftFragment, "REPORTS");
		}
		rightFragment = manager.findFragmentByTag("REPORT");
		if(rightFragment == null) {
			transaction.replace(R.id.main_right_container, new Fragment());
		}
		else {
			transaction.replace(R.id.main_right_container, rightFragment);
		}
		transaction.commit();
	}

	private void showPeopleList() {
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		leftFragment = manager.findFragmentByTag("PEOPLE");
		if(leftFragment == null) {
			leftFragment = MenuListFragment.newInstance(MenuListFragment.TYPE_PEOPLE);
			transaction.replace(R.id.main_list_container, leftFragment, "PEOPLE");
		}
		rightFragment = manager.findFragmentByTag("PERSON");
		if(rightFragment == null) {
			transaction.replace(R.id.main_right_container, new Fragment());
		}
		else {
			transaction.replace(R.id.main_right_container, rightFragment);
		}
		transaction.commit();
	}

	private void showLocationsList() {
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		leftFragment = manager.findFragmentByTag("LOCATIONS");
		if(leftFragment == null) {
			leftFragment = MenuListFragment.newInstance(MenuListFragment.TYPE_LOCATIONS);
			transaction.replace(R.id.main_list_container, leftFragment, "LOCATIONS");
		}
		rightFragment = manager.findFragmentByTag("LOCATION");
		if(rightFragment == null) {
			transaction.replace(R.id.main_right_container, new Fragment());
		}
		else {
			transaction.replace(R.id.main_right_container, rightFragment);
		}
		transaction.commit();
	}

	/**
	 * A listener method that listens for the selection of items from a MenuListFragment class.
	 * @param type the type of MenuListFragment object whose list is being selected
	 * @param listView the listView object associated with the ListFragment parent class
	 * @param position the ListView's selected position index
	 */
	public void OnMenuItemSelectedListener(String type, ListView listView, int position) {
		if(type.equals(MenuListFragment.TYPE_PEOPLE)) {
			Person person = (Person) listView.getAdapter().getItem(position);
			showPerson(person, position);
		}
		else if(type.equals(MenuListFragment.TYPE_LOCATIONS)) {
			Location location = (Location) listView.getAdapter().getItem(position);
			showLocation(location, position);
		}
		else if(type.equals(MenuListFragment.TYPE_REPORTS)) {
			Report report = (Report) listView.getAdapter().getItem(position);
			showReport(report, position);
		}
	}

	/**
	 * Set the right fragment as a person fragment and display it
	 * @param person the Person object to be displayed
	 * @param index the list index for the MenuListFragment
	 */
	private void showPerson(Person person, int index) {
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		PersonDetailsFragment details = new PersonDetailsFragment();
		details.setPerson(person);
		rightFragment = details;
		transaction.replace(R.id.main_right_container, rightFragment, "PERSON");
		transaction.commit();
	}

	/**
	 * Set the right fragment as a location fragment and display it
	 * @param location the Location object to be displayed
	 * @param index the list index for the MenuListFragment
	 */
	private void showLocation(Location location, int index) {
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		LocationDetailsFragment details = new LocationDetailsFragment();
		details.setLocation(location);
		rightFragment = details;
		transaction.replace(R.id.main_right_container, rightFragment, "LOCATION");
		transaction.commit();
	}

	/**
	 * Set the right fragment as a report fragment and display it
	 * @param report the Report object to be displayed
	 * @param index the list index for the MenuListFragment
	 */
	private void showReport(Report report, int index) {
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		ReportDetailsFragment details = new ReportDetailsFragment();
		details.setReport(report);
		rightFragment = details;
		transaction.replace(R.id.main_right_container, rightFragment, "REPORT");
		transaction.commit();
	}

	/**
	 * Returns the service's reference so that it's data can be accessed.
	 * @return null if the service isn't bound to anything
	 */
	public DBService getDBService() {
		return mBound ? dbService : null;
	}

	private void logout() {
		//Delete the saved login credentials
		SharedPreferences sharedPref = this.getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.remove(getString(R.string.key_saved_username));
		editor.remove(getString(R.string.key_saved_password));
		editor.apply();

		//Start a new Login Activity
		startActivity(new Intent(this, LoginActivity.class));
		finish();
	}

	private void showSettings() {
		//TODO: settings
	}

	private void newReport() {
		startActivity(new Intent(this, SurveyActivity.class));
	}
}
