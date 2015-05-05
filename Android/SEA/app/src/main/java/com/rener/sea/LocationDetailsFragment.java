package com.rener.sea;

import android.app.Fragment;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An Android fragment that manages the display of details for a single Location object
 */
public class LocationDetailsFragment extends Fragment implements AdapterView
        .OnItemSelectedListener {

    private static final int SHOW_LAYOUT = 0;
    private static final int EDIT_LAYOUT = 1;
    private Location location;
    private ViewFlipper flipper;
    private TextView textName, textLicense;
    private TextView textAddressLine1, textAddressLine2, textCity, textZipCode;
    private TextView textOwner, textManager, textAgent;
    private EditText editName, editLicense;
    private EditText editAddressLine1, editAddressLine2, editCity, editZipCode;
    private boolean viewCreated;
    private Spinner ownerSpinner, managerSpinner, agentSpinner;
    private List peopleList;
    private Menu options;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.location_details_fragment, container, false);

        //Set the flipper that switches show and edit views
        flipper = (ViewFlipper) view.findViewById(R.id.location_flipper);

        //Set the name views
        textName = (TextView) view.findViewById(R.id.location_text_name);
        editName = (EditText) view.findViewById(R.id.location_edit_name);

        //Set the license views
        textLicense = (TextView) view.findViewById(R.id.location_text_license);
        editLicense = (EditText) view.findViewById(R.id.location_edit_license);

        //Set the address line 1 views
        textAddressLine1 = (TextView) view.findViewById(R.id.address_text_line1);
        editAddressLine1 = (EditText) view.findViewById(R.id.address_edit_line1);

        //Set the address line 2 views
        textAddressLine2 = (TextView) view.findViewById(R.id.address_text_line2);
        editAddressLine2 = (EditText) view.findViewById(R.id.address_edit_line2);

        //Set the city views
        textCity = (TextView) view.findViewById(R.id.address_text_city);
        editCity = (EditText) view.findViewById(R.id.address_edit_city);

        //Set the zip code views
        textZipCode = (TextView) view.findViewById(R.id.address_text_zip_code);
        editZipCode = (EditText) view.findViewById(R.id.address_edit_zip_code);

        //Set the owner text
        textOwner = (TextView) view.findViewById(R.id.location_text_owner);

        //Set the manager text
        textManager = (TextView) view.findViewById(R.id.location_text_manager);

        //Set the agent text
        textAgent = (TextView) view.findViewById(R.id.location_text_agent);

        //Set the owner spinner
        ownerSpinner = (Spinner) view.findViewById(R.id.location_owner_spinner);
        ownerSpinner.setOnItemSelectedListener(this);

        //Set the manager spinner
        managerSpinner = (Spinner) view.findViewById(R.id.location_manager_spinner);
        managerSpinner.setOnItemSelectedListener(this);

        //Set the agent spinner
        agentSpinner = (Spinner) view.findViewById(R.id.location_agent_spinner);
        agentSpinner.setOnItemSelectedListener(this);


        //Populate the spinner lists
        DBHelper dbHelper = ((MainActivity) getActivity()).getDBHelper();
        peopleList = dbHelper.getAllPersons();
        Collections.sort(peopleList);
        List<Person> managerList = new ArrayList(peopleList);
        List<Person> agentList = new ArrayList(peopleList);
        List<Person> ownerList = new ArrayList(peopleList);

        //Add a dummy person to each list
        String dummy = getString(R.string.manager);
        managerList.add(0, new Person(dummy));
        dummy = getString(R.string.owner);
        ownerList.add(0, new Person(dummy));
        dummy = getString(R.string.agent);
        agentList.add(0, new Person(dummy));

        //Set the adapters
        DummyAdapter adapter = new DummyAdapter(getActivity(),
                android.R.layout.simple_list_item_1, ownerList, 0);
        ownerSpinner.setAdapter(adapter);
        adapter = new DummyAdapter(getActivity(),
                android.R.layout.simple_list_item_1, managerList, 0);
        managerSpinner.setAdapter(adapter);
        adapter = new DummyAdapter(getActivity(),
                android.R.layout.simple_list_item_1, agentList, 0);
        agentSpinner.setAdapter(adapter);

        viewCreated = true;
        setFields();
        flipper.setDisplayedChild(SHOW_LAYOUT);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_location_action:
                flipToEditLayout();
                break;
            case R.id.save_location_action:
                getFields();
                setFields();
                flipToShowLayout();
                //Notify the activity that data has changed
                ((MainActivity) getActivity()).onDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (view != null) {
            Person owner = (Person) ownerSpinner.getSelectedItem();
            location.setOwner(owner);
            Person manager = (Person) managerSpinner.getSelectedItem();
            location.setManager(manager);
            Person agent = (Person) agentSpinner.getSelectedItem();
            location.setAgent(agent);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        this.options = menu;
        inflater.inflate(R.menu.location_actions, menu);

        //Highlight the save and edit buttons
        int color = getResources().getColor(android.R.color.holo_green_light);
        ColorFilter filter = new PorterDuffColorFilter(color, PorterDuff.Mode.DARKEN);
        MenuItem save = menu.findItem(R.id.save_person_action);
        MenuItem edit = menu.findItem(R.id.edit_person_action);
        Drawable d = save.getIcon();
        d.setColorFilter(filter);
        save.setIcon(d);
        d = edit.getIcon();
        d.setColorFilter(filter);
        edit.setIcon(d);
    }

    /**
     * Sets the static views using the Location data
     */
    private void setFields() {

        //Set the name fields
        textName.setText(location.getName());
        editName.setText(location.getName());

        //Set the license fields
        textLicense.setText(location.getLicense());
        editLicense.setText(location.getLicense());

        //Set the address fields
        textAddressLine1.setText(location.getAddressLine(1));
        editAddressLine1.setText(location.getAddressLine(1));
        textAddressLine2.setText(location.getAddressLine(2));
        editAddressLine2.setText(location.getAddressLine(2));
        textCity.setText(location.getCity());
        editCity.setText(location.getCity());
        textZipCode.setText(location.getZipCode());
        editZipCode.setText(location.getZipCode());

        if (location.hasOwner()) {
            for (int i = 0; i < peopleList.size(); i++) {
                Person item = (Person) ownerSpinner.getAdapter().getItem(i);
                if (location.getOwner().getId() == item.getId())
                    ownerSpinner.setSelection(i);
            }
            String label = getResources().getString(R.string.owner_label);
            String owned = location.getOwner().toString();
            textOwner.setText(label + ": " + owned);
        } else {
            textOwner.setText(R.string.no_owner);
        }

        if (location.hasManager()) {
            for (int i = 0; i < peopleList.size(); i++) {
                Person item = (Person) ownerSpinner.getAdapter().getItem(i);
                if (location.getManager().getId() == item.getId())
                    managerSpinner.setSelection(i);
            }
            String label = getResources().getString(R.string.manager_label);
            String managed = location.getManager().toString();
            textManager.setText(label + ": " + managed);
        } else {
            textManager.setText(R.string.no_manager);
        }

        if (location.hasAgent()) {
            for (int i = 0; i < peopleList.size(); i++) {
                Person item = (Person) agentSpinner.getAdapter().getItem(i);
                if (location.getAgent().getId() == item.getId())
                    agentSpinner.setSelection(i);
            }
            String label = getResources().getString(R.string.agent_label);
            String assigned = location.getAgent().toString();
            textAgent.setText(label + " " + assigned);
        } else {
            textAgent.setText(R.string.no_agent);
        }
    }

    private void flipToEditLayout() {
        options.findItem(R.id.edit_location_action).setVisible(false);
        flipper.setDisplayedChild(EDIT_LAYOUT);
        options.findItem(R.id.save_location_action).setVisible(true);
    }

    private void flipToShowLayout() {
        options.findItem(R.id.save_location_action).setVisible(false);
        flipper.setDisplayedChild(SHOW_LAYOUT);
        options.findItem(R.id.edit_location_action).setVisible(true);
    }

    /**
     * Gets the data from the views when it is edited
     */
    private void getFields() {

        //Get the text from the fields
        String name = editName.getText().toString();
        String license = editLicense.getText().toString();
        String line1 = editAddressLine1.getText().toString();
        String line2 = editAddressLine2.getText().toString();
        String city = editCity.getText().toString();
        String zip = editZipCode.getText().toString();

        //TODO check input

        //Set the instance fields
        location.setName(name);
        location.setLicense(license);
        location.setAddressLine(1, line1);
        location.setAddressLine(2, line2);
        location.setCity(city);
        location.setZipCode(zip);
    }

    /**
     * Dynamically changes the Location object for the fragment
     *
     * @param location the new Location
     */
    public void setLocation(Location location) {
        this.location = location;
        if (viewCreated)
            setFields();
    }
}
