package com.rener.sea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SimpleStringListAdapter extends ArrayAdapter<Object> {

    private Context context;
    private List<Object> objects;
    private int layout;

    public SimpleStringListAdapter(Context context, int layout, List<Object> objects) {
        super(context, layout, objects);
        this.objects = objects;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;

        //Inflate the layout and set it's views if necessary
        if (convertView == null) {
            //View hasn't been created and must be initialized
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, parent, false);
            TextView textView = (TextView) row.findViewById(R.id.string_textview);
            String string = getItem(position).toString();
            textView.setText(string);

        } else {
            //View can be recycled
            row = convertView;
        }
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
}
