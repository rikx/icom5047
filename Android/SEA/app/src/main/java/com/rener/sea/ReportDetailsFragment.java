package com.rener.sea;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ViewFlipper;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * An Android fragment class used to manage the display of data pertaining to a report.
 */
public class ReportDetailsFragment extends Fragment implements View.OnClickListener {

	public static final int NO_APPOINTMENT_LAYOUT = 0;
	public static final int EDIT_APPOINTMENT_LAYOUT = 1;
	public static final int VIEW_APPOINTMENT_LAYOUT = 2;
    private Report report;
    private TextView textName, textLocation, textDate, textSubject, textType, textCreator,
            textFlowchart, textNotes;
	private FrameLayout appointmentLayout;
	private ViewFlipper appointmentFlipper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.report_details_fragment, container, false);

        //Set the static views
        textName = (TextView) view.findViewById(R.id.report_text_name);
        textLocation = (TextView) view.findViewById(R.id.report_text_location);
        textDate = (TextView) view.findViewById(R.id.report_text_date);
        textSubject = (TextView) view.findViewById(R.id.report_text_subject);
        textType = (TextView) view.findViewById(R.id.report_text_type);
        textCreator = (TextView) view.findViewById(R.id.report_text_creator);
        textFlowchart = (TextView) view.findViewById(R.id.report_text_flowchart);
        textNotes = (TextView) view.findViewById(R.id.report_text_notes);

        //Create dynamic views
        LinearLayout layoutInterview = (LinearLayout) view.findViewById(R.id.report_interview_layout);

        //Recreate the path taken through the flowchart
        Path path = report.getPath();
        for (PathEntry e : path) {
	        Option option = e.getOption();
            Item item = option.getParent();
            String data = e.getData();
            String question = item.getLabel();
            TextView textQuestion = new TextView(getActivity());
            textQuestion.setText("\t" + question);
            TextView textAnswer = new TextView(getActivity());
            String answer = data.equals("") ? option.getLabel() : data;
            textAnswer.setText("\t\t" + answer);
            layoutInterview.addView(textQuestion);
            layoutInterview.addView(textAnswer);
        }

	    appointmentLayout = (FrameLayout) view.findViewById(R.id.report_appointment_container);
		View aView = inflater.inflate(R.layout.appointment_details, appointmentLayout, false);
	    appointmentLayout.addView(aView);
	    appointmentFlipper = (ViewFlipper) aView.findViewById(R.id.appointment_flipper);
	    setFields();

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

    /**
     * Set the static views for this fragment
     */
    private void setFields() {
        textName.setText(report.getName());
        Locale locale = Locale.getDefault();
        String dateFormat = getResources().getString(R.string.date_format);
        textDate.setText(report.getDateString(dateFormat, locale));
        textType.setText(report.getType());
        textLocation.setText(report.getLocation().toString());
        textFlowchart.setText(report.getFlowchart().getName());
        textNotes.setText(report.getNotes());
        if (report.getCreator() != null) {
            String label = getResources().getString(R.string.creator_label);
            String creator = report.getCreator().getPerson().toString();
            textCreator.setText(label + ": " + creator);
        }
        if (report.getSubject() != null) {
            String label = getResources().getString(R.string.subject_label);
            String subject = report.getSubject().toString();
            textSubject.setText(label + ": " + subject);
        }
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

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.report_add_appointment_button :
				displayNewAppointmentLayout();
				break;
			case R.id.report_save_appointment_button :
				saveAppointment();
				break;
		}

	}

	private void setAppointmentViews() {
		Appointment appointment = report.getAppointment();
		if(appointment != null) {
			//TODO: review this
			appointmentFlipper.setDisplayedChild(VIEW_APPOINTMENT_LAYOUT);
			View view = appointmentFlipper;
			//Set appointment date view
			TextView dateText = (TextView) view.findViewById(R.id.appointment_date_text);
			Locale locale = Locale.getDefault();
			String dateFormat = getResources().getString(R.string.date_format);
			dateText.setText(appointment.getDateString(dateFormat, locale));
			//Set appointment purpose view
			TextView purposeText = (TextView) view.findViewById(R.id.appointment_purpose_text);
			purposeText.setText(appointment.getPurpose());
			//Set appointment creator view
			TextView creatorText= (TextView) view.findViewById(R.id.appointment_creator_text);
			creatorText.setText(appointment.getCreator().getPerson().toString());
		}
		else {
			Button add = (Button) appointmentFlipper.findViewById(R.id
					.report_add_appointment_button);
			add.setOnClickListener(this);
			appointmentFlipper.setDisplayedChild(NO_APPOINTMENT_LAYOUT);
		}
	}

	private void displayNewAppointmentLayout() {
		Button save = (Button) appointmentFlipper.findViewById(R.id.report_save_appointment_button);
		save.setOnClickListener(this);
		appointmentFlipper.setDisplayedChild(EDIT_APPOINTMENT_LAYOUT);
	}

	private void saveAppointment() {
		View view = appointmentFlipper;
		DatePicker datePicker = (DatePicker) view.findViewById(R.id.appointment_date_picker);
		TimePicker timePicker = (TimePicker) view.findViewById(R.id.appointment_time_picker);
		EditText purposeEdit = (EditText) view.findViewById(R.id.appointment_purpose_edit);
		String purpose = purposeEdit.getText().toString();
		int year = datePicker.getYear();
		int month = datePicker.getMonth();
		int dom = datePicker.getDayOfMonth();
		int hour = timePicker.getCurrentHour();
		int minute = timePicker.getCurrentMinute();
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, dom, hour, minute);
		Log.i(this.toString(), "appointment date set to "+calendar.toString());
		//TODO: create the new appointment with the data
	}
}
