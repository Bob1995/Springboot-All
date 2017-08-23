package com.akshay.model;

public class User {
	public String firstName;
	public String lastName;
	public Integer age;
	public Integer id;

	public User() {
		
	}

	public User(String firstName, String lastName, Integer age, Integer id) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.id = id;
	}
	public User(String firstName, String lastName, Integer age) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


}
