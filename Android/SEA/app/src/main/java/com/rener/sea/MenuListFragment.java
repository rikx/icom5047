package com.rener.sea;


import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MenuListFragment extends ListFragment {

	public static String TYPE_PEOPLE = "PEOPLE";
	public static String TYPE_REPORTS = "REPORTS";
	public static String TYPE_LOCATIONS = "LOCATIONS";
	public static String TYPE_MAIN = "MAIN";
	private int curPos = -1;
	private String type;
	private List list;
	OnMenuItemSelectedListener mCallback;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(savedInstanceState != null) {
			type = savedInstanceState.getString("type");
			curPos = savedInstanceState.getInt("index");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container
			,Bundle savedInstanceState) {
		return inflater.inflate(R.layout.menu_list_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		curPos = getArguments().getInt("index", -1);
		type = getArguments().getString("type");
		ArrayAdapter adapter = null;
		if(type.equals(TYPE_MAIN)) {
			adapter = new ArrayAdapter<>(getActivity(),
					android.R.layout.simple_list_item_1,
					getResources().getStringArray(R.array.menu_list_strings));
		}
		else if(type.equals(TYPE_PEOPLE)) {
			list = ((MainActivity)getActivity()).getDataFromDB().getPeople();
			adapter = new ArrayAdapter<>(getActivity(),
					android.R.layout.simple_list_item_1, list);
		}
		else if(type.equals(TYPE_LOCATIONS)) {
			list = ((MainActivity)getActivity()).getDataFromDB().getLocations();
			adapter = new ArrayAdapter<>(getActivity(),
					android.R.layout.simple_list_item_1, list);
		}
		else if(type.equals(TYPE_REPORTS)) {
			adapter = new ArrayAdapter<>(getActivity(),
					android.R.layout.simple_list_item_1,
					getResources().getStringArray(R.array.dummy_reports_list_strings));
		}
		setListAdapter(adapter);
		ListView l = getListView();
		l.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		if(curPos != -1)
			l.setItemChecked(curPos, true);
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putString("type", type);
		savedInstanceState.putInt("index", curPos);
		super.onSaveInstanceState(savedInstanceState);
	}

	public interface OnMenuItemSelectedListener {
		public void OnMenuItemSelectedListener(String type, ListView l, View v, int position);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (OnMenuItemSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
				+ " must implement OnMenuItemSelectedListener");
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if(curPos != position) {
			mCallback.OnMenuItemSelectedListener(type, l, v, position);
			super.onListItemClick(l, v, position, id);
		}
		getListView().setItemChecked(position, true);
		curPos = position;
	}

	public String getType() {
		return type;
	}

	public static MenuListFragment newInstance(String type) {
		MenuListFragment fragment = new MenuListFragment();
		Bundle args = new Bundle();
		args.putString("type", type);
		fragment.setArguments(args);
		return fragment;
	}

	public static MenuListFragment newInstance(String type, int index) {
		MenuListFragment fragment = new MenuListFragment();
		Bundle args = new Bundle();
		args.putString("type", type);
		args.putInt("index", index);
		fragment.setArguments(args);
		return fragment;
	}
}
