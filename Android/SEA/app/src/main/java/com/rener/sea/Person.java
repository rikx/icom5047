package com.rener.sea;

import org.json.JSONException;
import org.json.JSONObject;

public class Person {

	private long id;
	private String first_name = "";
	private String middle_name = "";
	private String last_name1 = "";
	private String last_name2 = "";
	private String email = "";
	private String phone_number = "";


	public Person(long id, String first_name, String last_name) {
		this.id = id;
		this.first_name = first_name;
		this.last_name1 = last_name;
	}

	public Person(String first_name, String last_name) {
		this.first_name = first_name;
		this.last_name1 = last_name;
	}

	public long getID() {
		return id;
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
			json.put("person_id", id);
			json.put("first_name", first_name);
			json.put("middle_name", middle_name);
			json.put("last_name1", last_name1);
			json.put("last_name2", last_name2);
			json.put("email", email);
			json.put("phone_number", phone_number);
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
		return json.toString();
	}
}
