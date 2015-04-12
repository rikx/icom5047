package com.rener.sea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportListAdapter extends ArrayAdapter {

	private Context context;
	private List<Report> reports;

	public ReportListAdapter(Context context, List reports) {
		super(context, R.layout.report_list_item, reports);
		this.context = context;
		this.reports = reports;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.report_list_item, parent, false);

		//Get the views
		TextView nameView = (TextView) view.findViewById(R.id.report_list_item_name);
		TextView locationView = (TextView) view.findViewById(R.id.report_list_item_location);
		TextView dateView = (TextView) view.findViewById(R.id.report_list_item_date);

		//Get the data
		Report report = reports.get(position);
		String name = report.getName();
		String location = report.getLocation().getName();
		Date date = report.getDate();

		//Set the views
		nameView.setText(name);
		locationView.setText(location);
		dateView.setText(new SimpleDateFormat("dd/LLL/yy").format(date));

		return view;

	}
}
