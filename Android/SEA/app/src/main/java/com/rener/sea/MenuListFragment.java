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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A fragment class used to display a list of objects in the application.
 */
public class MenuListFragment extends ListFragment implements TextWatcher, View.OnClickListener, Toolbar.OnMenuItemClickListener {

    public static String TYPE_PEOPLE = "PEOPLE";
    public static String TYPE_REPORTS = "REPORTS";
    public static String TYPE_LOCATIONS = "LOCATIONS";
    public static String TYPE_APPOINTMENTS = "APPOINTMENTS";
    private int curPos = -1;
    private String type;
    private ArrayAdapter adapter;
    private List list;
    private EditText editSearch;
    private Button clearSearchButton;
    private Toolbar toolbar;

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

        //Set the toolbar
        toolbar = (Toolbar) view.findViewById(R.id.menu_list_toolbar);
        //TODO: do something with the toolbar?

        //Initialize search views
        editSearch = (EditText) view.findViewById(R.id.inputSearch);
        editSearch.addTextChangedListener(this);
        clearSearchButton = (Button) view.findViewById(R.id.clearSearch);
        clearSearchButton.setOnClickListener(this);
        clearSearchButton.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getMenuInflater().inflate(R.menu.navigation_menu, toolbar.getMenu());
        toolbar.setOnMenuItemClickListener(this);

        //Restore arguments if they exist
        curPos = getArguments().getInt("index", -1);
        type = getArguments().getString("type");

        //Set the list and it's respective adapter
        DBHelper db = ((MainActivity) getActivity()).getDBHelper();
        String empty = getString(R.string.no_data);
        if (type.equals(TYPE_PEOPLE)) {
            empty = getString(R.string.no_people);
            list = db.getAllPersons();
            adapter = new ArrayAdapter<Person>(getActivity(),
                    android.R.layout.simple_list_item_1, list);
        } else if (type.equals(TYPE_LOCATIONS)) {
            empty = getString(R.string.no_locations);
            list = db.getAllLocations();
            adapter = new ArrayAdapter<Location>(getActivity(),
                    android.R.layout.simple_list_item_1, list);
        } else if (type.equals(TYPE_REPORTS)) {
            empty = getString(R.string.no_reports);
            list = db.getAllReports();
            adapter = new ReportListAdapter(getActivity(), list);
        } else if(type.equals(TYPE_APPOINTMENTS)) {
            //TODO: finish this
//            list = db.getAllAppointments();
//            adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
        }

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
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return getActivity().onOptionsItemSelected(menuItem);
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        int visibility = charSequence.length() > 0 ? View.VISIBLE : View.GONE ;
        clearSearchButton.setVisibility(visibility);
        //adapter.getFilter().filter(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clearSearch:
                editSearch.setText("");
                break;
        }
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
        DBHelper dbHelper = ((MainActivity) getActivity()).getDBHelper();
        int fcs = dbHelper.getAllFlowcharts().size();
        int ls = dbHelper.getAllLocations().size();
        boolean allow = (fcs != 0 && ls != 0);
        if (allow) {
            startActivity(new Intent(getActivity(), SurveyActivity.class));
            getActivity().finish();
        } else if (fcs == 0) {
            String message = getResources().getString(R.string.no_flowcharts);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        } else {
            String message = getResources().getString(R.string.no_locations);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private List<Calendar> initDummyAppointments() {
        List<Calendar> dates = new ArrayList<>();
        for(int i=0; i<4; i++)
            dates.add(Calendar.getInstance());
        return dates;
    }
}
