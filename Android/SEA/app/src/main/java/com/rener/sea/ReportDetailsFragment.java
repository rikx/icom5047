package com.rener.sea;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.Locale;

/**
 * An Android fragment class used to manage the display of data pertaining to a report.
 */
public class ReportDetailsFragment extends Fragment implements View.OnClickListener {

	public static final int NO_APPOINTMENT_LAYOUT = 0;
	public static final int APPOINTMENT_EDIT_LAYOUT = 1;
	public static final int APPOINTMENT_VIEW_LAYOUT = 2;
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
	    appointmentLayout = (FrameLayout) view.findViewById(R.id.report_appointment_container);
	    appointmentLayout.addView(appointmentFlipper);
        setFields(view);

        //Create dynamic views
        LinearLayout layoutInterview = (LinearLayout) view.findViewById(R.id.report_interview_layout);

        //Recreate the path taken through the flowchart
        Path path = report.getPath();
        for (PathEntry e : path) {
            Item item = e.getItem();
            Option option = e.getOption();
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
    private void setFields(View view) {
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
	    setAppointmentViews(view);
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
				appointmentFlipper.setDisplayedChild(APPOINTMENT_EDIT_LAYOUT);
		}

	}

	private void setAppointmentViews(View view) {
		if(report.getAppointment() != null) {
			appointmentFlipper.setDisplayedChild(APPOINTMENT_VIEW_LAYOUT);
			Appointment appointment = report.getAppointment();
			//Set appointment date view
			TextView dateText = (TextView) view.findViewById(R.id.appointment_date_text);
			Locale locale = Locale.getDefault();
			String dateFormat = getResources().getString(R.string.date_format);
			dateText.setText(appointment.getDateString(dateFormat, locale));
			//Set appointment purpose view
			TextView purposeText = (TextView) view.findViewById(R.id.appointment_purpose_text);
			purposeText.setText(appointment.getPurpose());
		}
		else {
			appointmentFlipper.setDisplayedChild(NO_APPOINTMENT_LAYOUT);
		}
	}
}
