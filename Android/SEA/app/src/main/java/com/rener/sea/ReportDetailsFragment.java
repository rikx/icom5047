package com.rener.sea;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ReportDetailsFragment extends Fragment {

	private Report report;
	private boolean viewCreated;
	private TextView textName, textLocation, textDate, textSubject, textType, textCreator,
			textFlowchart, textNotes;
	private List<TextView> questions;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.report_details_fragment, container, false);
		Context context = view.getContext();
		//Set the static views
		textName = (TextView) view.findViewById(R.id.report_text_name);
		textLocation = (TextView) view.findViewById(R.id.report_text_location);
		textDate = (TextView) view.findViewById(R.id.report_text_date);
		textSubject = (TextView) view.findViewById(R.id.report_text_subject);
		textType = (TextView) view.findViewById(R.id.report_text_type);
		textCreator = (TextView) view.findViewById(R.id.report_text_creator);
		textFlowchart = (TextView) view.findViewById(R.id.report_text_flowchart);
		textNotes = (TextView) view.findViewById(R.id.report_text_notes);
		setFields();

		//Create dynamic views
		LinearLayout layoutInterview = (LinearLayout) view.findViewById(R.id.report_interview_layout);

		Path path = report.getPath();
		for(Path.PathEntry e : path) {
			Item item = e.getItem();
			Option option = e.getOption();
			String data = e.getData();
			String question = item.getLabel();
			TextView textQuestion = new TextView(getActivity());
			textQuestion.setText("\t"+question);
			TextView textAnswer = new TextView(getActivity());
			String answer = data.equals("") ? option.getLabel() : data ;
			textAnswer.setText("\t\t"+answer);
			layoutInterview.addView(textQuestion);
			layoutInterview.addView(textAnswer);
		}

		/*
		List<Item> items = report.getFlowchart().getItems();
		for(Item i : items) {
			TextView textItem = new TextView(getActivity());
			textItem.setText("\t"+i.getLabel());
			layoutInterview.addView(textItem);
			List<Option> options = i.getOptions();
			for(Option o : options) {
				TextView textOption = new TextView(getActivity());
				textOption.setText("\t\t"+o.getLabel());
				layoutInterview.addView(textOption);
			}
		}
		*/

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

	private void setFields() {
		textName.setText(report.getName());
		textDate.setText(report.getDateString());
		textType.setText(report.getType());
		textLocation.setText(report.getLocation().toString());
		textFlowchart.setText(report.getFlowchart().getName());
		textNotes.setText(report.getNote());
		if(report.getCreator() != null) {
			String label = getResources().getString(R.string.creator_label);
			String creator = report.getCreator().getPerson().toString();
			textCreator.setText(label+": "+creator);
		}
		if(report.getSubject() != null) {
			String label = getResources().getString(R.string.subject_label);
			String subject = report.getSubject().toString();
			textSubject.setText(label + ": " + subject);
		}
	}

	public Report setReport(Report report) {
		this.report = report;
		if(viewCreated)
			setFields();
		return this.report;
	}
}