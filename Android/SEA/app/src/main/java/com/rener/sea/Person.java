package com.rener.sea;

public class Person {

	private long id;
	private String first_name = "";
	private String middle_name = "";
	private String last_name1 = "";
	private String last_name2 = "";
	private String email = "";
	public static final int DEFAULT_PHONE = 0;
	private int phone_number = DEFAULT_PHONE;


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

	public int getPhoneNumber() {
		return phone_number;
	}

	public boolean hasEmail() {
		return email!=null || !email.equals("");
	}

	public int setPhoneNumber(int phone_number) {
		return this.phone_number = phone_number;
	}

	public int setPhoneNumber(String phone_number) {
		try {
			return this.phone_number = Integer.parseInt(phone_number);
		} catch (NumberFormatException e) {
			return this.phone_number = DEFAULT_PHONE;
		}
	}

	public boolean hasPhoneNumber() {
		return phone_number != DEFAULT_PHONE;
	}

	public String toString() {
		return getFullNameFirstLast();
	}
}
