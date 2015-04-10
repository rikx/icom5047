package com.rener.sea;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<String> {

	private int hidingItemIndex;

	public CustomAdapter(Context context, int resource, List objects,
	                     int hidingItemIndex) {
		super(context, resource, objects);
		this.hidingItemIndex = hidingItemIndex;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View v = null;
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