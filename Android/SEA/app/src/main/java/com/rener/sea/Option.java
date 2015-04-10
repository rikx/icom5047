package com.rener.sea;

/**
 * A class representing an "edge" in a flowchart
 */
public class Option {

	private long id;
	private Item next;
	private String label;

	/**
	 * Construct a new Option object with the given ID, next Item in the flow, and a text label.
	 * @param id a unique ID
	 * @param next the next Item in the flow
	 * @param label some text
	 */
	public Option(long id, Item next, String label) {
		this.id = id;
		this.next = next;
		this.label = label;
	}

	/**
	 * Construct a new Option object with the given ID and next Item in the flow.
	 * @param id a unique ID
	 * @param next the next Item object in thw flow
	 */
	public Option(long id, Item next) {
		this.id = id;
		this.next = next;
		this.label = "";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Item getNext() {
		return next;
	}

	public void setNext(Item next) {
		this.next = next;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Check whether the two Option objects are the equivalent by comparing their IDs
	 * @param other the other Option object
	 * @return true if their IDs match
	 */
	public boolean equals(Option other) {
		return this.id == other.getId();
	}
}
