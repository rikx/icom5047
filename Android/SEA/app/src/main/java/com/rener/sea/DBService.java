package com.rener.sea;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DBService extends Service {

	private final IBinder mBinder = new DBBinder();
	private List<Person> people;
	private List<Location> locations;
	private List<Report> reports;
	private List<User> users;
	private List<Flowchart> flowcharts;
	private List<Item> items;
	private List<Option> options;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("DBService", "started");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		setDummyData();
		jsonTest();
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

	/**
	 * Authenticates user credentials
	 * TODO: hashing
	 * @param username the username
	 * @param password the password
	 * @return true if credentials are OK
	 */
	public boolean authLogin(String username, String password) {
		boolean ok = false;
		for(User u : users) {
			if(u.getUsername().equals(username)) {
				if(u.getPassword().matches(password)) ok = true;
			}
		}
		return ok;
	}

	public List<Person> getPeople() {
		return people;
	}

	public Person findPersonById(long id) {
		Person person = null;
		//Check if person is in memory
		for(Person p : people) {
			if(p.getId() == id) person = p;
		}
		if(person != null) return person;
		//TODO: Check if person is in local files
		//TODO: Query the server database
		return person;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public Location findLocationById(long id) {
		Location location = null;
		for(Location l : locations) {
			if(l.getId() == id) location = l;
		}
		return location;
	}

	public List<Report> getReports() {
		return reports;
	}

	public Report findReportById(long id) {
		Report report = null;
		for (Report r : reports) {
			if(r.getId() == id) report = r;
		}
		return report;
	}

	public List<Flowchart> getFlowcharts() {
		return flowcharts;
	}

	public Flowchart findFlowchartById(long id) {
		Flowchart flowchart = null;
		for (Flowchart f : flowcharts) {
			if(f.getId() == id) flowchart = f;
		}
		return flowchart;
	}

	public List<Item> getItems() {
		return items;
	}

	public Item findItemById(long id) {
		Item item = null;
		for (Item i : items) {
			if(i.getId() == id) item = i;
		}
		return item;
	}

	public List<Option> getOptions() {
		return options;
	}

	public Option findOptionById(long id) {
		Option option = null;
		for(Option o : options) {
			if(o.getId() == id) option = o;
		}
		return option;
	}

	public List<User> getUsers() {
		return users;
	}

	public User findUserById(long id) {
		User user = null;
		for(User u : users) {
			if(u.getId() == id) user = u;
		}
		return user;
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
		people.add(new Person(6, "Generic", "Person"));
		people.add(new Person(7, "Generic", "Agent"));
		findPersonById(0).setEmail("nelson.reyes@upr.edu");
		findPersonById(0).setPhoneNumber("787-403-1082");
		findPersonById(6).setEmail("generic.person@upr.edu");
		findPersonById(6).setPhoneNumber("555-555-5555");
		findPersonById(7).setEmail("generic.agent@upr.edu");
		findPersonById(7).setPhoneNumber("555-555-5555");

		locations = new ArrayList<>();
		locations.add(new Location(0, "Recinto Universitario de Mayagüez"));
		locations.add(new Location(1, "Finca Alzamorra"));
		locations.add(new Location(2, "Los Pollos Hermanos"));
		locations.add(new Location(3, "Betzabe's Office"));
		locations.add(new Location(4, "Generic Location"));
		findLocationById(2).setOwner(findPersonById(4));
		findLocationById(2).setManager(findPersonById(5));
		findLocationById(3).setOwner(findPersonById(6));

		users = new ArrayList<>();
		users.add(new User(0, "", "", findPersonById(6)));
		users.add(new User(1, "nelson.reyes", "iamnelson", findPersonById(0)));
		users.add(new User(2, "enrique.rodriguez2", "iamenrique", findPersonById(1)));
		users.add(new User(3, "ricardo.fuentes", "iamricardo", findPersonById(2)));
		users.add(new User(4, "ramon.saldana", "iamramon", findPersonById(3)));
		users.add(new User(5, "betzabe.rodriguez", "iambetzabe", findPersonById(6)));

		items = new ArrayList<>();
		items.add(new Item(1, "Is the cow sick?", Item.BOOLEAN));
		items.add(new Item(2, "How would you categorize this problem?", Item.MULTIPLE_CHOICE));
		items.add(new Item(3, "Record a description of the milk coloring, texture and smell",
				Item.OPEN));
		items.add(new Item(4, "Input amount of times cow eats a day", Item.CONDITIONAL));
		items.add(new Item(5, "Recommendation 1", Item.RECOMMENDATION));
		items.add(new Item(6, "Recommendation 2", Item.RECOMMENDATION));
		items.add(new Item(7, "Recommendation 3", Item.RECOMMENDATION));
		items.add(new Item(8, "Recommendation 4", Item.RECOMMENDATION));
		items.add(new Item(9, "Recommendation 5", Item.RECOMMENDATION));
		items.add(new Item(10, "End of flowchart test", Item.END));

		options = new ArrayList<>();
		options.add(new Option(1, findItemById(2),"Yes"));
		options.add(new Option(2, findItemById(5),"No"));
		findItemById(1).addOption(findOptionById(1));
		findItemById(1).addOption(findOptionById(2));
		options.add(new Option(3, findItemById(3),"Milk is discolored"));
		options.add(new Option(4, findItemById(6),"Injured leg"));
		options.add(new Option(5, findItemById(4),"Eating problems"));
		findItemById(2).addOption(findOptionById(3));
		findItemById(2).addOption(findOptionById(4));
		findItemById(2).addOption(findOptionById(5));
		options.add(new Option(6, findItemById(8),"USER INPUT"));
		options.add(new Option(7, findItemById(9),"USER INPUT"));
		findItemById(4).addOption(findOptionById(6));
		findItemById(4).addOption(findOptionById(7));
		options.add(new Option(8, findItemById(7), "USER INPUT"));
		findItemById(3).addOption(findOptionById(8));

		flowcharts = new ArrayList<>();
		Flowchart fc1 = new Flowchart(1, "Test Flowchart");
		flowcharts.add(fc1);
		for(Item i : items) fc1.addItem(i);

		reports = new ArrayList<>();
		reports.add(new Report(0,"My Report", findLocationById(4)));
		findReportById(0).setFlowchart(findFlowchartById(1));
		findReportById(0).setCreator(findUserById(0));

		Log.i(this.toString(), "dummy data set");
	}

	public static String peopleToJSON(List<Person> people) {
		JSONObject object = new JSONObject();
		for(Person p : people) {
			String json = p.toJSON();
			String sid = String.valueOf(p.getId());
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

	private void jsonTest() {

	}

}
