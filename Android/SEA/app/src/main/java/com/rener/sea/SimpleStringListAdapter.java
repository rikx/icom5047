package com.rener.sea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SimpleStringListAdapter extends ArrayAdapter<Object> implements
        SimpleStringFilterable {

    private Context context;
    private List<Object> objects;
    private int layout;
    private SimpleStringFilter filter;

    public SimpleStringListAdapter(Context context, int layout, List<Object> objects) {
        super(context, layout, objects);
        this.context = context;
        this.layout = layout;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        //View hasn't been created and must be initialized
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(layout, parent, false);
        TextView textView = (TextView) row.findViewById(R.id.string_textview);
        String string = getItem(position).toString();
        textView.setText(string);
        return row;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public SimpleStringFilter getSimpleStringFilter() {
        if (filter == null) filter = new SimpleStringFilter(objects);
        return filter;
    }

	public List<Object> getList() {
		return objects;
	}

	public void refreshList(List objects) {
		this.objects = objects;
		notifyDataSetChanged();
	}
}
