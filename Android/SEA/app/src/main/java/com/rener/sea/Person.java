package com.rener.sea;

public class Person {

	private long id;
	private String first_name = "";
	private String middle_initial = null;
	private String last_name = "";
	private String email = null;
	private int phone_number;

	public Person(long id, String first_name, String last_name) {
		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
	}

	public Person(String first_name, String last_name) {
		this.first_name = first_name;
		this.last_name = last_name;
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

	public String getMiddleInitial() {
		return middle_initial;
	}

	public String setMiddleInitial(String initial) {
		return this.middle_initial = initial;
	}

	public String getLastName() {
		return last_name;
	}

	public String setLastName(String name) {
		return this.last_name = name;
	}

	public String getFullNameFirstLast() {
		if(middle_initial != null)
			return (first_name+" "+middle_initial+". "+last_name).trim();
		else
			return (first_name+" "+last_name).trim();
	}

	public String getEmail() {
		return email;
	}

	public String setEmail(String email) {
		return this.email = email;
	}

	public int getPhone_number() {
		return phone_number;
	}

	public boolean hasEmail() {
		return email==null || email.equals("");
	}

	public int setPhone_number(int phone_number) {
		return this.phone_number = phone_number;
	}

	public String toString() {
		return getFullNameFirstLast();
	}
}
