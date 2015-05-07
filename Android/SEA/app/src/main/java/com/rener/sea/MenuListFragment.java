package com.rener.sea;


import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

/**
 * A fragment class used to display a list of objects in the application.
 */
public class MenuListFragment extends ListFragment implements TextWatcher {

    public static String TYPE_PEOPLE = "PEOPLE";
    public static String TYPE_REPORTS = "REPORTS";
    public static String TYPE_LOCATIONS = "LOCATIONS";
    private int curPos = -1;
    private String type;
    private ArrayAdapter adapter;
    private List list;
    private EditText editSearch;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_list_fragment, container, false);
        editSearch = (EditText) view.findViewById(R.id.inputSearch);
        editSearch.addTextChangedListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Restore arguments if they exist
        curPos = getArguments().getInt("index", -1);
        type = getArguments().getString("type");

        //Set the list and it's respective adapter
        DBHelper db = ((MainActivity) getActivity()).getDBHelper();
        String empty = getResources().getString(R.string.no_data);
        if (type.equals(TYPE_PEOPLE)) {
            empty = getResources().getString(R.string.no_people);
            list = db.getAllPersons();
            adapter = new ArrayAdapter<Person>(getActivity(),
                    android.R.layout.simple_list_item_1, list);
        } else if (type.equals(TYPE_LOCATIONS)) {
            empty = getResources().getString(R.string.no_locations);
            list = db.getAllLocations();
            adapter = new ArrayAdapter<Location>(getActivity(),
                    android.R.layout.simple_list_item_1, list);
        } else if (type.equals(TYPE_REPORTS)) {
            empty = getResources().getString(R.string.no_reports);
            list = db.getAllReports();
            adapter = new ReportListAdapter(getActivity(), list);
        }

        //Set the empty view
        //TODO: set empty view programmatically

        //Sort the list
        //Collections.sort(list);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_report_action:
                newReport();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Notifies the list adapter that data has changed and it should update it's views
     */
    public void onListDataChanged() {
        adapter.notifyDataSetChanged();
    }

    /**
     * Gets the MenuListFragment type
     * @return the MenuListFragment's type
     */
    public String getType() {
        return type;
    }

    private void newReport() {
        DBHelper dbHelper = ((MainActivity)getActivity()).getDBHelper();
        int fcs = dbHelper.getAllFlowcharts().size();
        int ls = dbHelper.getAllLocations().size();
        boolean allow = (fcs != 0 && ls != 0);
        if(allow) {
            startActivity(new Intent(getActivity(), SurveyActivity.class));
            getActivity().finish();
        }
        else if(fcs ==0) {
            String message = getResources().getString(R.string.no_flowcharts);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
        else {
            String message = getResources().getString(R.string.no_locations);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(!type.equals(TYPE_REPORTS))
            adapter.getFilter().filter(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
