package com.rener.sea;

import android.location.Address;

public class Location {

	private long id;
	private String name = "";
	private Person manager = null;
	private Person owner = null;
	private Address address = null;

	public Location(String name) {
		this.name = name;
	}

	public Location(long id, String name) {
		this.id = id;
		this.name = name;
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
}
