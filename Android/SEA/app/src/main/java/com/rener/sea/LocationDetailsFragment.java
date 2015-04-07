package com.rener.sea;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.List;

public class LocationDetailsFragment extends Fragment
		implements View.OnClickListener, AdapterView.OnItemSelectedListener {

	public static final int SHOW_LAYOUT = 0;
	public static final int EDIT_LAYOUT = 1;
	private Location location;
	private ViewFlipper flipper;
	private TextView textName, textAddressLine1, textAddressLine2, textCity, textZipCode;
	private TextView textOwner, textManager;
	private EditText editName, editAddressLine1, editAddressLine2, editCity, editZipCode;
	private boolean viewCreated;
	private Spinner ownerSpinner, managerSpinner;
	private List peopleList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
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

		//Set the owner spinner
		ownerSpinner = (Spinner) view.findViewById(R.id.location_owner_spinner);
		ownerSpinner.setOnItemSelectedListener(this);

		//Set the manager spinner
		managerSpinner = (Spinner) view.findViewById(R.id.location_manager_spinner);
		managerSpinner.setOnItemSelectedListener(this);

		peopleList = ((MainActivity)getActivity()).getDataFromDB().getPeople();
		ArrayAdapter adapter;
		adapter = new ArrayAdapter(getActivity(),
				android.R.layout.simple_list_item_1, peopleList);
		ownerSpinner.setAdapter(adapter);
		managerSpinner.setAdapter(adapter);

		//Set the button views
		view.findViewById(R.id.button_edit_location).setOnClickListener(this);
		view.findViewById(R.id.button_save_location).setOnClickListener(this);

		viewCreated = true;
		setFields();
		flipToShowLayout();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.button_edit_location :
				flipToEditLayout();
				break;
			case R.id.button_save_location :
				getFields();
				setFields();
				flipToShowLayout();
				break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
		if(view != null) {
			Person owner = (Person) ownerSpinner.getSelectedItem();
			location.setOwner(owner);
			Person manager = (Person) managerSpinner.getSelectedItem();
			location.setManager(manager);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {

	}

	private void setFields() {

		//Set the name fields
		textName.setText(location.getName());
		editName.setText(location.getName());

		//Set the address fields
		textAddressLine1.setText(location.getAddressLine(1));
		editAddressLine1.setText(location.getAddressLine(1));
		textAddressLine2.setText(location.getAddressLine(2));
		editAddressLine2.setText(location.getAddressLine(2));
		textCity.setText(location.getCity());
		editCity.setText(location.getCity());
		textZipCode.setText(location.getZipCode());
		editZipCode.setText(location.getZipCode());

		if(location.hasOwner()) {
			for(int i=0; i<peopleList.size(); i++) {
				Person item = (Person) ownerSpinner.getAdapter().getItem(i);
				if(location.getOwner().getID() == item.getID())
					ownerSpinner.setSelection(i);
			}
			String label = getResources().getString(R.string.owner_label);
			String owned = location.getOwner().toString();
			textOwner.setText(label+" "+owned);
		}
		else {
			textOwner.setText(R.string.no_owner);
		}

		if(location.hasManager()) {
			for(int i=0; i<peopleList.size(); i++) {
				Person item = (Person) ownerSpinner.getAdapter().getItem(i);
				if(location.getManager().getID() == item.getID())
					managerSpinner.setSelection(i);
			}
			String label = getResources().getString(R.string.manager_label);
			String managed = location.getManager().toString();
			textManager.setText(label+" "+managed);
		}
		else {
			textManager.setText(R.string.no_manager);
		}
	}

	private void flipToEditLayout() {
		flipper.setDisplayedChild(EDIT_LAYOUT);
	}

	private void flipToShowLayout() {
		flipper.setDisplayedChild(SHOW_LAYOUT);
	}

	private void getFields() {

		//Get the text from the fields
		String name = editName.getText().toString();
		String line1 = editAddressLine1.getText().toString();
		String line2 = editAddressLine2.getText().toString();
		String city = editCity.getText().toString();
		String zip = editZipCode.getText().toString();

		//TODO validate input

		//Set the instance fields
		location.setName(name);
		location.setAddressLine(1, line1);
		location.setAddressLine(2, line2);
		location.setCity(city);
		location.setZipCode(zip);
	}

	public Location setLocation(Location location) {
		this.location = location;
		if (viewCreated)
			setFields();
		return this.location;
	}
}
