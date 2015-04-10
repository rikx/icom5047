package com.rener.sea;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

/**
 * Represents an activity in which a survey can be completed and submitted as a report.
 */
public class SurveyActivity extends FragmentActivity implements AdapterView
		.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener,
		TextView.OnEditorActionListener, View.OnClickListener {

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
		setCreator();
	}

	private void setCreator() {
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		String username = sharedPref.getString(getString(R.string.key_saved_username), "DEFAULT");
		List<User> users = dbService.getUsers();
		User creator = null;
		for(User u : users) {
			if(u.getUsername().equals(username)) creator = u;
		}
		report.setCreator(creator);
	}

	private void flowchartSelected(Flowchart flowchart) {
		path = new Path();
		progressLayout.removeAllViews();
		newQuestion(flowchart.getFirst());
	}

	private void newQuestion(Item question) {
		List<Option> options = question.getOptions();
		//Display question and it's options
		TextView textQuestion = new TextView(this);
		textQuestion.setText(question.getLabel());
		String type = question.getType();
		if(type.equals(Item.BOOLEAN) || type.equals(Item.MULTIPLE_CHOICE)) {
			progressLayout.addView(textQuestion);
			RadioGroup group = new RadioGroup(this);
			group.setOrientation(RadioGroup.VERTICAL);
			group.setOnCheckedChangeListener(this);
			for(int i=0; i<options.size(); i++) {
				Option o = options.get(i);
				RadioButton button = new RadioButton(this);
				button.setId(i);
				button.setText(o.getLabel());
				group.addView(button);
			}
			progressLayout.addView(group);
			path.addEntry(question, null);
		}
		else if(type.equals(Item.OPEN)) {
			progressLayout.addView(textQuestion);
			EditText input = new EditText(this);
			input.setInputType(InputType.TYPE_CLASS_TEXT);
			input.setHint(getString(R.string.open_hint));
			input.setOnEditorActionListener(this);
			input.setImeActionLabel(getString(R.string.done), EditorInfo.IME_ACTION_DONE);
			progressLayout.addView(input);
			path.addEntry(question, null);
		}
		else if(type.equals(Item.CONDITIONAL)) {
			EditText input = new EditText(this);
			input.setInputType(InputType.TYPE_CLASS_NUMBER);
			input.setHint(getString(R.string.open_hint));
			input.setOnEditorActionListener(this);
			input.setImeActionLabel(getString(R.string.done), EditorInfo.IME_ACTION_DONE);
			progressLayout.addView(input);
			path.addEntry(question, null);
		}
		else if(type.equals(Item.RECOMMENDATION)) {
			progressLayout.addView(textQuestion);
			Option only = question.getOptions().get(0);
			path.addEntry(question, only);
			Item next = only.getNext();
			newQuestion(next);
		}
		else {
			Button buttonSubmit = new Button(this);
			buttonSubmit.setText(getString(R.string.submit));
			buttonSubmit.setOnClickListener(this);
			//path.addEntry(question, null);
			progressLayout.addView(buttonSubmit);
		}
	}

	private void questionAnswered(Item question, Option answer) {
		path.setLastEntry(question, answer);
		Item next = answer.getNext();
		newQuestion(next);
	}

	private void questionAnswered(Item question, Option answer, String data) {
		path.setLastEntry(question, answer, data);
		Item next = answer.getNext();
		newQuestion(next);
	}

	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int i) {
		Item item = path.getLastItem();
		Option checked = item.getOptions().get(i);
		questionAnswered(item, checked);
		for(View v : radioGroup.getTouchables()) {
			v.setEnabled(false);
		}
	}

	@Override
	public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
		EditText editInput = (EditText) textView;
		String input = editInput.getText().toString();
		editInput.setEnabled(false);
		handleUserInput(input);
		return true;
	}

	private void handleUserInput(String input) {
		Item item = path.getLastItem();
		String type = item.getType();
		if(type.equals(Item.OPEN)) {
			Option option = item.getOptions().get(0);
			questionAnswered(item, option, input);
		}
		else if(type.equals(Item.CONDITIONAL)) {
			double d = Double.valueOf(input);
			//TODO: handle conditional
		}
	}

	@Override
	public void onClick(View view) {
		Button clicked = (Button) view;
		if(clicked.getText().toString().equals(getString(R.string.submit))) {
			submit();
		}
	}

	private void submit() {
		report.setPath(path);
		String name = editName.getText().toString();
		report.setName(name);
		dbService.getReports().add(report);
		dbService.getFlowcharts().remove(0);
		dbService.getLocations().remove(0);
		dbService.getPeople().remove(0);
		startActivity(new Intent(this, MainActivity.class));
		finish();
	}
}
