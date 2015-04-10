package com.rener.sea;

import java.util.ArrayList;
import java.util.List;

public class Flowchart {

	private long id;
	private Item first, end;
	private String name;
	private User creator;
	private String version;
	private List<Item> items;

	public Flowchart(long id, String name) {
		this.id = id;
		this.name = name;
		items = new ArrayList<>();
	}

	public Flowchart(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long flowchart_id) {
		this.id = flowchart_id;
	}

	public Item getFirst() {
		return first;
	}

	public void setFirst(Item first) {
		this.first = first;
	}

	public Item getEnd() {
		return end;
	}

	public void setEnd(Item end) {
		this.end = end;
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public void addItem(Item item) {
		items.add(item);
	}

	public String toString() {
		return name;
	}
}
