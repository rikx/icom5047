package com.rener.sea;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an activity in which a survey can be completed and submitted as a report.
 * TODO: implement DBHelper into this class
 */
public class SurveyActivity extends FragmentActivity implements AdapterView
        .OnItemSelectedListener, RadioGroup.OnCheckedChangeListener,
        TextView.OnEditorActionListener, TextWatcher {

    private static final String GREATER_THAN_REGEX = "gt\\d+(\\.\\d+)?";
    private static final String LESS_THAN_REGEX = "lt\\d+(\\.\\d+)?";
    private static final String GREATER_EQUAL_REGEX = "ge\\d+(\\.\\d+)?";
    private static final String LESS_EQUAL_REGEX = "gt\\d+(\\.\\d+)?";
    private static final String RANGE_REGEX = "ra(\\[|\\()\\d+(\\.\\d+)?,\\d+(\\.\\d+)?(\\]|\\))";
    private DBHelper dbHelper;
    private Report report;
    private EditText editName, editNotes;
    private Spinner spinnerLocation, spinnerSubject, spinnerFlowchart;
    private Path path;
    private LinearLayout progressLayout;
    private boolean surveyEnded = false;
    private MenuItem submitItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(this.toString(), "created");
        setContentView(R.layout.activity_survey);

        //Set the static views
        editName = (EditText) findViewById(R.id.survey_edit_name);
        editName.addTextChangedListener(this);
        editNotes = (EditText) findViewById(R.id.survey_edit_notes);
        spinnerLocation = (Spinner) findViewById(R.id.survey_location_spinner);
        spinnerSubject = (Spinner) findViewById(R.id.survey_subject_spinner);
        spinnerFlowchart = (Spinner) findViewById(R.id.survey_flowchart_spinner);

        progressLayout = (LinearLayout) findViewById(R.id.survey_progress_layout);

	    dbHelper = new DBHelper(getApplicationContext());
        report = new Report(dbHelper, setCreator());
	    setData();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i(this.toString(), "configuration changed");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.survey_activity_actions, menu);
        submitItem = menu.findItem(R.id.save_report);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_report:
                submit();
                break;
            case R.id.discard_report:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i != 0) {
            switch (adapterView.getId()) {
                case R.id.survey_subject_spinner:
                    Person subject = (Person) spinnerSubject.getSelectedItem();
                    report.setSubject(subject);
                    break;
                case R.id.survey_flowchart_spinner:
	                spinnerFlowchart.setEnabled(false);
                    Flowchart flowchart = (Flowchart) spinnerFlowchart.getSelectedItem();
                    report.setFlowchart(flowchart);
                    flowchartSelected(flowchart);
                    break;
                case R.id.survey_location_spinner:
                    Location location = (Location) spinnerLocation.getSelectedItem();
                    report.setLocation(location);
                    break;
            }
            checkSubmittable();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        checkSubmittable();
    }

    private void setData() {
        List<Location> locations = new ArrayList<>(dbHelper.getAllLocations());
        List<Person> people = new ArrayList<>(dbHelper.getAllPersons());
        List<Flowchart> flowcharts = new ArrayList<>(dbHelper.getAllFlowcharts());
	    Log.i(this.toString(), "populate flowchart spinner:"+flowcharts.toString());
        Collections.sort(people);
        Collections.sort(flowcharts);
        Collections.sort(locations);

        //Add a dummy item
        locations.add(0, new Location(getString(R.string.location)));
        people.add(0, new Person(getString(R.string.subject)));
        flowcharts.add(0, new Flowchart(getString(R.string.flowchart)));

        //Set the adapter
        DummyAdapter locationAdapter = new DummyAdapter(this,
                android.R.layout.simple_list_item_1, locations, 0);
        spinnerLocation.setAdapter(locationAdapter);
        spinnerLocation.setOnItemSelectedListener(this);

        DummyAdapter personAdapter = new DummyAdapter(this,
                android.R.layout.simple_list_item_1, people, 0);
        spinnerSubject.setAdapter(personAdapter);
        spinnerSubject.setOnItemSelectedListener(this);

        DummyAdapter flowchartAdapter = new DummyAdapter(this,
                android.R.layout.simple_list_item_1, flowcharts, 0);
        spinnerFlowchart.setAdapter(flowchartAdapter);
        spinnerFlowchart.setOnItemSelectedListener(this);
    }

    private User setCreator() {
        SharedPreferences sharedPref = this.getSharedPreferences(
		        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String sUsername = sharedPref.getString(getString(R.string.key_saved_username), null);
        User creator = dbHelper.findUserByUsername(sUsername);
	    Log.i(this.toString(), "has creator "+creator.getUsername());
        return creator;
    }

    private void flowchartSelected(Flowchart flowchart) {
        path = new Path(report.getId(), dbHelper);
	    report.setPath(path);
        progressLayout.removeAllViews();
        newQuestion(flowchart.getFirst());
    }

    private void newQuestion(Item question) {
        List<Option> options = question.getOptions();
        //Display question and it's options
        TextView textQuestion = new TextView(this);
        textQuestion.setText(question.getLabel());
        String type = question.getType();
        switch (type) {
            case Item.BOOLEAN:
            case Item.MULTIPLE_CHOICE:
                progressLayout.addView(textQuestion);
                RadioGroup group = new RadioGroup(this);
                group.setOrientation(RadioGroup.VERTICAL);
                group.setOnCheckedChangeListener(this);
                for (int i = 0; i < options.size(); i++) {
                    Option o = options.get(i);
                    RadioButton button = new RadioButton(this);
                    button.setId(i);
                    button.setText(o.getLabel());
                    group.addView(button);
                }
                progressLayout.addView(group);
                path.addEntry(question, null);
                break;
            case Item.OPEN: {
                progressLayout.addView(textQuestion);
                EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setHint(getString(R.string.open_hint));
                input.setOnEditorActionListener(this);
                input.setImeActionLabel(getString(R.string.done), EditorInfo.IME_ACTION_DONE);
                progressLayout.addView(input);
                path.addEntry(question, null);
                break;
            }
            case Item.CONDITIONAL: {
                EditText input = new EditText(this);
                //Set this field to allow signed decimals
                input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL
                        | InputType.TYPE_NUMBER_FLAG_SIGNED);
                input.setHint(getString(R.string.open_hint));
                input.setOnEditorActionListener(this);
                input.setImeActionLabel(getString(R.string.done), EditorInfo.IME_ACTION_DONE);
                progressLayout.addView(input);
                path.addEntry(question, null);
                break;
            }
            case Item.RECOMMENDATION:
                progressLayout.addView(textQuestion);
                Option only = question.getOptions().get(0);
                path.addEntry(question, only);
                Item next = only.getNext();
                newQuestion(next);
                break;
            default:
                surveyEnded = true;
                checkSubmittable();
                break;
        }
    }

    private void checkSubmittable() {
        boolean submittable =
                (!editName.getText().toString().equals("") &&
                        spinnerLocation.getSelectedItemPosition() != 0 &&
                        spinnerSubject.getSelectedItemPosition() != 0 &&
                        spinnerFlowchart.getSelectedItemPosition() != 0 &&
                        surveyEnded);
        submitItem.setVisible(submittable);

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
        for (View v : radioGroup.getTouchables()) {
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        checkSubmittable();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void handleUserInput(String input) {
        Item item = path.getLastItem();
        String type = item.getType();
        if (type.equals(Item.OPEN)) {
            Option option = item.getOptions().get(0);
            questionAnswered(item, option, input);
        } else if (type.equals(Item.CONDITIONAL)) {
            //TODO: validate input
            double d = Double.valueOf(input);
            Option option = handleConditional(d, item.getOptions());
            questionAnswered(item, option, input);
        }
    }

    private Option handleConditional(double input, List<Option> options) {
        for (Option o : options) {
            String label = o.getLabel();
            if (label.matches(LESS_THAN_REGEX)) {
                String str = label.substring(2, label.length());
                double op = Double.valueOf(str);
                if (input < op) return o;
            } else if (label.matches(LESS_EQUAL_REGEX)) {
                String str = label.substring(2, label.length());
                double op = Double.valueOf(str);
                if (input <= op) return o;
            } else if (label.matches(GREATER_THAN_REGEX)) {
                String str = label.substring(2, label.length());
                double op = Double.valueOf(str);
                if (input > op) return o;
            } else if (label.matches(GREATER_EQUAL_REGEX)) {
                String str = label.substring(2, label.length());
                double op = Double.valueOf(str);
                if (input >= op) return o;
            } else if (label.matches(RANGE_REGEX)) {
                char left = label.charAt(2);
                char right = label.charAt(label.length() - 1);
                int comma = label.indexOf(',');
                String strA = label.substring(3, comma);
                String strB = label.substring(comma + 1, label.length() - 1);
                double opA = Double.valueOf(strA);
                double opB = Double.valueOf(strB);
                if (left == '(' && right == ')') {
                    if (input > opA && input < opB) return o;
                } else if (left == '[' && right == ')') {
                    if (input >= opA && input < opB) return o;
                } else if (left == '[' && right == ']') {
                    if (input >= opA && input <= opB) return o;
                } else if (left == '(' && right == ']') {
                    if (input > opA && input >= opB) return o;
                }
            }
        }
        return null;
    }

    private void submit() {

        //Finish the report
        report.setPath(path);
        String name = editName.getText().toString();
        report.setName(name);
        String notes = editNotes.getText().toString();
        report.setNotes(notes);
        //TODO: actually submit the report

        //Finish the activity
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}