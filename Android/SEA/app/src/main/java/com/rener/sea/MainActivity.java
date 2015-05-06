package com.rener.sea;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Represents an activity in which the primary navigation for the application is performed
 */
public class MainActivity extends FragmentActivity {

    private DBHelper dbHelper;
    private NetworkHelper networkHelper;
    private Fragment leftFragment;
    private Fragment rightFragment;
    private BroadcastReceiver networkReceiver;
    private Menu options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(this.toString(), "created");
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(getApplicationContext());

        //Manage network connectivity
        networkHelper = new NetworkHelper(getApplicationContext());
        networkHelper.isInternetAvailable();
        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                sync();
            }
        };
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);

        //Show the default view
        showReportsList();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i(this.toString(), "configuration changed");
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkReceiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        this.options = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Determine which Action Bar option was selected
        switch (item.getItemId()) {
            case R.id.reports:
                toggleTab(item);
                showReportsList();
                break;
            case R.id.people:
                toggleTab(item);
                showPeopleList();
                break;
            case R.id.locations:
                toggleTab(item);
                showLocationsList();
                break;
            case R.id.action_logout:
                logout();
                break;
            case R.id.action_settings:
                showSettings();
                break;
            case R.id.sync:
                sync();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void toggleTab(MenuItem item) {
        //TODO: highlight navigation buttons
    }

    private void showReportsList() {
        //Set the action bar title
        String app = getResources().getString(R.string.app_name);
        String label = getResources().getString(R.string.reports);
        String title = app+" > "+label;
        getActionBar().setTitle(title);

        //Perform the fragment transaction
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        leftFragment = manager.findFragmentByTag("REPORTS");
        if (leftFragment == null) {
            leftFragment = MenuListFragment.newInstance(MenuListFragment.TYPE_REPORTS);
            transaction.replace(R.id.main_list_container, leftFragment, "REPORTS");
        }
        rightFragment = manager.findFragmentByTag("REPORT");
        if (rightFragment == null) {
            transaction.replace(R.id.main_right_container, new Fragment());
        } else {
            transaction.replace(R.id.main_right_container, rightFragment);
        }
        transaction.commit();
    }

    private void showPeopleList() {
        //Set the action bar title
        String app = getResources().getString(R.string.app_name);
        String label = getResources().getString(R.string.people);
        String title = app+" > "+label;
        getActionBar().setTitle(title);

        //Perform the fragment transaction
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        leftFragment = manager.findFragmentByTag("PEOPLE");
        if (leftFragment == null) {
            leftFragment = MenuListFragment.newInstance(MenuListFragment.TYPE_PEOPLE);
            transaction.replace(R.id.main_list_container, leftFragment, "PEOPLE");
        }
        rightFragment = manager.findFragmentByTag("PERSON");
        if (rightFragment == null) {
            transaction.replace(R.id.main_right_container, new Fragment());
        } else {
            transaction.replace(R.id.main_right_container, rightFragment);
        }
        transaction.commit();
    }

    private void showLocationsList() {
        //Set the action bar title
        String app = getResources().getString(R.string.app_name);
        String label = getResources().getString(R.string.reports);
        String title = app+" > "+label;
        getActionBar().setTitle(title);

        //Perform the fragment transaction
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        leftFragment = manager.findFragmentByTag("LOCATIONS");
        if (leftFragment == null) {
            leftFragment = MenuListFragment.newInstance(MenuListFragment.TYPE_LOCATIONS);
            transaction.replace(R.id.main_list_container, leftFragment, "LOCATIONS");
        }
        rightFragment = manager.findFragmentByTag("LOCATION");
        if (rightFragment == null) {
            transaction.replace(R.id.main_right_container, new Fragment());
        } else {
            transaction.replace(R.id.main_right_container, rightFragment);
        }
        transaction.commit();
    }

    /**
     * A listener method that listens for the selection of items from a MenuListFragment class.
     *
     * @param type     the type of MenuListFragment object whose list is being selected
     * @param listView the listView object associated with the ListFragment parent class
     * @param position the ListView's selected position index
     */
    public void OnMenuItemSelectedListener(String type, ListView listView, int position) {
        if (type.equals(MenuListFragment.TYPE_PEOPLE)) {
            Person person = (Person) listView.getAdapter().getItem(position);
            showPerson(person);
        } else if (type.equals(MenuListFragment.TYPE_LOCATIONS)) {
            Location location = (Location) listView.getAdapter().getItem(position);
            showLocation(location);
        } else if (type.equals(MenuListFragment.TYPE_REPORTS)) {
            Report report = (Report) listView.getAdapter().getItem(position);
            showReport(report);
        }
    }

    /**
     * A listener method that listens for a some changes in the data being displayed
     */
    public void onDataSetChanged() {
        MenuListFragment fragment = (MenuListFragment) leftFragment;
        fragment.notifyDataChanged();
        hideKeyboard();
    }

    /**
     * Set the right fragment as a person fragment and display it
     *
     * @param person the Person object to be displayed
     */
    private void showPerson(Person person) {
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
     *
     * @param location the Location object to be displayed
     */
    private void showLocation(Location location) {
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
     *
     * @param report the Report object to be displayed
     */
    private void showReport(Report report) {
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
     *
     * @return null if the service isn't bound to anything
     */
    public DBHelper getDBHelper() {
        return dbHelper;
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

    /**
     * Method that simply hides the software keyboard on the application context
     */
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity
                .INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private void sync() {
        if (networkHelper.isInternetAvailable()) {
            //Create a new synchronization receiver
            final BroadcastReceiver syncReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    unregisterReceiver(this);
                    handleSyncResult(intent);
                }
            };
            IntentFilter filter = new IntentFilter("SYNC");
            registerReceiver(syncReceiver, filter);
            dbHelper.syncDB();
        }
    }

    private void scheduleSync(int delay) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                sync();
            }
        }, delay);
    }

    private void handleSyncResult(Intent intent) {
        int result = intent.getIntExtra("SYNC_RESULT", -1);
        switch (result) {
            case 200:
                syncSuccess();
                break;
            default:
                syncFailure();
                break;
        }
    }

    private void syncSuccess() {
        dbHelper.setSyncDone();
        onDataSetChanged();
    }

    private void syncFailure() {
        //scheduleSync(5000);
    }

}
