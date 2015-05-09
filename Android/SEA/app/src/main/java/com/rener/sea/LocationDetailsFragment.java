package com.rener.sea;

import android.app.Fragment;
import android.content.Intent;
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
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An Android fragment that manages the display of details for a single Location object
 */
public class LocationDetailsFragment extends Fragment implements DetailsFragment, AdapterView
        .OnItemSelectedListener, View.OnClickListener {

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
    private Spinner ownerSpinner, managerSpinner;
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
        textOwner.setOnClickListener(this);

        //Set the manager text
        textManager = (TextView) view.findViewById(R.id.location_text_manager);
        textManager.setOnClickListener(this);

        //Set the agent text
        textAgent = (TextView) view.findViewById(R.id.location_text_agent);

        //Set the owner spinner
        ownerSpinner = (Spinner) view.findViewById(R.id.location_owner_spinner);
        ownerSpinner.setOnItemSelectedListener(this);

        //Set the manager spinner
        managerSpinner = (Spinner) view.findViewById(R.id.location_manager_spinner);
        managerSpinner.setOnItemSelectedListener(this);

        populateSpinners();

        //Determine if the new location view should be displayed
        if(location != null) {
            setDataViews();
            flipper.setDisplayedChild(SHOW_LAYOUT);
        }
        else {
            flipper.setDisplayedChild(EDIT_LAYOUT);
        }
        viewCreated = true;
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
                saveLocation();
                break;
            case R.id.new_report_location_action:
                newReport();
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
//        int color = getResources().getColor(android.R.color.holo_green_light);
//        ColorFilter filter = new PorterDuffColorFilter(color, PorterDuff.Mode.DARKEN);
//        MenuItem save = menu.findItem(R.id.save_person_action);
//        MenuItem edit = menu.findItem(R.id.edit_person_action);
//        Drawable dSave = save.getIcon();
//        dSave.setColorFilter(filter);
//        save.setIcon(dSave);
//        Drawable dEdit = edit.getIcon();
//        dEdit.setColorFilter(filter);
//        edit.setIcon(dEdit);
    }

    @Override
    public boolean onDetailsChanged() {
        int displayed = flipper.getDisplayedChild();
        if(viewCreated) {
            if(displayed == SHOW_LAYOUT) {
                setDataViews();
            }
            else if(displayed == EDIT_LAYOUT) {
                populateSpinners();
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.location_text_owner:
                goToLocationOwner();
                break;
            case R.id.location_text_manager:
                goToLocationManager();
                break;
        }
    }

    /**
     * Sets the location views using the Location data
     */
    private void setDataViews() {

        //Set the name views
        String name = location.getName();
        textName.setText(name);
        editName.setText(name);

        //Set the license fields
        String licence = location.getLicense();
        textLicense.setText(licence);
        editLicense.setText(licence);

        //Set the address fields
        String al1 = location.getAddressLine(1);
        if(al1.equals("")) {
            textAddressLine1.setVisibility(View.GONE);
        }
        else {
            textAddressLine1.setText(al1);
        }
        editAddressLine1.setText(al1);
        String al2 = location.getAddressLine(2);
        if(al2.equals("")) {
            textAddressLine2.setVisibility(View.GONE);
        }
        else {
            textAddressLine2.setText(al2);
        }
        editAddressLine2.setText(al2);
        String city = location.getCity();
        if(city.equals("")) {
            textCity.setVisibility(View.GONE);
        }
        else {
            textCity.setText(city);
        }
        editCity.setText(city);
        String zip = location.getZipCode();
        if(zip.equals("")) {
            textZipCode.setVisibility(View.GONE);
        }
        else {
            textZipCode.setText(zip);
        }
        editZipCode.setText(zip);

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
            String label = getResources().getString(R.string.agent_label);
            String assigned = location.getAgent().toString();
            textAgent.setText(label + ": " + assigned);
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
        ((MainActivity)getActivity()).hideKeyboard();
        options.findItem(R.id.save_location_action).setVisible(false);
        flipper.setDisplayedChild(SHOW_LAYOUT);
        options.findItem(R.id.edit_location_action).setVisible(true);
    }

    private void saveLocation() {
        if(getFields()) {
            setDataViews();
            flipToShowLayout();
            ((MainActivity) getActivity()).onDataChanged();
        }
    }

    /**
     * Gets the data from the views when it is edited
     * @return true if all data was set correctly
     */
    private boolean getFields() {

        //Get the text from the fields
        String name = editName.getText().toString().trim();
        String licence = editLicense.getText().toString().trim();
        String line1 = editAddressLine1.getText().toString().trim();
        String line2 = editAddressLine2.getText().toString().trim();
        String city = editCity.getText().toString().trim();
        String zip = editZipCode.getText().toString().trim();

        if(name.equals("")) {
            editName.setText("");
            String message = getString(R.string.empty_location_name);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            return false;
        }

        //Set the instance fields
        location.setName(name);
        boolean valid = location.setLicense(licence) != 0;
        location.setAddressLine(1, line1);
        location.setAddressLine(2, line2);
        location.setCity(city);
        location.setZipCode(zip);

        if (licence.equals("") || !valid) {
            if(licence.equals("")) editLicense.setText("");
            String message = getString(R.string.invalid_licence);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /**
     * Dynamically changes the Location object for the fragment
     *
     * @param location the new Location
     */
    public void setLocation(Location location) {
        this.location = location;
        if (viewCreated)
            setDataViews();
    }

    private void populateSpinners() {
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
                android.R.layout.simple_spinner_dropdown_item, ownerList, 0);
        ownerSpinner.setAdapter(adapter);
        adapter = new DummyAdapter(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, managerList, 0);
        managerSpinner.setAdapter(adapter);
    }

    private void newReport() {
        DBHelper dbHelper = ((MainActivity)getActivity()).getDBHelper();
        int fcs = dbHelper.getAllFlowcharts().size();
        boolean allow = (fcs != 0);
        if(allow) {
            Intent intent = new Intent(getActivity(), SurveyActivity.class);
            intent.putExtra("LOCATION_ID", location.getId());
            startActivity(intent);
            getActivity().finish();
        }
        else {
            String message = getResources().getString(R.string.no_flowcharts);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void goToLocationOwner() {
        //TODO: test this
        long id = location.getOwner().getId();
        ((MainActivity)getActivity()).onDetailsRequest("PERSON", "OWNER", id);
    }

    private void goToLocationManager() {
        long id = location.getOwner().getId();
        ((MainActivity)getActivity()).onDetailsRequest("PERSON", "MANAGER", id);
    }
}
