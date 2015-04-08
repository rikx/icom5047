package com.rener.sea;

public class Item {

	public static final String RECOMMENDATION = "RECOM";
	public static final String END = "END";
	public static final String BOOLEAN = "BOOLEAN";
	public static final String MULTIPLE_CHOICE = "MULTI";
	public static final String OPEN = "OPEN";
	public static final String CONDITIONAL = "CONDITIONAL";
	private long id;
	private String label;
	private String type;

	public Item(long id, String label, String type) {
		this.id = id;
		this.label = label;
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
