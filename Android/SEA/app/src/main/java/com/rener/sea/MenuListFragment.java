package com.rener.sea;


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

	private int currentPosition = 0;
	private List menuItems;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		//Populate menu list (currently a dummy list)
		ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
				android.R.layout.simple_list_item_1,
				getResources().getStringArray(R.array.menu_list_strings));
		setListAdapter(adapter);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container
			,Bundle savedInstanceState) {
		return inflater.inflate(R.layout.menu_list_fragment, container, false);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		String selection = getListAdapter().getItem(position).toString();
		Context context = getActivity().getApplicationContext();

		//Highlight selected item
		l.setItemChecked(position, true);
		l.setSelection(position);
		l.setSelected(true);

		//Toast for selection feedback
		String user = ((MainActivity) getActivity()).getCurrentUser();
		Toast.makeText(context, user+" selected "+selection, Toast.LENGTH_SHORT).show();

		//Create new fragment to replace existing
		Bundle args = new Bundle();
		Fragment fragment = null;
		if(selection.equalsIgnoreCase("REPORTS"))
			fragment = new ReportsListFragment();
		else if(selection.equalsIgnoreCase("PEOPLE"))
			fragment = PeopleListFragment.newInstance(-1);
		else if(selection.equalsIgnoreCase("LOCATIONS"))
			fragment = new LocationsListFragment();
		else fragment = new Fragment();

		//Replace the selected fragment
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.menu_selected_container, fragment, selection);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		transaction.commit();

	}
}
