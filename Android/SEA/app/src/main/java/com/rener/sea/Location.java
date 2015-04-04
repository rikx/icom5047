package com.rener.sea;

import android.location.Address;

import java.util.Locale;

public class Location {

	private long id;
	private String name = "";
	private Person manager = null;
	private Person owner = null;
	private Address address = null;
	public static String PUERTO_RICO = "Puerto Rico";

	public Location(String name) {
		this.name = name;
	}

	public Location(long id, String name) {
		this.id = id;
		this.name = name;
		this.address = newAddress();
	}

	public long getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Person getManager() {
		return manager;
	}

	public void setManager(Person manager) {
		this.manager = manager;
	}

	public boolean hasManager() {
		return manager != null;
	}

	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}

	public boolean hasOwner() {
		return owner != null;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String toString() {
		return name;
	}

	public String getAddressLine(int n) {
		return address.getAddressLine(n-1);
	}

	public String setAddressLine(int n, String line) {
		if (line == null || line.equals(""))
			address.setAddressLine(n-1, null);
		else
			address.setAddressLine(n, line);
		return address.getAddressLine(n-1);
	}

	public String getCity() {
		return address.getLocality();
	}

	public boolean isMultiLineAddress() {
		return address.getMaxAddressLineIndex() > 0;
	}

	private static Address newAddress() {
		Address a = new Address(Locale.US);
		a.setAddressLine(0, "");
		a.setLocality("");
		a.setCountryName(PUERTO_RICO);
		a.setPostalCode("");
		return a;
	}
}
