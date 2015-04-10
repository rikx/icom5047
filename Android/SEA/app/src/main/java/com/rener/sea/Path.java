package com.rener.sea;

import java.util.Iterator;
import java.util.Stack;

public class Path implements Iterable<Path.PathEntry> {

	private Stack<PathEntry> path;

	public Path() {
		this.path = new Stack<>();
	}

	public void addEntry(Item item, Option option) {
		path.add(new PathEntry(item, option));
	}

	public void addEntry(Item item, Option option, String data) {
		path.push(new PathEntry(item, option, data));
	}

	public void setLastEntry(Item item, Option option) {
		PathEntry entry = path.peek();
		entry.setItem(item);
		entry.setOption(option);
	}

	public void setLastEntry(Item item, Option option, String data) {
		PathEntry entry = path.peek();
		entry.setItem(item);
		entry.setOption(option);
		entry.setData(data);
	}

	public Item getLastItem() {
		return path.get(path.size()-1).getItem();
	}

	@Override
	public Iterator<PathEntry> iterator() {
		return path.iterator();
	}

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
