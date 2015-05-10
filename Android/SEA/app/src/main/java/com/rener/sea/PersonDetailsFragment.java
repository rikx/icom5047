package com.rener.sea;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.ViewFlipper;

/**
 * An Android fragment class used to display data pertaining to a person.
 */
public class PersonDetailsFragment extends Fragment implements DetailsFragment, View.OnClickListener, Toolbar.OnMenuItemClickListener {

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
        LinearLayout emailLayout = (LinearLayout) view.findViewById(R.id.person_email_layout);
        emailLayout.setOnClickListener(this);
        textEmail = (TextView) view.findViewById(R.id.person_text_email);
        editEmail = (EditText) view.findViewById(R.id.person_edit_email);

        //Set the phone number views
        textPhoneNumber = (TextView) view.findViewById(R.id.person_text_phone);
        editPhoneNumber = (EditText) view.findViewById(R.id.person_edit_phone_number);

        if(person != null) {
            setDataViews();
            flipper.setDisplayedChild(SHOW_LAYOUT);
        }
        else {
            flipper.setDisplayedChild(EDIT_LAYOUT);
        }

        viewCreated = true;
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        options.clear();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Toolbar tb = ((MainActivity)getActivity()).getContextToolbar();
        options = tb.getMenu();
        options.clear();
        inflater.inflate(R.menu.person_actions, options);
        tb.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_person_action:
                flipToEditLayout();
                break;
            case R.id.save_person_action:
                if(person != null) {
                    savePerson();
                }
                else {
                    saveNewPerson();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onDetailsChanged() {
        int displayed = flipper.getDisplayedChild();
        if(viewCreated && displayed == SHOW_LAYOUT) {
            setDataViews();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.person_email_layout:
                sendEmail();
                break;
        }
    }

    private void setDataViews() {

        //Set the name fields
        textName.setText(person.getFullNameFirstLast());
        editFirstName.setText(person.getFirstName());
        editMiddleName.setText(person.getMiddleName());
        editLastName1.setText(person.getLastName1());
        editLastName2.setText(person.getLastName2());

        //Set the email fields if it exists
        if (person.hasEmail()) {
            String email = person.getEmail();
            textEmail.setText(email);
            editEmail.setText(email);
            textEmail.setVisibility(TextView.VISIBLE);
        } else {
            textEmail.setVisibility(TextView.GONE);
        }

        //Set the phone fields if it exists
        if (person.hasPhoneNumber()) {
            String phone = person.getPhoneNumber();
            textPhoneNumber.setText(phone);
            textPhoneNumber.setVisibility(TextView.VISIBLE);
            editPhoneNumber.setText(phone);
        } else {
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

    private boolean getFields() {

        //Get the text from the fields
        String strFirstName = editFirstName.getText().toString().trim();
        String strMiddleName = editMiddleName.getText().toString().trim();
        String strLastName1 = editLastName1.getText().toString().trim();
        String strLastName2 = editLastName2.getText().toString().trim();
        String strEmail = editEmail.getText().toString().trim();
        String strPhone = editPhoneNumber.getText().toString().trim();


        boolean allow = (!strFirstName.equals("") && !strLastName1.equals(""));
        if(!allow) {
            String message = "";
            if (strFirstName.equals("")) {
                editFirstName.setText("");
                message = getString(R.string.empty_first_name);
            }
            else if (strLastName1.equals("")) {
                editLastName1.setText("");
                message = getString(R.string.empty_last_name);
            }
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            return false;
        }

        //Set the person instance fields
        person.setFirstName(strFirstName);
        person.setMiddleName(strMiddleName);
        person.setLastName1(strLastName1);
        person.setLastName2(strLastName2);
        person.setEmail(strEmail);
        person.setPhoneNumber(strPhone);

        return true;
    }

    /**
     * Set this fragment's corresponding Person object by setting it's views with the person data
     *
     * @param person the person to be associated with this details fragment
     * @return the person that was set
     */
    public Person setPerson(Person person) {
        this.person = person;
        if (viewCreated)
            setDataViews();
        return this.person;
    }

    private void savePerson() {
        if(getFields()) {
            setDataViews();
            flipToShowLayout();
            //Notify the activity that data has changed
            ((MainActivity) getActivity()).onDataChanged();
        }
    }

    private void saveNewPerson() {
        //Get the text from the fields
        String strFirstName = editFirstName.getText().toString();
        String strMiddleName = editMiddleName.getText().toString();
        String strLastName1 = editLastName1.getText().toString();
        String strLastName2 = editLastName2.getText().toString();
        String strEmail = editEmail.getText().toString();
        String strPhone = editPhoneNumber.getText().toString();

        if(!strFirstName.equals("") && !strLastName1.equals("")) {
            //TODO: create the new person
        }
    }

    private void sendEmail() {
        String email = textEmail.getText().toString();
        if(!email.equals("")) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                String error = getString(R.string.no_email_clients_installed);
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getType() {
        return "PERSON";
    }
}
