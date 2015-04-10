package com.rener.sea;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single "node" for a flowchart
 */
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
    private List<Option> options;

	public Item(long id, String label, String type) {
		this.id = id;
		this.label = label;
		this.type = type;
		options = new ArrayList<>();
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

	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}

	/**
	 * Add an option to this Item's option list
	 * @param option the Option to be added
	 */
	public void addOption(Option option) {
		options.add(option);
	}
}
