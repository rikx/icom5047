package com.rener.sea;

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
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Collection;
import java.util.List;
import java.util.Stack;

/**
 * Represents an activity in which the data is managed and displayed
 */
public class SurveyActivity extends FragmentActivity implements AdapterView.OnItemSelectedListener {

	private DBService dbService;
	private boolean mBound = false;
	private Report report;
	private EditText editName;
	private Spinner spinnerLocation, spinnerSubject, spinnerFlowchart;
	private boolean viewCreated;
	private Path path;
	private LinearLayout progressLayout;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    Log.i(this.toString(), "created");
        setContentView(R.layout.activity_survey);

		//Set the static views
		editName = (EditText) findViewById(R.id.survey_edit_name);
		spinnerLocation = (Spinner) findViewById(R.id.survey_location_spinner);
		spinnerSubject = (Spinner) findViewById(R.id.survey_subject_spinner);
		spinnerFlowchart = (Spinner) findViewById(R.id.survey_flowchart_spinner);

		progressLayout = (LinearLayout) findViewById(R.id.survey_progress_layout);

		report = new Report();
		viewCreated = true;
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
			setServiceData();
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
		//inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			default :
				return super.onOptionsItemSelected(item);
		}
	}

	public boolean isBound() {
		return mBound;
	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
		if(i != 0) {
			switch (adapterView.getId()) {
				case R.id.survey_subject_spinner :
					Person subject = (Person) spinnerSubject.getSelectedItem();
					report.setSubject(subject);
					break;
				case R.id.survey_flowchart_spinner :
					Flowchart flowchart = (Flowchart) spinnerFlowchart.getSelectedItem();
					report.setFlowchart(flowchart);
					flowchartSelected(flowchart);
					break;
				case R.id.survey_location_spinner :
					Location location = (Location) spinnerLocation.getSelectedItem();
					report.setLocation(location);
					break;
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {

	}

	private void setServiceData() {
		List<Location> locations = dbService.getLocations();
		List<Person> people = dbService.getPeople();
		List<Flowchart> flowcharts = dbService.getFlowcharts();

		//Add a dummy item
		locations.add(0, new Location(getString(R.string.location)));
		people.add(0, new Person(getString(R.string.subject)));
		flowcharts.add(0, new Flowchart(getString(R.string.flowchart)));

		CustomAdapter locationAdapter = new CustomAdapter(this,
				android.R.layout.simple_list_item_1, locations, 0);
		spinnerLocation.setAdapter(locationAdapter);
		spinnerLocation.setOnItemSelectedListener(this);

		CustomAdapter personAdapter = new CustomAdapter(this,
				android.R.layout.simple_list_item_1, people, 0);
		spinnerSubject.setAdapter(personAdapter);
		spinnerSubject.setOnItemSelectedListener(this);

		CustomAdapter flowchartAdapter = new CustomAdapter(this,
				android.R.layout.simple_list_item_1, flowcharts, 0);
		spinnerFlowchart.setAdapter(flowchartAdapter);
		spinnerFlowchart.setOnItemSelectedListener(this);
	}

	private void flowchartSelected(Flowchart flowchart) {
		path = new Path();
		progressLayout.removeAllViews();
		newQuestion(flowchart.getFirst());
	}

	private void newQuestion(Item question) {
		List<Option> options = question.getOptions();
		//Display question
		TextView textQuestion = new TextView(this);
		textQuestion.setText(question.getLabel());
		progressLayout.addView(textQuestion);
		RadioGroup group = new RadioGroup(this);
		group.setOrientation(RadioGroup.VERTICAL);
		for(int i=0; i<options.size(); i++) {
			Option o = options.get(i);
			RadioButton button = new RadioButton(this);
			button.setId(i);
			button.setText(o.getLabel());
			group.addView(button);
		}
		progressLayout.addView(group);
	}

	private void questionAnswered(Item question, Option answer) {

	}
}
