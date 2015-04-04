package com.rener.sea;


import android.app.Activity;
import android.app.Fragment;
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
import java.util.List;

public class MenuListFragment extends ListFragment {

	private int curPos = -1;
	private Fragment selectedFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container
			,Bundle savedInstanceState) {
		return inflater.inflate(R.layout.menu_list_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
				android.R.layout.simple_list_item_1,
				getResources().getStringArray(R.array.menu_list_strings));
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		String selection = getListAdapter().getItem(position).toString();

		//Create new fragment to replace existing
		if(selection.equalsIgnoreCase("REPORTS"))
			selectedFragment = new ReportsListFragment();
		else if(selection.equalsIgnoreCase("PEOPLE"))
			selectedFragment = PeopleListFragment.newInstance(-1);
		else if(selection.equalsIgnoreCase("LOCATIONS"))
			selectedFragment = new LocationsListFragment();
		else selectedFragment = new ListFragment();

		//Replace the selected fragment
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.menu_selected_container, selectedFragment, selection);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		transaction.commit();

	}
}
