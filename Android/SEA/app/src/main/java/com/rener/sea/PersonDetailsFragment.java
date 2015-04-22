package com.rener.sea;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * An Android fragment class used to display data pertaining to a person.
 */
public class PersonDetailsFragment extends Fragment {

	private static final int SHOW_LAYOUT = 0;
	private static final int EDIT_LAYOUT = 1;
	private Person person;
	private ViewFlipper flipper;
	private TextView textName, textEmail, textPhoneNumber;
	private EditText editFirstName, editMiddleName, editLastName1, editLastName2;
	private EditText editEmail, editPhoneNumber;
	private boolean viewCreated;
	private Menu options;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.person_details_fragment, container, false);

		//Set the flipper that switches show and edit views
		flipper = (ViewFlipper) view.findViewById(R.id.person_flipper);

		//Set the name views
		textName = (TextView) view.findViewById(R.id.person_text_name);
		editFirstName = (EditText) view.findViewById(R.id.person_edit_first_name);
		editMiddleName = (EditText) view.findViewById(R.id.person_edit_middle_name);
		editLastName1 = (EditText) view.findViewById(R.id.person_edit_last_name1);
		editLastName2 = (EditText) view.findViewById(R.id.person_edit_last_name2);

		//Set the email views
		textEmail = (TextView) view.findViewById(R.id.person_text_email);
		editEmail = (EditText) view.findViewById(R.id.person_edit_email);

		//Set the phone number views
		textPhoneNumber = (TextView) view.findViewById(R.id.person_text_phone);
		editPhoneNumber = (EditText) view.findViewById(R.id.person_edit_phone_number);

		viewCreated= true;
		setFields();
		flipper.setDisplayedChild(SHOW_LAYOUT);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		//super.onCreateOptionsMenu(menu, inflater);
		this.options = menu;
		inflater.inflate(R.menu.person_actions, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.edit_person_action :
				flipToEditLayout();
				break;
			case R.id.save_person_action :
				getFields();
				setFields();
				flipToShowLayout();
				break;
		}

		return super.onOptionsItemSelected(item);
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
			String phone = person.getPhoneNumber();
			textPhoneNumber.setText(phone);
			textPhoneNumber.setVisibility(TextView.VISIBLE);
			editPhoneNumber.setText(phone);
		}
		else {
			textPhoneNumber.setVisibility(TextView.GONE);
		}
	}

	private void flipToEditLayout() {
		flipper.setDisplayedChild(EDIT_LAYOUT);
		options.findItem(R.id.edit_person_action).setVisible(false);
		options.findItem(R.id.save_person_action).setVisible(true);
	}

	private void flipToShowLayout() {
		flipper.setDisplayedChild(SHOW_LAYOUT);
		options.findItem(R.id.save_person_action).setVisible(false);
		options.findItem(R.id.edit_person_action).setVisible(true);
	}

	private void getFields() {

		//Get the text from the fields
		String strFirstName = editFirstName.getText().toString();
		String strMiddleName = editMiddleName.getText().toString();
		String strLastName1 = editLastName1.getText().toString();
		String strLastName2 = editLastName2.getText().toString();
		String strEmail = editEmail.getText().toString();
		String strPhone = editPhoneNumber.getText().toString();

		//TODO: validate input

		//Set the person instance fields
		person.setFirstName(strFirstName);
		person.setMiddleName(strMiddleName);
		person.setLastName1(strLastName1);
		person.setLastName2(strLastName2);
		person.setEmail(strEmail);
		person.setPhoneNumber(strPhone);
	}

	/**
	 * Set this fragment's corresponding Person object by setting it's views with the person data
	 * @param person the person to be associated with this details fragment
	 * @return the person that was set
	 */
	public Person setPerson(Person person) {
		this.person = person;
		if(viewCreated)
			setFields();
		return this.person;
	}
}
