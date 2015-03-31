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

import java.util.List;

public class LocationsListFragment extends ListFragment {

	private List locationsList;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		//Populate the list with dummy reports
		ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
				android.R.layout.simple_list_item_1,
				getResources().getStringArray(R.array.dummy_locations_list_strings));
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
}
