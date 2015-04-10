package com.rener.sea;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A class representing a person.
 */
public class Person implements Comparable<Person> {

	public final static String PERSON_ID = "person_id";
	public final static String FIRST_NAME = "first_name";
	public final static String MIDDLE_NAME = "middle_name";
	public final static String LAST_NAME1 = "last_name1";
	public final static String LAST_NAME2 = "last_name2";
	public final static String EMAIL = "email";
	public final static String PHONE_NUMBER = "phone_number";
	private long id;
	private String first_name = "";
	private String middle_name = "";
	private String last_name1 = "";
	private String last_name2 = "";
	private String email = "";
	private String phone_number = "";

    public int getSpecializationID() {
        return specializationID;
    }

    public void setSpecializationID(int specializationID) {
        this.specializationID = specializationID;
    }

    private int specializationID;

	public Person(long id) {
		this.id = id;
	}

	public Person(long id, String first_name, String last_name) {
		this.id = id;
		this.first_name = first_name;
		this.last_name1 = last_name;
	}

	public Person(String first_name, String last_name) {
		this.first_name = first_name;
		this.last_name1 = last_name;
	}

	public Person(String name) {
		this.first_name = name;
	}

	public long getId() {
		return id;
	}

	public long setID(long id) {
		return this.id = id;
	}

	public String getFirstName() {
		return first_name;
	}

	public String setFirstName(String name) {
		return this.first_name = name;
	}

	public String getMiddleName() {
		return middle_name;
	}

	public String setMiddleName(String initial) {
		return this.middle_name = initial;
	}

	public boolean hasMiddleName() {
		return middle_name != null || !middle_name.equals("");
	}

	public String getLastName1() {
		return last_name1;
	}

	public String setLastName1(String name) {
		return this.last_name1 = name;
	}

	public String getLastName2() {
		return last_name2;
	}

	public String setLastName2(String name) {
		return this.last_name2 = name;
	}

	/**
	 * Get the full name of a person with order "first name" and "last name".
	 * @return a string with the person's full name
	 */
	public String getFullNameFirstLast() {
		if(hasMiddleName())
			return (first_name+" "+middle_name+" "+last_name1+" "+last_name2).trim();
		else
			return (first_name+" "+last_name1+" "+last_name2).trim();
	}

	public String getEmail() {
		return email;
	}

	public String setEmail(String email) {
		return this.email = email;
	}

	public String getPhoneNumber() {
		return phone_number;
	}

	public boolean hasEmail() {
		return email!=null || !email.equals("");
	}

	public String setPhoneNumber(String phone_number) {
		return this.phone_number = phone_number;
	}

	public boolean hasPhoneNumber() {
		return phone_number != null || !phone_number.equals("");
	}

	public String toString() {
		return getFullNameFirstLast();
	}

	@Override
	public int compareTo(Person p) {
		int compare = toString().compareTo(p.toString());
		return compare;
	}

	/**
	 * Determine whether the provided person is equivalent to this one by comparing their IDs
	 * @param other the other Person object
	 * @return true if their IDs match
	 */
	public boolean equals(Person other) {
		return this.id == other.getId();
	}
}
