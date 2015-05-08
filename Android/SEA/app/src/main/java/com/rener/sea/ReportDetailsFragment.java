package com.rener.sea;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.Calendar;
import java.util.Locale;

/**
 * An Android fragment class used to manage the display of data pertaining to a report.
 */
public class ReportDetailsFragment extends Fragment implements View.OnClickListener, DetailsFragment {

    public static final int NO_APPOINTMENT_LAYOUT = 0;
    private int appointmentLayout = NO_APPOINTMENT_LAYOUT;
    public static final int VIEW_APPOINTMENT_LAYOUT = 1;
    private Report report;
    private TextView textName, textLocation, textDate,textCreator,
            textFlowchart, textNotes;
    private LinearLayout interviewLayout;
    private ViewFlipper appointmentFlipper;
    private View appointmentView;
    private boolean viewCreated;
    private AlertDialog appointmentDialog;

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
        textNotes = (TextView) view.findViewById(R.id.report_text_notes);

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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("appointmentLayout", appointmentLayout);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setAppointmentViews();
    }

    private void setInterviewLayout() {
        Path path = report.getPath();
        Log.i(this.toString(), "path="+path.toString());
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
        String dateLabel = getResources().getString(R.string.date_label);
        String date = dateLabel + ": " + report.getDateString(dateFormat, locale);
        textDate.setText(date);

        //Set the location
        String locLabel = getResources().getString(R.string.location_label);
        String location = locLabel + ": " + report.getLocation().toString();
        textLocation.setText(location);

        //Set the flowchart
        String fcLabel = getResources().getString(R.string.flowchart_label);
        String fc = fcLabel + ": " + report.getFlowchart().getName();
        textFlowchart.setText(fc);

        //Set the notes
        textNotes.setText(report.getNotes());

        //Set the creator
        if (report.getCreator() != null) {
            String label = getResources().getString(R.string.creator_label);
            String creator = report.getCreator().getPerson().toString();
            textCreator.setText(label + ": " + creator);
        }

        //Set the appointment if it exists
        setAppointmentViews();
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
        if(viewCreated) {
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
                if(saveAppointment()) appointmentDialog.dismiss();
                break;
            case R.id.report_cancel_appointment_button:
                appointmentDialog.dismiss();
                break;
            case R.id.report_continue_survey_button:
                continueReport();
                break;
        }
    }

    private void setAppointmentViews() {
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
        Locale locale = Locale.getDefault();
        String dateFormat = getResources().getString(R.string.date_format_long);
        String timeFormat = getResources().getString(R.string.time_format);
        String format = dateFormat + " " + timeFormat;
        String appLabel = getResources().getString(R.string.appointment_label);
        String date = appointment.getDateString(format, locale);
        String fullDate = appLabel + ": " + date;
        TextView dateText = (TextView) view.findViewById(R.id.appointment_date_text);
        dateText.setText(fullDate);

        //Set appointment purpose view
        String purposeLabel = getResources().getString(R.string.purpose_label);
        String purpose = appointment.getPurpose();
        TextView purposeText = (TextView) view.findViewById(R.id.appointment_purpose_text);
        purposeText.setText(purposeLabel + ": " + purpose);

        //Set appointment creator view
        String creatorLabel = getResources().getString(R.string.appointment_set_by_label);
        String creator = appointment.getCreator().getPerson().toString();
        TextView creatorText = (TextView) view.findViewById(R.id.appointment_creator_text);
        creatorText.setText(creatorLabel + ": " + creator);

        //Display the layout
        appointmentLayout = VIEW_APPOINTMENT_LAYOUT;
        appointmentView.setVisibility(View.GONE);
        appointmentFlipper.setDisplayedChild(VIEW_APPOINTMENT_LAYOUT);
        appointmentView = appointmentFlipper.findViewById(R.id.view_appointment_layout);
        appointmentView.setVisibility(View.VISIBLE);
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

        if(!past) {
            //Get the current user id
            MainActivity main = (MainActivity) getActivity();
            SharedPreferences sharedPref = main.getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            String sUsername = sharedPref.getString(getString(R.string.key_saved_username), null);
            DBHelper dbHelper = main.getDBHelper();
            User creator = dbHelper.findUserByUsername(sUsername);

            //Create the appointment and set the views for it
            new Appointment(-1, report.getId(), creator.getId(), calendar, purpose, dbHelper);
            setAppointmentViews();
            ((MainActivity) getActivity()).onDataChanged();
        }
        else {
            String message = getString(R.string.past_date_message);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }

        return !past;
    }

    private void displayAppointmentDialog() {
        if(appointmentDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();
            appointmentView = inflater.inflate(R.layout.appointment_edit, null);
            Button save = (Button) appointmentView.findViewById(R.id.report_save_appointment_button);
            save.setOnClickListener(this);
            Button cancel = (Button) appointmentView.findViewById(R.id
                    .report_cancel_appointment_button);
            cancel.setOnClickListener(this);
            builder.setView(appointmentView);

            appointmentDialog = builder.create();
            appointmentDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                    .SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
        appointmentDialog.show();
    }
}
