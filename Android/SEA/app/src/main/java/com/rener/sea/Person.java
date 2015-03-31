package com.rener.sea;

public class Person {

	public long id;
	private String first_name = "";
	private String middle_initial = null;
	private String last_name = "";
	private String email = null;

	public Person(String first_name, String last_name) {
		this.first_name = first_name;
		this.last_name = last_name;
	}

	public Person(String first_name, String middle_initial, String last_name) {
		this.first_name = first_name;
		this.middle_initial = middle_initial;
		this.last_name = last_name;
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

	public String toString() {
		return getFullNameFirstLast();
	}
}
