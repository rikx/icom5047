package com.rener.sea;

import java.util.Iterator;
import java.util.Stack;

/**
 * A class representing an "answered survey" by making a path of options (edges) through a
 * flowchart. It's underlying data structure is that of a stack.
 * TODO: revisar!!!!!!!
 */
public class Path implements Iterable<PathEntry> {

    private Stack<PathEntry> path;
    private long reportID = -1;
    private DBHelper dbHelper = null;

    /**
     * Constructs a new Path object by initializing it's stack structure.
     */
    public Path() {
        this.path = new Stack<>();
    }
    public Path(long report_id, DBHelper db) {
        this.path = new Stack<>();
        this.reportID = report_id;
        this.dbHelper = db;
    }

    /**
     * Add a new entry to this Path object by pushing it to it's stack.
     * The new entry represents an "answered question" that has no associated data.
     *
     * @param option the option object for the entry
     */
    public void addEntry(Option option) {

        path.push(new PathEntry(reportID, option.getId(), dbHelper));
    }
    /**
     * Add a new entry to this Path object by pushing it to it's stack.
     * The new entry represents an "answered question" that has no associated data.
     *
     * @param item   the item object for the entry
     * @param option the option object for the entry
     */
    public void addEntry(Item item, Option option) {
        path.push(new PathEntry(item, option));
    }
    /**
     * Add a new entry to this Path object by pushing it to it's stack.
     * The new entry represents an "answered question" that has some associated data.
     *
     * @param option some Option object
     * @param data   the associated data
     */
    public void addEntry(Option option, String data) {
        path.push(new PathEntry(reportID, option.getId(),data, dbHelper));
    }
    /**
     * Add a new entry to this Path object by pushing it to it's stack.
     * The new entry represents an "answered question" that has some associated data.
     *
     * @param item   some Item object
     * @param option some Option object
     * @param data   the associated data
     */
    public void addEntry(Item item, Option option, String data) {
        path.push(new PathEntry(item, option, data));
    }
    /**
     * Explicitly set the top of stack entry  for this Path with some Item and Option objects.
     *
     * @param item   some Item object
     * @param option some Option object
     */
    public void setLastEntry(Item item, Option option) {
        PathEntry entry = path.pop();
        entry.setItem(item);
        entry.setOption(option);
        path.push(entry);
    }

    /**
     * Explicitly set the top of of stack entry for this Path with some Item and Option objects.
     * This method implies the entry has some associated data.
     *
     * @param item   some Item obejct
     * @param option some Option object
     * @param data   the associated data
     */
    public void setLastEntry(Item item, Option option, String data) {
        PathEntry entry = path.pop();
        entry.setItem(item);
        entry.setOption(option);
        entry.setData(data);
        path.push(entry);
    }

    /**
     * Get this Path object's top of stack entry.
     *
     * @return the Item object for this path's to of stack
     */
    public Item getLastItem() {
        return path.peek().getItem();
    }

    @Override
    public Iterator<PathEntry> iterator() {
        return path.iterator();
    }

}
