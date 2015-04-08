package com.rener.sea;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class ReportDetailsFragment extends Fragment {

	private Report report;
	private boolean viewCreated;
	private TextView textName, textLocation, textDate, textSubject, textType, textCreator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.report_details_fragment, container, false);

		//Set the views
		textName = (TextView) view.findViewById(R.id.report_text_name);
		textLocation = (TextView) view.findViewById(R.id.report_text_location);
		textDate = (TextView) view.findViewById(R.id.report_text_date);
		textSubject = (TextView) view.findViewById(R.id.report_text_subject);
		textType = (TextView) view.findViewById(R.id.report_text_type);

		viewCreated = true;
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

	private void setFields() {
		textName.setText(report.getName());
		textDate.setText(report.getDateString());
		textType.setText(report.getType());
		textLocation.setText(report.getLocation().toString());
	}

	public Report setReport(Report report) {
		this.report = report;
		if(viewCreated)
			setFields();
		return this.report;
	}
}
