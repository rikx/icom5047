package com.rener.sea;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PersonDetailsFragment extends Fragment {

	private long person_id;
	private Person person;

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

		//Display the person's name
		String name = person.getFullNameFirstLast();
		TextView nameText = (TextView) view.findViewById(R.id.text_person_name);
		nameText.setText(name);

		//Display the person's email
		String email = person.getEmail();
		if(email != null) {
			TextView emailText = (TextView) view.findViewById(R.id.text_person_email);
			emailText.setText(email);
			emailText.setVisibility(TextView.VISIBLE);
		}

		String phone = Integer.toString(person.getPhone_number());
		if(phone != null) {
			TextView phoneText = (TextView) view.findViewById(R.id.text_person_phone);
			phoneText.setText(phone);
			phoneText.setVisibility(TextView.VISIBLE);
		}

		return view;

	}

	public void editDetails(View view) {

	}

	public void saveDetails(View view) {

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
