package com.rener.sea;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Appointment {

	private Date date;
	private String reason;

	public Appointment() {
		date = new Date();
		reason = "";
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDateString(String format, Locale locale) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
		return sdf.format(date);
	}
}
