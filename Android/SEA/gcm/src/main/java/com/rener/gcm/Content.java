package com.rener.gcm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Content implements Serializable {

	private List<String> registrationIds;
	private Map<String,String> data;

	public void addRegistrationId(String regId) {
		if(registrationIds == null) {
			registrationIds = new LinkedList<>();
		}
		registrationIds.add(regId);
	}

	public void createData(String title, String message ) {
		if(data == null) {
			data = new HashMap<>();
		}
		data.put("title", title);
		data.put("message", message);
	}

}
