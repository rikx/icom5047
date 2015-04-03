package com.rener.sea;

import java.util.ArrayList;
import java.util.List;

public class DBEmulator {

	public List<Person> people;
	public List<Location> locations;
	public List<Report> reports;

	public DBEmulator() {
		people = new ArrayList<>();
		people.add(new Person(0, "Nelson", "Reyes"));
		people.add(new Person(1, "Enrique", "Rodriguez"));
		people.add(new Person(2, "Ricardo", "Fuentes"));
		people.add(new Person(3, "Ramón", "Saldaña"));
		people.add(new Person(4, "Gustavo", "Fring"));
		findPersonByID(0).setEmail("nelson.reyes@upr.edu");

		locations = new ArrayList<>();
		locations.add(new Location(0, "Recinto Universitario de Mayagüez"));
		locations.add(new Location(1, "Finca Alzamorra"));
		locations.add(new Location(2, "Los Pollos Hermanos"));
		findLocationByID(2).setOwner(findPersonByID(4));
	}

	public List<Person> getPeople() {
		return people;
	}

	public Person findPersonByID(long id) {
		Person person = null;
		for(Person p : people) {
			if(p.getID() == id) person = p;
		}
		return person;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public Location findLocationByID(long id) {
		Location location = null;
		for(Location l : locations) {
			if(l.getID() == id) location = l;
		}
		return location;
	}

	public void commit() {

	}

}
