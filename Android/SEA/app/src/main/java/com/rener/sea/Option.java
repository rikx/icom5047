package com.rener.sea;

public class Option {

	private long id;
	private Item parent;
	private Item next;
	private String label;

	public Option(long id, Item parent, Item next, String label) {
		this.id = id;
		this.parent = parent;
		this.next = next;
		this.label = label;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Item getParent() {
		return parent;
	}

	public void setParent(Item parent) {
		this.parent = parent;
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
