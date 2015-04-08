package com.rener.sea;

import android.util.Log;
import android.util.Pair;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Report {

	private long id;
	private User creator;
	private Location location;
	private Person subject;
	private Flowchart flowchart;
	private String note = "";
	private Date date;
	private List<Pair<Option,String>> path;

	public Report(long id) {
		this.id = id;
		this.date = new Date();
		this.path = new ArrayList<>();
	}

	public Report(long id, Location location) {
		this.id = id;
		this.location = location;
		this.date = new Date();
		this.path = new ArrayList<>();
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
		this.path = new ArrayList<>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public void addOptionToPath(Option option, String data) {
		Pair<Option,String> pair = new Pair<Option,String>(option, data);
		path.add(pair);
	}

	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/LLL/yy");
		String s = location.getName()+"\t"+sdf.format(date);
		//Log.i("REPORT", s);
		return s;
	}
}
