package com.rener.sea;

import android.location.Address;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class Location implements Comparable<Location> {

	public static final String PUERTO_RICO = "Puerto Rico";
	private long id;
	private String name = "";
	private Person manager = null;
	private Person owner = null;
	private Person agent = null;
	private Address address = null;
	private long address_id;
	private String license = "";

	public Location(String name) {
		this.name = name;
		this.address = Location.newAddress();
	}

	public Location(long id, String name) {
		this.id = id;
		this.name = name;
		this.address = newAddress();
	}

	public long getId() {
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

	public Person setManager(Person manager) {
		return this.manager = manager;
	}

	public boolean hasManager() {
		return manager != null;
	}

	public Person getOwner() {
		return owner;
	}

	public Person setOwner(Person owner) {
		return this.owner = owner;
	}

	public boolean hasOwner() {
		return owner != null;
	}

	public Person getAgent() {
		return agent;
	}

	public Person setAgent(Person agent) {
		return this.agent = agent;
	}

	public boolean hasAgent() {
		return agent != null;
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
			address.setAddressLine(n-1, line);
		return address.getAddressLine(n-1);
	}

	public String getCity() {
		return address.getLocality();
	}

	public String setCity(String city) {
		address.setLocality(city);
		return address.getLocality();
	}

	public String getZipCode() {
		return address.getPostalCode();
	}

	public String setZipCode(String code) {
		address.setPostalCode(code);
		return address.getPostalCode();
	}

	public long getAddress_id() {
		return address_id;
	}

	public long setAddressId(long id) {
		return this.address_id = id;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	private static Address newAddress() {
		Address a = new Address(Locale.US);
		a.setAddressLine(0, "");
		a.setLocality("");
		a.setCountryName(PUERTO_RICO);
		a.setPostalCode("");
		return a;
	}

	/**
	 * TODO: finish this
	 * @return
	 */
	public String toJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("location_id", id);
			json.put("name", name);
			json.put("manager_id", manager.getId());
			json.put("owner_id", owner.getId());
			json.put("agent_id", agent.getId());
			json.put("address_line1", this.getAddressLine(1));
			json.put("address_line2", this.getAddressLine(2));
			json.put("city", this.getCity());
			json.put("zip_code", this.getZipCode());
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
		return json.toString();
	}

	@Override
	public int compareTo(Location l) {
		int compare = toString().compareTo(l.toString());
		return compare;
	}

	public boolean equals(Location l) {
		return this.id == l.getId();
	}
}
