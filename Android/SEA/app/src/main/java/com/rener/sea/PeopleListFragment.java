package com.rener.sea;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PeopleListFragment extends ListFragment {

	private List<Person> peopleList = null;
	private int curPos = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Populate the list with dummy reports
		peopleList = new ArrayList<Person>();
		populateList();

		//Set the adapter for the list fragment
		ArrayAdapter<Person> adapter = new ArrayAdapter<Person>(getActivity(),
				android.R.layout.simple_list_item_1, peopleList);
		setListAdapter(adapter);

		//Set the current position in the list
		curPos = getArguments().getInt("index", -1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container
			, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.people_list_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getListView().setItemChecked(curPos, true);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("index", curPos);
	}

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

	    //Highlight selected item
	    l.setItemChecked(position, true);

	    //Show the item's details
	    showDetails(position);
    }

	private void populateList() {

		//Set the list with data from DB
		peopleList = ((MainActivity)getActivity()).getDB().getPeople();
	}

	public List getPeopleList() {
		return peopleList;
	}

	private void showDetails(int index) {
		//Set the context
		Context context = getActivity().getApplicationContext();

		//Set the selected person
		Person person = (Person) getListAdapter().getItem(index);
		getListView().setItemChecked(index, true);


		//Check if a details fragment is shown, replace if needed or show a new one
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		PersonDetailsFragment details = (PersonDetailsFragment)getFragmentManager()
				.findFragmentByTag("DETAILS");
		if (details == null) {
			//Create a new details fragment
			details = PersonDetailsFragment.newInstance(person);
			PeopleListFragment list = PeopleListFragment.newInstance(index);
			transaction.replace(R.id.menu_list_container, list, "PEOPLE");
			transaction.replace(R.id.menu_selected_container, details, "DETAILS");
		}
		else if (curPos != index){
			details = PersonDetailsFragment.newInstance(person);
			transaction.replace(R.id.menu_selected_container, details);
		}
		curPos = index;
		transaction.commit();
	}

	public static PeopleListFragment newInstance(int index) {
		PeopleListFragment fragment = new PeopleListFragment();
		Bundle args = new Bundle();
		args.putInt("index", index);
		fragment.setArguments(args);
		return fragment;
	}

	public static PeopleListFragment newInstance() {
		PeopleListFragment fragment = new PeopleListFragment();
		Bundle args = new Bundle();
		args.putInt("index", -1);
		fragment.setArguments(args);
		return fragment;
	}
}
