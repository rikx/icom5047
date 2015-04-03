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

public class LocationsListFragment extends ListFragment {

	private List<Location> locationsList;
	private int curPos = -1;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		locationsList = new ArrayList<Location>();
		populateList();

		//Set the list adapter
		ArrayAdapter<Location> adapter = new ArrayAdapter<Location>(getActivity(),
				android.R.layout.simple_list_item_1, locationsList);
		setListAdapter(adapter);

	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container
            , Bundle savedInstanceState) {
		return inflater.inflate(R.layout.locations_list_fragment, container, false);
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
		//Define people list with dummy data
		locationsList = ((MainActivity)getActivity()).getDB().getLocations();
	}
}
