package com.rener.sea;

import java.util.Iterator;
import java.util.Stack;

/**
 * A class representing an "answered survey" by making a path of options (edges) through a
 * flowchart. It's underlying data structure is that of a stack.
 */
public class Path implements Iterable<Path.PathEntry> {

	private Stack<PathEntry> path;

	/**
	 * Constructs a new Path object by initializing it's stack structure.
	 */
	public Path() {
		this.path = new Stack<>();
	}

	/**
	 * Add a new entry to this Path object by pushing it to it's stack.
	 * The new entry represents an "answered question" that has no associated data.
	 * @param item the
	 * @param option
	 */
	public void addEntry(Item item, Option option) {
		path.add(new PathEntry(item, option));
	}

	/**
	 * Add a new entry to this Path object by pushing it to it's stack.
	 * The new entry represents an "answered question" that has some associated data.
	 * @param item some Item object
	 * @param option some Option object
	 * @param data the associated data
	 */
	public void addEntry(Item item, Option option, String data) {
		path.push(new PathEntry(item, option, data));
	}

	/**
	 * Explicitly set the top of stack entry  for this Path with some Item and Option objects.
	 * @param item some Item object
	 * @param option some Option object
	 */
	public void setLastEntry(Item item, Option option) {
		PathEntry entry = path.peek();
		entry.setItem(item);
		entry.setOption(option);
	}

	/**
	 * Explicitly set the top of of stack entry for this Path with some Item and Option objects.
	 * This method implies the entry has some associated data.
	 * @param item some Item obejct
	 * @param option some Option object
	 * @param data the associated data
	 */
	public void setLastEntry(Item item, Option option, String data) {
		PathEntry entry = path.peek();
		entry.setItem(item);
		entry.setOption(option);
		entry.setData(data);
	}

	/**
	 * Get this Path object's top of stack entry.
	 * @return the Item object for this path's to of stack
	 */
	public Item getLastItem() {
		return path.get(path.size()-1).getItem();
	}

	@Override
	public Iterator<PathEntry> iterator() {
		return path.iterator();
	}

	/**
	 * Wrapper class representing a trio of an Item, Option, and String.
	 * This class is used as a helper to facilitate navigating through a flowchart.
	 * It is made public simply so it can be iterated upon.
	 */
	public class PathEntry {
		private Item item;
		private Option option;
		private String data;

		PathEntry(Item item, Option option) {
			this.item = item;
			this.option = option;
			this.data = "";
		}

		PathEntry(Item item, Option option, String data) {
			this.item = item;
			this.option = option;
			this.data = data;
		}

		public Item getItem() {
			return item;
		}

		public void setItem(Item item) {
			this.item = item;
		}

		public Option getOption() {
			return option;
		}

		public void setOption(Option option) {
			this.option = option;
		}

		public String getData() {
			return data;
		}

		public void setData(String data) {
			this.data = data;
		}
	}

}
