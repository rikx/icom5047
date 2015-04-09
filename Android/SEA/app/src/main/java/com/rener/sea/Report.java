package com.rener.sea;

import android.util.Log;
import android.util.Pair;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Report implements Comparable<Report> {

	public static final String DATE_FORMAT = "dd/LLL/yy";
	public static final int NEW_REPORT_ID = -1;
	private long id;
	private String name;
	private User creator;
	private Location location;
	private Person subject;
	private Flowchart flowchart;
	private String note = "";
	private Date date;
	private String type = "";
	private Path path;

	public Report() {
		this.id = NEW_REPORT_ID;
		this.name = "";
		this.date = new Date();
		this.path = new Path();
	}

	public Report(long id, String name) {
		this.id = id;
		this.name = name;
		this.date = new Date();
		this.path = new Path();
	}

	public Report(long id, String name, Location location) {
		this.id = id;
		this.name = name;
		this.location = location;
		this.date = new Date();
		this.path = new Path();
	}

	public Report(long id, User creator, Location location, Person subject, Flowchart flowchart,
	              String note, Date date) {
		this.id = id;
		this.creator = creator;
		this.location = location;
		this.subject = subject;
		this.flowchart = flowchart;
		this.note = note;
		this.date = date;
		this.path = new  Path();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Person getSubject() {
		return subject;
	}

	public void setSubject(Person subject) {
		this.subject = subject;
	}

	public Flowchart getFlowchart() {
		return flowchart;
	}

	public void setFlowchart(Flowchart flowchart) {
		this.flowchart = flowchart;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDateString() {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return sdf.format(date);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void addToPath(Option option, String data) {
		Item item = findOptionParent(option);
		path.addEntry(item, option, data);
	}

	public void addToPath(Option option) {
		Item item = findOptionParent(option);
		path.addEntry(item, option);
	}

	public Path getPath() {
		return path;
	}

	private Item findOptionParent(Option option) {
		Item item = null;
		for(Item i : flowchart.getItems()) {
			for(Option o : i.getOptions()) {
				if(o.getId() == option.getId()) item = i;
			}
		}
		return item;
	}

	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		String s = name+"\n"+location.getName()+"\t"+sdf.format(date);
		//Log.i("REPORT", s);
		return s;
	}

	@Override
	public int compareTo(Report r) {
		int compare = this.date.compareTo(r.getDate());
		return compare;
	}
}
