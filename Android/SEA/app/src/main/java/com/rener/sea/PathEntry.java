package com.rener.sea;

/**
 * Wrapper class representing a trio of an Item, Option, and String.
 * This class is used as a helper to facilitate navigating through a flowchart.
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