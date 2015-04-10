package com.rener.sea;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a flowchart within the system
 */
public class Flowchart implements Comparable<Flowchart> {

	private long id;
	private Item first, end;
	private String name;
	private User creator;
	private String version;
	private List<Item> items;

	/**
	 * Constructs a new Flowchart object set to the given ID and name
	 * @param id a unique ID
	 * @param name a name
	 */
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

	public void setId(long id) {
		this.id = id;
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

	/**
	 * Add a Item object to the list of items for this flowchart.
	 * @param item the Item object to be added
	 */
	public void addItem(Item item) {
		items.add(item);
	}

	public String toString() {
		return name;
	}

	@Override
	public int compareTo(Flowchart other) {
		int compare = toString().compareTo(other.toString());
		return compare;
	}
}
