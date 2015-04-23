package com.rener.sea;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Represents a Report in the system
 * A report is a survey that has been answered
 */
public class Report implements Comparable<Report> {

	public static final int NEW_REPORT_ID = -1;
	private long id;
	private String name;
	private User creator;
	private Location location;
	private Person subject;
	private Flowchart flowchart;
	private String notes = "";
	private Date date;
	private String type = "";
	private Path path;

	/**
	 * Constructs a new Report with no ID and some default value
	 * Used to represent a Report that has been created but hasn't been assigned a unique ID
	 */
	public Report() {
		this.id = NEW_REPORT_ID;
		this.name = "";
		this.date = new Date();
		this.path = new Path();
	}

	/**
	 * Constructs a new Report object with a given ID and name
	 * @param id
	 * @param name
	 */
	public Report(long id, String name) {
		this.id = id;
		this.name = name;
		this.date = new Date();
		this.path = new Path();
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

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDateString(String format, Locale locale) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
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

	public void setPath(Path path) {
		this.path = path;
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
		return super.toString();
	}

	@Override
	public int compareTo(@NonNull Report r) {
		return this.date.compareTo(r.getDate());
	}
}
