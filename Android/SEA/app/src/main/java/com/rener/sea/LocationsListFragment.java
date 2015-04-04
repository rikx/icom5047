package com.rener.sea;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
	    l.setItemChecked(position, true);
		showDetails(position);
    }

	private void populateList() {
		//Define people list with dummy data
		locationsList = ((MainActivity)getActivity()).getDB().getLocations();
	}

	private void showDetails(int index) {

		//Set the selected object
		Location location = (Location) getListAdapter().getItem(index);
		getListView().setItemChecked(index, true);


		//Check if a details fragment is shown, replace if needed or show a new one
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		LocationDetailsFragment details = (LocationDetailsFragment)getFragmentManager()
				.findFragmentByTag("DETAILS");
		if (details == null) {
			//Create a new details fragment
			details = LocationDetailsFragment.newInstance(location);
			LocationsListFragment list = LocationsListFragment.newInstance(index);
			transaction.replace(R.id.menu_list_container, list, "LOCATIONS");
			transaction.replace(R.id.menu_selected_container, details, "DETAILS");
		}
		else if (curPos != index){
			details = LocationDetailsFragment.newInstance(location);
			transaction.replace(R.id.menu_selected_container, details);
		}
		curPos = index;
		transaction.commit();
	}

	public static LocationsListFragment newInstance(int index) {
		LocationsListFragment fragment = new LocationsListFragment();
		Bundle args = new Bundle();
		args.putInt("index", index);
		fragment.setArguments(args);
		return fragment;
	}

	public static LocationsListFragment newInstance() {
		LocationsListFragment fragment = new LocationsListFragment();
		Bundle args = new Bundle();
		args.putInt("index", -1);
		fragment.setArguments(args);
		return fragment;
	}
}
