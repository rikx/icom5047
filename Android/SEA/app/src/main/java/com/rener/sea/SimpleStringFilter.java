package com.rener.sea;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SimpleStringFilter {

    private List<Object> sourceObjects;
    private List<Object> filteredObjects;

    public SimpleStringFilter(List<Object> objects) {
        this.filteredObjects = objects;
        this.sourceObjects = new ArrayList<>(objects);
    }

    public void filter(String string) {
        String key = string.trim().toLowerCase(Locale.getDefault());
        filteredObjects.clear();
        if (key.length() <= 0) {
            filteredObjects.addAll(sourceObjects);
        } else {
            for (Object o : sourceObjects) {
                String target = o.toString().toLowerCase(Locale.getDefault());
                if (target.contains(key)) {
                    filteredObjects.add(o);
                }
            }
        }
    }
}
