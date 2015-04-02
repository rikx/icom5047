package com.rener.sea;

import java.util.ArrayList;
import java.util.List;

public class DBEmulator {

	public List<Person> people;
	public List<Location> locations;
	public List<Report> reports;

	public DBEmulator() {
		people = new ArrayList<>();
		people.add(new Person(0, "Nelson", "Reyes Ciena"));
		people.add(new Person(1, "Enrique", "Rodriguez"));
		people.add(new Person(2, "Ricardo", "Fuentes"));
		people.add(new Person(3, "Ramón", "Saldaña"));
		findPersonByID(0).setEmail("nelson.reyes@upr.edu");
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

}
