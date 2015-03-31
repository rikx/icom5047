package com.rener.sea;

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

	private List peopleList = null;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		//Populate the list with dummy reports
		peopleList = new ArrayList<Person>();
		populateList();

		//Set the adapter for the list fragment
		ArrayAdapter<Person> adapter = new ArrayAdapter<Person>(getActivity(),
				android.R.layout.simple_list_item_1, peopleList);
		setListAdapter(adapter);

	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container
            , Bundle savedInstanceState) {
		return inflater.inflate(R.layout.people_list_fragment, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String selection = getListAdapter().getItem(position).toString();
        Context context = getActivity().getApplicationContext();

		//Show a toast for feedback
        Toast.makeText(context, selection + " selected!", Toast.LENGTH_SHORT).show();

		//TODO Do something with the selection
    }

	private void populateList() {

		//Dummy data
		peopleList.add(new Person("Nelson", "E", "Reyes Ciena"));
		peopleList.add(new Person("Enrique", "Rodriguez"));
		peopleList.add(new Person("Ricardo", "Fuentes"));
		peopleList.add(new Person("Ramón", "Saldaña"));
	}
}
