package com.rener.sea;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class LocationDetailsFragment extends Fragment implements View.OnClickListener {

	public static final int SHOW_LAYOUT = 0;
	public static final int EDIT_LAYOUT = 1;
	private long location_id;
	private Location location;
	private ViewFlipper flipper;
	private TextView textName, textAddressLine1, textAddressLine2, textCity, textZipCode;
	private EditText editName, editAddressLine1, editAddressLine2, editCity, editZipCode;

	public LocationDetailsFragment() {
		//Empty constructor
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setLocation();
	}

	private void setLocation() {

		this.location_id = getArguments().getLong("location_id");
		DBEmulator db = ((MainActivity)getActivity()).getDB();
		this.location = db.findLocationByID(location_id);
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

		//Populate the fields
		setFields();

		//Set the button listeners
		view.findViewById(R.id.button_edit_location).setOnClickListener(this);
		view.findViewById(R.id.button_save_location).setOnClickListener(this);

		flipToShowLayout();

		return view;
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

	private void setFields() {

		//Set the name fields
		textName.setText(location.getName());
		editName.setText(location.getName());


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

		//TODO validate input

		//Set the instance fields
		location.setName(name);

	}

	public static LocationDetailsFragment newInstance(Location location) {
		LocationDetailsFragment fragment = new LocationDetailsFragment();
		Bundle args = new Bundle();
		long id = location.getID();
		args.putLong("location_id", id);
		fragment.setArguments(args);
		return fragment;
	}
}
