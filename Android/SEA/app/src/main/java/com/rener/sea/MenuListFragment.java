package com.rener.sea;


import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A fragment class used to display a list of objects in the application.
 */
public class MenuListFragment extends ListFragment {

    public static String TYPE_PEOPLE = "PEOPLE";
    public static String TYPE_REPORTS = "REPORTS";
    public static String TYPE_LOCATIONS = "LOCATIONS";
    private int curPos = -1;
    private String type;
    private ArrayAdapter adapter;

    /**
     * Create a MenuListFragment instance with a given type
     *
     * @param type the MenuListFragment type
     * @return a new MenuListFragment object
     */
    public static MenuListFragment newInstance(String type) {
        MenuListFragment fragment = new MenuListFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Create a MenuListFragment instance with a given type and preselected index
     *
     * @param type  the MenuListFragment type
     * @param index the preselected index
     * @return a new MenuListFragment object
     */
    public static MenuListFragment newInstance(String type, int index) {
        MenuListFragment fragment = new MenuListFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Restore the state
        if (savedInstanceState != null) {
            type = savedInstanceState.getString("type");
            curPos = savedInstanceState.getInt("index");
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Restore arguments if they exist
        curPos = getArguments().getInt("index", -1);
        type = getArguments().getString("type");

        //Set the list and it's respective adapter
        List list = new ArrayList();
        DBHelper db = ((MainActivity) getActivity()).getDBHelper();
        //ArrayAdapter adapter = null;
        if (type.equals(TYPE_PEOPLE)) {

            list = db.getAllPersons();
            Log.i(this.toString(), " " + getActivity() + " Get all People " + list.toString());
            adapter = new ArrayAdapter<Person>(getActivity(),
                    android.R.layout.simple_list_item_1, list);
        } else if (type.equals(TYPE_LOCATIONS)) {
            list = db.getAllLocations();
            adapter = new ArrayAdapter<Location>(getActivity(),
                    android.R.layout.simple_list_item_1, list);
        } else if (type.equals(TYPE_REPORTS)) {
            list = db.getAllReports();
            adapter = new ReportListAdapter(getActivity(), list);
        }

        //Sort the list
        Collections.sort(list);

        //Set the list adapter
        setListAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("type", type);
        savedInstanceState.putInt("index", curPos);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (curPos != position) {
            MainActivity callback = (MainActivity) getActivity();
            callback.OnMenuItemSelectedListener(type, l, position);
        }
        curPos = position;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (type.equals(TYPE_REPORTS)) {
            inflater.inflate(R.menu.reports_actions, menu);
        } else if (type.equals(TYPE_PEOPLE))
            inflater.inflate(R.menu.people_actions, menu);
        else if (type.equals(TYPE_LOCATIONS))
            inflater.inflate(R.menu.locations_actions, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    public void notifyDataChanged() {
        adapter.notifyDataSetChanged();
    }

    public String getType() {
        return type;
    }

    public int getPosition() {
        return curPos;
    }
}
