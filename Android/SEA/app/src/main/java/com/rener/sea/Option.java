package com.rener.sea;

public class Option {

	private long id;
	private Item next;
	private String label;

	public Option(long id, Item next, String label) {
		this.id = id;
		this.next = next;
		this.label = label;
	}

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
}
