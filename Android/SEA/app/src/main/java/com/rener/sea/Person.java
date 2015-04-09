package com.rener.sea;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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

	public Person(String json) {
		Person p = Person.fromJSON(json);
		this.id = p.getId();
		this.first_name = p.getFirstName();
		this.middle_name = p.getMiddleName();
		this.last_name1 = p.getLastName1();
		this.last_name2 = p.getLastName1();
		this.email = p.getEmail();
		this.phone_number = p.getPhoneNumber();
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

	public String toJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put(PERSON_ID, id);
			json.put(FIRST_NAME, first_name);
			json.put(MIDDLE_NAME, middle_name);
			json.put(LAST_NAME1, last_name1);
			json.put(LAST_NAME2, last_name2);
			json.put(EMAIL, email);
			json.put(PHONE_NUMBER, phone_number);
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
		Log.i("JSON-Person", json.toString());
		return json.toString();
	}

	public static String toJSON(Person person) {
		JSONObject json = new JSONObject();
		try {
			json.put(PERSON_ID, person.getId());
			json.put(FIRST_NAME, person.getFirstName());
			json.put(MIDDLE_NAME, person.getMiddleName());
			json.put(LAST_NAME1, person.getLastName1());
			json.put(LAST_NAME2, person.getLastName2());
			json.put(EMAIL, person.getEmail());
			json.put(PHONE_NUMBER, person.getPhoneNumber());
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
		Log.i("JSON-Person", json.toString());
		return json.toString();
	}

	public static Person fromJSON(String json) {
		try {
			JSONObject p = new JSONObject(json);
			String strID = p.getString(PERSON_ID);
			long id = Long.getLong(strID);
			Person person = new Person(id);
			person.setFirstName(p.getString(FIRST_NAME));
			person.setMiddleName(p.getString(MIDDLE_NAME));
			person.setLastName1(p.getString(LAST_NAME2));
			person.setLastName2(p.getString(LAST_NAME2));
			person.setEmail(p.getString(EMAIL));
			person.setPhoneNumber(p.getString(PHONE_NUMBER));
			return person;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int compareTo(Person p) {
		int compare = toString().compareTo(p.toString());
		return compare;
	}

	public boolean equals(Person p) {
		return this.id == p.getId();
	}
}
