package com.rener.sea;

public class User {

	private long user_id;
	private String username;
	private String salt;
	private String password;
	private Person person;

	public User(long user_id, String username, String password) {
		this.user_id = user_id;
		this.username = username;
		this.password = password;
		this.salt = "";
	}

	public User(long user_id, String username, String password, Person person) {
		this.user_id = user_id;
		this.username = username;
		this.password = password;
		this.salt = "";
		this.person = person;
	}

	public long getId() {
		return user_id;
	}

	public long setId(long user_id) {
		return this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public String setUsername(String username) {
		return this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public String setPassword(String password) {
		return this.password = password;
	}

	public Person getPerson() {
		return person;
	}

	public Person setPerson(Person person) {
		return this.person = person;
	}

}
