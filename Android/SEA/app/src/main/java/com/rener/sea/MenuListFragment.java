package com.rener.sea;


import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
		list = new ArrayList();
		DBService db = ((MainActivity)getActivity()).getDBService();
		if(type.equals(TYPE_MAIN)) {
			list = Arrays.asList(getResources().getStringArray(R.array.main_list_strings));
		}
		else if(type.equals(TYPE_PEOPLE)) {
			list = db.getPeople();
		}
		else if(type.equals(TYPE_LOCATIONS)) {
			list = db.getLocations();
		}
		else if(type.equals(TYPE_REPORTS)) {
			list = db.getReports();
		}
		Collections.sort(list);
		ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),
				android.R.layout.simple_list_item_1, list);
		setListAdapter(adapter);
		ListView l = getListView();
		l.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		l.setItemChecked(curPos, curPos !=-1 ? true : false);
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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	public String getType() {
		return type;
	}

	public int getPosition() {
		return curPos;
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
