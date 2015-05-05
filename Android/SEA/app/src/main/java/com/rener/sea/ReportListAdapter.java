package com.rener.sea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class ReportListAdapter extends ArrayAdapter<Report> {

    private Context context;

    public ReportListAdapter(Context context, List<Report> reports) {
        super(context, R.layout.report_list_item, reports);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        ReportHolder holder;
        Report report = getItem(position);

        //Inflate the layout and set it's views if necessary
        if (convertView == null) {
            //View hasn't been created and must be initialized
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.report_list_item, parent, false);

            holder = new ReportHolder();
            holder.nameText = (TextView) row.findViewById(R.id.report_list_item_name);
            holder.locationText = (TextView) row.findViewById(R.id.report_list_item_location);
            holder.dateText = (TextView) row.findViewById(R.id.report_list_item_date);
            row.setTag(holder);

            //Get the data
            String name = report.getName();
            String location = report.getLocation().getName();

            //Set the views
            holder.nameText.setText(name);
            holder.locationText.setText(location);
            //Set the date format according to the locale
            String dateFormat = getContext().getResources().getString(R.string.date_format);
            Locale locale = Locale.getDefault();
            String date = report.getDateString(dateFormat, locale);
            holder.dateText.setText(date);
        } else {
            //View can be recycled
            row = convertView;
        }
        return row;

    }

    private static class ReportHolder {
        TextView nameText;
        TextView locationText;
        TextView dateText;
    }
}
