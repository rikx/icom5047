package com.rener.sea;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class PersonDetailsFragment extends Fragment implements View.OnClickListener {

	public static final int SHOW_PERSON_LAYOUT = 0;
	public static final int EDIT_PERSON_LAYOUT = 1;
	private long person_id;
	private Person person;
	private ViewFlipper flipper;
	private TextView textName, textEmail, textPhoneNumber;
	private EditText editFirstName, editMiddleName, editLastName1, editLastName2;
	private EditText editEmail, editPhoneNumber;


	public PersonDetailsFragment() {
		this.person = null;
	}

	public long getPersonID() {
		return person_id;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Define the Person to show by finding it's ID
		this.person_id = getArguments().getLong("person_id");
		DBEmulator db = ((MainActivity)getActivity()).getDB();
		this.person = db.findPersonByID(person_id);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.person_details_fragment, container, false);

		//Set the flipper that switches show and edit views
		flipper = (ViewFlipper) view.findViewById(R.id.person_flipper);

		//Set the name views
		textName = (TextView) view.findViewById(R.id.text_person_name);
		editFirstName = (EditText) view.findViewById(R.id.person_edit_first_name);
		editMiddleName = (EditText) view.findViewById(R.id.person_edit_middle_name);
		editLastName1 = (EditText) view.findViewById(R.id.person_edit_last_name1);
		editLastName2 = (EditText) view.findViewById(R.id.person_edit_last_name2);

		//Set the email views
		textEmail = (TextView) view.findViewById(R.id.text_person_email);
		editEmail = (EditText) view.findViewById(R.id.person_edit_email);

		//Set the phone number views
		textPhoneNumber = (TextView) view.findViewById(R.id.text_person_phone);
		editPhoneNumber = (EditText) view.findViewById(R.id.person_edit_phone_number);

		//Populate the fields
		setFields();

		//Set the button listeners
		view.findViewById(R.id.button_edit_person).setOnClickListener(this);
		view.findViewById(R.id.button_save_person).setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.button_edit_person :
				flipToEditLayout();
				break;
			case R.id.button_save_person :
				getFields();
				setFields();
				flipToShowLayout();
				break;
		}
	}

	private void setFields() {

		//Set the name fields
		textName.setText(person.getFullNameFirstLast());
		editFirstName.setText(person.getFirstName());
		editMiddleName.setText(person.getMiddleName());
		editLastName1.setText(person.getLastName1());
		editLastName2.setText(person.getLastName2());

		//Set the email fields if it exists
		if(person.hasEmail()) {
			String email = person.getEmail();
			textEmail.setText(email);
			editEmail.setText(email);
			textEmail.setVisibility(TextView.VISIBLE);
		}
		else {
			textEmail.setVisibility(TextView.GONE);
		}

		//Set the phone fields if it exists
		if(person.hasPhoneNumber()) {
			String phone = Integer.toString(person.getPhoneNumber());
			textPhoneNumber.setText(phone);
			textPhoneNumber.setVisibility(TextView.VISIBLE);
			editPhoneNumber.setText(phone);
		}
		else {
			textPhoneNumber.setVisibility(TextView.GONE);
		}

		flipToShowLayout();

	}

	private void flipToEditLayout() {
		flipper.setDisplayedChild(EDIT_PERSON_LAYOUT);
	}

	private void flipToShowLayout() {
		flipper.setDisplayedChild(SHOW_PERSON_LAYOUT);
	}

	private void getFields() {

		//Get the text from the fields
		String strFirstName = editFirstName.getText().toString();
		String strMiddleName = editMiddleName.getText().toString();
		String strLastName1 = editLastName1.getText().toString();
		String strLastName2 = editLastName2.getText().toString();
		String strEmail = editEmail.getText().toString();
		String strPhone = editPhoneNumber.getText().toString();

		//Set the person instance fields
		person.setFirstName(strFirstName);
		person.setMiddleName(strMiddleName);
		person.setLastName1(strLastName1);
		person.setLastName2(strLastName2);
		person.setEmail(strEmail);
		person.setPhoneNumber(strPhone);
	}

	public static PersonDetailsFragment newInstance(Person person) {
		PersonDetailsFragment fragment = new PersonDetailsFragment();
		Bundle args = new Bundle();
		long id = person.getID();
		args.putLong("person_id", id);
		fragment.setArguments(args);
		return fragment;
	}
}
