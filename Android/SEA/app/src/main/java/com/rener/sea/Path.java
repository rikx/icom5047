package com.rener.sea;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Path implements Iterable<Path.PathEntry> {

	private List<PathEntry> path;
	private int size = 0;

	public Path() {
		this.path = new ArrayList<>();
	}

	public void addEntry(Item item, Option option) {
		path.add(new PathEntry(item, option));
	}

	public void addEntry(Item item, Option option, String data) {
		path.add(new PathEntry(item, option, data));
	}

	public int getSize() {
		return path.size();
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
