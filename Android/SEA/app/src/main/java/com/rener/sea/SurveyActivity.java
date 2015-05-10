package com.rener.sea;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Represents an activity in which a survey can be completed and submitted as a report.
 */
public class SurveyActivity extends FragmentActivity implements AdapterView
        .OnItemSelectedListener, RadioGroup.OnCheckedChangeListener,
        TextView.OnEditorActionListener, TextWatcher, View.OnClickListener, Toolbar.OnMenuItemClickListener {

    private static final String GREATER_THAN_REGEX = "gt\\d+(\\.\\d+)?";
    private static final String LESS_THAN_REGEX = "lt\\d+(\\.\\d+)?";
    private static final String GREATER_EQUAL_REGEX = "ge\\d+(\\.\\d+)?";
    private static final String LESS_EQUAL_REGEX = "gt\\d+(\\.\\d+)?";
    private static final String RANGE_REGEX = "ra(\\[|\\()\\d+(\\.\\d+)?,\\d+(\\.\\d+)?(\\]|\\))";
    private DBHelper dbHelper;
    private Report report;
    private EditText editName, editNotes;
    private Spinner spinnerLocation, spinnerFlowchart;
    private Path path;
    private LinearLayout progressLayout;
    private boolean surveyEnded = false;
    private MenuItem submitItem;
    private Button nextButton;
    private RadioGroup currentGroup;
    private int groupChecked = -1;
    private EditText currentText;
    private Menu options;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(this.toString(), "created");
        setContentView(R.layout.activity_survey);

        //Toolbar
        getActionBar().hide();
        toolbar = (Toolbar) findViewById(R.id.survey_toolbar);
        options = toolbar.getMenu();
        getMenuInflater().inflate(R.menu.survey_activity_actions, options);
        toolbar.setOnMenuItemClickListener(this);
        submitItem = options.findItem(R.id.save_report);

        //Set the DBHelper
        dbHelper = new DBHelper(getApplicationContext());

        //Check if a new report must be created
        Intent intent = getIntent();
        long id = intent.getLongExtra("REPORT_ID", -1);
        if(id == -1) {
            report = new Report(dbHelper, setCreator());
        }
        else {
            report = new Report(id, dbHelper);
        }

        //Set the static views
        editName = (EditText) findViewById(R.id.survey_edit_name);
        editName.addTextChangedListener(this);
        editNotes = (EditText) findViewById(R.id.survey_edit_notes);
        spinnerLocation = (Spinner) findViewById(R.id.survey_location_spinner);
        spinnerFlowchart = (Spinner) findViewById(R.id.survey_flowchart_spinner);

        progressLayout = (LinearLayout) findViewById(R.id.survey_progress_layout);
        nextButton = (Button) findViewById(R.id.survey_next_question_button);
        nextButton.setOnClickListener(this);

        setSpinnerData();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i(this.toString(), "configuration changed");
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
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
    }

    @Override
    public void onBackPressed() {
        report.destroy();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_report:
                submit();
                break;
            case R.id.discard_report:
                report.destroy();
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i != 0) {
            switch (adapterView.getId()) {
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

    private void setSpinnerData() {
        List<Location> locations = new ArrayList<>(dbHelper.getAllLocations());
        List<Person> people = new ArrayList<>(dbHelper.getAllPersons());
        List<Flowchart> flowcharts = new ArrayList<>(dbHelper.getAllFlowcharts());
        Log.i(this.toString(), "populate flowchart spinner:" + flowcharts.toString());
        Collections.sort(people);
        Collections.sort(flowcharts);
        Collections.sort(locations);

        //Add a dummy item
        locations.add(0, new Location(getString(R.string.location)));
        flowcharts.add(0, new Flowchart(getString(R.string.flowchart)));

        //Set the adapter
        DummyAdapter locationAdapter = new DummyAdapter(this,
                android.R.layout.simple_list_item_1, locations, 0);
        spinnerLocation.setAdapter(locationAdapter);
        spinnerLocation.setOnItemSelectedListener(this);

        DummyAdapter flowchartAdapter = new DummyAdapter(this,
                android.R.layout.simple_list_item_1, flowcharts, 0);
        spinnerFlowchart.setAdapter(flowchartAdapter);
        spinnerFlowchart.setOnItemSelectedListener(this);

        //Set preselected location if necessary
        long loc_id = getIntent().getLongExtra("LOCATION_ID", -1);
        if(loc_id != -1) {
            for(int i=0; i<locations.size(); i++) {
                Location l = (Location) spinnerLocation.getAdapter().getItem(i);
                if(l.getId() == loc_id)
                    spinnerLocation.setSelection(i);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.survey_next_question_button :
                nextPressed();
                break;
        }
    }

    private User setCreator() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String sUsername = sharedPref.getString(getString(R.string.key_saved_username), null);
        User creator = dbHelper.findUserByUsername(sUsername);
        Log.i(this.toString(), "has creator " + creator.getUsername());
        return creator;
    }

    private void flowchartSelected(Flowchart flowchart) {
        path = new Path(report.getId(), dbHelper);
        report.setPath(path);
        progressLayout.removeAllViews();
        nextButton.setVisibility(View.VISIBLE);
        newQuestion(flowchart.getFirst());
    }

    private void newQuestion(Item question) {
        List<Option> options = question.getOptions();
        //Display question and it's options
        TextView textQuestion = new TextView(this);
        int sequence = path.size() + 1;
        textQuestion.setText(sequence + ". " + question.getLabel());
        textQuestion.setPadding(0, 30, 0, 0);
        String type = question.getType();
        switch (type) {
            case Item.BOOLEAN:
            case Item.MULTIPLE_CHOICE:
                progressLayout.addView(textQuestion);
                currentGroup = new RadioGroup(this);
                currentGroup.setOrientation(RadioGroup.VERTICAL);
                currentGroup.setOnCheckedChangeListener(this);
                groupChecked = -1;
                for (int i = 0; i < options.size(); i++) {
                    Option o = options.get(i);
                    RadioButton button = new RadioButton(this);
                    button.setId(i);
                    button.setText(o.getLabel());
                    currentGroup.addView(button);
                }
                progressLayout.addView(currentGroup);
                break;
            case Item.OPEN: {
                progressLayout.addView(textQuestion);
                currentText = new EditText(this);
                currentText.setInputType(InputType.TYPE_CLASS_TEXT);
                currentText.setHint(getString(R.string.open_hint));
                progressLayout.addView(currentText);
                break;
            }
            case Item.CONDITIONAL: {
                currentText = new EditText(this);
                //Set this field to allow signed decimals
                currentText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL
                        | InputType.TYPE_NUMBER_FLAG_SIGNED);
                currentText.setHint(getString(R.string.open_hint));
                currentText.setOnEditorActionListener(this);
                currentText.setImeActionLabel(getString(R.string.done), EditorInfo.IME_ACTION_DONE);
                progressLayout.addView(currentText);
                break;
            }
            case Item.RECOMMENDATION:
                highlightTextView(textQuestion);
                progressLayout.addView(textQuestion);
                Option only = question.getOptions().get(0);
                questionAnswered(only);
                break;
            default:
                endSurvey();
                break;
        }
    }

    private void checkSubmittable() {
        //TODO: incomplete reports
        boolean submittable =
                (!editName.getText().toString().trim().equals("") &&
                        spinnerLocation.getSelectedItemPosition() != 0 &&
                        spinnerFlowchart.getSelectedItemPosition() != 0 &&
                        surveyEnded);
        submitItem.setVisible(submittable);

    }

    private void questionAnswered(Option answer) {
        Item next = answer.getNext();
        path.addEntry(answer);
        newQuestion(next);
        scrollToBottom();
    }

    private void questionAnswered(Option answer, String data) {
        Item next = answer.getNext();
        path.addEntry(answer, data);
        newQuestion(next);
        scrollToBottom();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        currentGroup = radioGroup;
        groupChecked = i;
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        String input = currentText.getText().toString();
        if(!input.equals("")) nextPressed();
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



    private void handleUserInput(Item item, String input) {
        String type = item.getType();
        if (type.equals(Item.OPEN)) {
            currentText.setEnabled(false);
            Option option = item.getOptions().get(0);
            questionAnswered(option, input);
        } else if (type.equals(Item.CONDITIONAL)) {
            currentText.setEnabled(false);
            double d = Double.valueOf(input);
            Option option = handleConditional(d, item.getOptions());
            questionAnswered(option, input);
        }
    }

    private Option handleConditional(double input, List<Option> options) {
        Option o = options.get(0);
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
        return options.get(1);
    }

    private void submit() {

        //Finish the report
        report.setPath(path);
        String name = editName.getText().toString().trim();
        report.setName(name);
        String notes = editNotes.getText().toString().trim();
        report.setNotes(notes);

        //Finish the activity
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void nextPressed() {
        Item item = path.isEmpty() ? report.getFlowchart().getFirst() : path.getLastOption()
                .getNext();
        String type = item.getType();
        if(type.equals(Item.CONDITIONAL) || type.equals(Item.OPEN)) {
            String input = currentText.getText().toString().trim();
            handleUserInput(item, input);
        }
        else if(type.equals(Item.BOOLEAN) || type.equals(Item.MULTIPLE_CHOICE)) {
            if(groupChecked != -1) {
                for(int i=0; i<currentGroup.getChildCount(); i++) {
                    currentGroup.getChildAt(i).setEnabled(false);
                }
                Option checked = item.getOptions().get(groupChecked);
                questionAnswered(checked);
            }
        }
    }

    private void endSurvey() {
        nextButton.setEnabled(false);
        String ended = getString(R.string.survey_completed);
        nextButton.setText(ended);
        surveyEnded = true;
        checkSubmittable();
    }

    private void highlightTextView(TextView textView) {
        String text = textView.getText().toString();
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
        textView.setText(spanString);
    }

    private void scrollToBottom() {
        final ScrollView scroll = (ScrollView) findViewById(R.id.survey_activity_view);
        scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.fullScroll(View.FOCUS_DOWN);
            }
        });
    }
}