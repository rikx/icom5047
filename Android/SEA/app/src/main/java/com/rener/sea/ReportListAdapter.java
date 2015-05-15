package com.rener.sea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReportListAdapter extends ArrayAdapter<Report> {

    private Context context;
    private List<Report> reports;
    private Filter filter;

    public ReportListAdapter(Context context, List<Report> reports) {
        super(context, R.layout.report_list_item, reports);
        this.reports = reports;
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

    @Override
    public int getCount() {
        return reports.size();
    }

    @Override
    public Report getItem(int position) {
        return reports.get(position);
    }

    @Override
    public long getItemId(int position) {
        return reports.get(position).getId();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) filter = new ReportFilter(reports);
        return filter;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private static class ReportHolder {
        TextView nameText;
        TextView locationText;
        TextView dateText;
    }


    private class ReportFilter extends Filter {

        private ArrayList<Report> sourceReports;

        public ReportFilter(List<Report> reports) {
            sourceReports = new ArrayList<>();
            synchronized (this) {
                sourceReports.addAll(reports);
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String constraint = charSequence.toString().toLowerCase();
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Report> filtered = new ArrayList<>();
                for (Report r : sourceReports) {
                    boolean hit = isHit(r, constraint);
                    if (hit) filtered.add(r);
                }
                results.values = filtered;
                results.count = filtered.size();
            } else {
                synchronized (this) {
                    results.values = sourceReports;
                    results.count = sourceReports.size();
                }
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            ArrayList<Report> filtered = (ArrayList<Report>) filterResults.values;
            notifyDataSetChanged();
            clear();
            for (Report r : filtered)
                add(r);
            notifyDataSetInvalidated();
        }

        private boolean isHit(Report report, String constraint) {

            //Get the values
            String name = report.getName().toLowerCase();
            String location = report.getLocation().toString().toLowerCase();
            String dateFormat = getContext().getResources().getString(R.string.date_format);
            Locale locale = Locale.getDefault();
            String date = report.getDateString(dateFormat, locale).toLowerCase();

            //Determine if hit
            boolean nameHit = name.contains(constraint);
            boolean locationHit = location.contains(constraint);
            boolean dateHit = date.contains(constraint);
            boolean hit = (nameHit || locationHit || dateHit);

            return hit;
        }
    }
}
