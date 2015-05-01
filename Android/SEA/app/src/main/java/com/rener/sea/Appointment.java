package com.rener.sea;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Appointment {

	private Date date;
	private String purpose;

	public Appointment() {
		date = new Date();
		purpose = "";
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getDateString(String format, Locale locale) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
		return sdf.format(date);
	}
}
