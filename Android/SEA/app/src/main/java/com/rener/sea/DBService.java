package com.rener.sea;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
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

	public Person findPersonById(long id) {
		Person person = null;
		//Check if person is in memory
		for(Person p : people) {
			if(p.getID() == id) person = p;
		}
		if(person != null) return person;
		//TODO: Check if person is in local files
		//TODO: Query the server database
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

	public Location findLocationById(long id) {
		Location location = null;
		for(Location l : locations) {
			if(l.getId() == id) location = l;
		}
		return location;
	}

	public boolean writeJSONToFile(String filename, String json) {
		FileOutputStream outputStream;
		try {
			outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
			outputStream.write(json.getBytes());
			outputStream.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
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
		findPersonById(0).setEmail("nelson.reyes@upr.edu");
		findPersonById(0).setPhoneNumber("787-403-1082");

		locations = new ArrayList<>();
		locations.add(new Location(0, "Recinto Universitario de Mayagüez"));
		locations.add(new Location(1, "Finca Alzamorra"));
		locations.add(new Location(2, "Los Pollos Hermanos"));
		locations.add(new Location(3, "Betzabe's Office"));
		findLocationById(2).setOwner(findPersonById(4));
		findLocationById(2).setManager(findPersonById(5));
		findLocationById(3).setOwner(findPersonById(6));

		Log.i(this.toString(), "dummy data set");
	}

	public static String peopleToJSON(List<Person> people) {
		JSONObject object = new JSONObject();
		for(Person p : people) {
			String json = p.toJSON();
			String sid = String.valueOf(p.getID());
			try {
				object.put(sid, json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		Log.i("JSON-List<Person>", object.toString());
		return object.toString();
	}

	public static List<Person> peopleFromJSON(String json) {
		try {
			JSONObject object = new JSONObject(json);
			Iterator<String> iterator = object.keys();
			List list = new ArrayList<Person>();
			while(iterator.hasNext()) {
				Person p = new Person(iterator.next());
				list.add(p);
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}
