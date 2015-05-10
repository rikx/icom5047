package com.rener.sea;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.ViewFlipper;

import java.util.Calendar;
import java.util.Locale;

/**
 * An Android fragment class used to manage the display of data pertaining to a report.
 */
public class ReportDetailsFragment extends Fragment implements View.OnClickListener,
        DetailsFragment, Toolbar.OnMenuItemClickListener, TextWatcher {

    public static final int NO_APPOINTMENT_LAYOUT = 0;
    private int appointmentLayout = NO_APPOINTMENT_LAYOUT;
    public static final int VIEW_APPOINTMENT_LAYOUT = 1;
    private Report report;
    private TextView textName, textLocation, textDate, textCreator,
            textFlowchart;
    private EditText editNotes;
    private LinearLayout interviewLayout;
    private ViewFlipper appointmentFlipper;
    private View appointmentView;
    private boolean viewCreated;
    private AlertDialog appointmentDialog;
    private Toolbar toolbar;
    private Menu options;
    private MenuItem addAppointmentAction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            appointmentLayout = savedInstanceState.getInt("appointmentLayout",
                    NO_APPOINTMENT_LAYOUT);
            Log.i(this.toString(), "restoring appointment layout: " + appointmentLayout);
        }

        View view = inflater.inflate(R.layout.report_details_fragment, container, false);

        //Set the static views
        textName = (TextView) view.findViewById(R.id.report_text_name);
        textLocation = (TextView) view.findViewById(R.id.report_text_location);
        textDate = (TextView) view.findViewById(R.id.report_text_date);
        textCreator = (TextView) view.findViewById(R.id.report_text_creator);
        textFlowchart = (TextView) view.findViewById(R.id.report_text_flowchart);

        //Set the edit views
        editNotes = (EditText) view.findViewById(R.id.report_text_notes);
        editNotes.addTextChangedListener(this);

        //Set the location layout listener
        LinearLayout location = (LinearLayout) view.findViewById(R.id.report_location_layout);
        location.setOnClickListener(this);

        //Create dynamic views
        interviewLayout = (LinearLayout) view.findViewById(R.id.report_interview_layout);

        //Recreate the path taken through the flowchart
        setInterviewLayout();

        FrameLayout appointmentLayout = (FrameLayout) view.findViewById(R.id.report_appointment_container);
        View aView = inflater.inflate(R.layout.appointment_details, appointmentLayout, false);
        appointmentLayout.addView(aView);
        appointmentFlipper = (ViewFlipper) aView.findViewById(R.id.appointment_flipper);
        appointmentView = appointmentFlipper.findViewById(R.id.no_appointment_layout);
        setDataViews();

        viewCreated = true;
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar = ((MainActivity) getActivity()).getContextToolbar();
        toolbar.setOnMenuItemClickListener(this);
        options = toolbar.getMenu();
        getActivity().getMenuInflater().inflate(R.menu.report_actions, options);
        boolean visible = (appointmentLayout == NO_APPOINTMENT_LAYOUT);
        options.findItem(R.id.add_appointment_action).setVisible(visible);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Toolbar tb = ((MainActivity)getActivity()).getContextToolbar();
        options = tb.getMenu();
        options.clear();
        inflater.inflate(R.menu.report_actions, options);
        tb.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.save_report_details:
                saveReport();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("appointmentLayout", appointmentLayout);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        inflateAppointmentLayout();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String previous = report.getNotes().trim();
        String current = charSequence.toString().trim();
        if(!previous.equals(current)) {
            MenuItem save = options.findItem(R.id.save_report_details);
            save.setEnabled(true);
            save.setVisible(true);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void setInterviewLayout() {
        Path path = report.getPath();
        Log.i(this.toString(), "path=" + path.toString());
        int sequence = 1;
        for (PathEntry e : path) {
            //Get the interview element information
            Option option = e.getOption();
            Item item = option.getParent();
            String data = e.getData();
            String question = sequence + ". " + item.getLabel();
            String answer = data.equals("") ? option.getLabel() : data;
            sequence++;

            //Set the interview element views
            TextView seqView = new TextView(getActivity());
            seqView.setText(sequence + ".");
            TextView questionView = new TextView(getActivity());
            questionView.setText(question);
            questionView.setPadding(4, 0, 0, 0);
            TextView answerView = new TextView(getActivity());
            answerView.setText(answer);
            answerView.setPadding(16, 0, 0, 0);
            if (item.getType().equals(Item.RECOMMENDATION)) {
                highlightTextView(seqView);
                highlightTextView(questionView);
            }

            //Set the element layout
            interviewLayout.addView(questionView);
            interviewLayout.addView(answerView);
        }
//        long status = report.getStatus();
//        if(status == 0) {
//            //TODO: handle incomplete report
//            Button continueButton = (Button) getActivity().findViewById(R.id
//                    .report_continue_survey_button);
//            continueButton.setVisibility(View.VISIBLE);
//            continueButton.setOnClickListener(this);
//        }
    }

    /**
     * Set the static views for this fragment
     */
    private void setDataViews() {

        //Set the name
        textName.setText(report.getName());

        //Set the date
        Locale locale = Locale.getDefault();
        String dateFormat = getResources().getString(R.string.date_format);
        String date = report.getDateString(dateFormat, locale);
        textDate.setText(date);

        //Set the location
        String location = report.getLocation().toString();
        textLocation.setText(location);

        //Set the flowchart
        String fc = report.getFlowchart().getName();
        textFlowchart.setText(fc);

        //Set the notes
        editNotes.setText(report.getNotes());

        //Set the creator
        String creator = report.getCreator().getPerson().toString();
        textCreator.setText(creator);

        //Set the appointment if it exists
        inflateAppointmentLayout();
    }

    /**
     * Set this fragment's corresponding Report object by setting it's views with the report data.
     *
     * @param report the report associated with this details fragment
     * @return the report that was set
     */
    public Report setReport(Report report) {
        return this.report = report;
    }

    private void continueReport() {
        Intent intent = new Intent(getActivity(), SurveyActivity.class);
        intent.putExtra("REPORT_ID", report.getId());
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public boolean onDetailsChanged() {
        if (viewCreated) {
            setDataViews();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.report_add_appointment_button:
                displayAppointmentDialog();
                break;
            case R.id.report_save_appointment_button:
                if (saveAppointment()) appointmentDialog.dismiss();
                break;
            case R.id.report_cancel_appointment_button:
                appointmentDialog.dismiss();
                break;
            case R.id.report_continue_survey_button:
                continueReport();
                break;
            case R.id.report_location_layout:
                goToReportLocation();
                break;
        }
    }

    private void inflateAppointmentLayout() {
        Appointment appointment = report.getAppointment();
        if (appointment != null) {
            displayAppointmentLayout(appointment);
        } else if (appointmentLayout == NO_APPOINTMENT_LAYOUT) {
            Button add = (Button) appointmentFlipper.findViewById(R.id
                    .report_add_appointment_button);
            add.setOnClickListener(this);
            appointmentView.setVisibility(View.GONE);
            appointmentFlipper.setDisplayedChild(NO_APPOINTMENT_LAYOUT);
            appointmentView = appointmentFlipper.findViewById(R.id.no_appointment_layout);
            appointmentView.setVisibility(View.VISIBLE);
        }
    }

    private void displayAppointmentLayout(Appointment appointment) {
        View view = appointmentFlipper;

        //Set appointment date view
        String dateFormat = getResources().getString(R.string.date_format_long);
        String timeFormat = getResources().getString(R.string.time_format);
        String format = dateFormat + " " + timeFormat;
        String date = appointment.getDateString(format);
        TextView dateText = (TextView) view.findViewById(R.id.appointment_date_text);
        dateText.setText(date);

        //Set appointment purpose view
        String purpose = appointment.getPurpose();
        TextView purposeText = (TextView) view.findViewById(R.id.appointment_purpose_text);
        purposeText.setText(purpose);

        //Set appointment creator view
        String creatorLabel = getString(R.string.creator_label);
        String creator = appointment.getCreator().getPerson().toString();
        TextView creatorText = (TextView) view.findViewById(R.id.appointment_creator_text);
        creatorText.setText(creatorLabel + ": " + creator);

        //Display the layout
        appointmentLayout = VIEW_APPOINTMENT_LAYOUT;
        appointmentFlipper.setVisibility(View.GONE);
        appointmentFlipper.setDisplayedChild(VIEW_APPOINTMENT_LAYOUT);
        appointmentFlipper.setVisibility(View.VISIBLE);
    }

    private boolean saveAppointment() {
        //Get the input date, time and purpose from the views
        View view = appointmentView;
        DatePicker datePicker = (DatePicker) view.findViewById(R.id.appointment_date_picker);
        TimePicker timePicker = (TimePicker) view.findViewById(R.id.appointment_time_picker);
        EditText purposeEdit = (EditText) view.findViewById(R.id.appointment_purpose_edit);
        String purpose = purposeEdit.getText().toString().trim();
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int dom = datePicker.getDayOfMonth();
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dom, hour, minute);
        Calendar now = Calendar.getInstance();

        boolean past = calendar.before(now);

        if (!past) {
            //Get the current user id
            MainActivity main = (MainActivity) getActivity();
            SharedPreferences sharedPref = main.getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            String sUsername = sharedPref.getString(getString(R.string.key_saved_username), null);
            DBHelper dbHelper = main.getDBHelper();
            User creator = dbHelper.findUserByUsername(sUsername);

            //Create the appointment and set the views for it
            String dateFormat = getString(R.string.date_format_medium);
            new Appointment(-1, report.getId(), creator.getId(), calendar, purpose, dbHelper, dateFormat);
            inflateAppointmentLayout();
            ((MainActivity) getActivity()).onDataChanged();
        } else {
            String message = getString(R.string.past_date_message);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }

        return !past;
    }

    private void displayAppointmentDialog() {
        if (appointmentDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.appointment_edit, null);
            Button save = (Button) view.findViewById(R.id.report_save_appointment_button);
            save.setOnClickListener(this);
            Button cancel = (Button) view.findViewById(R.id.report_cancel_appointment_button);
            cancel.setOnClickListener(this);
            builder.setView(view);
            appointmentView = view;

            appointmentDialog = builder.create();
            appointmentDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                    .SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
        appointmentDialog.show();
    }

    private void goToReportLocation() {
        long id = report.getLocation().getId();
        ((MainActivity) getActivity()).onDetailsRequest("LOCATION", "LOCATION", id);
    }

    public String getType() {
        return "REPORT";
    }

    private void highlightTextView(TextView textView) {
        String text = textView.getText().toString();
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
        textView.setText(spanString);
    }

    private void saveReport() {
        MenuItem save = options.findItem(R.id.save_report_details);
        save.setEnabled(false);
        String notes = editNotes.getText().toString().trim();
        report.setNotes(notes);
        save.setVisible(false);
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromInputMethod(editNotes.getWindowToken(), InputMethodManager
                .HIDE_IMPLICIT_ONLY);
        editNotes.clearFocus();
    }
}