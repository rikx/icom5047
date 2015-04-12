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

public class ReportListAdapter extends ArrayAdapter<Report> {

	private Context context;
	private List<Report> reports;

	public ReportListAdapter(Context context, List<Report> reports) {
		super(context, R.layout.report_list_item, reports);
		this.context = context;
		this.reports = reports;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ReportHolder holder;

		//Inflate the layout if necessary
		if(row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.report_list_item, parent, false);

			holder = new ReportHolder();
			holder.nameText = (TextView) row.findViewById(R.id.report_list_item_name);
			holder.locationText = (TextView) row.findViewById(R.id.report_list_item_location);
			holder.dateText = (TextView) row.findViewById(R.id.report_list_item_date);
			row.setTag(holder);
		}
		else {
			holder = (ReportHolder) row.getTag();
		}

		//Get the data
		Report report = reports.get(position);
		String name = report.getName();
		String location = report.getLocation().getName();
		Date date = report.getDate();

		//Set the views
		holder.nameText.setText(name);
		holder.locationText.setText(location);
		holder.dateText.setText(new SimpleDateFormat("dd/LLL/yy").format(date));

		return row;

	}

	private static class ReportHolder {
		TextView nameText;
		TextView locationText;
		TextView dateText;
	}
}
