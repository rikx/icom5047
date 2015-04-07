package com.rener.sea;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBService extends Service {

	public final static String PEOPLE = "people";
	public final static String LOCATIONS = "locations";
	public final static String REPORTS = "reports";
	private final IBinder mBinder = new DBBinder();
	private List<Person> people;
	private List<Location> locations;
	private List<Report> reports;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("DBService", "started");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		setDummyData();
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(this.toString(), "bound to " + intent.toString());
		return mBinder;
	}

	@Override
	public void onDestroy() {
		Log.i(this.toString(), "destroyed");
		super.onDestroy();
	}

	/**
	 * Class used for the client binder
	 */
	public class DBBinder extends Binder {
		DBService getService() {
			return DBService.this;
		}
	}

	public boolean checkLogin(String username, String password) {
		//TODO Authenticate login credentials
		return true;
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

	public boolean addPerson(Person person) {
		return people.add(person);
	}

	public List<Location> getLocations() {
		return locations;
	}

	public boolean addLocation(Location location) {
		return locations.add(location);
	}

	public Location findLocationByID(long id) {
		Location location = null;
		for(Location l : locations) {
			if(l.getID() == id) location = l;
		}
		return location;
	}

	public void writeDataToFile() {
		String filename = "people";
		String string = "";
		FileOutputStream outputStream;
		try {
			outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
			outputStream.write(string.getBytes());
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setDummyData() {
		people = new ArrayList<>();
		people.add(new Person(0, "Nelson", "Reyes"));
		people.add(new Person(1, "Enrique", "Rodriguez"));
		people.add(new Person(2, "Ricardo", "Fuentes"));
		people.add(new Person(3, "Ramón", "Saldaña"));
		people.add(new Person(6, "Betzabe", "Rodriguez"));
		people.add(new Person(4, "Gustavo", "Fring"));
		people.add(new Person(5, "Dennis", "Markowski"));
		findPersonByID(0).setEmail("nelson.reyes@upr.edu");
		findPersonByID(0).setPhoneNumber("787-403-1082");

		locations = new ArrayList<>();
		locations.add(new Location(0, "Recinto Universitario de Mayagüez"));
		locations.add(new Location(1, "Finca Alzamorra"));
		locations.add(new Location(2, "Los Pollos Hermanos"));
		locations.add(new Location(3, "Betzabe's Office"));
		findLocationByID(2).setOwner(findPersonByID(4));
		findLocationByID(2).setManager(findPersonByID(5));
		findLocationByID(3).setOwner(findPersonByID(6));

		Log.i(this.toString(), "dummy data set");
	}

	public static String peopleToJSON(List<Person> people) {
		JSONArray array = new JSONArray();
		for(Person p : people) {
			String s = p.toJSON();
			array.put(s);
		}
		Log.i("JSON-List<Person>", array.toString());
		return array.toString();
	}

	public static List<Person> peopleFromJSON(String json) {
		try {
			JSONArray array = new JSONArray(json);
			List list = new ArrayList<Person>(array.length());
			for(int i=0; i<array.length(); i++) {
				String s = array.getString(i);
				list.add(new Person(s));
				return list;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

}
