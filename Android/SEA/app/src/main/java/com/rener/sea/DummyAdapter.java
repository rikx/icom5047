package com.rener.sea;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * ArrayAdapter that facilitates hiding a dummy item
 */
public class DummyAdapter extends ArrayAdapter<String> {

    private int hidingItemIndex;

    public DummyAdapter(Context context, int resource, List objects,
                        int hidingItemIndex) {
        super(context, resource, objects);
        this.hidingItemIndex = hidingItemIndex;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v;
        if (position == hidingItemIndex) {
            TextView tv = new TextView(getContext());
            tv.setVisibility(View.GONE);
            v = tv;
        } else {
            v = super.getDropDownView(position, null, parent);
        }
        return v;
    }
}